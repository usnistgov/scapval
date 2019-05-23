/**
 * Portions of this software was developed by employees of the National Institute
 * of Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 17 United States Code Section 105, works of NIST employees are
 * not subject to copyright protection in the United States and are considered to
 * be in the public domain. Permission to freely use, copy, modify, and distribute
 * this software and its documentation without fee is hereby granted, provided that
 * this notice and disclaimer of warranty appears in all copies.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS' WITHOUT ANY WARRANTY OF ANY KIND, EITHER
 * EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY
 * THAT THE SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND FREEDOM FROM
 * INFRINGEMENT, AND ANY WARRANTY THAT THE DOCUMENTATION WILL CONFORM TO THE
 * SOFTWARE, OR ANY WARRANTY THAT THE SOFTWARE WILL BE ERROR FREE. IN NO EVENT
 * SHALL NIST BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM, OR
 * IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */

package gov.nist.scap.validation;

import static gov.nist.decima.xml.document.CompositeXMLDocument.COMPOSITE_NS_URI;
import static gov.nist.scap.validation.NamespaceConstants.NS_ARF_1_1;
import static gov.nist.scap.validation.NamespaceConstants.NS_XLINK;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import gov.nist.decima.core.document.DocumentException;
import gov.nist.decima.xml.document.CompositeXMLDocument;
import gov.nist.decima.xml.document.JDOMDocument;
import gov.nist.decima.xml.document.MutableXMLDocument;
import gov.nist.decima.xml.document.SimpleXMLDocumentResolver;
import gov.nist.decima.xml.document.XMLDocument;
import gov.nist.decima.xml.jdom2.saxon.xpath.SaxonXPathFactory;
import gov.nist.decima.xml.templating.document.post.template.Action;
import gov.nist.decima.xml.templating.document.post.template.DefaultTemplateProcessor;
import gov.nist.decima.xml.templating.document.post.template.InsertChildAction;
import gov.nist.decima.xml.templating.document.post.template.ModifyAttributeAction;
import gov.nist.scap.validation.component.SCAP11Components;
import gov.nist.scap.validation.exceptions.SCAPException;
import gov.nist.scap.validation.utils.FileUtils;
import gov.nist.scap.validation.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.transform.JDOMSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Obtains and merges in remote resources in SCAP or component files and combines SCAP 1.1 content
 * into a single file for validation.
 */

public class ContentCombiner {
  private static final Logger log = LogManager.getLogger(ContentCombiner.class);
  private static final String SOURCE_BASE_LOCATION = "classpath:templates/scap-11/source-combiner-base.xml";
  private static final String RESULT_BASE_LOCATION = "classpath:templates/scap-11/result-combiner-base.xml";

