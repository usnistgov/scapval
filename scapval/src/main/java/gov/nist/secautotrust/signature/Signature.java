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
import gov.nist.secautotrust.signature.enums.SignatureType;
import gov.nist.secautotrust.signature.model.SigKeyInfo;
import gov.nist.secautotrust.signature.model.SigObject;
import gov.nist.secautotrust.signature.model.SigReference;
import gov.nist.secautotrust.signer.MappedURIDereferencer;
import gov.nist.secautotrust.util.CustomNamespaceContext;
import gov.nist.secautotrust.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

/**
 * A class encapsulating the signing logic
 */
public class Signature {
  public static class Builder implements ReferenceBuilderFactory {
    // the id of the signature
    private String id;
    // signature information
    private SignatureType sigType;
    private CanonicalizationType canonicalizationType;
    private HashType hashType;

    // key information
    private KeyInfoBuilder keyInfo;

    // output stream
    private OutputStream os;

    // signature output location information
    private InputStream sourceForOutput;
    private String insertXpath;
    private Map<String, String> insertXpathNamespaceMap;
    private boolean insertAsSibling;

    private boolean includeProperties;

    private List<DefaultManifestBuilder> manifests;
    private List<DefaultReferenceBuilder> references;
    private List<EnvelopingSigObject.Builder> objects;
    private List<PropertiesSigReference.Builder> sigProps;

    private List<String> creators;

    public Builder() {
      manifests = new LinkedList<DefaultManifestBuilder>();
      references = new LinkedList<DefaultReferenceBuilder>();
      objects = new LinkedList<EnvelopingSigObject.Builder>();
      sigProps = new LinkedList<PropertiesSigReference.Builder>();
    }

    public ManifestBuilder newManifestBuilder() {
      DefaultManifestBuilder result = new DefaultManifestBuilder();
      manifests.add(result);
      return result;
    }

    public Builder newObject(String id, InputStream content) {
      objects.add(new EnvelopingSigObject.Builder().id(id).content(content));
      return this;
    }

    public Builder newObject(String id, Element content) {
      objects.add(new EnvelopingSigObject.Builder().id(id).element(content));
      return this;
    }

    public Builder newSignatureProperty(Element content) {
      sigProps.add(new PropertiesSigReference.Builder().element(content));
      return this;
    }

    @Override
    public ReferenceBuilder newEnvelopedReferenceBuilder() {
      return newReferenceBuilder(SignatureRelationship.ENVELOPED);
    }

    @Override
    public ReferenceBuilder newDetachedReferenceBuilder() {
      return newReferenceBuilder(SignatureRelationship.DETACHED);
    }

    @Override
    public ReferenceBuilder newEnvelopingReferenceBuilder() {
      return newReferenceBuilder(SignatureRelationship.ENVELOPING);
    }

    private ReferenceBuilder newReferenceBuilder(SignatureRelationship relationship) {
      DefaultReferenceBuilder result = new DefaultReferenceBuilder(relationship);
      references.add(result);
      return result;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder outputStream(OutputStream os) {
      this.os = os;
      return this;
    }

    public Builder sigType(SignatureType sigType) {
      this.sigType = sigType;
      return this;
    }

    public Builder canonicalization(CanonicalizationType canonicalizationType) {
      this.canonicalizationType = canonicalizationType;
      return this;
    }

    public Builder sourceForOutput(InputStream sourceForOutput) {
      this.sourceForOutput = sourceForOutput;
      return this;
    }

    public Builder insertXpath(String insertXpath) {
      this.insertXpath = insertXpath;
      return this;
    }

    public Builder insertXpathNamespaceMap(Map<String, String> insertXpathNamespaceMap) {
      this.insertXpathNamespaceMap = insertXpathNamespaceMap;
      return this;
    }

    public Builder insertAsSibling(boolean insertAsSibling) {
      this.insertAsSibling = insertAsSibling;
      return this;
    }

    public Builder keyInfoBuilder(KeyInfoBuilder builder) {
      this.keyInfo = builder;
      return this;
    }

    public Builder includeDefaultSignatureProperties(boolean value) {
      this.includeProperties = value;
      return this;
    }

    public Builder creators(List<String> creators) {
      this.creators = creators;
      return this;
    }

    public Signature build() throws IllegalStateException {
      // TODO: encode all pre-conditions here
      if (sigType == null) {
        throw new IllegalStateException("sigType is required");
      } else if (keyInfo == null) {
        throw new IllegalStateException("keyInfoBuilder is required");
      } else if (os == null) {
        throw new IllegalStateException("outputStream is required");
      } else if (insertXpath != null && sourceForOutput == null) {
        throw new IllegalStateException("sourceForOutput is required if inserting into existing content");
      }
      // create the default id
      if (id == null) {
        id = Util.generateId("dsig");
      }
      // default to inclusive
      if (canonicalizationType == null) {
        canonicalizationType = CanonicalizationType.INCLUSIVE_1_1;
      }
      if (hashType == null) {
        hashType = HashType.SHA512;
      }
      return new Signature(this);
    }
  }

  // First, create a DOM XMLSignatureFactory that will be used to
  // generate the XMLSignature and marshal it to DOM.
  private final XMLSignatureFactory xmlSignatureFactory;
  private final DocumentBuilderFactory documentBuilderFactory;
  private DefaultUriResolver resolver = new DefaultUriResolver();

  private String id;

  // signature information
  private SignatureType sigType;
  private HashType hashType;
  private CanonicalizationType canonicalizationType;

  // key information
  private SigKeyInfo keyInfo;

  // output stream
  private OutputStream os;

  // signature output location information
  private InputStream sourceForOutput;
  private String insertXpath;
  private Map<String, String> insertXpathNamespaceMap;
  private boolean insertAsSibling;

