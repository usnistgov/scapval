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

package gov.nist.secautotrust.signature.config;

import gov.nist.secautotrust.signature.UriResolver;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

public class ValidateSigConfig {
  private final InputStream content;
  private final PublicKey trustedPublicKey;

  private final List<Node> signatureNodes;
  private final boolean checkCounterSign;
  private final UriResolver resolver;

  public static class Builder {
    private InputStream content;
    private PublicKey trustedPublicKey;
    private List<Node> signatureNodes;
    private boolean checkCounterSign;
    private UriResolver resolver;

    public Builder() {
      signatureNodes = new LinkedList<Node>();
    }

    public Builder content(InputStream content) {
      this.content = content;
      return this;
    }

    public Builder trustedPublicKey(PublicKey publicKey) {
      this.trustedPublicKey = publicKey;
      return this;
    }

    public Builder addSignatureNode(Node node) {
      this.signatureNodes.add(node);
      return this;
    }

    public Builder addSignatureNodes(Collection<Node> nodes) {
      this.signatureNodes.addAll(nodes);
      return this;
    }

    public Builder resolver(UriResolver resolver) {
      this.resolver = resolver;
      return this;
    }

    public Builder checkCounterSign(boolean validate) {
      this.checkCounterSign = validate;
      return this;
    }

    public ValidateSigConfig build() throws IllegalStateException {
      if ((content == null && signatureNodes.size() == 0) || trustedPublicKey == null) {
        throw new IllegalStateException("missing required parameters");
      }
      if (content != null && signatureNodes.size() > 0) {
        throw new IllegalStateException("cannot supply both the content and nodes to validate");
      }
      return new ValidateSigConfig(this);
    }
  }

  private ValidateSigConfig(Builder builder) {
    this.content = builder.content;
    this.signatureNodes = builder.signatureNodes;
    this.trustedPublicKey = builder.trustedPublicKey;
    this.resolver = builder.resolver;
    this.checkCounterSign = builder.checkCounterSign;
  }

  public InputStream getContent() {
    return content;
  }

  public PublicKey getTrustedPublicKey() {
    return trustedPublicKey;
  }

  public UriResolver getResolver() {
    return resolver;
  }

  public boolean isCheckCounterSign() {
    return checkCounterSign;
  }

  public List<Node> getSignatureNodes() {
    return Collections.unmodifiableList(signatureNodes);
  }
}
