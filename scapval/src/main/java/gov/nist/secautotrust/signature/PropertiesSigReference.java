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

package gov.nist.secautotrust.signature;

import gov.nist.secautotrust.signature.enums.CanonicalizationType;
import gov.nist.secautotrust.signature.enums.HashType;
import gov.nist.secautotrust.signature.model.SigObject;
import gov.nist.secautotrust.util.Util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.SignatureProperties;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * An implementation of the properties section in a signature
 */
class PropertiesSigReference
    extends DefaultSigReference
    implements SigObject {
  private static final String DSIG_NAMESPACE = "http://scap.nist.gov/schema/xml-dsig/1.0";
  private static final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1/";
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  public static class Builder {
    private Date timestamp;
    private long nonce;
    private List<String> creators;
    private Element element;

    private CanonicalizationType canonicalizationType;
    private HashType hashType;

    public Builder canonicalizationType(CanonicalizationType canonicalizationType) {
      this.canonicalizationType = canonicalizationType;
      return this;
    }

    public Builder hashType(HashType hashType) {
      this.hashType = hashType;
      return this;
    }

    public Builder timestamp(Date timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder nonce(long nonce) {
      this.nonce = nonce;
      return this;
    }

    public Builder creators(List<String> creators) {
      this.creators = creators;
      return this;
    }

    public Builder element(Element element) {
      this.element = element;
      return this;
    }

    public PropertiesSigReference build() {
      if (hashType == null || canonicalizationType == null || ((timestamp == null || nonce == 0) && element == null)) {
        throw new IllegalStateException("missing required parameters");
      }
      if ((timestamp != null || nonce != 0) && element != null) {
        throw new IllegalStateException("only 1 element or (timestamp/nonce) may be specified");
      }

      if (element != null)
        return new PropertiesSigReference(element, canonicalizationType, hashType);
      else
        return new PropertiesSigReference(timestamp, nonce, creators, canonicalizationType, hashType);
    }

  }

  private Date timestamp;
  private long nonce;
  private List<String> creators;
  private Element element;

  private PropertiesSigReference(CanonicalizationType canonicalizationType, HashType hashType) {
    super(SignatureRelationship.ENVELOPING, "#" + Util.generateId("sig-props"), canonicalizationType,
        (List<XPathType>) null, (Element) null, SignatureProperties.TYPE, hashType);

  }

  public PropertiesSigReference(Date timestamp, long nonce, List<String> creators,
      CanonicalizationType canonicalizationType, HashType hashType) {
    this(canonicalizationType, hashType);
    this.timestamp = timestamp;
    this.nonce = nonce;
    this.creators = creators;
  }

  public PropertiesSigReference(Element element, CanonicalizationType canonicalizationType, HashType hashType) {
    this(canonicalizationType, hashType);
    this.element = element;
  }

  @Override
  public XMLObject createObject(Signature signer) throws Exception {

    Element ele;
    if (element != null) {
      ele = element;
    } else {
      DocumentBuilderFactory dbf = signer.getDocumentBuilderFactory();

      Document doc = dbf.newDocumentBuilder().newDocument();
      Element root = doc.createElementNS(DSIG_NAMESPACE, "dsig:signature-info");
      root.setAttribute("xmlns:dsig", DSIG_NAMESPACE);
      root.setAttribute("xmlns:dc", DC_NAMESPACE);
      if (creators != null) {
        for (String creator : creators) {
          Element c = doc.createElementNS(DC_NAMESPACE, "dc:creator");
          c.setTextContent(creator);
          root.appendChild(c);
        }
      }
      Element ts = doc.createElementNS(DC_NAMESPACE, "dc:date");
      ts.setTextContent(sdf.format(timestamp));
      root.appendChild(ts);

      Element nonceElement = doc.createElementNS(DSIG_NAMESPACE, "dsig:nonce");
      nonceElement.setTextContent(Long.toHexString(nonce));
      root.appendChild(nonceElement);
      ele = root;
    }

    XMLSignatureFactory factory = signer.getSignatureFactory();
    return factory
        .newXMLObject(
            Collections
                .singletonList(factory.newSignatureProperties(
                    Collections.singletonList(factory.newSignatureProperty(
                        Collections.singletonList(new DOMStructure(ele)), "#" + signer.getId(), (String) null)),
                    getUri().substring(1))),
            (String) null, (String) null, (String) null);

  }
}