  /**
   * Combines individual SCAP component files into a single data-stream file for validation in
   * SCAPVal. This is used for SCAP 1.1 content only. Including the sourceDS is optional otherwise the
   * parameter can be null
   *
   * @param scapContentsDir
   *          a File object of the directory containing the SCAP 1.1 files, not null
   * @param scapUseCase
   *          the SCAP 1.1 use case under assessment, not null
   * @param maxDownloadSize
   *          an int (in MiB) of the maximum external file download size supported, not null
   * @param isOnline
   *          a boolean of the online status of this validation, not null
   * @param sourceDS
   *          an XMLDocument of source content that will be included in the combined result content.
   *          Can be null in the case of non result content validation.
   * @return The combined SCAP 11 XMLDocument
   */
  public static XMLDocument combineSCAP11(File scapContentsDir, String scapUseCase, int maxDownloadSize,
      boolean isOnline, XMLDocument sourceDS) throws SCAPException, IOException, DocumentException, JDOMException {
    Objects.requireNonNull(scapContentsDir, "scapContentsDir cannot be null.");
    Objects.requireNonNull(scapUseCase, "scapUseCase cannot be null.");
    Objects.requireNonNull(maxDownloadSize, "maxDownloadSize cannot be null.");
    Objects.requireNonNull(isOnline, "isOnline cannot be null.");

    // This map will store all the individual SCAP 1.1 components
    Map<String, JDOMDocument> scapComponentDocs = new LinkedHashMap<>();

    // find out exactly where the bundle is located, could be in the root or one level deep
    File scap11BundleDir = locateSCAP11Bundle(scapContentsDir.getAbsolutePath());
    if (scap11BundleDir == null) {
      throw new SCAPException("Unable to find SCAP 1.1 content in: " + scapContentsDir.getAbsolutePath());
    } else {
      log.debug("Found SCAP 1.1 bundle in: " + scap11BundleDir.getAbsolutePath());
    }

    // look for and download any XCCDF remote check-content-ref to include with the detected
    // SCAP
    // 11 bundle dir
    Map<String, String> remoteURLandFilenames = new LinkedHashMap<>();
    if (isOnline) {
      File[] files = scap11BundleDir.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.getName().contains("xccdf")) {
            // remote resources will attempt to be downloaded and if successful
            // remoteURLandFilename
            // will contain the original URL and downloaded filename
            remoteURLandFilenames = getXCCDF11RemoteResources(file, scap11BundleDir, maxDownloadSize);
          }
        }
      } else {
        throw new SCAPException("Unable to find SCAP 1.1 content in: " + scapContentsDir.getAbsolutePath());
      }
    }

    // gather all SCAP 1.1 files in the detected bundle dir, these will be sorted in appropriate
    // component order
    File[] scap11Files = gatherSCAP11Files(scap11BundleDir);

    for (File file : scap11Files) {
      // for each component file
      log.info("Found Component File " + file + "(SHA-256: "
          + FileUtils.getFileHash(file, FileUtils.DEFAULT_HASH_ALGORITHM) + ")");
      scapComponentDocs.put(file.getName(), new JDOMDocument(file));
    }

    // now we have a list of all component XML documents, build an xml template dynamically
    // utilizing <sub> elements.
    URL baseTemplateURL;
    String baseTemplateRootXpath;
    // Namespace baseTemplateNamespace;
    SaxonXPathFactory xpathFactory = new SaxonXPathFactory();
    MutableXMLDocument template = null;
    List<Action> actions = new LinkedList<>();
    Element templateSub = new Element("sub", COMPOSITE_NS_URI);

    // source specific template processing
    if (sourceDS == null) {
      baseTemplateURL = (new URL(SOURCE_BASE_LOCATION));
      baseTemplateRootXpath = "//*[local-name()='data-stream' and namespace-uri()='"
          + NamespaceConstants.NS_SOURCE_DS_1_1.getNamespaceString() + "']";
      String xpathDataStreamUseCase = new StringBuilder(baseTemplateRootXpath).append("/@use-case").toString();
      String xpathDataStreamTimestamp = new StringBuilder(baseTemplateRootXpath).append("/@timestamp").toString();

      // customize the template data-stream per user specified scap 1.1 use case and timestamp
      actions.add(new ModifyAttributeAction(xpathFactory, xpathDataStreamUseCase,
          Collections.singletonMap(templateSub.getNamespacePrefix(), templateSub.getNamespaceURI()), scapUseCase));
      actions.add(new ModifyAttributeAction(xpathFactory, xpathDataStreamTimestamp,
          Collections.singletonMap(templateSub.getNamespacePrefix(), templateSub.getNamespaceURI()),
          new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date())));
    } else { // result specific template processing
      // if sourceDS is provided, insert the source tag and included datastream within
      baseTemplateURL = (new URL(RESULT_BASE_LOCATION));
      baseTemplateRootXpath = "/*:results";

      Element sourceElement = new Element("source");
      sourceElement = sourceElement.addContent(sourceDS.getJDOMDocument().getRootElement().detach());
      actions.add(new InsertChildAction(xpathFactory, baseTemplateRootXpath,
          Collections.singletonMap(sourceElement.getNamespacePrefix(), sourceElement.getNamespaceURI()),
          Collections.singletonList(sourceElement), null));

    }

    // now insert substitution elements for the template which will be used to insert the
    // individual components
    for (Map.Entry<String, JDOMDocument> pair : scapComponentDocs.entrySet()) {
      String filename = pair.getKey();
      Element templateSubLocal = new Element("sub", COMPOSITE_NS_URI);
      templateSubLocal.setAttribute("name", filename);
      actions.add(new InsertChildAction(xpathFactory, baseTemplateRootXpath,
          Collections.singletonMap(templateSubLocal.getNamespacePrefix(), templateSubLocal.getNamespaceURI()),
          Collections.singletonList(templateSubLocal), null));
    }

    URL contextSystemId = new File(scap11BundleDir.getAbsolutePath()).toURI().toURL();
    DefaultTemplateProcessor defaultTemplateProcessor
        = new DefaultTemplateProcessor(contextSystemId, baseTemplateURL, actions);
    template = defaultTemplateProcessor.generate(new SimpleXMLDocumentResolver());

    // populate the template with map of name and content
    Map<String, JDOMDocument> compMap = new LinkedHashMap<>();
    for (Map.Entry<String, JDOMDocument> scapComponentDoc : scapComponentDocs.entrySet()) {
      String filename = scapComponentDoc.getKey();
      SCAP11Components contentType = SCAP11Components.getByFileNameAndUseCase(filename, scapUseCase);

      Element dataStreamComponent;

      if (sourceDS == null) { // process into source ds components
        dataStreamComponent
            = new Element(SCAP11Components.getByFileNameAndUseCase(filename, scapUseCase).getCombinedDocLocalName(),
                NamespaceConstants.NS_SOURCE_DS_1_1.getNamespace());
        dataStreamComponent.setAttribute("id", filename);
        dataStreamComponent.setAttribute("content-type", contentType.toString());
      } else { // process into result components
        dataStreamComponent = new Element("result");
        dataStreamComponent.setAttribute("id", filename);
      }

      Element clonedComponent = scapComponentDoc.getValue().getJDOMDocument().getRootElement().clone();

      // in order to have a valid DS, if we downloaded a remote resource we must
      // use its original URL and not filename as the @id.
      // in the below remoteURLandFilename, the value is the downloaded file name and the
      // key was the original URL used to download
      for (Map.Entry<String, String> remoteURLandFilename : remoteURLandFilenames.entrySet()) {
        if (remoteURLandFilename.getValue().equals(filename)) {
          dataStreamComponent.setAttribute("id", remoteURLandFilename.getKey());
        }
      }
      dataStreamComponent.addContent(clonedComponent);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Source xmlSource = new JDOMSource(dataStreamComponent);
      Result outputTarget = new StreamResult(outputStream);
      try {
        TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
      } catch (TransformerException e) {
        throw new SCAPException("Problem with the component to combine " + e.getMessage());
      }
      InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

      // create a valid system id for each component
      String systemID
          = new File(scap11BundleDir.getAbsolutePath() + FileUtils.PATH_SEPERATOR + filename).toURI().toString();

      compMap.put(filename, new JDOMDocument(is, systemID));
    }

    CompositeXMLDocument compositeXMLDocument = new CompositeXMLDocument(template, compMap);

    // write out the composite doc for validation
    File scap11CombinedContent = new File(scapContentsDir.getName() + ".xml");

    if (!scap11CombinedContent.exists()
        && (!scap11CombinedContent.createNewFile() || !scap11CombinedContent.canWrite())) {
      throw new IOException("Problem processing the SCAP 1.1 components. Need write access to: "
          + scap11CombinedContent.getAbsolutePath() + " in order to continue.");
    }

    // Need to wait to delete so the report generation can occur. This will delete the file when
    // the JVM exits
    scap11CombinedContent.deleteOnExit();

    return compositeXMLDocument.toJDOMDocument(scap11CombinedContent);
  }

  /**
   * Determines exactly where the SCAP bundle is located. This could the root of the specified
   * Directory or within an embedded directory one level deep
   *
   * @param scapContentsDir
   *          the user specified directory where SCAP 1.1 content resides, not null
   * @return a File object of the directory where the SCAP bundle is located within the specified
   *         scapContentsDir
   */
  public static File locateSCAP11Bundle(String scapContentsDir) {
    // look for scap 1.1 files in the specified root dir or in an embedded dir.
    // once the first valid SCAP 1.1 file is found that directory will will be considered our
    // bundle
    Objects.requireNonNull(scapContentsDir, "scapContentsDir cannot be null.");
    File[] dirFiles = new File(scapContentsDir).listFiles();
    if (dirFiles != null) {
      for (File file : dirFiles) {
        if (SCAP11Components.isValidSCAP11FileName(file.getName())) {
          return file.getParentFile();
        } else if (file.isDirectory()) {
          // we will search one dir level deep for a bundle
          for (File file1 : file.listFiles()) {
            if (SCAP11Components.isValidSCAP11FileName(file1.getName())) {
              return file1.getParentFile();
            }
          }
        }
      }
    }
    // did not find a bundle
    return null;
  }

  /**
   * Gathers all scap files and returns in the required component order for DS creation
   *
   * @param bundleDir
   *          a File object of the directory containing SCAP 1.1 files, not null
   * @return a File Array of SCAP 11 files in the required component order for DS creation
   */
  public static File[] gatherSCAP11Files(File bundleDir) {
    Objects.requireNonNull(bundleDir, "bundleDir cannot be null.");
    List<File> scap11files = new LinkedList<>();
    // get list of valid SCAP 1.1 file names
    for (File file : bundleDir.listFiles()) {
      if (file.isFile() && SCAP11Components.isValidSCAP11FileName(file.getName())) {
        scap11files.add(file);
      }
    }

    // In order to be compliant with scap-data-stream_0.2.xsd certain components must be in a
    // certain order
    File[] arraySCAP11files = scap11files.toArray(new File[scap11files.size()]);
    // The below sorts as file array to ensure proper order
    Arrays.sort(arraySCAP11files, new Comparator<File>() {
      public int compare(File f1, File f2) {
        if (f1.getName().contains("xccdf") && !f2.getName().contains("xccdf")) {
          return -1; // 1st arg should be before the 2nd
        } else if (!f1.getName().contains("xccdf") && f2.getName().contains("xccdf")) {
          return 1; // 2nd arg should be before the 1st
        } else if (f1.getName().contains("cpe-dictionary") && !f2.getName().contains("cpe-dictionary")) {
          return 1; // 2nd arg should be before the 1st
        } else if (!f1.getName().contains("cpe-dictionary") && f2.getName().contains("cpe-dictionary")) {
          return -1; // 1st arg should be before the 2nd
        }
        return 0; // leaves files in same order
      }
    });

    return arraySCAP11files;
  }

  /**
   * Includes a SCAP source data-stream into an ARF file using the arf:report-requests element. This
   * is used for SCAP 1.2 and 1.3 content only.
   *
   * @param arfFile
   *          an XMLDocument of SCAP 1.2+ result content, not null
   * @param dsFile
   *          an XMLDocument of SCAP 1.2+ source content, not null
   * @param combinedOutput
   *          a File where the merged content will be saved to, not null
   */
  public static void mergeARFWithDS(XMLDocument arfFile, XMLDocument dsFile, File combinedOutput)
      throws IOException, SCAPException, URISyntaxException {
    Objects.requireNonNull(arfFile, "ARFFile cannot be null.");
    Objects.requireNonNull(dsFile, "DSFile cannot be null.");
    Objects.requireNonNull(combinedOutput, "combinedOutput cannot be null.");

    // all files should have already been through validateCLI() so no need validate again
    String dsFilePath = Paths.get(dsFile.getOriginalLocation().toURI()).toString();
    String arfFilePath = Paths.get(arfFile.getOriginalLocation().toURI()).toString();
    String combinedOutputPath = combinedOutput.getAbsolutePath();

    log.info("Attempting to embed the source data-steam: " + dsFilePath + " into the ARF file: " + arfFilePath + " and "
        + "creating file: " + combinedOutputPath);

    ValidationNotes.getInstance().createValidationNote("Validation included -sourceds content file: " + dsFilePath);

    JDOMDocument newCombined = new JDOMDocument(arfFile);
    Element reportRequests
        = newCombined.getJDOMDocument(true).getRootElement().getChild("report-requests", NS_ARF_1_1.getNamespace());
    if (reportRequests == null) {
      // couldn't find existing arf:report-requests element so we'll create a new one in
      // position 2
      reportRequests = newCombined.getJDOMDocument(true).getRootElement()
          .addContent(2, new Element("report-requests", NS_ARF_1_1.getNamespace()))
          .getChild("report-requests", NS_ARF_1_1.getNamespace());
    }
    // add the user specified datastream to a new report request
    Element newReportRequest = new Element("report-request", NS_ARF_1_1.getNamespace()).setAttribute("id", dsFilePath)
        .addContent(new Element("content", NS_ARF_1_1.getNamespace()));
    reportRequests = reportRequests.addContent(newReportRequest);
    // get the appropriate report-request (the last one added)
    Element reportRequestContent = reportRequests.getChildren().get(reportRequests.getChildren().size() - 1)
        .getChild("content", NS_ARF_1_1.getNamespace());
    if (reportRequestContent == null) {
      throw new SCAPException("There was problem embedding " + dsFilePath + " into " + arfFilePath);
    }
    // add the datastream
    Document merged = reportRequestContent.addContent(dsFile.getJDOMDocument().getRootElement().detach()).getDocument();
    // copy new content to the combined Document
    Document newCombinedDoc = newCombined.getJDOMDocument().setContent(merged.getRootElement().detach());
    try {
      // copy to result the combinedOutput
      new JDOMDocument(newCombinedDoc, new URL("file:" + combinedOutput.getAbsolutePath())).copyTo(combinedOutput);
    } catch (DocumentException e) {
      throw new SCAPException("There was problem embedding " + dsFilePath + " into " + arfFilePath);
    }
  }

  /**
   * Augments the XMLDocument provided with remote resources (file/http/https) specified in
   * component-ref The remote resource is read and merged into the datastream being validated.
   *
   * @param xmlDocument
   *          an XMLDocument containing remote resources to be gathered and merged in, not null
   * @param scapVersion
   *          the SCAP version under assessment, not null
   * @param maxDownloadSize
   *          an int (in MiB) of the maximum external file download size supported, not null
   */
  public static void mergeRemoteResourcesInDS(XMLDocument xmlDocument, SCAPVersion scapVersion, int maxDownloadSize) {
    Objects.requireNonNull(xmlDocument, "xmlDocument cannot be null.");
    Objects.requireNonNull(scapVersion, "scapVersion cannot be null.");
    Objects.requireNonNull(maxDownloadSize, "maxDownloadSize cannot be null.");

    String scapComponentRefsXpath
        = "//*[namespace-uri()='" + scapVersion.getDSNamespace().getURI() + "' and local-name()='component-ref']";
    // attempt to resolve any scap remote component resources
    List<Element> componentRemoteRefs = XMLUtils.getXpathElements(xmlDocument, scapComponentRefsXpath);
    for (Element component : componentRemoteRefs) {
      if (component.getAttribute("href", NS_XLINK.getNamespace()) != null) {
        Attribute idAttribute = component.getAttribute("id");
        Attribute xlinkHrefAttribute = component.getAttribute("href", NS_XLINK.getNamespace());

        URL remoteComponentURL = null;

        // startsWith() will work for http and https
        if (xlinkHrefAttribute.getValue().startsWith("http")) {
          // Note: http redirects are currently not supported in SCAPVal
          // this is a remote file that needs to be read and merged into this DS
          try {
            // make sure the component-ref URL is valid
            remoteComponentURL = new URL(xlinkHrefAttribute.getValue());

          } catch (MalformedURLException e) {
            // Invalid external reference URL, log and move on
            log.info("Invalid http/https location for component-ref with id " + idAttribute.getValue() + " and URL "
                + xlinkHrefAttribute.getValue() + " Will not download the remote component." + e.getMessage());
            ValidationNotes.getInstance()
                .createValidationNote("Invalid http/https location for component-ref with id " + idAttribute.getValue()
                    + " and URL " + xlinkHrefAttribute.getValue() + " Will not download the remote component.");
            continue;
          }
        } else if (xlinkHrefAttribute.getValue().startsWith("file:")) {
          // this is a file that needs to be read and merged into this DS
          String relativeDir = null;
          String fileNameToMerge = null;
          try {
            // will look for the file reference relative to the directory of the
            // validation target
            relativeDir = new File(xmlDocument.getOriginalLocation().toURI()).getParent() + File.separator;
            fileNameToMerge = xlinkHrefAttribute.getValue().split("file:")[1];
            // need to prepend the file: protocol to beginning of path full
            remoteComponentURL = new URL("file:" + relativeDir + fileNameToMerge);
          } catch (URISyntaxException e) {
            log.info("Cannot access the directory of the validation target: " + relativeDir + " and URL "
                + xlinkHrefAttribute.getValue() + " Will not include this file in validation." + e.getMessage());
            ValidationNotes.getInstance()
                .createValidationNote("Cannot access the directory of the validation target: " + relativeDir
                    + " and URL " + xlinkHrefAttribute.getValue() + " Will not include this file in validation.");
            continue;
          } catch (MalformedURLException e) {
            log.info("Cannot read file at location specified for component-ref with id " + idAttribute.getValue()
                + " and URL file:" + relativeDir + fileNameToMerge + " Will not include this file in validation."
                + e.getMessage());
            ValidationNotes.getInstance()
                .createValidationNote("Cannot read file at location specified for component-ref with id "
                    + idAttribute.getValue() + " and URL file:" + relativeDir + fileNameToMerge
                    + " Will not include this file in validation.");
            continue;
          }
        } else {
          // no resolvable remote ref is found
          continue;
        }
        try {
          log.info("Found a remote component-ref with id " + idAttribute.getValue() + " and URL "
              + xlinkHrefAttribute.getValue() + " will attempt to acquire and include in validation.");
          // make sure the component-ref ID is valid and use that for the remote resource
          // component name
          String newComponentID = null;
          if (idAttribute.getValue().matches("scap_[^_]+_cref_.+")) {
            newComponentID = idAttribute.getValue().replaceAll("cref", "comp");

            // after speaking with Dragos P. we will append the original URL to help
            // identify
            // this as a remote resource
            // the schema id restricts commas (,) and slashes (/) so we'll replace with
            // underscore (_) */
            String newxlinkHrefAttribute
                = ("#" + newComponentID + "_" + remoteComponentURL).replace("/", "_").replace(":", "_");

            // update the component-ref xlink:href to now look within the DS for the
            // remote
            // resource content
            xlinkHrefAttribute.setValue(newxlinkHrefAttribute);

          } else {
            log.info("Invalid component-ref id " + idAttribute.getValue() + " detected for a remote resource. The "
                + "remote resource will not be used");
            ValidationNotes.getInstance().createValidationNote("Invalid component-ref id " + idAttribute.getValue()
                + " detected for a remote resource. The remote resource will not be used");
            continue;
          }

          // read the from filesystem or download the remote component file
          File remoteComponentFile = null;
          if (remoteComponentURL.getProtocol().equals("file")) {
            remoteComponentFile = new File(remoteComponentURL.getPath());
          } else if (remoteComponentURL.getProtocol().startsWith("http")) {
            remoteComponentFile = FileUtils.downloadFile(remoteComponentURL, maxDownloadSize * 1024 * 1024);
          } else {
            log.info("Invalid component-ref id " + idAttribute.getValue() + " detected for a remote resource. The "
                + "remote resource will not be used");
            ValidationNotes.getInstance().createValidationNote("Invalid component-ref id " + idAttribute.getValue()
                + " detected for a remote resource. The remote resource will not be used");
            continue;
          }

          if (remoteComponentFile != null) {
            // create the remote component DOM content
            XMLDocument remoteComponentContentDoc = new JDOMDocument(remoteComponentFile);
            Element remoteComponentContentElement = remoteComponentContentDoc.getJDOMDocument().getRootElement();
            // create the <component> element to place the external content
            Element newComponent = new Element("component", scapVersion.getDSNamespace())
                .setAttribute("id",
                    new String(newComponentID + "_" + remoteComponentURL).replace("/", "_").replace(":", "_"))
                // timestamp="2016-01-22T14:00:00">
                .setAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
            // add content the downloaded remote component
            newComponent = newComponent.addContent(remoteComponentContentElement.clone());

            // now add to original doc
            xmlDocument.getJDOMDocument().getRootElement().addContent(newComponent);

            // if there were no problems, the content was added, include as a report note
            String remoteContentIncluded = "Validation included remote component-ref with id "
                    + idAttribute.getValue() + " and url " + remoteComponentURL;
            ValidationNotes.getInstance().createValidationNote(remoteContentIncluded);
            log.info(remoteContentIncluded);
          } else {
            // could not download the external reference, log and store as note
            log.info("Unable to download component-ref with id " + idAttribute.getValue() + " and url "
                + xlinkHrefAttribute.getValue());
            ValidationNotes.getInstance().createValidationNote("Unable to download component-ref with id "
                + idAttribute.getValue() + " and url " + xlinkHrefAttribute.getValue());
            continue;
          }
        } catch (IOException e) {
          // unable to download or merge with existing content, log and move on
          String ioException = "Unable to utilize remote component-ref with id " + idAttribute.getValue() + " and url "
                  + remoteComponentURL + " - " + e.getMessage();
          ValidationNotes.getInstance().createValidationNote(ioException);
          log.info(ioException);
          continue;
        } catch (DocumentException e) {
          // problem with the downloaded remote component-ref
          String docException = "There was a problem in the XML of remote component-ref with id " + idAttribute.getValue() + " and url "
                  + remoteComponentURL + " Unable to utilize this remote component-ref. - " + e.getMessage();
          log.info(docException);
          ValidationNotes.getInstance().createValidationNote(docException);
          continue;
        }
      } else {
        // did not find a file:/http:/https: prefix continue on
        continue;
      }
    }
  }

  /**
   * Downloads XCCDF 1.1.4 check-content-ref remote references and includes in the specified
   * scapContentsDir along with an appropriate filename and suffix
   *
   * @param xccdfFile
   *          a File of the XCCDF 1.1.4 content which contains the remote references
   * @param scapContentsDir
   *          a File object of the directory containing the SCAP 1.1 bundle files, not null
   * @param maxDownloadSize
   *          an int (in MiB) of the maximum external file download size supported, not null
   */
  public static Map<String, String> getXCCDF11RemoteResources(File xccdfFile, File scapContentsDir, int maxDownloadSize)
      throws SCAPException {
    Objects.requireNonNull(xccdfFile, "XCCDFFile cannot be null.");
    Objects.requireNonNull(scapContentsDir, "SCAPContentsDir cannot be null.");
    Objects.requireNonNull(maxDownloadSize, "maxDownloadSize cannot be null.");

    // This list will contain the check-content-ref remote URL as well as downloaded local
    // filename
    Map<String, String> remoteURLandFilename = new LinkedHashMap<>();

    String xccdfRemoteCheckRefsXpath = "//*[namespace-uri()=\"http://checklists.nist.gov/xccdf/1.1\" and "
        + "local-name()=\"Rule\"]//*[namespace-uri()=\"http://checklists.nist.gov/xccdf/1.1\" and local-name()"
        + "=\"check-content-ref\"]/@href";
    XMLDocument xccdfComponent = null;
    try {
      xccdfComponent = new JDOMDocument(xccdfFile);
    } catch (DocumentException | FileNotFoundException e) {
      throw new SCAPException("Unable to parse XCCDF File: " + e.getMessage());
    }

    // attempt to resolve any scap remote component resources
    List<Attribute> componentRemoteRefs = XMLUtils.getXpathAttributes(xccdfComponent, xccdfRemoteCheckRefsXpath);
    for (Attribute attribute : componentRemoteRefs) {
      String contentRef = attribute.getValue();
      if (contentRef.startsWith("http")) {
        // this is a remote ref
        URL remoteComponentURL = null;
        try {
          remoteComponentURL = new URL(contentRef);
          log.info("Found an XCCDF remote check-component-ref: " + contentRef + " will attempt to"
              + " download and include in validation.");

          // we will rename the check-component-ref to match the downloaded file name
          String downloadedRefFileName = FileUtils.getFilenameFromURLNoExtension(contentRef) + "-patches.xml";
          attribute.setValue(downloadedRefFileName);

          // download the remote reference file
          File remoteComponentFile = FileUtils.downloadFile(remoteComponentURL, maxDownloadSize * 1024 * 1024);
          if (remoteComponentFile != null && remoteComponentFile.length() != 0) {
            // copy the file to our SCAP 1.1 dir
            File destFile
                = new File(scapContentsDir.getAbsolutePath() + FileUtils.PATH_SEPERATOR + downloadedRefFileName);

            if (!destFile.createNewFile()) {
              log.info("Unable to included " + destFile.getAbsolutePath());
              continue;
            }

            // the remote reference file will be removed after validation
            destFile.deleteOnExit();

            if (destFile.canWrite()) {
              log.info("Including XCCDF remote reference: " + destFile.getAbsolutePath());
              Files.copy(remoteComponentFile.toPath(), destFile.toPath(), REPLACE_EXISTING);
              remoteURLandFilename.put(contentRef, downloadedRefFileName);
            } else {
              log.info("Unable to download XCCDF remote check-component-ref: " + contentRef);
              ValidationNotes.getInstance()
                  .createValidationNote("Unable to download XCCDF remote check-component-ref: " + contentRef);
              continue;
            }
          } else {
            // could not download the external reference, log and store as note
            log.info("Unable to download XCCDF remote check-component-ref: " + contentRef);
            ValidationNotes.getInstance()
                .createValidationNote("Unable to download XCCDF remote check-component-ref: " + contentRef);
            continue;
          }
        } catch (MalformedURLException e) {
          // Invalid external reference URL, log and move on
          log.info("Invalid URL for XCCDF remote check-component-ref:" + contentRef + " Will not "
              + "download the remote component." + e.getMessage());
          continue;
        } catch (IOException e) {
          // unable to download or merge with existing content, log and move on
          log.info("Unable to utilize the XCCDF remote check-component-ref reference with URL: " + e.getMessage());
          continue;
        }

      }
    }
    return remoteURLandFilename;
  }

}
