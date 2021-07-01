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

package gov.nist.secautotrust.signer.config;

import gov.nist.secautotrust.signature.KeyInfoBuilder;
import gov.nist.secautotrust.signature.enums.HashType;
import gov.nist.secautotrust.signature.enums.SignatureType;

import java.io.InputStream;
import java.io.OutputStream;

public class ScapResultSignerConfig {
  private final HashType hashType;
  private final InputStream content;
  private final SignatureType sigType;
  private final KeyInfoBuilder keyInfo;
  private final OutputStream os;
  private final boolean isCounterSigning;

  public static class Builder {
    private HashType hashType = HashType.SHA512;
    private InputStream content;
    private SignatureType sigType;
    private KeyInfoBuilder keyInfo;
    private OutputStream os;
    private boolean isCounterSigning;

    public Builder outputStream(OutputStream os) {
      this.os = os;
      return this;
    }

    public Builder hashType(HashType hashType) {
      this.hashType = hashType;
      return this;
    }

    public Builder content(InputStream content) {
      this.content = content;
      return this;
    }

    public Builder sigType(SignatureType sigType) {
      this.sigType = sigType;
      return this;
    }

    public Builder keyInfo(KeyInfoBuilder keyInfo) {
      this.keyInfo = keyInfo;
      return this;
    }

    public Builder isCounterSigning(boolean isCounterSigning) {
      this.isCounterSigning = isCounterSigning;
      return this;
    }

    public ScapResultSignerConfig build() throws IllegalStateException {
      if (hashType == null) {
        throw new IllegalStateException("missing hashType");
      } else if (content == null) {
        throw new IllegalStateException("missing content");
      } else if (sigType == null) {
        throw new IllegalStateException("missing sigType");
      } else if (keyInfo == null) {
        throw new IllegalStateException("missing keyInfo");
      } else if (os == null) {
        throw new IllegalStateException("missing os");
      }
      return new ScapResultSignerConfig(this);
    }
  }

  private ScapResultSignerConfig(Builder builder) {
    hashType = builder.hashType;
    content = builder.content;
    sigType = builder.sigType;
    keyInfo = builder.keyInfo;
    os = builder.os;
    isCounterSigning = builder.isCounterSigning;
  }

  public HashType getHashType() {
    return hashType;
  }

  public InputStream getContent() {
    return content;
  }

  public SignatureType getSigType() {
    return sigType;
  }

  public KeyInfoBuilder getKeyInfo() {
    return keyInfo;
  }

  public OutputStream getOutputStream() {
    return os;
  }

  public boolean isCounterSigning() {
    return isCounterSigning;
  }

}
