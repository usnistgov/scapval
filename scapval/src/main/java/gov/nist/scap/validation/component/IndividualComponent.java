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

package gov.nist.scap.validation.component;

import gov.nist.decima.xml.document.XMLDocument;
import gov.nist.scap.validation.NamespaceConstants;

import java.util.Objects;

/**
 * Valid Components for -componentfile checks and their specifics.
 */
public enum IndividualComponent {
  // each associated namespace, can be used to identify component types
  XCCDF_1_1_4(NamespaceConstants.NS_XCCDF_1_1_4.getNamespaceString(), "XCCDF 1.1.4"),
  XCCDF_1_2(NamespaceConstants.NS_XCCDF_1_2.getNamespaceString(), "XCCDF 1.2"),
  OVAL_DEF(NamespaceConstants.NS_OVAL_DEF_5.getNamespaceString(), "OVAL Definitions"),
  OVAL_RES(NamespaceConstants.NS_OVAL_RES_5.getNamespaceString(), "OVAL Results"),
  OCIL(NamespaceConstants.NS_OCIL_2_0.getNamespaceString(), "OCIL");

  private String namespace;
  private String name;

  IndividualComponent(String namespace, String name) {
    this.namespace = namespace;
    this.name = name;
  }

  /**
   * Returns the type of IndividualComponent by a provided namespace String.
   *
   * @param namespace
   *          the namespace string of desired component
   * @return the matching IndividualComponent
   */
  public static IndividualComponent getByNamespace(String namespace) {
    Objects.requireNonNull(namespace, "namespace cannot be null.");
    for (IndividualComponent component : IndividualComponent.values()) {
      if (component.namespace.equals(namespace)) {
        return component;
      }
    }
    return null;
  }

  /**
   * Returns the type of IndividualComponent found in the provided XML Document.
   *
   * @param xmlDocument
   *          the XML document of the IndividualComponent
   * @return the matching IndividualComponent
   */
  public static IndividualComponent getByXML(XMLDocument xmlDocument) {
    Objects.requireNonNull(xmlDocument, "xmlDocument cannot be null.");
    String namespace = xmlDocument.getJDOMDocument().getRootElement().getNamespaceURI();
    return IndividualComponent.getByNamespace(namespace);
  }

  /**
   * Provides all of the supported component namespaces.
   *
   * @return a String containing all of the supported component namesapces
   */
  public static String getAllComponentNamespaces() {
    StringBuilder retval = new StringBuilder();
    for (IndividualComponent component : IndividualComponent.values()) {
      retval.append(" " + component.namespace + " ");
    }
    return retval.toString();
  }

  public String getName() {
    return name;
  }
}
