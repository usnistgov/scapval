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
//

// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.02 at 03:42:10 PM PST 
//

package gov.nist.secautotrust.tmsad.input;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for canonicalization-type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="canonicalization-type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
 *     &lt;enumeration value="http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"/>
 *     &lt;enumeration value="http://www.w3.org/2006/12/xml-c14n11"/>
 *     &lt;enumeration value="http://www.w3.org/2006/12/xml-c14n11#WithComments"/>
 *     &lt;enumeration value="http://www.w3.org/2001/10/xml-exc-c14n#"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "canonicalization-type")
@XmlEnum
public enum CanonicalizationType {

  @XmlEnumValue("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")
  HTTP_WWW_W_3_ORG_TR_2001_REC_XML_C_14_N_20010315("http://www.w3.org/TR/2001/REC-xml-c14n-20010315"),
  @XmlEnumValue("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")
  HTTP_WWW_W_3_ORG_TR_2001_REC_XML_C_14_N_20010315_WITH_COMMENTS(
      "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"),
  @XmlEnumValue("http://www.w3.org/2006/12/xml-c14n11")
  HTTP_WWW_W_3_ORG_2006_12_XML_C_14_N_11("http://www.w3.org/2006/12/xml-c14n11"),
  @XmlEnumValue("http://www.w3.org/2006/12/xml-c14n11#WithComments")
  HTTP_WWW_W_3_ORG_2006_12_XML_C_14_N_11_WITH_COMMENTS("http://www.w3.org/2006/12/xml-c14n11#WithComments"),
  @XmlEnumValue("http://www.w3.org/2001/10/xml-exc-c14n#")
  HTTP_WWW_W_3_ORG_2001_10_XML_EXC_C_14_N("http://www.w3.org/2001/10/xml-exc-c14n#");

  private final String value;

  CanonicalizationType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static CanonicalizationType fromValue(String v) {
    for (CanonicalizationType c : CanonicalizationType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}