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
import gov.nist.secautotrust.signature.enums.CanonicalizationType;
import gov.nist.secautotrust.signature.enums.HashType;
import gov.nist.secautotrust.signature.enums.SignatureType;
import gov.nist.secautotrust.signature.exception.TMSADException;
import gov.nist.secautotrust.signature.model.IReferenceValidationResult;
import gov.nist.secautotrust.signature.model.ISignatureValidationResult;
import gov.nist.secautotrust.signer.ScapDataStreamSigner;
import gov.nist.secautotrust.signer.config.ScapDataStreamSignerConfig;
import gov.nist.secautotrust.tmsad.input.Reference;
import gov.nist.secautotrust.tmsad.input.Reference.Xpath;
import gov.nist.secautotrust.tmsad.input.ReferenceType;
import gov.nist.secautotrust.tmsad.input.Sign;
import gov.nist.secautotrust.tmsad.input.Sign.Manifest;
import gov.nist.secautotrust.tmsad.input.Signs;
import gov.nist.secautotrust.tmsad.input.Signs.XpathNamespacePrefixMap.Ns;
import gov.nist.secautotrust.util.Util;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathExpressionException;

public class SecAutoTrustMain {

  private static void buildReference(ReferenceBuilderFactory factory, Reference ref,
      Map<String, String> namespaceContextMap) {
    ReferenceBuilder refBuilder;
    if (ref.getType() != null && ref.getType() == ReferenceType.ENVELOPED) {
      refBuilder = factory.newEnvelopedReferenceBuilder();
    } else if (ref.getType() != null && ref.getType() == ReferenceType.ENVELOPING) {
      // TODO implement enveloping signatures
      throw new UnsupportedOperationException("Enveloping signatures is not currently supported via the command line.");
    } else {
      refBuilder = factory.newDetachedReferenceBuilder();
    }
    if (ref.getUri().equals("#")) {
      System.out.println("Certain versions of Java have a known bug when processing a reference URI \"#\". "
          + "If an error such as \"java.lang.StringIndexOutOfBoundsException: String index out of range: 1\" "
          + "is encountered, change \"#\" references to blank string: \"\"");
    }
    refBuilder.uri(ref.getUri());
    refBuilder.hashType(HashType.fromUrlString(ref.getDigestType().value()));
    if (ref.getCanonicalization() != null)
      refBuilder.canonicalization(CanonicalizationType.fromUrlString(ref.getCanonicalization().value()));
    for (Xpath xpath : ref.getXpath()) {
      XPathType.Filter filterType = null;
      switch (xpath.getType()) {
      case INTERSECT:
        filterType = XPathType.Filter.INTERSECT;
        break;
      case SUBTRACT:
        filterType = XPathType.Filter.SUBTRACT;
        break;
      case UNION:
        filterType = XPathType.Filter.UNION;
        break;
      }

      refBuilder.xpath(new XPathType(xpath.getExpression(), filterType, namespaceContextMap));
    }
  }

  public static void sign(String file) throws Exception {
    sign(file, null, null);
  }

  public static void sign(String file, String providedKeystorePassword, String providedCertificatePassword)
      throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    File xmlInput = new File(file);
    if (!(xmlInput.exists() && xmlInput.canRead())) {
      throw new IllegalStateException("File " + xmlInput.getCanonicalPath() + " MUST exist and be readable.");
    }

    JAXBContext context = JAXBContext.newInstance(Sign.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        .newSchema(new URL("classpath:tmsad/secautotrust.xsd")));
    Signs signs = (Signs) unmarshaller.unmarshal(xmlInput);

    File outputFile = new File(signs.getOutputFile());
    if (outputFile.exists()) {
      throw new IllegalStateException("File " + outputFile.getCanonicalPath() + " MUST not exist.");
    }
    boolean hasSourceFile = false;
    if (signs.getSourceFile() != null) {
      File sourceFile = new File(signs.getSourceFile());
      if (!(sourceFile.exists() && sourceFile.canRead())) {
        throw new IllegalStateException("File " + sourceFile.getCanonicalPath() + " MUST exist and be readable.");
      }
      hasSourceFile = true;
    }
    if (!hasSourceFile && signs.getSign().size() > 1) {
      throw new IllegalStateException("If no source file is specified, then only 1 signature definition is permitted.");
    }
    for (int i = 0, size = signs.getSign().size(); i < size; i++) {
      if (hasSourceFile && signs.getSign().get(i).getInsert() != null) {
        // do nothing
      } else if (!hasSourceFile && signs.getSign().get(i).getInsert() == null) {
        // do nothing
      } else {
        throw new IllegalStateException("If a source file is specified, every <sign> MUST specify an <insert> element. "
            + "If no source file is specified, the <sign> MUST NOT specify an <insert> element.");
      }
    }

