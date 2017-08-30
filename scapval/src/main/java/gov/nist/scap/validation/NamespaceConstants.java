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

import org.jdom2.Namespace;

/** Namespaces used throughout SCAPVal. */
public enum NamespaceConstants {
  NS_SOURCE_DS_1_3("http://scap.nist.gov/schema/scap/source/1.2"),
  NS_SOURCE_DS_1_2("http://scap.nist.gov/schema/scap/source/1.2"),
  NS_SOURCE_DS_1_1("http://scap.nist.gov/schema/data-stream/0.2"),
  NS_RESULTS_DS_1_2("http://scap.nist.gov/schema/asset-reporting-format/1.1"),
  NS_XCCDF_1_1_4("http://checklists.nist.gov/xccdf/1.1"),
  NS_XCCDF_1_2("http://checklists.nist.gov/xccdf/1.2"),
  NS_OCIL_2_0("http://scap.nist.gov/schema/ocil/2.0"),
  NS_OVAL_COM_5("http://oval.mitre.org/XMLSchema/oval-common-5"),
  NS_OVAL_DEF_5("http://oval.mitre.org/XMLSchema/oval-definitions-5"),
  NS_OVAL_RES_5("http://oval.mitre.org/XMLSchema/oval-results-5"),
  NS_CPE_DICT_2("http://cpe.mitre.org/dictionary/2.0"),
  NS_XLINK("http://www.w3.org/1999/xlink"),
  NS_ARF_1_1("http://scap.nist.gov/schema/asset-reporting-format/1.1");

  private String namespace;

  NamespaceConstants(String namespace) {
    this.namespace = namespace;
  }

  public Namespace getNamespace() {
    return Namespace.getNamespace(this.namespace);
  }

  public String getNamespaceString() {
    return this.namespace;
  }
}
