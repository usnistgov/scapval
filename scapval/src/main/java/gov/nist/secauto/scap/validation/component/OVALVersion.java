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

package gov.nist.secauto.scap.validation.component;

import gov.nist.secauto.scap.validation.Application;
import gov.nist.secauto.scap.validation.NamespaceConstants;
import gov.nist.secauto.scap.validation.SCAPVersion;

import org.jdom2.Element;
import org.jdom2.filter.Filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import javax.xml.transform.stream.StreamSource;

/**
 * Valid OVAL versions and their specifics.
 */
public enum OVALVersion {
  // schema dir location, schematron definitions filename, schematron results filename, schematron
  // system char filename
  V5_3(
      new String[] { "xsd/mitre/oval/oval_5.3/", "oval-definitions-schematron-5.3.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_4(
      new String[] { "xsd/mitre/oval/oval_5.4/", "oval-definitions-schematron-5.4.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_5(
      new String[] { "xsd/mitre/oval/oval_5.5/", "oval-definitions-schematron-5.5.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_6(
      new String[] { "xsd/mitre/oval/oval_5.6/", "oval-definitions-schematron-5.6.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_7(
      new String[] { "xsd/mitre/oval/oval_5.7/", "oval-definitions-schematron-5.7.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_8(
      new String[] { "xsd/mitre/oval/oval_5.8/", "oval-definitions-schematron-5.8.sch",
          "oval-results-schematron-5.8.sch" }),
  V5_9(
      new String[] { "xsd/mitre/oval/oval_5.9/", "oval-definitions-schematron-5.9.sch",
          "oval-results-schematron-5.10.sch" }),
  V5_10(
      new String[] { "xsd/mitre/oval/oval_5.10/", "oval-definitions-schematron-5.10.sch",
          "oval-results-schematron-5.10.sch" }),
  V5_10_1(
      new String[] { "xsd/mitre/oval/oval_5.10.1/", "oval-definitions-schematron-5.10.1.sch",
          "oval-results-schematron-5.10.1.sch" }),
  V5_11(
      new String[] { "xsd/mitre/oval/oval_5.11/", "oval-definitions-schematron-5.11.sch",
          "oval-results-schematron-5.11.sch" }),
  V5_11_1(
      new String[] { "xsd/mitre/oval/oval_5.11.1/", "oval-definitions-schematron-5.11.1.sch",
          "oval-results-schematron-5.11.1.sch" }),
  V5_11_2(
      new String[] { "xsd/mitre/oval/oval_5.11.2/", "oval-definitions-schematron-5.11.2.sch",
          "oval-results-schematron-5.11.2.sch", "oval-system-characteristics-schematron-5.11.2.sch" });
  // V5_11_2(
  // new String[] { "xsd/mitre/oval/oval_5.11.2/", "oval-definitions-schematron-5.11.2.sch",
  // "oval-results-schematron-5.11.2.sch" });

  private String[] validationFiles;

  OVALVersion(String[] validationFiles) {
    this.validationFiles = validationFiles;
  }

  /**
   * Returns the OVALVersion based on a string version (e.g. "5.11")
   *
   * @param stringVersion
   *          the oval version as a string
   * @return the OVALVersion enum
   */
  public static OVALVersion getByString(String stringVersion) {
    Objects.requireNonNull(stringVersion, "name cannot be null.");
    for (OVALVersion version : OVALVersion.values()) {
      if (version.getVersionString().equals(stringVersion)) {
        return version;
      }
    }
    return null;
  }

  /**
   * Returns the OVALVersion found with an OVAL component Element.
   *
   * @param ovalComponent
   *          the OVAL Component as an JDOM Element
   * @return the OVALVersion enum
   */
  public static OVALVersion getOVALVersion(Element ovalComponent) {
    Objects.requireNonNull(ovalComponent, "ovalComponent cannot be null.");
    String ovalVersionString = "";
    Iterator<Element> iterator = ovalComponent.getDescendants(Filters.element());
    while (iterator.hasNext()) {
      Element decendantElement = iterator.next();
      if (decendantElement.getName().equals("schema_version") && !decendantElement.hasAttributes()
          && decendantElement.getNamespace().equals(NamespaceConstants.NS_OVAL_COM_5.getNamespace())) {
        ovalVersionString = decendantElement.getValue();
        break;
      }
    }
    return OVALVersion.getByString(ovalVersionString);
  }

  public String getVersionString() {
    return this.toString().replace("_", ".").replace("V", "");
  }

  /**
   * Returns the Directory path containing this OVAL version's schema files.
   *
   * @return the path to the applicable schema files as a String
   */
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

  /**
   * Returns the Definition Schematron filename.
   *
   * @return the filename as a String
   */
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

  /**
   * Returns the Result Schematron filename.
   *
   * @return the filename as a String
   */
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

  /**
   * Returns the System Characteristics Schematron filename.
   *
   * @return the filename as a String
   */
  public String getSystemCharacteristicsSchematron() {
    String systemCharSchematron = null;
    for (String validationFile : this.validationFiles) {
      if (validationFile.contains("characteristics")) {
        systemCharSchematron = validationFile;
        break;
      }
    }
    return systemCharSchematron;
  }

  /**
   * Returns a list of applicable OVAL schemas based on provided SCAPVersion and ContentType.
   *
   * @param scapVersion
   *          for SCAP validation the SCAP version, for Component file this should be null
   * @param contentType
   *          the type of content under validation, not null
   * @return a LinkedList of StreamSource with each applicable OVAL schema
   */
  public LinkedList<StreamSource> getOVALSchemas(SCAPVersion scapVersion, Application.ContentType contentType)
      throws RuntimeException {
    Objects.requireNonNull(contentType, "contentType cannot be null.");

    // this list will be populated with the applicable OVAL schema files
    LinkedList<StreamSource> schemaList = new LinkedList<>();

    // the OVAL schema directory will depend on the SCAP version/type
    String ovalSchemaDir = null;

    if (contentType.equals(Application.ContentType.RESULT)) {
      // for SCAP Result content use the specified SCAP OVAL schema version per RES-202-1
      ovalSchemaDir = OVALVersion.getByString(scapVersion.getOvalSupportedVersion().getVersionString()).getSchemaDir();
    } else {
      // otherwise use this specified OVALVersion (from oval:schema_version) to define the Schema
      // Directory to pull from
      ovalSchemaDir = this.getSchemaDir();
    }

    try {
      // this file is created when scapval is built with the create-file-list-mojo plugin
      URL ovalSchemaListURL = new URL("classpath:xsd/oval-schema-list.txt");

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(ovalSchemaListURL.openStream()))) {
        String line;
        // each oval directory and files within will be iterated though
        while ((line = reader.readLine()) != null) {
          // prepend the required parent dir
          line = "xsd/" + line;
          // only load the applicable schemas
          if (line.contains(ovalSchemaDir)) {
            if (contentType.equals(Application.ContentType.SOURCE)) {
              // if this is a source check load everything except oval-results-schema
              if (line.startsWith(ovalSchemaDir) && line.endsWith(".xsd") && !line.contains("oval-results-schema")) {
                schemaList.add(new StreamSource("classpath:" + line));
              }
            } else {
              // otherwise load everything else contained in the specified ovalSchemaDir
              if (line.startsWith(ovalSchemaDir) && line.endsWith(".xsd")) {
                schemaList.add(new StreamSource("classpath:" + line));
              }
            }
          }
        }
      }

    } catch (IOException e) {
      throw new RuntimeException("There was a problem loading required OVAL schema files - " + e);
    }

    return schemaList;
  }

}
