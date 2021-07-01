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

import gov.nist.secautotrust.signature.enums.HashType;
import gov.nist.secautotrust.signature.enums.SignatureType;

import java.io.OutputStream;

public class ScapDataStreamSignerConfig {

  private final HashType hashType;
  private final String content;
  private final SignatureType sigType;
  private final OutputStream os;
  private final String keyStore;
  private final String alias;
  private String outputFileLocation;

  public static class Builder {
    private HashType hashType = HashType.SHA512;
    private String content;
    private SignatureType sigType;
    private OutputStream os;
    private String keystore;
    private String alias;
    private String outputFileLocation;

    public Builder outputStream(OutputStream os) {
      this.os = os;
      return this;
    }

    public Builder hashType(HashType hashType) {
      this.hashType = hashType;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder sigType(SignatureType sigType) {
      this.sigType = sigType;
      return this;
    }

    public Builder keystore(String keystore) {
      this.keystore = keystore;
      return this;
    }

    public Builder alias(String alias) {
      this.alias = alias;
      return this;
    }

    public Builder outputFileLocation(String outputFileLocation) {
      this.outputFileLocation = outputFileLocation;
      return this;
    }

    public ScapDataStreamSignerConfig build() throws IllegalStateException {
      if (hashType == null) {
        throw new IllegalStateException("missing hashType");
      } else if (content == null) {
        throw new IllegalStateException("missing content");
      } else if (sigType == null) {
        throw new IllegalStateException("missing sigType");
      } else if (keystore == null) {
        throw new IllegalStateException("missing keystore");
      } else if (alias == null) {
        throw new IllegalStateException("missing alias");
      } else if (os == null) {
        throw new IllegalStateException("missing os");
      } else if (outputFileLocation == null) {
        throw new IllegalStateException("missing outputFileLocation");
      }

      return new ScapDataStreamSignerConfig(this);
    }
  }

  private ScapDataStreamSignerConfig(Builder builder) {
    hashType = builder.hashType;
    content = builder.content;
    sigType = builder.sigType;
    keyStore = builder.keystore;
    alias = builder.alias;
    os = builder.os;
    outputFileLocation = builder.outputFileLocation;
  }

  public HashType getHashType() {
    return hashType;
  }

  public String getContent() {
    return content;
  }

  public SignatureType getSigType() {
    return sigType;
  }

  public String getKeyStore() {
    return keyStore;
  }

  public String getAlias() {
    return alias;
  }

  public OutputStream getOutputStream() {
    return os;
  }

  public String getOutputFileLocation() {
    return outputFileLocation;
  }

}
