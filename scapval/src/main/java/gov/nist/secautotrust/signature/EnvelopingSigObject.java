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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.nist.secautotrust.signature.enums.CanonicalizationType;
import gov.nist.secautotrust.signature.enums.HashType;
import gov.nist.secautotrust.signature.model.SigObject;
import gov.nist.secautotrust.signature.model.SigReference;

import java.io.*;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Implementation of an enveloping signature object
 */
class EnvelopingSigObject
    extends DefaultSigReference
    implements SigReference, SigObject {

  public static class Builder {
    private CanonicalizationType canonicalizationType;
    private HashType hashType;
    private String id;
    private InputStream content;
    private Element element;

    public Builder canonicalizationType(CanonicalizationType canonicalizationType) {
      this.canonicalizationType = canonicalizationType;
      return this;
    }

    public Builder hashType(HashType hashType) {
      this.hashType = hashType;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder content(InputStream content) {
      this.content = content;
      return this;
    }

    public Builder element(Element element) {
      this.element = element;
      return this;
    }

    public EnvelopingSigObject build() {
      if (hashType == null || canonicalizationType == null || id == null || (content == null && element == null)) {
        throw new IllegalStateException("missing required parameters");
      }
      if (content != null && element != null) {
        throw new IllegalStateException("only 1 of content or element may be set");
      }

      if (content != null)
        return new EnvelopingSigObject(id, content, canonicalizationType, hashType);
      else
        return new EnvelopingSigObject(id, element, canonicalizationType, hashType);
    }

  }

  /** the content stream */
  private InputStream content;
  private Element element;
  /** the id of the xml object */
  private String id;

  EnvelopingSigObject(String id, InputStream content, CanonicalizationType canonicalizationType, HashType hashType) {
    super(SignatureRelationship.ENVELOPING, "#" + id, canonicalizationType, (List<XPathType>) null, (Element) null,
        XMLObject.TYPE, hashType);
    this.content = content;
    this.id = id;
  }

  EnvelopingSigObject(String id, Element element, CanonicalizationType canonicalizationType, HashType hashType) {
    super(SignatureRelationship.ENVELOPING, "#" + id, canonicalizationType, (List<XPathType>) null, (Element) null,
        XMLObject.TYPE, hashType);
    this.element = element;
    this.id = id;
  }

  @Override
  public XMLObject createObject(Signature signer) throws Exception {
    XMLSignatureFactory factory = signer.getSignatureFactory();
    if (element == null) {
      DocumentBuilderFactory dbf = signer.getDocumentBuilderFactory();
      Document doc = dbf.newDocumentBuilder().parse(content);
      element = doc.getDocumentElement();
    }
    return factory.newXMLObject(Collections.singletonList(new DOMStructure(element)), id, (String) null, (String) null);
  }

}
