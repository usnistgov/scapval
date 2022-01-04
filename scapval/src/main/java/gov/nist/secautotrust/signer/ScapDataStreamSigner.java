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

package gov.nist.secautotrust.signer;

import gov.nist.secautotrust.logging.MessageBroadcaster;
import gov.nist.secautotrust.logging.MessageBroadcasterFactory;
import gov.nist.secautotrust.signer.config.ScapDataStreamSignerConfig;
import gov.nist.secauto.trust.tmsad.config.DigestType;
import gov.nist.secauto.trust.tmsad.config.ObjectFactory;
import gov.nist.secauto.trust.tmsad.config.Reference;
import gov.nist.secauto.trust.tmsad.config.Sign;
import gov.nist.secauto.trust.tmsad.config.Sign.KeyInfo;
import gov.nist.secauto.trust.tmsad.config.Sign.Manifest;
import gov.nist.secauto.trust.tmsad.config.SignatureType;
import gov.nist.secauto.trust.tmsad.config.Signs;
import gov.nist.secauto.trust.tmsad.config.Signs.XpathNamespacePrefixMap.Ns;
import gov.nist.secautotrust.util.ScapNamespaceContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class ScapDataStreamSigner {
  @SuppressWarnings("unused")
  private static final MessageBroadcaster broadcaster = MessageBroadcasterFactory.broadcaster();

  private static final XPathExpression datastreamExpr;
  private static final XPathExpression refExpr;

  private static final String xlinkNs = "http://www.w3.org/1999/xlink";

  private static final String xpath = "//ds:data-stream-collection";
  private static final Map<String, String> nsMap = new HashMap<String, String>();

  private final ObjectFactory objFac = new ObjectFactory();

  public static final String EXCLUDE_EXTERNAL_REFERENCE_PROPERTY = "gov.nist.secautotrust.signer.external.reference";

  static {
    nsMap.put("ds", "http://scap.nist.gov/schema/scap/source/1.2");
  }

  static {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    xpath.setNamespaceContext(new ScapNamespaceContext());
    XPathExpression temp1;
    XPathExpression temp2;
    try {
      temp1 = xpath.compile("ds:data-stream-collection/ds:data-stream");
      temp2 = xpath.compile("*/ds:component-ref");
    } catch (XPathExpressionException e) {
      // Should never happen
      temp1 = null;
      temp2 = null;
    }
    datastreamExpr = temp1;
    refExpr = temp2;
  }

  private ScapDataStreamSigner() {

  }

  public static ScapDataStreamSigner getInstance() {
    return new ScapDataStreamSigner();
  }

  // The primary method that accepts a configuration object and writes the
  // datastream signature(s) to the outputstream.
  public void createConfig(ScapDataStreamSignerConfig config) throws TransformerFactoryConfigurationError,
      ParserConfigurationException, IOException, SAXException, XPathExpressionException {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();

    ByteArrayOutputStream contentInOut = new ByteArrayOutputStream();
    byte[] b = new byte[64 * 1024];
    InputStream is = new FileInputStream(new File(config.getContent()));
    int readLength = is.read(b);
    while (readLength >= 0) {
      contentInOut.write(b, 0, readLength);
      readLength = is.read(b);
    }
    is.close();
    // Parse the datastream collection
    Document doc = db.parse(new ByteArrayInputStream(contentInOut.toByteArray()));

    // Extract all of the datastream elements
    NodeList datastreamNL = (NodeList) datastreamExpr.evaluate(doc, XPathConstants.NODESET);

    Signs signs = objFac.createSigns();
    signs.setOutputFile(config.getOutputFileLocation());
    signs.setSourceFile(config.getContent());

    Ns nsDs = objFac.createSignsXpathNamespacePrefixMapNs();
    nsDs.setPrefix("ds");
    nsDs.setUri("http://scap.nist.gov/schema/scap/source/1.2");
    signs.setXpathNamespacePrefixMap(objFac.createSignsXpathNamespacePrefixMap());
    signs.getXpathNamespacePrefixMap().getNs().add(nsDs);

    // Iterate through all of the datastream elements
    for (int i = 0, size = datastreamNL.getLength(); i < size; i++) {
      Sign sign = objFac.createSign();
      sign.setDigestType(DigestType.fromValue(config.getHashType().toUriString()));
      sign.setInsert(objFac.createSignInsert());
      sign.getInsert().setInsertXpath(xpath);
      sign.setSignatureType(SignatureType.fromValue(config.getSigType().toUrlString()));

      KeyInfo ski = objFac.createSignKeyInfo();
      ski.setKeyStore(config.getKeyStore());
      ski.setAlias(config.getAlias());
      sign.setKeyInfo(ski);

      Element datastreamNode = (Element) datastreamNL.item(i);
      // Build up the "reference" that is the datastream node for signing
      Reference dsRef = objFac.createReference();
      dsRef.setUri("#" + datastreamNode.getAttribute("id"));
      dsRef.setDigestType(DigestType.fromValue(config.getHashType().toUriString()));
      sign.getReference().add(dsRef);

      // Get all of the "ref" elements that this datastream contains
      NodeList refNL = (NodeList) refExpr.evaluate(datastreamNode, XPathConstants.NODESET);
      for (int j = 0, len = refNL.getLength(); j < len; j++) {
        // Get the ref id
        String ref = ((Element) refNL.item(j)).getAttributeNS(xlinkNs, "href");
        // Set this to false when testing
        String exRef = System.getProperty(EXCLUDE_EXTERNAL_REFERENCE_PROPERTY);
        if (ref.startsWith("#") || !(exRef != null && "true".equals(exRef.toLowerCase()))) {
          if (sign.getManifest().size() == 0) {
            Manifest manifest = objFac.createSignManifest();
            sign.getManifest().add(manifest);
          }
          Reference maniref = objFac.createReference();
          maniref.setUri(ref);
          maniref.setDigestType(DigestType.fromValue(config.getHashType().toUriString()));
          sign.getManifest().get(0).getReference().add(maniref);
        }
      }
      signs.getSign().add(sign);

    }

    JAXB.marshal(signs, config.getOutputStream());
  }
}