    Map<String, String> namespaceContextMap = null;
    if (signs.getXpathNamespacePrefixMap() != null) {
      namespaceContextMap = new HashMap<String, String>();
      for (Ns ns : signs.getXpathNamespacePrefixMap().getNs()) {
        namespaceContextMap.put(ns.getPrefix(), ns.getUri());
      }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (int i = 0, size = signs.getSign().size(); i < size; i++) {

      Sign config = signs.getSign().get(i);

      Signature.Builder builder = new Signature.Builder();
      builder.includeDefaultSignatureProperties(true);
      builder.creators(config.getCreators());
      // Write to the output only if this is the last signature
      FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
      builder.outputStream(i + 1 < size ? baos : new BufferedOutputStream(fileOutputStream));
      builder.sigType(SignatureType.fromUrlString(config.getSignatureType().value()));
      if (config.getCanonicalization() != null)
        builder.canonicalization(CanonicalizationType.fromUrlString(config.getCanonicalization().value()));

      String keystorePassword = null;

      if (!"MSCAPI".equals(config.getKeyInfo().getKeyStore())) {
        File jks = new File(config.getKeyInfo().getKeyStore());
        if (!(jks.exists() && jks.canRead())) {
          throw new IllegalStateException("The Java Keystore Store (JKS) must exist and be readable.");
        }

        if (providedKeystorePassword != null) {
          keystorePassword = providedKeystorePassword;
        } else {
          System.out.print("Enter keystore password: ");
          keystorePassword = br.readLine();
        }

      }
      KeyStore keystore = getKeyStore(config.getKeyInfo().getKeyStore(),
          keystorePassword != null ? keystorePassword.toCharArray() : null, true);

      String certPassword = null;
      if (!"MSCAPI".equals(config.getKeyInfo().getKeyStore())) {

        if (providedCertificatePassword != null) {
          certPassword = providedKeystorePassword;
        } else {
          System.out.print("Enter certificate password: ");
          certPassword = br.readLine();
        }

      }
      KeyInfo keyInfo = getKeyInfo(keystore, certPassword != null ? certPassword.toCharArray() : null,
          config.getKeyInfo().getAlias());
      if (keyInfo == null) {
        System.out.println("Failed to locate key information");
        System.exit(1);
      }
      KeyInfoBuilder kib
          = new KeyInfoBuilder().privateKey(keyInfo.keyPair.getPrivate()).publicKey(keyInfo.keyPair.getPublic());
      // Add each cert in the chain
      for (X509Certificate cert : keyInfo.certChain) {
        kib.certificate(cert);
      }
      builder.keyInfoBuilder(kib);

      if (config.getInsert() != null) {
        if (i == 0)
          builder.sourceForOutput(new BufferedInputStream(new FileInputStream(signs.getSourceFile())));
        else {
          builder.sourceForOutput(new ByteArrayInputStream(baos.toByteArray()));
          baos = new ByteArrayOutputStream();
        }
        builder.insertXpath(config.getInsert().getInsertXpath());
        builder.insertXpathNamespaceMap(namespaceContextMap);
        // todo what is this below
        // builder.insertAsSibling(config.getInsert().getInsertAsSibling() > 0);
        // builder.insertAsSibling(config.getInsert().getInsertXpath("") > 0)
      }

      for (Reference ref : config.getReference()) {
        buildReference(builder, ref, namespaceContextMap);
      }

      for (Manifest manifest : config.getManifest()) {
        ManifestBuilder manifestBuilder = builder.newManifestBuilder();
        manifestBuilder.id(Util.generateId("manifest"));
        manifestBuilder.canonicalizationType(CanonicalizationType.fromUrlString(config.getCanonicalization().value()));
        manifestBuilder.hashType(HashType.fromUrlString(config.getDigestType().value()));
        for (Reference ref : manifest.getReference()) {
          buildReference(manifestBuilder, ref, namespaceContextMap);
        }
      }

      for (Sign.SignatureProperty sigProp : config.getSignatureProperty()) {
        if (sigProp.getAny() instanceof Element) {
          builder.newSignatureProperty((Element) sigProp.getAny());
        } else {
          throw new IllegalStateException("Signature properties MUST be XML.");
        }
      }

      builder.build().signContents();
      fileOutputStream.close();
    }

  }

