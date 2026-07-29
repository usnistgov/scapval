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

import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * Regression guard for the 1.4.3-rc1 defect where scapval.bat hardcoded "scapval-1.4.2.jar" and
 * the shipped Windows launcher pointed at a file absent from the distribution. Nothing in the
 * build inspected the packaged output, so the broken zip reached users.
 *
 * These checks run against the assembled distribution after the package phase and assert the
 * outcome (each launcher names a jar that is present) rather than the mechanism (a Maven
 * placeholder is present), so a changed finalName or a disabled filter is caught too.
 */
public class DistributionLauncherIT {

  private static final String BAT = "scapval.bat";
  private static final String SH = "scapval.sh";

  private static final Pattern BAT_JAR_ARG = Pattern.compile("-jar\\s+\"%~dp0([^\"]+)\"");
  private static final Pattern SH_JAR_ARG = Pattern.compile("-jar\\s+\"([^\"]+)\"");

  private static final String BUILD_DIRECTORY = required("project.build.directory");
  private static final String FINAL_NAME = required("project.build.finalName");
  private static final String PACKAGING = required("project.packaging");

  /** The exploded distribution, target/scapval-&lt;version&gt;. */
  private static final File DISTRIBUTION = new File(BUILD_DIRECTORY, FINAL_NAME);

  /** The archive users download, target/scapval-&lt;version&gt;.zip. */
  private static final File ARCHIVE = new File(BUILD_DIRECTORY, FINAL_NAME + ".zip");

  /** The jar both launchers must invoke, scapval-&lt;version&gt;.jar. */
  private static final String EXPECTED_JAR = FINAL_NAME + "." + PACKAGING;

  private static String required(String name) {
    String value = System.getProperty(name);
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalStateException("System property '" + name
          + "' was not passed to the integration test. Check the maven-failsafe-plugin"
          + " systemPropertyVariables configuration in scapval/pom.xml.");
    }
    return value;
  }

  private static File launcher(String name) {
    File file = new File(DISTRIBUTION, name);
    assertTrue("Distribution is missing " + name + ". Expected it at " + file.getAbsolutePath()
        + ". Was the assembly built?", file.isFile());
    return file;
  }

  private static String read(String name) throws IOException {
    return new String(Files.readAllBytes(launcher(name).toPath()), StandardCharsets.UTF_8);
  }

  private static String jarArgument(Pattern pattern, String launcherName) throws IOException {
    String content = read(launcherName);
    Matcher matcher = pattern.matcher(content);
    assertTrue("Could not find a -jar argument in " + launcherName
        + ". The launch line was rewritten in a way this guard does not understand;"
        + " update the pattern or the launcher.", matcher.find());
    String argument = matcher.group(1);
    // Tolerate a directory prefix such as $SCAPVAL_HOME/ in the shell launcher.
    int lastSlash = argument.lastIndexOf('/');
    return lastSlash < 0 ? argument : argument.substring(lastSlash + 1);
  }

  private static void assertNamesTheDistributedJar(String launcherName, String named) {
    assertEquals(launcherName + " launches a jar whose name does not match this build."
        + " A version literal was most likely hardcoded instead of filtered.",
        EXPECTED_JAR, named);
    assertTrue(launcherName + " launches '" + named + "', which is not in the distribution at "
        + DISTRIBUTION.getAbsolutePath() + ". Running it would fail with"
        + " \"Unable to access jarfile\".", new File(DISTRIBUTION, named).isFile());
  }

  @Test
  public void batLauncherNamesAnExistingJar() throws IOException {
    assertNamesTheDistributedJar(BAT, jarArgument(BAT_JAR_ARG, BAT));
  }

  @Test
  public void shLauncherNamesAnExistingJar() throws IOException {
    assertNamesTheDistributedJar(SH, jarArgument(SH_JAR_ARG, SH));
  }

  /**
   * Resource filtering supplies the jar name. Check for "${project." specifically: scapval.sh
   * legitimately contains shell expansions such as ${JAVA_OPTS[@]} and ${MINIMUM_JAVA_VERSION}.
   */
  @Test
  public void launchersHaveNoUnresolvedMavenProperties() throws IOException {
    for (String name : new String[] { BAT, SH }) {
      assertTrue(name + " contains an unresolved '${project.' property. Resource filtering for"
          + " src/main/distro is no longer being applied.", !read(name).contains("${project."));
    }
  }

  /**
   * .gitattributes pins *.bat to CRLF and the assembly fileSet sets no lineEnding, so nothing
   * else enforces this on the packaged file. CRLF was lost once before (commit fd459e4).
   */
  @Test
  public void batLauncherKeepsCrlfLineEndings() throws IOException {
    byte[] content = Files.readAllBytes(launcher(BAT).toPath());
    for (int i = 0; i < content.length; i++) {
      if (content[i] == '\n') {
        assertTrue(BAT + " has a bare LF at byte " + i + ". Windows batch files must keep CRLF.",
            i > 0 && content[i - 1] == '\r');
      }
    }
  }

  @Test
  public void archiveContainsTheLauncherTargetJar() throws IOException {
    assertTrue("Distribution archive not found at " + ARCHIVE.getAbsolutePath(), ARCHIVE.isFile());
    try (ZipFile zip = new ZipFile(ARCHIVE)) {
      assertNotNull(ARCHIVE.getName() + " does not contain " + EXPECTED_JAR
          + ", so the launchers in it cannot start.", zip.getEntry(EXPECTED_JAR));
    }
  }

  /**
   * End-to-end smoke test of the shell launcher, run from outside the distribution root to cover
   * the companion defect where the jar was named by a bare relative path.
   */
  @Test
  public void shLauncherRunsOutsideDistributionRoot() throws IOException, InterruptedException {
    Assume.assumeFalse("scapval.sh cannot be executed on this platform",
        System.getProperty("os.name", "").toLowerCase().startsWith("windows"));

    File workingDirectory = new File(BUILD_DIRECTORY);
    // Redirect to a file rather than a pipe so a chatty or hung run cannot deadlock the test.
    File log = File.createTempFile("scapval-version-", ".log", workingDirectory);
    log.deleteOnExit();

    Process process = new ProcessBuilder(launcher(SH).getAbsolutePath(), "-version")
        .directory(workingDirectory)
        .redirectErrorStream(true)
        .redirectOutput(log)
        .start();

    boolean finished;
    try {
      finished = process.waitFor(5, TimeUnit.MINUTES);
    } finally {
      process.destroyForcibly();
    }

    String output = new String(Files.readAllBytes(log.toPath()), StandardCharsets.UTF_8);
    assertTrue("scapval.sh -version did not finish within 5 minutes. Output:\n" + output, finished);
    assertEquals("scapval.sh -version failed when run from " + workingDirectory.getAbsolutePath()
        + ". Output:\n" + output, 0, process.exitValue());
    assertTrue("scapval.sh -version did not report " + FINAL_NAME + ". Output:\n" + output,
        output.contains(FINAL_NAME));
  }
}
