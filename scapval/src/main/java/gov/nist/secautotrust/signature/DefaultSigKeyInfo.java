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

import gov.nist.secautotrust.signature.model.SigKeyInfo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

/**
 * A default implementation for KeyInfo block within a signature
 */
class DefaultSigKeyInfo implements SigKeyInfo {
  private PublicKey key;
  private PrivateKey privateKey;
  private List<X509Certificate> certs;
  private static String SKI_OID = "2.5.29.14";

  DefaultSigKeyInfo(PublicKey key, PrivateKey privateKey) {
    this.key = key;
    this.privateKey = privateKey;
  }

  public void setCerts(List<X509Certificate> certs) {
    this.certs = certs;
  }

  @Override
  public KeyInfo createKeyInfo(Signature sign) throws Exception {
    // Create a KeyValue containing the PublicKey
    KeyInfoFactory kif = sign.getSignatureFactory().getKeyInfoFactory();
    KeyValue kv = kif.newKeyValue(key);
    List<XMLStructure> keyInfoList = new LinkedList<XMLStructure>();
    // Add the public key used for signing
    keyInfoList.add(kv);
    if (certs != null) {
      // Add each cert as an X509Data...this is used to build a trust chain on validation
      for (int i = 0, size = certs.size(); i < size; i++) {
        X509Certificate cert = certs.get(i);
        List<Object> certList = new LinkedList<Object>();
        certList.add(cert.getSubjectX500Principal().getName());
        if (cert.getExtensionValue(SKI_OID) != null) {
          certList.add(cert.getExtensionValue(SKI_OID));
        }
        // the chain is always sequential, so if another cert exists in the list, then that cert issued this
        // one
        if ((i + 1) < certs.size()) {
          certList.add(kif.newX509IssuerSerial(certs.get(i).getIssuerX500Principal().getName(),
              certs.get(i + 1).getSerialNumber()));
        }
        certList.add(cert);
        keyInfoList.add(kif.newX509Data(certList));
      }
    }
    // Create a KeyInfo and add the KeyValue to it
    return kif.newKeyInfo(keyInfoList);
  }

  @Override
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

}