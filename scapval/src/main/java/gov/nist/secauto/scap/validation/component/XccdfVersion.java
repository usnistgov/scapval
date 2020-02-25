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

import java.util.Objects;

/**
 * Valid XCCDF versions and their specifics.
 */
public enum XccdfVersion {
  V1_1_4(new String[] { "classpath:xsd/nist/xccdf/1.1/xccdf-1.1.4.xsd" }), // no schematron available for this version
  V1_2(new String[] { "classpath:xsd/nist/xccdf/1.2/xccdf_1.2.xsd", "classpath:rules/other/xccdf-1.2.sch" });

  private String[] validationFiles;
  // private String namespace;

  XccdfVersion(String[] validationFiles) {
    this.validationFiles = validationFiles;
  }

  /**
   * Returns the XccdfVersion enum by version string.
   *
   * @param version
   *          The XCCDF string version to check (e.g. "1.2")
   * @return the matching XccdfVersion enum
   */
  public static XccdfVersion getByString(String version) {
    Objects.requireNonNull(version, "version cannot be null.");
    switch (version) {
    case "1.1.4":
      return XccdfVersion.V1_1_4;
    case "1.2":
      return XccdfVersion.V1_2;
    default:
    }
    return null;
  }

  public String getSchemaLocation() {
    return validationFiles[0];
  }

  /**
   * Returns the schematron location for this given version.
   *
   * @return a string with the location
   */
  public String getSchematron() {
    String defSchematron = null;
    for (String validationFile : this.validationFiles) {
      if (validationFile.contains("sch")) {
        defSchematron = validationFile;
        break;
      }
    }
    return defSchematron;
  }

}
