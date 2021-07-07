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

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="key-info">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="key-store" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="alias" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="insert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="insert-xpath" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *                 &lt;attribute name="insert-as-sibling" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://scap.nist.gov/tmsad/input}reference" maxOccurs="unbounded"/>
 *         &lt;element name="manifest" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://scap.nist.gov/tmsad/input}reference" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="signature-property" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any processContents='lax' namespace='##other'/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="creators" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="signature-type" use="required" type="{http://scap.nist.gov/tmsad/input}signature-type" />
 *       &lt;attribute name="canonicalization" type="{http://scap.nist.gov/tmsad/input}canonicalization-type" default="http://www.w3.org/2006/12/xml-c14n11" />
 *       &lt;attribute name="digest-type" type="{http://scap.nist.gov/tmsad/input}digest-type" default="http://www.w3.org/2001/04/xmlenc#sha512" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "keyInfo", "insert", "reference", "manifest", "signatureProperty", "creators" })
@XmlRootElement(name = "sign")
public class Sign {

  @XmlElement(name = "key-info", required = true)
  protected Sign.KeyInfo keyInfo;
  protected Sign.Insert insert;
  @XmlElement(required = true)
  protected List<Reference> reference;
  protected List<Sign.Manifest> manifest;
  @XmlElement(name = "signature-property")
  protected List<Sign.SignatureProperty> signatureProperty;
  protected List<String> creators;
  @XmlAttribute(name = "signature-type", required = true)
  protected SignatureType signatureType;
  @XmlAttribute
  protected CanonicalizationType canonicalization;
  @XmlAttribute(name = "digest-type")
  protected DigestType digestType;

  /**
   * Gets the value of the keyInfo property.
   *
   * @return possible object is {@link Sign.KeyInfo }
   *
   */
  public Sign.KeyInfo getKeyInfo() {
    return keyInfo;
  }

  /**
   * Sets the value of the keyInfo property.
   *
   * @param value
   *          allowed object is {@link Sign.KeyInfo }
   *
   */
  public void setKeyInfo(Sign.KeyInfo value) {
    this.keyInfo = value;
  }

  /**
   * Gets the value of the insert property.
   *
   * @return possible object is {@link Sign.Insert }
   *
   */
  public Sign.Insert getInsert() {
    return insert;
  }

  /**
   * Sets the value of the insert property.
   *
   * @param value
   *          allowed object is {@link Sign.Insert }
   *
   */
  public void setInsert(Sign.Insert value) {
    this.insert = value;
  }

  /**
   * A signature reference. This reference MUST be resolvable, and validate, in order to verify the
   * signature.Gets the value of the reference property.
   *
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the reference property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getReference().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Reference }
   *
   *
   */
  public List<Reference> getReference() {
    if (reference == null) {
      reference = new ArrayList<Reference>();
    }
    return this.reference;
  }

  /**
   * Gets the value of the manifest property.
   *
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the manifest property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getManifest().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Sign.Manifest }
   *
   *
   */
  public List<Sign.Manifest> getManifest() {
    if (manifest == null) {
      manifest = new ArrayList<Sign.Manifest>();
    }
    return this.manifest;
  }

  /**
   * Gets the value of the signatureProperty property.
   *
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the signatureProperty property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getSignatureProperty().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Sign.SignatureProperty }
   *
   *
   */
  public List<Sign.SignatureProperty> getSignatureProperty() {
    if (signatureProperty == null) {
      signatureProperty = new ArrayList<Sign.SignatureProperty>();
    }
    return this.signatureProperty;
  }

  /**
   * Gets the value of the creators property.
   *
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the creators property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getCreators().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   *
   *
   */
  public List<String> getCreators() {
    if (creators == null) {
      creators = new ArrayList<String>();
    }
    return this.creators;
  }

  /**
   * Gets the value of the signatureType property.
   *
   * @return possible object is {@link SignatureType }
   *
   */
  public SignatureType getSignatureType() {
    return signatureType;
  }

  /**
   * Sets the value of the signatureType property.
   *
   * @param value
   *          allowed object is {@link SignatureType }
   *
   */
  public void setSignatureType(SignatureType value) {
    this.signatureType = value;
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
   *       &lt;attribute name="insert-xpath" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
   *       &lt;attribute name="insert-as-sibling" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class Insert {

    @XmlAttribute(name = "insert-xpath", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String insertXpath;
    @XmlAttribute(name = "insert-as-sibling")
    protected Boolean insertAsSibling;

    /**
     * Gets the value of the insertXpath property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getInsertXpath() {
      return insertXpath;
    }

    /**
     * Sets the value of the insertXpath property.
     *
     * @param value
     *          allowed object is {@link String }
     *
     */
    public void setInsertXpath(String value) {
      this.insertXpath = value;
    }

    /**
     * Gets the value of the insertAsSibling property.
     *
     * @return possible object is {@link Boolean }
     *
     */
    public boolean isInsertAsSibling() {
      if (insertAsSibling == null) {
        return false;
      } else {
        return insertAsSibling;
      }
    }

    /**
     * Sets the value of the insertAsSibling property.
     *
     * @param value
     *          allowed object is {@link Boolean }
     *
     */
    public void setInsertAsSibling(Boolean value) {
      this.insertAsSibling = value;
    }

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
   *       &lt;attribute name="key-store" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
   *       &lt;attribute name="alias" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class KeyInfo {

    @XmlAttribute(name = "key-store", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String keyStore;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String alias;

    /**
     * Gets the value of the keyStore property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getKeyStore() {
      return keyStore;
    }

    /**
     * Sets the value of the keyStore property.
     *
     * @param value
     *          allowed object is {@link String }
     *
     */
    public void setKeyStore(String value) {
      this.keyStore = value;
    }

    /**
     * Gets the value of the alias property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAlias() {
      return alias;
    }

    /**
     * Sets the value of the alias property.
     *
     * @param value
     *          allowed object is {@link String }
     *
     */
    public void setAlias(String value) {
      this.alias = value;
    }

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
   *       &lt;sequence>
   *         &lt;element ref="{http://scap.nist.gov/tmsad/input}reference" maxOccurs="unbounded"/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = { "reference" })
  public static class Manifest {

    @XmlElement(required = true)
    protected List<Reference> reference;

    /**
     * A reference on the manifest. This reference need not validate in order to verify the
     * signature.Gets the value of the reference property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is why
     * there is not a <CODE>set</CODE> method for the reference property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getReference().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Reference }
     *
     *
     */
    public List<Reference> getReference() {
      if (reference == null) {
        reference = new ArrayList<Reference>();
      }
      return this.reference;
    }

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
   *       &lt;sequence>
   *         &lt;any processContents='lax' namespace='##other'/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = { "any" })
  public static class SignatureProperty {

    @XmlAnyElement(lax = true)
    protected Object any;

    /**
     * Gets the value of the any property.
     * 
     * @return possible object is {@link Element } {@link Object }
     * 
     */
    public Object getAny() {
      return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *          allowed object is {@link Element } {@link Object }
     * 
     */
    public void setAny(Object value) {
      this.any = value;
    }

  }

}