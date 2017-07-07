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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enumerates the valid component files of SCAP 1.1 and their associated characteristics.
 * This is not required for SCAP 1.2/1.3 versions as they implement a native data-stream approach
 * The order of these matter for some of the methods which iterates the fileSourceNameSuffix with .endswith()
 */
public enum SCAP11Components {
  CPE_DICTIONARY("cpe-dictionary.xml", "cpe-dictionary-res.xml", "<cpe-dict:cpe-list>", "cpe-dictionary-content"),
  CPE_INVENTORY("cpe-oval.xml", "cpe-oval-res.xml", "<oval-def:oval_definitions>", "check-system-content"),
  XCCDF_BENCHMARK("xccdf.xml", "xccdf-res.xml", "<xccdf:Benchmark>", "xccdf-content"),
  OVAL_COMPLIANCE("oval.xml", "oval-res.xml", "<oval-def:oval_definitions>", "check-system-content"),
  OVAL_PATCH("patches.xml", "patches-res.xml", "<oval-def:oval_definitions>", "check-system-content"),
  OVAL_VULNERABILITY("oval.xml", "oval-res.xml", "<oval-def:oval_definitions>", "check-system-content"),
  OCIL_QUESTIONNAIRE("ocil.xml", "ocil-res.xml", "<ocil:ocil>", "check-system-content");

  private String fileSourceNameSuffix;
  private String fileResultsNameSuffix;
  private String documentElement;
  private String combinedDocLocalName;

  SCAP11Components(String fileNameSuffix, String fileResultsNameSuffix, String documentElement,
                   String combinedDocLocalName) {
    this.fileSourceNameSuffix = fileNameSuffix;
    this.fileResultsNameSuffix = fileResultsNameSuffix;
    this.documentElement = documentElement;
    this.combinedDocLocalName = combinedDocLocalName;
  }

  public static SCAP11Components getByFileNameAndUseCase(String fileName, String scapUseCase) {
    Objects.requireNonNull(fileName, "fileName cannot be null.");
    Objects.requireNonNull(scapUseCase, "scapUseCase cannot be null.");
    for (SCAP11Components component : SCAP11Components.values()) {
      if (fileName.endsWith(component.getFileSourceNameSuffix())) {
        //OVAL_COMPLIANCE or OVAL_VULNERABILITY have the same file prefix so usecase
        //must be used to determine which. Otherwise return solely on filename prefix
        if (fileName.endsWith("oval.xml") && !fileName.endsWith("cpe-oval.xml")) {
          return (scapUseCase.equals("CONFIGURATION")) ? OVAL_COMPLIANCE : OVAL_VULNERABILITY;
        }
        return component;
      }
      if (fileName.endsWith(component.getResultsFileNameSuffix())) {
        //OVAL_COMPLIANCE or OVAL_VULNERABILITY have the same file prefix so usecase
        //must be used to determine which. Otherwise return solely on filename prefix
        if (fileName.endsWith("oval-res.xml") && !fileName.endsWith("cpe-oval-res.xml")) {
          return (scapUseCase.equals("CONFIGURATION")) ? OVAL_COMPLIANCE : OVAL_VULNERABILITY;
        }
        return component;
      }
    }
    return null;
  }

  public static String[] getSourceFileNameSuffixes() {
    List<String> returnValue = new ArrayList<String>();
    for (SCAP11Components components : SCAP11Components.values()) {
      returnValue.add(components.getFileSourceNameSuffix());
    }
    return returnValue.toArray(new String[0]);
  }

  public static String[] getResultFileNameSuffixes() {
    List<String> returnValue = new ArrayList<String>();
    for (SCAP11Components components : SCAP11Components.values()) {
      returnValue.add(components.getResultsFileNameSuffix());
    }
    return returnValue.toArray(new String[0]);
  }

  public static boolean isValidSCAP11FileName(String fileName) {
    Objects.requireNonNull(fileName, "fileName cannot be null.");
    boolean isValidFileNameSuffix = false;
    for (String validSuffix : SCAP11Components.getSourceFileNameSuffixes()) {
      if (fileName.endsWith(validSuffix)) {
        isValidFileNameSuffix = true;
      }
    }
    for (String validSuffix : SCAP11Components.getResultFileNameSuffixes()) {
      if (fileName.endsWith(validSuffix)) {
        isValidFileNameSuffix = true;
      }
    }
    return isValidFileNameSuffix;
  }

  public String getFileSourceNameSuffix() {
    return this.fileSourceNameSuffix;
  }

  public String getResultsFileNameSuffix() {
    return this.fileResultsNameSuffix;
  }

  public String[] getFileNameSuffixes() {
    return new String[]{this.fileSourceNameSuffix, this.fileResultsNameSuffix};
  }

  public String getDocumentElement() {
    return this.documentElement;
  }

  public String getCombinedDocLocalName() {
    return combinedDocLocalName;
  }
}
