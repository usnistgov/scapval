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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.scap.validation.exceptions.ConfigurationException;
import gov.nist.secauto.scap.validation.exceptions.SCAPException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AutoDetectRejectionTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void rejectsAutoNonXmlWithContentError() throws Exception {
    File candidate = write("bad.xml", "not XML");
    SCAPException rejection = assertRejected("-auto", candidate.getAbsolutePath());
    assertEquals("Unable to auto-detect the content type of: " + candidate.getAbsolutePath()
        + ". The file does not appear to be SCAP source, result, or component content."
        + " Use -file, -resultfile, or -componentfile to specify the content type explicitly.",
        rejection.getMessage());
    assertNotNull(rejection.getCause());
  }

  @Test
  public void rejectsAutoDoctype() throws Exception {
    // An internal entity needs no listener or external resource; the declaration itself is banned.
    File candidate = write("doctype.xml", "<!DOCTYPE root [<!ENTITY value 'internal'>]><root>&value;</root>");
    SCAPException rejection = assertRejected("-auto", candidate.getAbsolutePath());
    assertEquals("\"" + candidate.getAbsolutePath()
        + "\" was rejected: DOCTYPE declarations are not allowed in SCAP content.", rejection.getMessage());
    assertNotNull(rejection.getCause());
  }

  @Test
  public void rejectsFileNonXmlWithoutExplicitVersion() throws Exception {
    // Same message as before the parser hardening: the version cannot be read, so ask for it.
    File candidate = write("bad.xml", "not XML");
    try {
      new Application().runCLI(new String[] { "-file", candidate.getAbsolutePath() });
      fail("Candidate must be rejected with a configuration error");
    } catch (ConfigurationException e) {
      assertEquals("-scapversion could not be determined from the content. Please specify it explicitly.",
          e.getMessage());
    }
  }

  @Test
  public void rejectsFileDoctypeWithoutExplicitVersion() throws Exception {
    File candidate = write("doctype.xml", "<!DOCTYPE root [<!ENTITY value 'internal'>]><root>&value;</root>");
    assertTrue(assertRejected("-file", candidate.getAbsolutePath()).getMessage().contains("DOCTYPE"));
  }

  @Test
  public void autoNonXmlExitsOne() throws Exception {
    File candidate = write("bad.xml", "not XML");
    File output = temporaryFolder.newFolder("output");
    File console = new File(output, "console.log");
    String classpath = Arrays.stream(System.getProperty("surefire.test.class.path",
        System.getProperty("java.class.path")).split(Pattern.quote(File.pathSeparator)))
        .map(entry -> new File(entry).getAbsolutePath()).collect(Collectors.joining(File.pathSeparator));
    Process process = new ProcessBuilder(new File(System.getProperty("java.home"), "bin/java").getAbsolutePath(),
        "-Djava.protocol.handler.pkgs=sun.net.www.protocol", "-cp", classpath,
        Application.class.getName(), "-auto", candidate.getAbsolutePath())
        .directory(output).redirectErrorStream(true).redirectOutput(console).start();
    try {
      assertTrue("CLI must finish", process.waitFor(120, TimeUnit.SECONDS));
      String log = Files.readString(console.toPath());
      assertEquals(log, 1, process.exitValue());
      assertTrue(log, log.contains("Unable to auto-detect the content type"));
    } finally {
      if (process.isAlive()) {
        process.destroyForcibly();
        process.waitFor();
      }
    }
  }

  private File write(String name, String content) throws Exception {
    File candidate = temporaryFolder.newFile(name);
    Files.write(candidate.toPath(), content.getBytes(StandardCharsets.UTF_8));
    return candidate;
  }

  private SCAPException assertRejected(String... args) throws Exception {
    try {
      new Application().runCLI(args);
      fail("Candidate must be rejected with a content error");
      return null;
    } catch (SCAPException e) {
      return e;
    }
  }
}
