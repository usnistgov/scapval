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

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.dsig.spec.XPathType;

import org.w3c.dom.Element;

/**
 * A reference builder for building default references.
 */
class DefaultReferenceBuilder implements ReferenceBuilder {
  private InputStream content;
  private SignatureRelationship relationship;
  private String uri;
  private CanonicalizationType canonicalization = CanonicalizationType.INCLUSIVE_1_1;
  private Element transform;
  private List<XPathType> xpath = new LinkedList<XPathType>();
  private String type;
  private HashType hashType = HashType.SHA512;

  DefaultReferenceBuilder(SignatureRelationship relationship) {
    if (relationship == null) {
      throw new NullPointerException("relationship cannot be null");
    }
    this.relationship = relationship;
  }

  public ReferenceBuilder content(InputStream content) {
    this.content = content;
    return this;
  }

  public ReferenceBuilder uri(String uri) {
    this.uri = uri;
    return this;
  }

  public ReferenceBuilder canonicalization(CanonicalizationType canonicalization) {
    this.canonicalization = canonicalization;
    return this;
  }

  public ReferenceBuilder transform(Element transform) {
    this.transform = transform;
    return this;
  }

  public ReferenceBuilder xpath(XPathType xpath) {
    this.xpath.add(xpath);
    return this;
  }

  public ReferenceBuilder type(String type) {
    this.type = type;
    return this;
  }

  private boolean isLocal() {
    return uri.length() == 0 || uri.charAt(0) == '#';
  }

  @Override
  public ReferenceBuilder hashType(HashType hashType) {
    this.hashType = hashType;
    return this;
  }

  DefaultSigReference build(SignatureContext context) throws IllegalStateException {
    if (uri == null) {
      throw new IllegalStateException("missing required parameters");
    }
    if (content != null && !isLocal()) {
      context.addInputStream(uri, content);
    }
    return new DefaultSigReference(relationship, uri, canonicalization, xpath, transform, type, hashType);
  }
}
