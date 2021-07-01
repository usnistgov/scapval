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

import java.util.LinkedList;
import java.util.List;

/**
 * Default manifest builder
 */
public class DefaultManifestBuilder implements ManifestBuilder {
  private String id;
  private List<DefaultReferenceBuilder> refBuilders;
  private CanonicalizationType canonicalizationType;
  private HashType hashType;

  DefaultManifestBuilder() {
    this.refBuilders = new LinkedList<DefaultReferenceBuilder>();
  }

  public ManifestBuilder id(String id) {
    this.id = id;
    return this;
  }

  public ManifestBuilder canonicalizationType(CanonicalizationType canonicalizationType) {
    this.canonicalizationType = canonicalizationType;
    return this;
  }

  public ManifestBuilder hashType(HashType hashType) {
    this.hashType = hashType;
    return this;
  }

  ManifestSigReference build(SignatureContext context) throws IllegalStateException {
    if (id == null || refBuilders.size() == 0) {
      throw new IllegalStateException("missing required parameters");
    }
    LinkedList<DefaultSigReference> references = new LinkedList<DefaultSigReference>();
    for (DefaultReferenceBuilder builder : refBuilders) {
      references.add(builder.build(context));
    }
    return new ManifestSigReference("#" + id, references, canonicalizationType, hashType);
  }

  @Override
  public ReferenceBuilder newEnvelopedReferenceBuilder() {
    DefaultReferenceBuilder builder = new DefaultReferenceBuilder(SignatureRelationship.ENVELOPED);
    this.refBuilders.add(builder);
    return builder;
  }

  @Override
  public ReferenceBuilder newEnvelopingReferenceBuilder() {
    DefaultReferenceBuilder builder = new DefaultReferenceBuilder(SignatureRelationship.ENVELOPING);
    this.refBuilders.add(builder);
    return builder;
  }

  public ReferenceBuilder newDetachedReferenceBuilder() {
    DefaultReferenceBuilder builder = new DefaultReferenceBuilder(SignatureRelationship.DETACHED);
    this.refBuilders.add(builder);
    return builder;
  }
}