  public static void validateSignature(String file, String pathToKeystore, String alias) throws IllegalStateException,
      IOException, TMSADException, ParserConfigurationException, SAXException, XMLSignatureException {
    validateSignature(file, pathToKeystore, alias, null);
  }

  public static void validateSignature(String file, String pathToKeystore, String alias,
      String providedKeystorePassword) throws IllegalStateException, IOException, TMSADException,
      ParserConfigurationException, SAXException, XMLSignatureException {
    String keystorePassword = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    if (!"MSCAPI".equals(pathToKeystore)) {
      File jks = new File(pathToKeystore);
      if (!(jks.exists() && jks.canRead())) {
        throw new IllegalStateException("The Java Keystore Store (JKS) must exist and be readable.");
      }
      if (providedKeystorePassword != null) {
        keystorePassword = providedKeystorePassword;
      } else {
        System.out.print("Enter keystore password: ");
        keystorePassword = br.readLine();
      }
      if (keystorePassword.trim().length() == 0) {
        keystorePassword = null;
      }
    }
    KeyStore keystore;
    PublicKey key;
    try {
      keystore = getKeyStore(pathToKeystore, keystorePassword != null ? keystorePassword.toCharArray() : null, false);
      key = getPublicKey(keystore, alias);
    } catch (KeyStoreException e) {
      throw new TMSADException(e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      throw new TMSADException(e.getMessage());
    } catch (CertificateException e) {
      throw new TMSADException(e.getMessage());
    }

    ValidateSigConfig config
        = new ValidateSigConfig.Builder().content(new FileInputStream(file)).trustedPublicKey(key).build();

    List<ISignatureValidationResult> results = XMLValidator.validateContent(config);
    if (results.size() == 0) {
      throw new TMSADException("No signatures found.");
    }
    for (ISignatureValidationResult result : results) {
      if (!result.isSignatureValid()) {
        throw new TMSADException("Signature is not valid: " + result.getSignatureId());
      } else {
        System.out.println("Signature is valid: " + result.getSignatureId());
      }
      for (IReferenceValidationResult ref : result.getSignatureReferenceResults()) {
        if (!ref.isReferenceDigestValid()) {
          throw new TMSADException("Reference is not valid: " + ref.getReferenceURI());
        } else {
          System.out.println("Reference is valid: " + ref.getReferenceURI());
        }
      }
      for (List<IReferenceValidationResult> refList : result.getManifestsReferenceResults()) {
        for (IReferenceValidationResult ref : refList) {
          if (!ref.isReferenceDigestValid()) {
            throw new TMSADException("Manifest reference is not valid: " + ref.getReferenceURI());
          } else {
            System.out.println("Manifest reference is valid: " + ref.getReferenceURI());
          }
        }
      }
    }
  }

  public static void createScapSignConfig(String configOut, String scapIn, String scapOut, String digestAlg,
      String sigAlg, String pathToKeystore, String alias, String includeExternalReferences)
      throws IllegalStateException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {

    File fOut = new File(configOut);
    File fScapIn = new File(scapIn);
    if (fOut.exists()) {
      throw new IllegalStateException("Cannot overwrite file: " + configOut);
    }
    if (!fScapIn.exists() || !fScapIn.canRead()) {
      throw new IllegalStateException("Cannot read SCAP 1.2 file: " + scapIn);
    }

    HashType ht = HashType.valueOf(digestAlg);
    if (ht == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("Did not recognize hash type: " + digestAlg + ". Valid values: ");
      for (HashType item : HashType.values()) {
        sb.append(item + ", ");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
      throw new IllegalStateException(sb.toString());
    }
    SignatureType st = SignatureType.valueOf(sigAlg);
    if (st == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("Did not recognize signature type: " + sigAlg + ". Valid values: ");
      for (SignatureType item : SignatureType.values()) {
        sb.append(item + ", ");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
      throw new IllegalStateException(sb.toString());
    }

    Boolean bIncludeExternalReferences = includeExternalReferences.toLowerCase().equals("true") ? true
        : includeExternalReferences.toLowerCase().equals("false") ? false : null;
    if (bIncludeExternalReferences == null) {
      throw new IllegalStateException("The boolean to include external references MUST be \"true\" or \"false\"");
    }

    System.setProperty(ScapDataStreamSigner.EXCLUDE_EXTERNAL_REFERENCE_PROPERTY,
        bIncludeExternalReferences ? "false" : "true");

    ScapDataStreamSignerConfig config = new ScapDataStreamSignerConfig.Builder().content(scapIn)
        .hashType(HashType.valueOf(digestAlg)).sigType(SignatureType.valueOf(sigAlg)).keystore(pathToKeystore)
        .alias(alias).outputStream(new FileOutputStream(fOut)).outputFileLocation(scapOut).build();

    ScapDataStreamSigner.getInstance().createConfig(config);

  }

  public static void listAliases(String pathToKeystore) throws Exception {
    listAliases(pathToKeystore, null);
  }

  public static void listAliases(String pathToKeystore, String providedKeystorePassword) throws Exception {
    KeyStore keystore = getKeyStore(pathToKeystore, true, providedKeystorePassword);
    Enumeration<String> alias = keystore.aliases();
    System.out.println("Alias in the keystore:\n");
    while (alias.hasMoreElements()) {
      System.out.println(alias.nextElement());
    }
  }

  public static void listTrustChain(String pathToKeystore, String alias) throws Exception {
    listTrustChain(pathToKeystore, alias, null);
  }

  public static void listTrustChain(String pathToKeystore, String alias, String providedKeystorePassword)
      throws Exception {
    List<KeyStore> keystoreList = new LinkedList<KeyStore>();
    keystoreList.add(getKeyStore(pathToKeystore, true, providedKeystorePassword));
    if ("MSCAPI".equals(pathToKeystore)) {
      keystoreList.add(getKeyStore(pathToKeystore, false));
    }
    Certificate[] certs = keystoreList.get(0).getCertificateChain(alias);
    if (certs == null) {
      System.out.println("failed to locate certificate chain for alias: (" + alias + ")");
      return;
    }
    System.out.println("Certificate chain by alias (starts with target cert; ends with trusted cert):\n");
    for (Certificate cert : certs) {
      String currentAlias = keystoreList.get(0).getCertificateAlias(cert);
      if (currentAlias == null && keystoreList.size() > 1) {
        currentAlias = keystoreList.get(1).getCertificateAlias(cert);
      }
      System.out.println(currentAlias == null ? "<unknown alias>" : currentAlias);
    }
  }

  private static KeyInfo getKeyInfo(KeyStore keystore, char[] password, String alias)
      throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
    // Get private key
    Key key = keystore.getKey(alias, password);
    if (key instanceof PrivateKey) {
      // Get certificate of public key
      Certificate[] cert = keystore.getCertificateChain(alias);
      X509Certificate[] xcert = new X509Certificate[cert.length];
      for (int i = 0, size = cert.length; i < size; i++) {
        xcert[i] = (X509Certificate) cert[i];
      }
      // Get public key
      PublicKey publicKey = xcert[0].getPublicKey();
      // Return a key pair
      KeyInfo keyInfo = new KeyInfo();
      keyInfo.keyPair = new KeyPair(publicKey, (PrivateKey) key);
      keyInfo.certChain = xcert;
      return keyInfo;
    }
    return null;
  }

  private static PublicKey getPublicKey(KeyStore keystore, String alias)
      throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    Certificate cert = keystore.getCertificate(alias);
    if (cert == null) {
      // see if there is a chain and just grab the first certificate
      Certificate[] chain = keystore.getCertificateChain(alias);
      if (chain != null && chain.length > 0) {
        cert = chain[0];
      }
    }
    return cert.getPublicKey();
  }

  private static KeyStore getKeyStore(String pathToKeystore, boolean isForSigning) throws Exception {
    return getKeyStore(pathToKeystore, isForSigning, null);
  }

  private static KeyStore getKeyStore(String pathToKeystore, boolean isForSigning, String providedKeystorePassword)
      throws Exception {
    String keystorePassword = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    if (!"MSCAPI".equals(pathToKeystore)) {
      File jks = new File(pathToKeystore);
      if (!(jks.exists() && jks.canRead())) {
        throw new IllegalStateException("The Java Keystore Store (JKS) must exist and be readable.");
      }

      if (providedKeystorePassword != null) {
        keystorePassword = providedKeystorePassword;
      } else {
        System.out.print("Enter keystore password: ");
        keystorePassword = br.readLine();
      }

    }
    return getKeyStore(pathToKeystore, keystorePassword != null ? keystorePassword.toCharArray() : null, isForSigning);

  }

  private static KeyStore getKeyStore(String keystorePath, char[] password, boolean isForSigning)
      throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    // Load the KeyStore
    KeyStore keystore;
    if ("MSCAPI".equals(keystorePath)) {
      // If doing validation, then look at the trusted roots for the
      // trusted cert, otherwise get a personal cert
      if (isForSigning)
        keystore = KeyStore.getInstance("Windows-MY");
      else
        keystore = KeyStore.getInstance("Windows-ROOT");
      keystore.load(null, null);
    } else {
      keystore = KeyStore.getInstance("JKS");
      FileInputStream file = new FileInputStream(keystorePath);
      try {
        keystore.load(new BufferedInputStream(file), password);
      } finally {
        try {
          file.close();
        } catch (IOException e) {
          // close quietly...
        }
      }
    }
    return keystore;
  }

