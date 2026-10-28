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

package gov.nist.secauto.scap.validation;

import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secautotrust.signature.KeyInfoBuilder;
import gov.nist.secautotrust.signature.SecAutoTrustMain;
import gov.nist.secautotrust.signature.Signature;
import gov.nist.secautotrust.signature.XMLValidator;
import gov.nist.secautotrust.signature.config.ValidateSigConfig;
import gov.nist.secautotrust.signature.enums.SignatureType;
import gov.nist.secautotrust.signer.ScapDataStreamSigner;
import gov.nist.secautotrust.signer.ScapResultsSigner;
import gov.nist.secautotrust.signer.config.ScapDataStreamSignerConfig;
import gov.nist.secautotrust.signer.config.ScapResultSignerConfig;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XXESignatureTest {
  @Rule
  public TemporaryFolder temporary = new TemporaryFolder();
  private HttpServer server;
  private String origin;
  private String canary;
  private File local;
  private String keystore;
  private final AtomicInteger requests = new AtomicInteger();

  @Before
  public void setup() throws Exception {
    ClasspathHandler.initialize();
    keystore = new URL("classpath:src/test/resources/tmsad/test.jks").getPath();
    canary = "HARMLESS-SIGNATURE-XXE-" + UUID.randomUUID();
    local = write("canary.txt", canary);
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    origin = "http://127.0.0.1:" + server.getAddress().getPort();
    server.createContext("/", exchange -> {
      requests.incrementAndGet();
      byte[] response = ("<!ENTITY leaked SYSTEM '" + local.toURI() + "'>").getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, response.length);
      try (java.io.OutputStream output = exchange.getResponseBody()) {
        output.write(response);
      }
    });
    server.start();
  }

  @After
  public void shutdown() {
    if (server != null) {
      server.stop(0);
    }
  }

  private String[] declarations() {
    return new String[] {
        "<!DOCTYPE root [<!ENTITY leaked SYSTEM '" + local.toURI() + "'>]>",
        "<!DOCTYPE root [<!ENTITY % remote SYSTEM '" + origin + "/parameter.dtd'>%remote;]>",
        "<!DOCTYPE root SYSTEM '" + origin + "/remote.dtd'>",
        "<!DOCTYPE root [<!ENTITY leaked 'internal'>]>" };
  }

  @Test
  public void signatureValidationRejectsDoctypeWithoutDisclosure() throws Exception {
    for (String declaration : declarations()) {
      File candidate = write(UUID.randomUUID() + ".xml", declaration
          + "<root xmlns:ds='http://www.w3.org/2000/09/xmldsig#'><ds:Signature>&leaked;</ds:Signature></root>");
      rejected(() -> SecAutoTrustMain.validateSignature(candidate.toString(), keystore, "test-alias", "123456"));
    }
  }

  @Test
  public void signingRejectsDoctypeInSourceWithoutDisclosure() throws Exception {
    File source = new File(new URL("classpath:src/test/resources/tmsad/scap-data-stream.xml").getPath());
    for (String declaration : declarations()) {
      File candidate = write(UUID.randomUUID() + ".xml", declaration + "<root>&leaked;</root>");
      File config = new File(temporary.getRoot(), UUID.randomUUID() + ".xml");
      File output = new File(temporary.getRoot(), UUID.randomUUID() + ".signed.xml");
      SecAutoTrustMain.createScapSignConfig(config.toString(), source.toString(), output.toString(), "SHA256",
          "RSA_SHA256", keystore, "test-alias", "false");
      String xml = Files.readString(config.toPath()).replace(source.toString(), candidate.toString());
      assertTrue(xml.contains(candidate.toString()));
      Files.writeString(config.toPath(), xml);
      rejected(() -> SecAutoTrustMain.sign(config.toString(), "123456", "123456"));
      if (output.exists()) {
        assertFalse(Files.readString(output.toPath()).contains(canary));
      }
    }
  }

  @Test
  public void jaxbConfigurationRejectsDoctypeWithoutDisclosure() throws Exception {
    for (String declaration : declarations()) {
      File config = write(UUID.randomUUID() + ".xml", declaration
          + "<signs xmlns='http://scap.nist.gov/tmsad/input' output-file='unused'>&leaked;</signs>");
      rejected(() -> SecAutoTrustMain.sign(config.toString(), "123456", "123456"));
    }
  }

  @Test
  public void signingAndSignatureSitesRejectDoctypeThroughSharedFactory() throws Exception {
    java.security.KeyPair keys = java.security.KeyPairGenerator.getInstance("RSA").generateKeyPair();
    KeyInfoBuilder keyInfo = new KeyInfoBuilder().privateKey(keys.getPrivate()).publicKey(keys.getPublic());
    for (String declaration : declarations()) {
      File candidate = write(UUID.randomUUID() + ".xml", declaration + "<root>&leaked;</root>");
      try (java.io.InputStream content = Files.newInputStream(candidate.toPath())) {
        rejected(() -> XMLValidator.validateContent(new ValidateSigConfig.Builder().content(content)
            .trustedPublicKey(keys.getPublic()).build()));
      }
      rejected(() -> ScapDataStreamSigner.getInstance().createConfig(new ScapDataStreamSignerConfig.Builder()
          .content(candidate.toString()).sigType(SignatureType.RSA_SHA256).keystore(keystore).alias("test-alias")
          .outputStream(new ByteArrayOutputStream()).outputFileLocation("unused").build()));
      try (java.io.InputStream content = Files.newInputStream(candidate.toPath())) {
        rejected(() -> ScapResultsSigner.signResultStream(new ScapResultSignerConfig.Builder().content(content)
            .sigType(SignatureType.RSA_SHA256).keyInfo(keyInfo).outputStream(new ByteArrayOutputStream()).build()));
      }
      try (java.io.InputStream content = Files.newInputStream(candidate.toPath())) {
        Signature.Builder builder = new Signature.Builder().sigType(SignatureType.RSA_SHA256).keyInfoBuilder(keyInfo)
            .outputStream(new ByteArrayOutputStream()).sourceForOutput(content).insertXpath("/root");
        builder.newDetachedReferenceBuilder().uri("");
        Signature signature = builder.build();
        rejected(signature::signContents);
      }
      try (java.io.InputStream content = Files.newInputStream(candidate.toPath())) {
        Signature signature = new Signature.Builder().sigType(SignatureType.RSA_SHA256).keyInfoBuilder(keyInfo)
            .outputStream(new ByteArrayOutputStream()).newObject("untrusted", content).build();
        rejected(signature::signContents);
      }
    }
  }

  private File write(String name, String content) throws Exception {
    File file = new File(temporary.getRoot(), name);
    Files.writeString(file.toPath(), content);
    return file;
  }

  private void rejected(Attempt attempt) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream previousOut = System.out;
    PrintStream previousErr = System.err;
    try (PrintStream capture = new PrintStream(output, true, StandardCharsets.UTF_8.name())) {
      System.setOut(capture);
      System.setErr(capture);
      try {
        attempt.run();
        fail("DOCTYPE input must be rejected");
      } catch (Exception expected) {
        boolean parseRejection = false;
        for (Throwable cause = expected; cause != null; cause = cause.getCause()) {
          assertFalse(String.valueOf(cause.getMessage()).contains(canary));
          if (cause instanceof SAXParseException && cause.getMessage().contains("DOCTYPE")) {
            parseRejection = true;
          }
        }
        assertTrue("Expected DOCTYPE parse rejection: " + expected, parseRejection);
      }
    } finally {
      System.setOut(previousOut);
      System.setErr(previousErr);
    }
    assertFalse(output.toString(StandardCharsets.UTF_8.name()).contains(canary));
    assertEquals(0, requests.get());
  }

  private interface Attempt {
    void run() throws Exception;
  }
}
