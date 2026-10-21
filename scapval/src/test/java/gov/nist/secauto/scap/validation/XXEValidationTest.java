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
import gov.nist.secauto.decima.core.document.DocumentException;
import gov.nist.secauto.scap.validation.candidate.ScapDocumentSniffer;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXNotRecognizedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class XXEValidationTest {
  @Rule
  public TemporaryFolder temporary = new TemporaryFolder();
  private HttpServer server;
  private String origin;
  private String canary;
  private File local;
  private final AtomicInteger requests = new AtomicInteger();

  @Before
  public void setup() throws Exception {
    ClasspathHandler.initialize();
    canary = "HARMLESS-SCAP-XXE-" + UUID.randomUUID();
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

  @Test
  public void snifferInitializationFailsWithUnsupportedMandatoryControl() throws Exception {
    File log = new File(temporary.getRoot(), "initialization.log");
    Process process = new ProcessBuilder(new File(System.getProperty("java.home"), "bin/java").toString(), "-cp",
        System.getProperty("surefire.test.class.path", System.getProperty("java.class.path")),
        InitializationProbe.class.getName()).redirectErrorStream(true).redirectOutput(log).start();
    try {
      org.junit.Assert.assertTrue(process.waitFor(30, TimeUnit.SECONDS));
      assertEquals(Files.readString(log.toPath()), 0, process.exitValue());
      org.junit.Assert.assertTrue(Files.readString(log.toPath()).contains("SECURE_INITIALIZATION_REJECTED"));
    } finally {
      process.destroyForcibly();
    }
  }

  public static class InitializationProbe {
    public static void main(String[] args) {
      System.setProperty("javax.xml.parsers.SAXParserFactory", RejectingFactory.class.getName());
      try {
        new ScapDocumentSniffer().findContentType("not-opened.xml");
        throw new AssertionError("Unsafe initialization continued");
      } catch (IllegalStateException expected) {
        System.out.println("SECURE_INITIALIZATION_REJECTED");
      }
    }
  }

  public static class RejectingFactory
      extends SAXParserFactory {
    @Override
    public void setXIncludeAware(boolean aware) {
      // The mandatory feature setter below is the failure under test.
    }

    @Override
    public SAXParser newSAXParser() {
      throw new AssertionError("Parser must not be created after a mandatory control fails");
    }

    @Override
    public boolean getFeature(String name) {
      return false;
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
      throw new SAXNotRecognizedException("Required security feature unavailable");
    }
  }

  @Test
  public void allSnifferOperationsRejectEntitiesAndPartialDetection() throws Exception {
    String[] declarations = {
        "<!DOCTYPE root [<!ENTITY leaked SYSTEM '" + local.toURI() + "'>]>",
        "<!DOCTYPE root [<!ENTITY leaked SYSTEM '" + origin + "/general'>]>",
        "<!DOCTYPE root [<!ENTITY % remote SYSTEM '" + origin + "/parameter'>%remote;]>",
        "<!DOCTYPE root SYSTEM '" + origin + "/dtd'>",
        "<!DOCTYPE root [<!ENTITY % local SYSTEM '" + local.toURI() + "'>%local;]>",
        "<!DOCTYPE root [<!ENTITY leaked 'internal'>]>" };
    ScapDocumentSniffer sniffer = new ScapDocumentSniffer();
    for (String declaration : declarations) {
      File candidate = write(UUID.randomUUID() + ".xml", declaration + "<root>&leaked;</root>");
      rejected(() -> sniffer.findContentType(candidate.toString()), IllegalArgumentException.class);
      rejected(() -> sniffer.findSCAPVersion(candidate.toString()), IllegalArgumentException.class);
      rejected(() -> sniffer.findUseCase(candidate.toString()), IllegalArgumentException.class);
    }
    File partial = write("partial.xml", "<data-stream-collection xmlns='http://scap.nist.gov/schema/scap/source/1.2'>"
        + "<data-stream scap-version='1.4' use-case='CONFIGURATION'></bad>");
    rejected(() -> sniffer.findContentType(partial.toString()), IllegalArgumentException.class);
    rejected(() -> sniffer.findSCAPVersion(partial.toString()), IllegalArgumentException.class);
    rejected(() -> sniffer.findUseCase(partial.toString()), IllegalArgumentException.class);
    assertEquals(0, requests.get());
  }

  @Test
  public void explicitVersionAndComponentPathsCannotBypassDecimaProtection() throws Exception {
    File candidate = write("candidate.xml", "<!DOCTYPE root SYSTEM '" + origin + "/dtd'><root/>");
    rejected(() -> new Application().runCLI(new String[] { "-file", candidate.toString(), "-scapversion", "1.4" }),
        DocumentException.class);
    rejected(
        () -> new Application().runCLI(new String[] { "-resultfile", candidate.toString(), "-scapversion", "1.4" }),
        DocumentException.class);
    rejected(() -> new Application().runCLI(new String[] { "-componentfile", candidate.toString() }),
        DocumentException.class);
    rejected(() -> new Application().runProgrammatic(
        new String[] { "-file", candidate.toString(), "-scapversion", "1.4" }, null), DocumentException.class);
    assertEquals(0, requests.get());
  }

  @Test
  public void secondarySourceDocumentIsIndependentlyProtected() throws Exception {
    File result = write("result.xml",
        "<asset-report-collection xmlns='http://scap.nist.gov/schema/asset-reporting-format/1.1'/> ");
    File source
        = write("source.xml", "<!DOCTYPE root [<!ENTITY leaked SYSTEM '" + local.toURI() + "'>]><root>&leaked;</root>");
    Application app = new Application();
    app.parseCLI(new String[] { "-resultfile", result.toString(), "-scapversion", "1.4", "-sourceds", source.toString(),
        "-combinedoutput", new File(temporary.getRoot(), "combined.xml").toString() });
    rejected(() -> {
      Application.preAssessmentProcessing();
      return null;
    }, DocumentException.class);
    assertEquals(0, requests.get());
  }

  @Test
  public void downloadedComponentRejectsEntitiesWithoutSilentOmission() throws Exception {
    AtomicInteger componentRequests = new AtomicInteger();
    server.createContext("/component.xml", exchange -> {
      componentRequests.incrementAndGet();
      byte[] response = ("<!DOCTYPE root [<!ENTITY leak SYSTEM '" + local.toURI()
          + "'>]><root>&leak;</root>").getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, response.length);
      try (java.io.OutputStream output = exchange.getResponseBody()) {
        output.write(response);
      }
    });
    File source = write("download-source.xml", "<data-stream-collection "
        + "xmlns='http://scap.nist.gov/schema/scap/source/1.2' xmlns:xlink='http://www.w3.org/1999/xlink'>"
        + "<data-stream><component-ref id='scap_example_cref_remote' xlink:href='" + origin
        + "/component.xml'/></data-stream></data-stream-collection>");
    gov.nist.secauto.decima.xml.document.JDOMDocument document
        = new gov.nist.secauto.decima.xml.document.JDOMDocument(source);
    rejected(() -> {
      ContentCombiner.mergeRemoteResourcesInDS(document, SCAPVersion.V1_4, 1);
      return null;
    }, IllegalArgumentException.class);
    assertEquals(1, componentRequests.get());
    assertEquals(0, requests.get());
    assertFalse(document.asString(org.jdom2.output.Format.getRawFormat()).contains(canary));
  }

  @Test
  public void batchReportCannotExpandExistingMaliciousResults() throws Exception {
    write("result.xml", "<!DOCTYPE root SYSTEM '" + origin + "/dtd'><root>&leaked;</root>");
    File summary = BatchSummaryReportGenerator.generate(Collections.singletonList(
        new BatchSummaryReportGenerator.BatchFileResult("candidate.xml", false, "report.html", "result.xml")),
        temporary.getRoot(), "test", temporary.getRoot().toString());
    assertNotNull(summary);
    String html = Files.readString(summary.toPath());
    assertFalse(html.contains(canary));
    assertEquals(0, requests.get());
  }

  private File write(String name, String content) throws Exception {
    File file = new File(temporary.getRoot(), name);
    Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
    return file;
  }

  private void rejected(Attempt attempt, Class<? extends Exception> expectedType) throws Exception {
    try {
      attempt.run();
      fail("XXE input must be rejected");
    } catch (Exception expected) {
      if (!expectedType.isInstance(expected)) {
        throw expected;
      }
      for (Throwable cause = expected; cause != null; cause = cause.getCause()) {
        assertFalse(String.valueOf(cause.getMessage()).contains(canary));
      }
    }
  }

  private interface Attempt {
    Object run() throws Exception;
  }
}
