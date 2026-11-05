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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
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
import java.util.regex.Matcher;
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
    assertUnreadableXml(candidate, rejection, 1);
    assertTrue(rejection.getMessage(), rejection.getMessage().contains("(line 1,"));
  }

  @Test
  public void rejectsAutoTruncatedDataStream() throws Exception {
    File candidate = writeTruncatedDataStream();
    assertUnreadableXml(candidate, assertRejected("-auto", candidate.getAbsolutePath()), 2);
  }

  @Test
  public void rejectsAutoMismatchedTag() throws Exception {
    File candidate = write("mismatched.xml",
        "<data-stream-collection xmlns='http://scap.nist.gov/schema/scap/source/1.2'>\n"
            + "<data-stream scap-version='1.4' use-case='CONFIGURATION'></bad>");
    assertUnreadableXml(candidate, assertRejected("-auto", candidate.getAbsolutePath()), 2);
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
    File candidate = write("bad.xml", "not XML");
    SCAPException rejection = assertRejected("-file", candidate.getAbsolutePath());
    assertUnreadableXml(candidate, rejection, 1);
    assertTrue(rejection.getMessage(), rejection.getMessage().contains("(line 1,"));
  }

  @Test
  public void rejectsFileTruncatedWithoutExplicitVersion() throws Exception {
    File candidate = writeTruncatedDataStream();
    assertUnreadableXml(candidate, assertRejected("-file", candidate.getAbsolutePath()), 2);
  }

  @Test
  public void rejectsResultFileTruncatedWithoutExplicitVersion() throws Exception {
    File candidate = writeTruncatedDataStream();
    assertUnreadableXml(candidate, assertRejected("-resultfile", candidate.getAbsolutePath()), 2);
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
      assertTrue(log, log.contains("could not be read as XML"));
    } finally {
      if (process.isAlive()) {
        process.destroyForcibly();
        process.waitFor();
      }
    }
  }

  private void assertUnreadableXml(File candidate, SCAPException rejection, int minimumLine) {
    String message = rejection.getMessage();
    assertTrue(message, message.startsWith("\"" + candidate.getAbsolutePath() + "\" could not be read as XML:"));
    assertFalse(message, message.contains("Unable to auto-detect"));
    assertFalse(message, message.contains("DOCTYPE"));
    Matcher location = Pattern.compile("\\(line (\\d+), column -?\\d+\\)\\.$").matcher(message);
    assertTrue(message, location.find());
    assertTrue(message, Integer.parseInt(location.group(1)) >= minimumLine);
    assertNotNull(rejection.getCause());
  }

  private File writeTruncatedDataStream() throws Exception {
    byte[] content = Files.readAllBytes(new File(
        "src/test/resources/candidates/scap-13/source_data_stream_collection_sample.xml").toPath());
    File candidate = temporaryFolder.newFile("truncated.xml");
    Files.write(candidate.toPath(), Arrays.copyOf(content, content.length / 2));
    return candidate;
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
