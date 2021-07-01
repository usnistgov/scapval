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

import gov.nist.secautotrust.signature.Signature;
import gov.nist.secautotrust.signer.config.ScapResultSignerConfig;
import gov.nist.secautotrust.util.ScapNamespaceContext;
import gov.nist.secautotrust.util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScapResultsSigner {
  private static final XPathExpression extendedInfosExpr;
  private static final String ARF_NS = "http://scap.nist.gov/schema/asset-reporting-format/1.1";
  private static final String INSERT_XPATH = "arf:asset-report-collection/arf:extended-infos/arf:extended-info[last()]";
  private static final String EXCLUDE_XPATH
      = "/arf:asset-report-collection/arf:extended-infos/arf:extended-info[dsig:Signature]";
  private static final String EXCLUDE_WRAPPER_ELEMENT_XPATH
      = "/arf:asset-report-collection/arf:extended-infos[count(arf:extended-info[dsig:Signature]) = count(*)]";
  private static final String SIG_XPATH
      = "arf:asset-report-collection/arf:extended-infos/arf:extended-info/dsig:Signature";

  static {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    xpath.setNamespaceContext(new ScapNamespaceContext());
    XPathExpression temp1;
    try {
      temp1 = xpath.compile("arf:asset-report-collection/arf:extended-infos");
    } catch (XPathExpressionException e) {
      // Should never happen
      temp1 = null;
    }
    extendedInfosExpr = temp1;
  }

  private ScapResultsSigner() {

  }

  // The main method that signs an ARF stream
  public static void signResultStream(ScapResultSignerConfig config)
      throws TransformerFactoryConfigurationError, Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();

    // Build a document out of the ARF input
    Document doc = db.parse(config.getContent());

    Signature.Builder builder = new Signature.Builder().sigType(config.getSigType())
        .outputStream(config.getOutputStream()).keyInfoBuilder(config.getKeyInfo()).insertXpath(INSERT_XPATH)
        .insertXpathNamespaceMap(ScapNamespaceContext.createPrefixNamespaceMap("arf"));

    prepareSourceOutput(doc);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // If counter signing, then find the signature to counter sign and make
    // that the source content for the signing
    if (config.isCounterSigning()) {
      XPath xpath = XPathFactory.newInstance().newXPath();
      xpath.setNamespaceContext(new ScapNamespaceContext());
      // Get the signature to counter sign
      Element node = (Element) xpath.evaluate(SIG_XPATH, doc, XPathConstants.NODE);
      // Remove the existing signature extended-info element from the
      // document
      node.getParentNode().getParentNode().removeChild(node.getParentNode());
      ByteArrayOutputStream sigBAOS = new ByteArrayOutputStream();
      TransformerFactory.newInstance().newTransformer().transform(new DOMSource(node), new StreamResult(sigBAOS));
      // Create the main reference to sign with the content of the
      // existing signature
      builder.newObject(Util.generateId("obj-cs"), new ByteArrayInputStream(sigBAOS.toByteArray()));

      // Marshall the document with the signature extended-info removed in
      // preparation to create the "sourceForOutput" input stream
      TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(baos));
    } else {
      // Else just use the ARF document passed in as the "sourceForOutput"
      // input stream
      TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(baos));

      // Create the main reference which is the ARF document excluding any
      // signature "extended-info"s, as well as the extended-infos element
      // if it only contains signature extended-info elements
      builder.newDetachedReferenceBuilder().uri("")
          .xpath(new XPathType(EXCLUDE_WRAPPER_ELEMENT_XPATH, XPathType.Filter.SUBTRACT,
              ScapNamespaceContext.createPrefixNamespaceMap("arf", "dsig")))
          .xpath(new XPathType(EXCLUDE_XPATH, XPathType.Filter.SUBTRACT,
              ScapNamespaceContext.createPrefixNamespaceMap("arf", "dsig")))
          .hashType(config.getHashType());

    }
    builder.sourceForOutput(new ByteArrayInputStream(baos.toByteArray()));
    builder.build().signContents();
  }

  // To prepare the ARF document for output, we may need to add the
  // extended-infos element if it doesn't already exist, and we need to add
  // the extended-info element that will hold the signature
  private static void prepareSourceOutput(Document doc) throws TransformerConfigurationException, TransformerException,
      TransformerFactoryConfigurationError, XPathExpressionException {
    Element extendedinfos = (Element) extendedInfosExpr.evaluate(doc, XPathConstants.NODE);

    if (extendedinfos == null) {
      extendedinfos = doc.createElementNS(ARF_NS, "extended-infos");
      doc.getFirstChild().appendChild(extendedinfos);
    }

    Element ele = doc.createElementNS(ARF_NS, "extended-info");
    ele.setAttribute("id", Util.generateId("dsig"));
    extendedinfos.appendChild(ele);
  }
}
