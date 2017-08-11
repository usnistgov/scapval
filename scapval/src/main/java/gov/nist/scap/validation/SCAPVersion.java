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

import gov.nist.scap.validation.component.*;
import org.jdom2.Namespace;

/** Enumerates the versions of SCAP, associated use Cases,
 * and version specific info (OVAL, OCIL, XCCDF, CPE, CCE, CVSS) */
public enum SCAPVersion {
  V1_1("1.1", new String[] { "CONFIGURATION", "VULNERABILITY_XCCDF_OVAL", "SYSTEM_INVENTORY", "OVAL_ONLY" },
      OVALVersion.V5_8, OCILVersion.V2_0, XCCDFVersion.V1_1_4, CPEVersion.V2_2, CCEVersion.V5, CVSSVersion.V2_0),
  V1_2("1.2", new String[] { "CONFIGURATION", "VULNERABILITY", "INVENTORY", "OTHER" },
      OVALVersion.V5_10_1, OCILVersion.V2_0, XCCDFVersion.V1_2, CPEVersion.V2_3, CCEVersion.V5, CVSSVersion.V2_0),
  V1_3("1.3", new String[] { "CONFIGURATION", "VULNERABILITY", "INVENTORY", "OTHER" },
      OVALVersion.V5_11_2, OCILVersion.V2_0, XCCDFVersion.V1_2, CPEVersion.V2_3, CCEVersion.V5, CVSSVersion.V3_0);

  private String name;
  private String[] useCases;
  private OVALVersion ovalSupportedVersion;
  private OCILVersion ocilSupportedVersion;
  private XCCDFVersion xccdfSupportedVersion;
  private CPEVersion cpeSupportedVersion;
  private CCEVersion cceSupportedVersion;
  private CVSSVersion cvssSupportedVersion;

  SCAPVersion(String name, String[] useCases, OVALVersion ovalSupportedVersion, OCILVersion
      ocilSupportedVersion, XCCDFVersion xccdfSupportedVersion, CPEVersion cpeSupportedVersion,
              CCEVersion cceSupportedVersion, CVSSVersion cvssSupportedVersion) {

    this.name = name;
    this.useCases = useCases;
    this.ovalSupportedVersion = ovalSupportedVersion;
    this.ocilSupportedVersion = ocilSupportedVersion;
    this.xccdfSupportedVersion = xccdfSupportedVersion;
    this.cpeSupportedVersion = cpeSupportedVersion;
    this.cceSupportedVersion = cceSupportedVersion;
    this.cvssSupportedVersion = cvssSupportedVersion;
  }

  public static SCAPVersion getByString(String name) {
    for (SCAPVersion version : SCAPVersion.values()) {
      if (version.getVersion().equals(name)) {
        return version;
      }
    }
    return null;
  }

  public static String getVersionsSupported() {
    StringBuilder builder = new StringBuilder();
    SCAPVersion[] a = values();
    int last = a.length - 1;
    for (int i = 0; ; i++) {
      builder.append(a[i].name);
      if (i == last) {
        return builder.toString();
      }
      builder.append(", ");
    }
  }

  public String getVersion() {
    return this.name;
  }

  public String[] getUseCases() {
    return this.useCases;
  }

  public boolean isUseCaseValid(String usecase) {
    usecase = usecase.toUpperCase();
    String[] cases = this.getUseCases();
    int last = cases.length - 1;
    for (int i = 0; i < last; i++) {
      if (usecase.equals(cases[i])) {
        return true;
      }
    }
    return false;
  }

  public OVALVersion getOvalSupportedVersion() {
    return ovalSupportedVersion;
  }

  public OCILVersion getOcilSupportedVersion() {
    return ocilSupportedVersion;
  }

  public XCCDFVersion getXccdfSupportedVersion() {
    return xccdfSupportedVersion;
  }

  public CPEVersion getCpeSupportedVersion() {
    return cpeSupportedVersion;
  }

  public CCEVersion getCceSupportedVersion() {
    return cceSupportedVersion;
  }

  public CVSSVersion getCvssSupportedVersion() {
    return cvssSupportedVersion;
  }

  public Namespace getDSNamespace() {
    switch (this) {
      case V1_1:
        return NamespaceConstants.NS_SOURCE_DS_1_1.getNamespace();
      case V1_2:
        return NamespaceConstants.NS_SOURCE_DS_1_2.getNamespace();
      case V1_3:
        return NamespaceConstants.NS_SOURCE_DS_1_3.getNamespace();
    }
    return null;
  }
}
