/**
 * Portions of this software was developed by employees of the National Institute
 * of Standards and Technology (NIST), an agency of the Federal Government and is
 * being made available as a public service. Pursuant to title 17 United States
 * Code Section 105, works of NIST employees are not subject to copyright
 * protection in the United States. This software may be subject to foreign
 * copyright. Permission in the United States and in foreign countries, to the
 * extent that NIST may hold copyright, to use, copy, modify, create derivative
 * works, and distribute this software and its documentation without fee is hereby
 * granted on a non-exclusive basis, provided that this notice and disclaimer
 * of warranty appears in all copies.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS' WITHOUT ANY WARRANTY OF ANY KIND, EITHER
 * EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY
 * THAT THE SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND FREEDOM FROM
 * INFRINGEMENT, AND ANY WARRANTY THAT THE DOCUMENTATION WILL CONFORM TO THE
 * SOFTWARE, OR ANY WARRANTY THAT THE SOFTWARE WILL BE ERROR FREE.  IN NO EVENT
 * SHALL NIST BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM,
 * OR IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */

package gov.nist.secauto.scap.validation;

import static gov.nist.secauto.decima.xml.document.CompositeXMLDocument.COMPOSITE_NS_URI;
import static gov.nist.secauto.scap.validation.NamespaceConstants.NS_ARF_1_1;
import static gov.nist.secauto.scap.validation.NamespaceConstants.NS_XLINK;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import gov.nist.secauto.decima.core.document.DocumentException;
import gov.nist.secauto.decima.xml.document.CompositeXMLDocument;
import gov.nist.secauto.decima.xml.document.JDOMDocument;
import gov.nist.secauto.decima.xml.document.MutableXMLDocument;
import gov.nist.secauto.decima.xml.document.SimpleXMLDocumentResolver;
import gov.nist.secauto.decima.xml.document.XMLDocument;
import gov.nist.secauto.decima.xml.jdom2.saxon.xpath.SaxonXPathFactory;
import gov.nist.secauto.decima.xml.templating.document.post.template.Action;
import gov.nist.secauto.decima.xml.templating.document.post.template.DefaultTemplateProcessor;
import gov.nist.secauto.decima.xml.templating.document.post.template.InsertChildAction;
import gov.nist.secauto.decima.xml.templating.document.post.template.ModifyAttributeAction;
import gov.nist.secauto.scap.validation.exceptions.SCAPException;
import gov.nist.secauto.scap.validation.utils.FileUtils;
import gov.nist.secauto.scap.validation.utils.XMLUtils;

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

  /**
   * Includes a SCAP source data-stream into an ARF file using the arf:report-requests element. This
   * is used for SCAP 1.2, 1.3 and 1.4 content only.
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
            String remoteContentIncluded = "Validation included remote component-ref with id " + idAttribute.getValue()
                + " and url " + remoteComponentURL;
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
          String docException
              = "There was a problem in the XML of remote component-ref with id " + idAttribute.getValue() + " and url "
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

}