  private List<SigObject> objects = new LinkedList<SigObject>();
  private List<SigReference> references = new LinkedList<SigReference>();

  private Signature(Builder builder) {
    this.id = builder.id;

    documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(true);

    // Need to specify the provider on xmlsec-1.5.1, otherwise the JDK
    // default is used, which has a bug in certain versions of Java 7
    xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");

    this.sigType = builder.sigType;
    this.canonicalizationType = builder.canonicalizationType;
    this.hashType = builder.hashType;

    // key information
    this.keyInfo = builder.keyInfo.build();

    // output stream
    this.os = builder.os;

    // signature output location information
    this.sourceForOutput = builder.sourceForOutput;
    this.insertXpath = builder.insertXpath;
    this.insertXpathNamespaceMap = builder.insertXpathNamespaceMap;
    this.insertAsSibling = builder.insertAsSibling;

    SignatureContext context = new SignatureContext() {
      @Override
      public void addInputStream(String uri, InputStream input) {
        resolver.add(uri, input);
      }
    };

    for (DefaultReferenceBuilder r : builder.references) {
      references.add(r.build(context));
    }
    for (EnvelopingSigObject.Builder o : builder.objects) {
      EnvelopingSigObject eso = o.canonicalizationType(canonicalizationType).hashType(hashType).build();
      references.add(eso);
      objects.add(eso);
    }
    // now add the signature properties
    if (builder.includeProperties) {
      PropertiesSigReference psr = new PropertiesSigReference.Builder().canonicalizationType(canonicalizationType)
          .hashType(hashType).timestamp(new Date()).nonce(new Random().nextLong()).creators(builder.creators).build();
      references.add(psr);
      objects.add(psr);
    }
    for (DefaultManifestBuilder m : builder.manifests) {
      ManifestSigReference manifest = m.build(context);
      objects.add(manifest);
      references.add(manifest);
    }
    for (PropertiesSigReference.Builder props : builder.sigProps) {
      PropertiesSigReference psr = props.canonicalizationType(canonicalizationType).hashType(hashType).build();
      references.add(psr);
      objects.add(psr);
    }

  }

  String getId() {
    return id;
  }

  XMLSignatureFactory getSignatureFactory() {
    return xmlSignatureFactory;
  }

  DocumentBuilderFactory getDocumentBuilderFactory() {
    return documentBuilderFactory;
  }

  String getDigestAlgorithm() {
    return hashType.toUriString();
  }

  public void signContents() throws Exception {
    List<Reference> refs = new ArrayList<Reference>(references.size());
    List<XMLObject> objs = new ArrayList<XMLObject>(objects.size());

    for (SigReference r : references) {
      refs.add(r.createReference(this));
    }
    for (SigObject o : objects) {
      objs.add(o.createObject(this));
    }
    XMLSignature signature = createSignature(sigType.toUrlString(), refs, objs);

    // Create the Node that will hold the resulting XMLSignature
    // If the signature is going to be written into an existing document,
    // then deal with it, otherwise create a new document
    Node node;
    if (this.insertXpath != null) {
      node = getNode(this.sourceForOutput, this.insertXpath, this.insertXpathNamespaceMap);
    } else {
      node = documentBuilderFactory.newDocumentBuilder().newDocument();
    }

    DOMSignContext signContext;
    // Create a the signcontext and specify where the signature should go
    // (i.e. as a child or sibling of node)
    if (insertAsSibling) {
      signContext = new DOMSignContext(keyInfo.getPrivateKey(), node.getParentNode(), node);
    } else {
      signContext = new DOMSignContext(keyInfo.getPrivateKey(), node);
    }
    signContext.setURIDereferencer(new MappedURIDereferencer(xmlSignatureFactory.getURIDereferencer(), resolver));
    // Marshal, generate (and sign) the XMLSignature. The DOM
    // Document will contain the XML Signature if this method returns
    // successfully.
    signature.sign(signContext);

    // output the resulting document
    Transformer trans = TransformerFactory.newInstance().newTransformer();

    StreamResult result = new StreamResult(os);
    Document doc;
    // Get the source document to write out
    if (node instanceof Document) {
      doc = (Document) node;
    } else {
      doc = node.getOwnerDocument();
    }
    doc.setXmlStandalone(true);
    trans.transform(new DOMSource(doc), result);
  }

  private XMLSignature createSignature(String signatureMethod, List<Reference> references, List<XMLObject> objects)
      throws Exception {
    // Create the SignedInfo
    SignedInfo si = xmlSignatureFactory.newSignedInfo(
        xmlSignatureFactory.newCanonicalizationMethod(canonicalizationType.toUriString(),
            (C14NMethodParameterSpec) null),
        xmlSignatureFactory.newSignatureMethod(signatureMethod, (SignatureMethodParameterSpec) null), references);
    return xmlSignatureFactory.newXMLSignature(si, keyInfo.createKeyInfo(this), objects, id, (String) null);
  }

  private static Element getNode(InputStream source, String xpathExpr, Map<String, String> xpathNamespaceMap)
      throws XPathException, ParserConfigurationException, SAXException, IOException {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    if (xpathNamespaceMap != null) {
      xpath.setNamespaceContext(new CustomNamespaceContext(xpathNamespaceMap));
    }

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(source);
    HashSet<String> ids = new HashSet<String>();
    Util.setIdOnDOM(doc, ids);

    // This is the node identified where the signature will either be
    // includes as the last child, or as a preceding sibling
    Node node = (Node) xpath.evaluate(xpathExpr, doc, XPathConstants.NODE);
    if (node == null) {
      throw new XPathException("The XPath did not return an element: " + xpathExpr);
    }
    return node instanceof Document ? (Element) node.getFirstChild() : (Element) node;
  }
}