  // private static void help() {
  // System.out.println("Incorrect arguments. The first argument MUST be \"SIGN\", \"VALIDATE\",
  // \"SCAP_SOURCE\", \"LIST\" or \"CHAIN\".");
  // System.out.println("If \"SIGN\" is specified, the second argument MUST be a signing configuration
  // file. No other arguments shall be specified.");
  // System.out.println("If \"VALIDATE\" is specified, the second argument MUST be an XML document
  // with a signature to validate.");
  // System.out.println(" The third argument MUST be a Java Keystore (JKS) file, or specify \"MSCAPI\"
  // to use a certificate installed in Windows.");
  // System.out.println(" The fourth argument MUST be the alias of the certificate of the trusted root
  // to use in the keystore.");
  // System.out.println("If \"SCAP_SOURCE\" is specified, the second argument MUST be a file path
  // where the output configuration is written.");
  // System.out.println(" The third argument MUST be an SCAP 1.2 data stream.");
  // System.out.println(" The fourth argument MUST be a file path where the signed SCAP data stream
  // will be written to.");
  // System.out.print(" The fifth argument MUST be the digest algorithm to use. Valid values are ");
  // StringBuilder sb = new StringBuilder();
  // for (HashType item : HashType.values()) {
  // sb.append(item + ", ");
  // }
  // sb.deleteCharAt(sb.length() - 1);
  // sb.deleteCharAt(sb.length() - 1);
  // System.out.println(sb.toString());
  // System.out.print(" The sixth argument MUST be the signature algorithm to use. Valid values are
  // ");
  // sb = new StringBuilder();
  // for (SignatureType item : SignatureType.values()) {
  // sb.append(item + ", ");
  // }
  // sb.deleteCharAt(sb.length() - 1);
  // sb.deleteCharAt(sb.length() - 1);
  // System.out.println(sb.toString());
  // System.out.println(" The seventh argument MUST be a Java Keystore (JKS) file, or specify
  // \"MSCAPI\" to use a certificate installed in Windows.");
  // System.out.println(" The eight argument MUST be the alias of the certificate to use to sign the
  // content.");
  // System.out.println(" The ninth argument MUST be \"true\" or \"false\", to indicate if external
  // references should be signed.");
  // System.out.println("If \"LIST\" is specified, the second argument MUST be a Java Keystore (JKS)
  // file, or specify \"MSCAPI\" to show the certificates installed in Windows.");
  // System.out.println("If \"CHAIN\" is specified, the second argument MUST be a Java Keystore (JKS)
  // file, or specify \"MSCAPI\" to use a certificate installed in Windows.");
  // System.out.println(" The third argument MUST be the alias of the certificate whose chain will be
  // displayed.");
  // System.exit(1);
  // }

  private static class KeyInfo {
    private KeyPair keyPair;
    private X509Certificate[] certChain;
  }
}