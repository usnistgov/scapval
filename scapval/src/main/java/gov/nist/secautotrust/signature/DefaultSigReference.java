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
import gov.nist.secautotrust.signature.model.SigReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

import org.w3c.dom.Element;

/**
 * A default implementation of a reference contained within a signature block
 */
class DefaultSigReference implements SigReference {
  private SignatureRelationship relationship;
  private String uri;
  private CanonicalizationType canonicalization = CanonicalizationType.INCLUSIVE_1_1;
  private List<XPathType> xpath;
  private Element transform;
  private String type;
  private HashType hashType = HashType.SHA512;

  DefaultSigReference(SignatureRelationship relationship, String uri, CanonicalizationType canonicalization,
      List<XPathType> xpath, Element transform, String type, HashType hashType) {
    this.relationship = relationship;
    this.uri = uri;
    if (canonicalization != null)
      this.canonicalization = canonicalization;
    this.xpath = xpath;
    this.transform = transform;
    this.type = type;
    if (this.xpath == null) {
      this.xpath = Collections.emptyList();
    }
    if (hashType != null)
      this.hashType = hashType;
  }

  public String getUri() {
    return uri;
  }

  public Reference createReference(Signature signer) throws Exception {
    XMLSignatureFactory factory = signer.getSignatureFactory();

    // apply transform in the following order:
    // Enveloped Transform (remove the signature block)
    // XPATH2 Filter
    // XSLT Transform only if output is well-formed XML
    // XML Canonicalization (only if there are no additional XSLT transforms)
    // XSLT Transform which output non-xml format

    // Build the list of transforms for this reference
    List<Transform> transformList = new ArrayList<Transform>();
    // If enveloped, then add the appropriate transform
    if (relationship == SignatureRelationship.ENVELOPED) {
      transformList.add(factory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
    }
    // now check to see if canonicalization is needed
    if (canonicalization != null) {
      transformList.add(factory.newTransform(canonicalization.toUriString(), (TransformParameterSpec) null));
    }

    // If the client specified additional XPaths, then apply them
    if (!xpath.isEmpty()) {
      transformList.add(factory.newTransform(Transform.XPATH2, new XPathFilter2ParameterSpec(xpath)));
    }

    // if the client specified an XSLT, then apply it
    if (transform != null) {
      transformList
          .add(factory.newTransform(Transform.XSLT, new XSLTTransformParameterSpec(new DOMStructure(transform))));
    }
    return factory.newReference(uri, factory.newDigestMethod(hashType.toUriString(), null), transformList, type,
        (String) null);
  }
}