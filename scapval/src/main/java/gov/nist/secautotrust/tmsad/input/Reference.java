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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xpath" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="expression" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                 &lt;attribute name="type" use="required" type="{http://scap.nist.gov/tmsad/input}xpath-type" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="uri" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="digest-type" type="{http://scap.nist.gov/tmsad/input}digest-type" default="http://www.w3.org/2001/04/xmlenc#sha512" />
 *       &lt;attribute name="canonicalization" type="{http://scap.nist.gov/tmsad/input}canonicalization-type" default="http://www.w3.org/2006/12/xml-c14n11" />
 *       &lt;attribute name="type" type="{http://scap.nist.gov/tmsad/input}reference-type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "xpath" })
@XmlRootElement(name = "reference")
public class Reference {

  protected List<Reference.Xpath> xpath;
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anyURI")
  protected String uri;
  @XmlAttribute(name = "digest-type")
  protected DigestType digestType;
  @XmlAttribute
  protected CanonicalizationType canonicalization;
  @XmlAttribute
  protected ReferenceType type;

  /**
   * Gets the value of the xpath property.
   *
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the xpath property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getXpath().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Reference.Xpath }
   *
   *
   */
  public List<Reference.Xpath> getXpath() {
    if (xpath == null) {
      xpath = new ArrayList<Reference.Xpath>();
    }
    return this.xpath;
  }

  /**
   * Gets the value of the uri property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getUri() {
    return uri;
  }

  /**
   * Sets the value of the uri property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setUri(String value) {
    this.uri = value;
  }

  /**
   * Gets the value of the digestType property.
   * 
   * @return possible object is {@link DigestType }
   * 
   */
  public DigestType getDigestType() {
    if (digestType == null) {
      return DigestType.HTTP_WWW_W_3_ORG_2001_04_XMLENC_SHA_512;
    } else {
      return digestType;
    }
  }

  /**
   * Sets the value of the digestType property.
   * 
   * @param value
   *          allowed object is {@link DigestType }
   * 
   */
  public void setDigestType(DigestType value) {
    this.digestType = value;
  }

  /**
   * Gets the value of the canonicalization property.
   * 
   * @return possible object is {@link CanonicalizationType }
   * 
   */
  public CanonicalizationType getCanonicalization() {
    if (canonicalization == null) {
      return CanonicalizationType.HTTP_WWW_W_3_ORG_2006_12_XML_C_14_N_11;
    } else {
      return canonicalization;
    }
  }

  /**
   * Sets the value of the canonicalization property.
   * 
   * @param value
   *          allowed object is {@link CanonicalizationType }
   * 
   */
  public void setCanonicalization(CanonicalizationType value) {
    this.canonicalization = value;
  }

  /**
   * Gets the value of the type property.
   * 
   * @return possible object is {@link ReferenceType }
   * 
   */
  public ReferenceType getType() {
    return type;
  }

  /**
   * Sets the value of the type property.
   * 
   * @param value
   *          allowed object is {@link ReferenceType }
   * 
   */
  public void setType(ReferenceType value) {
    this.type = value;
  }

  /**
   * <p>
   * Java class for anonymous complex type.
   * 
   * <p>
   * The following schema fragment specifies the expected content contained within this class.
   * 
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;attribute name="expression" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
   *       &lt;attribute name="type" use="required" type="{http://scap.nist.gov/tmsad/input}xpath-type" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class Xpath {

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String expression;
    @XmlAttribute(required = true)
    protected XpathType type;

    /**
     * Gets the value of the expression property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getExpression() {
      return expression;
    }

    /**
     * Sets the value of the expression property.
     * 
     * @param value
     *          allowed object is {@link String }
     * 
     */
    public void setExpression(String value) {
      this.expression = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link XpathType }
     * 
     */
    public XpathType getType() {
      return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *          allowed object is {@link XpathType }
     * 
     */
    public void setType(XpathType value) {
      this.type = value;
    }

  }

}
