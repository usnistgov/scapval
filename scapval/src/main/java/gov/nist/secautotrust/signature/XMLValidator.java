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

import gov.nist.secautotrust.signature.config.ValidateSigConfig;
import gov.nist.secautotrust.signature.exception.TMSADException;
import gov.nist.secautotrust.signature.model.IReferenceValidationResult;
import gov.nist.secautotrust.signature.model.ISignatureValidationResult;
import gov.nist.secautotrust.signer.MappedURIDereferencer;
import gov.nist.secautotrust.util.Util;

import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.crypto.KeySelector;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLValidator {

  public static List<ISignatureValidationResult> validateContent(ValidateSigConfig config)
      throws TMSADException, ParserConfigurationException, IOException, SAXException, XMLSignatureException {
    // Instantiate the document to be validated
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document doc = dbf.newDocumentBuilder().parse(config.getContent());
    HashSet<String> ids = new HashSet<String>();
    Util.setIdOnDOM(doc, ids);

    XMLValidator validator = new XMLValidator(config.getResolver());
    List<ISignatureValidationResult> resultList = new LinkedList<ISignatureValidationResult>();
    if (config.getContent() != null) {
      NodeList nl = getSignatureElements(doc);
      for (int i = 0, size = nl.getLength(); i < size; i++) {
        resultList.add(validator.validateSignature(nl.item(i), config.getTrustedPublicKey()));
      }
    } else {
      for (Node node : config.getSignatureNodes()) {
        resultList.add(validator.validateSignature(node, config.getTrustedPublicKey()));
      }
    }
    return resultList;
  }

  private static NodeList getSignatureElements(Document doc) throws XMLSignatureException {
    // Find the first Signature element
    NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
    if (nl.getLength() == 0) {
      throw new XMLSignatureException("Cannot find Signature element");
    }
    return nl;
  }

  // Create a DOM XMLSignatureFactory that will be used to unmarshall the
  // document containing the XMLSignature
  private XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM", new XMLDSigRI());
  private UriResolver resolver;

  private XMLValidator(UriResolver resolver) {
    this.resolver = resolver;
  }

  private ISignatureValidationResult validateSignature(Node node, PublicKey trustedPublicKey) throws TMSADException {

    // Build and verify the trust chain of certificates on the signature
    // The returned array will alway have exactly 2 values...the first
    // is the root public key, and the second is the target cert public key
    PublicKey[] keyChain;
    try {
      keyChain = buildAndVerifyCertificateChain((Element) node);
      // Compare the root public key to the trusted one passed in
      if (!isSameKey(keyChain[0], trustedPublicKey)) {
        throw new SignatureException("The root of the certificate chain is not trusted.");
      }

      // Set the target cert public key as the one to use
      KeySelector keySelector = KeySelector.singletonKeySelector(keyChain[1]);

      // Create a DOMValidateContext and specify a KeyValue KeySelector
      // and document context
      DOMValidateContext valContext = new DOMValidateContext(keySelector, node);
      if (resolver != null) {
        valContext.setURIDereferencer(new MappedURIDereferencer(factory.getURIDereferencer(), resolver));
      }

      // unmarshall the XMLSignature
      XMLSignature signature = factory.unmarshalXMLSignature(valContext);

      // Validate the XMLSignature (generated above)
      signature.validate(valContext);
      boolean sv = signature.getSignatureValue().validate(valContext);
      SignatureValidationResult result = new SignatureValidationResult();
      result.setSignatureValid(sv);
      result.setSignatureId(signature.getId());

      // check the validation status of each Reference
      List<?> refs = signature.getSignedInfo().getReferences();
      List<IReferenceValidationResult> refList = new LinkedList<IReferenceValidationResult>();
      for (Object r : refs) {
        if (r instanceof Reference) {
          boolean refValid = ((Reference) r).validate(valContext);
          ReferenceValidationResult rvr = new ReferenceValidationResult();
          rvr.setRefUri(((Reference) r).getURI());
          rvr.setRefValid(refValid);
          refList.add(rvr);
        }
      }
      result.setSigRefs(refList);

      List<?> objs = signature.getObjects();
      List<List<IReferenceValidationResult>> manList = new LinkedList<List<IReferenceValidationResult>>();
      for (Object obj : objs) {
        if (obj instanceof XMLObject) {
          List<?> contents = ((XMLObject) obj).getContent();
          for (Object contentObj : contents) {
            if (contentObj instanceof Manifest) {
              List<IReferenceValidationResult> manRefListResult = new LinkedList<IReferenceValidationResult>();
              List<?> manRefList = ((Manifest) contentObj).getReferences();
              for (Object ref : manRefList) {
                if (ref instanceof Reference) {
                  boolean refValid = ((Reference) ref).validate(valContext);
                  ReferenceValidationResult rvr = new ReferenceValidationResult();
                  rvr.setRefUri(((Reference) ref).getURI());
                  rvr.setRefValid(refValid);
                  manRefListResult.add(rvr);
                }
              }
              manList.add(manRefListResult);
            }
          }
        }
      }
      result.setManiRefs(manList);
      return result;

    } catch (Exception e) {
      e.printStackTrace();
      throw new TMSADException(e.getMessage());
    }

  }

  // TODO
  private PublicKey[] buildAndVerifyCertificateChain(Element n)
      throws XMLSignatureException, MarshalException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
      NoSuchProviderException, CertPathBuilderException, CertificateException, SignatureException, KeyException {

    NodeList nl = n.getElementsByTagNameNS(XMLSignature.XMLNS, "KeyInfo");

    if (nl.getLength() == 0) {
      throw new XMLSignatureException("Cannot find KeyInfo element");
    }

    KeyInfoFactory kif = factory.getKeyInfoFactory();
    KeyInfo keyInfo = kif.unmarshalKeyInfo(new DOMStructure(nl.item(0)));
    List<X509Certificate> certList = new LinkedList<X509Certificate>();
    PublicKey keyForSigning = null;
    // Get the relevant data off of the KeyInfo
    for (Object obj : keyInfo.getContent()) {
      if (obj instanceof X509Data) {
        for (Object object : ((X509Data) obj).getContent()) {
          if (object instanceof X509Certificate) {
            certList.add((X509Certificate) object);
          }
        }
      }
      if (obj instanceof KeyValue) {
        keyForSigning = ((KeyValue) obj).getPublicKey();
      }
    }

    if (keyForSigning == null) {
      throw new SignatureException("Signature/KeyInfo/KeyValue was not found...it is required for validation.");
    }

    // If no certs are found, then just use the key value
    if (certList.size() == 0) {
      PublicKey[] returnVal = new PublicKey[2];
      returnVal[0] = keyForSigning;
      returnVal[1] = keyForSigning;
      return returnVal;
    }
    // If only one cert is found, then ensure it is self signed, and the key matches the KeyValue
    if (certList.size() == 1) {
      if (isSelfSigned(certList.get(0))) {
        if (isSameKey(keyForSigning, certList.get(0).getPublicKey())) {
          PublicKey[] returnVal = new PublicKey[2];
          returnVal[0] = certList.get(0).getPublicKey();
          returnVal[1] = certList.get(0).getPublicKey();
          return returnVal;
        } else {
          throw new SignatureException("The discovered cert public key does not match the key used for signing.");
        }
      } else {
        throw new SignatureException("Only 1 cert is found and it is not self signed.");
      }
    }

    X509Certificate trustAnchorCert = null;
    X509Certificate targetCert = null;
    // search for self signed cert (root) and target cert
    for (X509Certificate cert : certList) {
      if (isSelfSigned(cert)) {
        if (trustAnchorCert != null) {
          throw new SignatureException("More than one self signed certificate was found.");
        } else {
          trustAnchorCert = cert;
        }
      } else if (isSameKey(keyForSigning, cert.getPublicKey())) {
        targetCert = cert;
      }
    }
    if (trustAnchorCert == null) {
      throw new SignatureException("Could not find a self signed certificate on the signature.");
    }
    if (targetCert == null) {
      throw new SignatureException("Could not find a certificate matching the key used to sign.");
    }
    certList.remove(trustAnchorCert);

    // Create the selector that specifies the starting certificate
    X509CertSelector selector = new X509CertSelector();
    selector.setCertificate(targetCert);
    // Create the trust anchor
    Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
    trustAnchors.add(new TrustAnchor(trustAnchorCert, null));
    // Configure the PKIX certificate builder algorithm parameters
    PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);
    // Disable CRL checks
    pkixParams.setRevocationEnabled(false);
    // Specify a list of all certificates (minus the self signed one)
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    CertStore intermediateCertStore
        = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
    pkixParams.addCertStore(intermediateCertStore);

    // Build and verify the certification chain
    CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");
    PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixParams);
    PublicKey[] returnVal = new PublicKey[2];
    returnVal[0] = trustAnchorCert.getPublicKey();
    returnVal[1] = result.getPublicKey();
    return returnVal;

  }

  // Compare keys
  private boolean isSameKey(PublicKey key1, PublicKey key2) {
    if (key1.getAlgorithm() != null && key2.getAlgorithm() != null) {
      if (!key1.getAlgorithm().equals(key2.getAlgorithm())) {
        return false;
      }
    }
    if (key1.getFormat() != null && key2.getFormat() != null) {
      if (!key1.getFormat().equals(key2.getFormat())) {
        return false;
      }
    }
    if (key1.getEncoded().length != key2.getEncoded().length) {
      return false;
    }
    for (int i = 0, size = key1.getEncoded().length; i < size; i++) {
      if (key1.getEncoded()[i] != key2.getEncoded()[i]) {
        return false;
      }
    }
    return true;
  }

  public static boolean isSelfSigned(X509Certificate cert)
      throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
    try {
      // Try to verify certificate signature with its own public key
      PublicKey key = cert.getPublicKey();
      cert.verify(key);
      return true;
    } catch (SignatureException sigEx) {
      // Invalid signature --> not self-signed
      return false;
    } catch (InvalidKeyException keyEx) {
      // Invalid key --> not self-signed
      return false;
    }
  }

  private static class SignatureValidationResult implements ISignatureValidationResult {

    private boolean isSignatureValid = false;
    private String signatureId = null;
    private List<IReferenceValidationResult> sigRefs = null;
    private List<List<IReferenceValidationResult>> maniRefs = null;

    public void setSigRefs(List<IReferenceValidationResult> sigRefs) {
      this.sigRefs = sigRefs;
    }

    public void setManiRefs(List<List<IReferenceValidationResult>> maniRefs) {
      this.maniRefs = maniRefs;
    }

    public void setSignatureValid(boolean isSignatureValid) {
      this.isSignatureValid = isSignatureValid;
    }

    public void setSignatureId(String signatureId) {
      this.signatureId = signatureId;
    }

    @Override
    public List<List<IReferenceValidationResult>> getManifestsReferenceResults() {
      return maniRefs;
    }

    @Override
    public List<IReferenceValidationResult> getSignatureReferenceResults() {
      return sigRefs;
    }

    @Override
    public boolean isSignatureValid() {
      return isSignatureValid;
    }

    @Override
    public String getSignatureId() {
      return signatureId;
    }
  }

  private static class ReferenceValidationResult implements IReferenceValidationResult {

    private String refUri = null;
    private boolean isRefValid = false;

    public void setRefUri(String refUri) {
      this.refUri = refUri;
    }

    public void setRefValid(boolean isRefValid) {
      this.isRefValid = isRefValid;
    }

    @Override
    public String getReferenceURI() {
      return refUri;
    }

    @Override
    public boolean isReferenceDigestValid() {
      return isRefValid;
    }
  }
}
