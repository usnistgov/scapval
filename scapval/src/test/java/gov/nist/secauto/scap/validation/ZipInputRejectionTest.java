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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.decima.core.document.DocumentException;
import gov.nist.secauto.scap.validation.exceptions.ConfigurationException;
import gov.nist.secauto.scap.validation.exceptions.SCAPException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipInputRejectionTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void rejectsZipSource13() throws Exception {
    File zip = createZip();
    assertRejected(zip, "-file", zip.getAbsolutePath(), "-scapversion", "1.3");
  }

  @Test
  public void rejectsZipSource14() throws Exception {
    File zip = createZip();
    assertRejected(zip, "-file", zip.getAbsolutePath(), "-scapversion", "1.4");
  }

  @Test
  public void rejectsAutoZip() throws Exception {
    File zip = createZip();
    assertRejected(zip, "-auto", zip.getAbsolutePath());
  }

  @Test
  public void rejectsZipRenamedXml() throws Exception {
    File zip = renameAsXml(createZip());
    assertRejected(zip, "-file", zip.getAbsolutePath(), "-scapversion", "1.4");
  }

  @Test
  public void rejectsAutoZipRenamedXml() throws Exception {
    File zip = renameAsXml(createZip());
    assertRejected(zip, "-auto", zip.getAbsolutePath());
  }

  @Test
  public void rejectsZipSourceDatastreamForResult() throws Exception {
    File zip = createZip();
    File result = copyFixture("candidates/scap-14/ARF-results.xml", "result.xml");
    assertRejected(zip, "-resultfile", result.getAbsolutePath(), "-scapversion", "1.4",
        "-sourceds", zip.getAbsolutePath());
  }

  @Test
  public void batchIgnoresZipAndValidatesXml() throws Exception {
    File zip = createZip();
    File valid = new File(zip.getParentFile(), "valid.xml");
    Files.copy(new File("src/test/resources/candidates/components/oval/oval-variables-5-12-3.xml").toPath(),
        valid.toPath());
    File output = temporaryFolder.newFolder("output");
    File console = new File(output, "console.log");
    // Batch reports use the working directory; no output-directory option is available.
    // Resolve classpath entries before starting the CLI in a temporary working directory.
    String classpath = Arrays.stream(System.getProperty("surefire.test.class.path",
        System.getProperty("java.class.path")).split(Pattern.quote(File.pathSeparator)))
        .map(entry -> new File(entry).getAbsolutePath()).collect(Collectors.joining(File.pathSeparator));
    Process process = null;
    try {
      process = new ProcessBuilder(new File(System.getProperty("java.home"), "bin/java").getAbsolutePath(),
          "-Djava.protocol.handler.pkgs=sun.net.www.protocol",
          "-cp", classpath, Application.class.getName(), "-auto", zip.getParent())
          .directory(output).redirectErrorStream(true).redirectOutput(console).start();
      assertTrue("Batch validation must finish", process.waitFor(120, TimeUnit.SECONDS));
      assertEquals("The valid XML must pass; the ZIP must not be processed. CLI output:\n"
          + new String(Files.readAllBytes(console.toPath()), StandardCharsets.UTF_8), 0, process.exitValue());
      assertTrue(new File(output, "valid-validation-result.xml").isFile());
      assertFalse(new File(output, "evil-validation-result.xml").exists());
      assertFalse(new File(output, "evil-validation-report.html").exists());
      String summary = new String(Files.readAllBytes(new File(output, "batch-validation-summary.html").toPath()),
          StandardCharsets.UTF_8);
      assertTrue(summary.contains("valid.xml"));
      assertFalse(summary.contains("evil"));
    } finally {
      if (process != null && process.isAlive()) {
        process.destroyForcibly();
        process.waitFor();
      }
      assertNothingExtracted(zip);
    }
  }

  private File createZip() throws IOException {
    File zip = new File(temporaryFolder.newFolder("input"), "evil.zip");
    try (ZipOutputStream output = new ZipOutputStream(new FileOutputStream(zip))) {
      String[] entries = { "content.xml", "../zip-escape-canary.txt", "nested/../../zip-escape-canary2.txt" };
      for (String entry : entries) {
        output.putNextEntry(new ZipEntry(entry));
        output.write("<content>harmless canary</content>".getBytes(StandardCharsets.UTF_8));
        output.closeEntry();
      }
    }
    return zip;
  }

  private File renameAsXml(File zip) throws IOException {
    return Files.move(zip.toPath(), new File(zip.getParentFile(), "evil.xml").toPath()).toFile();
  }

  private File copyFixture(String fixture, String name) throws IOException {
    File target = new File(temporaryFolder.getRoot(), name);
    Files.copy(new File("src/test/resources/" + fixture).toPath(), target.toPath());
    return target;
  }

  private void assertRejected(File zip, String... args) throws Exception {
    boolean rejected = false;
    try {
      rejected = new Application().runCLI(args) != 0;
    } catch (ConfigurationException | SCAPException | DocumentException e) {
      // These are the CLI harness's expected input rejection exceptions.
      rejected = true;
    } finally {
      assertNothingExtracted(zip);
    }
    assertTrue("ZIP input must not succeed", rejected);
  }

  private void assertNothingExtracted(File zip) throws IOException {
    assertFalse(new File(temporaryFolder.getRoot(), "zip-escape-canary.txt").exists());
    assertFalse(new File(temporaryFolder.getRoot(), "zip-escape-canary2.txt").exists());
    // Also cover traversal relative to a hypothetical extraction directory.
    assertFalse(new File(zip.getParentFile(), "zip-escape-canary.txt").exists());
    assertFalse(new File(zip.getParentFile(), "zip-escape-canary2.txt").exists());
    assertFalse(new File(zip.getParentFile(), "evil").exists());
    try (java.util.stream.Stream<java.nio.file.Path> siblings = Files.list(zip.getParentFile().toPath())) {
      assertFalse("No extraction directory may appear next to the input", siblings.anyMatch(Files::isDirectory));
    }
  }
}
