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
 * SHALL NASA BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM, OR
 * IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */

package gov.nist.scap.validation.component;

import gov.nist.scap.validation.Application;
import gov.nist.scap.validation.NamespaceConstants;
import gov.nist.scap.validation.SCAPVersion;
import org.jdom2.Element;
import org.jdom2.filter.Filters;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** Valid OVAL versions and their specifics. */
public enum OVALVersion {
  // schema dir location, schematron definitions filename, schematron results filename
  V5_3(
      new String[] { "xsd/mitre/oval/oval_5.3", "oval-definitions-schematron-5.3.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_4(
      new String[] { "xsd/mitre/oval/oval_5.4", "oval-definitions-schematron-5.4.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_5(
      new String[] { "xsd/mitre/oval/oval_5.5", "oval-definitions-schematron-5.5.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_6(
      new String[] { "xsd/mitre/oval/oval_5.6", "oval-definitions-schematron-5.6.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_7(
      new String[] { "xsd/mitre/oval/oval_5.7", "oval-definitions-schematron-5.7.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_8(
      new String[] { "xsd/mitre/oval/oval_5.8", "oval-definitions-schematron-5.8.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_9(
      new String[] { "xsd/mitre/oval/oval_5.9", "oval-definitions-schematron-5.9.sch",
          "oval-results-schematron-5.10.sch" }),
  V5_10(
      new String[] { "xsd/mitre/oval/oval_5.10", "oval-definitions-schematron-5.10.sch",
          "oval-results-schematron-5.10.sch" }),
  V5_10_1(
      new String[] { "xsd/mitre/oval/oval_5.10.1", "oval-definitions-schematron-5.10.1.sch",
          "oval-results-schematron-5.10.1.sch" }),
  V5_11(
      new String[] { "xsd/mitre/oval/oval_5.11", "oval-definitions-schematron-5.11.sch",
          "oval-results-schematron-5.11.sch" }),
  V5_11_1( //5.11.1 schematrons are not available in github so using 5.11 version
      new String[] { "xsd/mitre/oval/oval_5.11.1", "oval-definitions-schematron-5.11.sch",
          "oval-results-schematron-5.11.sch" }),
  V5_11_2(
      new String[] { "xsd/mitre/oval/oval_5.11.2", "oval-definitions-schematron-5.11.sch",
          "oval-results-schematron-5.12.sch" });

  private String[] validationFiles;

  OVALVersion(String[] validationFiles) {
    this.validationFiles = validationFiles;
  }

  public static OVALVersion getByString(String name) {
    Objects.requireNonNull(name, "name cannot be null.");
    for (OVALVersion version : OVALVersion.values()) {
      if (version.getVersionString().equals(name)) {
        return version;
      }
    }
    return null;
  }

  public static OVALVersion getOVALVersion(org.jdom2.Element ovalComponent) {
    Objects.requireNonNull(ovalComponent, "ovalComponent cannot be null.");
    String ovalVersionString = "";
    Iterator<Element> iterator = ovalComponent.getDescendants(Filters.element());
    while (iterator.hasNext()) {
      Element decendantElement = iterator.next();
      if (decendantElement.getName().equals("schema_version") && decendantElement.getNamespace()
          .equals(NamespaceConstants.NS_OVAL_COM_5.getNamespace())) {
        ovalVersionString = decendantElement.getValue();
        break;
      }
    }
    return OVALVersion.getByString(ovalVersionString);
  }

  public String getVersionString() {
    return this.toString().replace("_", ".").replace("V", "");
  }

  public String getSchemaDir() {
    String schemaDir = null;
    for (String validationFile : this.validationFiles) {
      if (validationFile.startsWith("xsd")) {
        schemaDir = validationFile;
        break;
      }
    }
    return schemaDir;
  }

  public String getDefinitionSchematron() {
    String defSchematron = null;
    for (String validationFile : this.validationFiles) {
      if (validationFile.contains("definitions")) {
        defSchematron = validationFile;
        break;
      }
    }
    return defSchematron;
  }

  public String getResultSchematron() {
    String resSchematron = null;
    for (String validationFile : this.validationFiles) {
      if (validationFile.contains("results")) {
        resSchematron = validationFile;
        break;
      }
    }
    return resSchematron;
  }

  public LinkedList<StreamSource> getSCAPOVALSchemas(SCAPVersion scapVersion, Application
      .ContentType contentType) {
    Objects.requireNonNull(contentType, "contentType cannot be null.");
    LinkedList<StreamSource> schemaList = new LinkedList<>();
    // enumerate and load all the oval schema files per specified OVAL version.
    // If we are running in production from a .jar file we'll load resources from the jar.
    // Otherwise we'll just use listfiles() when running with Maven or within Dev Env
    String ovalSchemaDirPath = this.getSchemaDir();
    File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation()
        .getPath());
    try {
      if (jarFile.isFile()) { // jar file detected, we'll load the resources
        final JarFile jar;
        jar = new JarFile(jarFile);
        final Enumeration<JarEntry> entries = jar.entries();
        // get all the jar entries and iterate for our specific oval version .xsd
        while (entries.hasMoreElements()) {
          String filename = entries.nextElement().getName();
          if (scapVersion == null) {
            //for individual oval component check, we'll use the specified oval:schema_version for
            // all schemas
            if (filename.startsWith(ovalSchemaDirPath + "/") && filename.endsWith(".xsd")) {
              schemaList.add(new StreamSource("classpath:" + filename));
            }
          } else {
            //this oval content is embedded in SCAP. we will use the specified schema_version for
            // all schemas except for the
            //SCAP results which will use the SCAP specified OVAL schema version
            if (contentType.equals(Application.ContentType.RESULT)) {
              //this should include OVAL results so only use the specified SCAP OVAL schema
              // version per RES-202-1
              if (filename.startsWith(OVALVersion.getByString(scapVersion.getOvalSupportedVersion
                  ().getVersionString()).getSchemaDir() + "/") && filename.endsWith(".xsd")) {
                schemaList.add(new StreamSource("classpath:" + filename));
              }
            }
            //this is not result content, load the specified oval schemas minus oval-results-schema
            else if (filename.startsWith(ovalSchemaDirPath + "/") && filename.endsWith(".xsd") &&
                !filename.contains("oval-results-schema")) {
              schemaList.add(new StreamSource("classpath:" + filename));
            }
          }
        }
        jar.close();
      } else { // we are running outside the jar, we can load the schemas directly from the
        // filesystem
        URL url = this.getClass().getResource("/" + ovalSchemaDirPath);
        if (url != null) {
          try {
            File ovalSchemaDirFile = new File(url.toURI());
            if (contentType.equals(Application.ContentType.COMPONENT)) {
              //for individual oval component check, we'll use the specified oval:schema_version
              // for all schemas
              for (File schema : ovalSchemaDirFile.listFiles()) {
                schemaList.add(new StreamSource(schema.getAbsoluteFile()));
              }
            } else {
              //this OVAL content is embedded in SCAP. We will use the specified schema_version
              // for all schemas except for the
              //SCAP results which will use the SCAP specified OVAL schema version
              if (contentType.equals(Application.ContentType.RESULT)) {
                url = this.getClass().getResource("/" + (OVALVersion.getByString(scapVersion
                    .getOvalSupportedVersion().getVersionString()).getSchemaDir()));
                ovalSchemaDirFile = new File(url.toURI());
                //this should include OVAL results so only use the specified SCAP OVAL schema
                // version per RES-202-1
                for (File schema : ovalSchemaDirFile.listFiles()) {
                  schemaList.add(new StreamSource(schema.getAbsoluteFile()));

                }
              }
              for (File schema : ovalSchemaDirFile.listFiles()) {
                //this is not result content, load the specified oval schemas minus
                // oval-results-schema
                if (!schema.getName().contains("oval-results-schema")) {
                  schemaList.add(new StreamSource(schema.getAbsoluteFile()));
                }
              }
            }
          } catch (URISyntaxException e) {
            throw new RuntimeException("There was a problem loading a .xsd schema file in " + url
                .getPath() + " : " + e.getMessage());
          }
        } else {
          //this shouldn't happen unless OVALVersion is incorrect or the xsds cannot be located
          throw new RuntimeException("Unable to access required XSD schemas: " + "/" +
              ovalSchemaDirPath);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("There was a problem loading a .xsd schema file: " + e
          .getMessage());
    }

    return schemaList;
  }

}
