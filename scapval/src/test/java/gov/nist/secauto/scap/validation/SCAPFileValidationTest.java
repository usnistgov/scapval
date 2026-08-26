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

import gov.nist.secauto.decima.core.assessment.result.BaseRequirementResult;
import gov.nist.secauto.decima.core.assessment.result.ResultStatus;
import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.scap.validation.Application;
import gov.nist.secauto.scap.validation.SCAPValAssessmentResults;
import gov.nist.secauto.scap.validation.SCAPValWrapper;
import gov.nist.secauto.scap.validation.SCAPVersion;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SCAPFileValidationTest {

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void SCAP12SourceExpectPass() throws Exception {
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_2)
            .useCase("CONFIGURATION").submissionFileLocation(testFile).run();
    // assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        // this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP12SourceExpectFail() throws Exception {
    boolean passed = true;
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall-ERROR.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_2)
            .useCase("CONFIGURATION").isOnline(false).submissionFileLocation(testFile).run(); // assure many results
    // were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        passed = false;
      }
    }
    if (passed) {
      Assert.fail("There should have been a failed assessment.");
    }
  }

  @Test
  public void SCAP13SourceExpectPass() throws Exception {
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-13/source_data_stream_collection_sample.xml").getFile())
            .getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_3)
            .useCase("CONFIGURATION").isOnline(true).submissionFileLocation(testFile).run();
    // assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    // SRC-216 and SRC-329 are expected to FAIL because the test candidate contains deprecated
    // OVAL elements (e.g. macos:plist510_test/object/state) which trigger these MUST requirements
    java.util.Set<String> expectedFails = java.util.Set.of("SRC-216", "SRC-329");
    java.util.List<String> unexpectedFails = new java.util.ArrayList<>();
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        String id = baseRequirementResult.getBaseRequirement().getId();
        if (!expectedFails.contains(id)) {
          unexpectedFails.add(id);
        }
      }
    }
    if (!unexpectedFails.isEmpty()) {
      Assert.fail("Should not have had a result with FAIL. Unexpected failed requirements: " + unexpectedFails);
    }
  }

  @Test
  public void SCAP13SourceExpectFail() throws Exception {
    boolean passed = true;
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-13/source_data_stream_collection_sample-NO-USE-CASE.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_3)
            .useCase("CONFIGURATION").submissionFileLocation(testFile).run();
    // assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        passed = false;
      }
    }
    if (passed) {
      Assert.fail("There should have been a failed assessment.");
    }
  }

  @Test
  public void SCAP14SourceExpectPass() throws Exception {
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-14/source_data_stream_collection_sample.xml").getFile())
            .getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_4)
            .useCase("CONFIGURATION").isOnline(true).submissionFileLocation(testFile).run();
    // assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        // this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP14SourceOVAL5123ExpectPass() throws Exception {
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-14/source_data_stream_collection_sample-oval5123.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_4)
            .useCase("CONFIGURATION").isOnline(true).submissionFileLocation(testFile).run();
    // assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        // a SCAP 1.4 source datastream declaring embedded OVAL 5.12.3 should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP14SourceUnbundledOVAL5121ExpectPass() throws Exception {
    // Same content as SCAP14SourceOVAL5123ExpectPass, declaring an OVAL 5.12 patch level that has no
    // schema bundle of its own. SP 800-126Ar4 approves the whole 5.12.x family, so the run must
    // complete with no FAIL.
    Path testFile = copyFixtureWithOvalVersion(
        "src/test/resources/candidates/scap-14/source_data_stream_collection_sample-oval5123.xml", "5.12.3",
        "5.12.1");
    try {
      SCAPValAssessmentResults assessmentResults
          = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_4)
              .useCase("CONFIGURATION").isOnline(true).submissionFileLocation(testFile.toString()).run();
      Assert.assertTrue("A source data stream declaring unbundled OVAL 5.12.1 must reach the assessment phase",
          assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
      for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
          .getBaseRequirementResults()) {
        Assert.assertNotEquals("Unexpected FAIL for " + baseRequirementResult.getBaseRequirement().getId(),
            ResultStatus.FAIL, baseRequirementResult.getStatus());
      }
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  @Test
  public void SCAP14ResultUnbundledOVAL5121ProducesAssessmentResults() throws Exception {
    Path testFile = copyFixtureWithOvalVersion("src/test/resources/candidates/scap-14/ARF-results-oval5123.xml",
        "5.12.3", "5.12.1");
    try {
      SCAPValAssessmentResults assessmentResults
          = new SCAPValWrapper.Builder().submissionType(Application.ContentType.RESULT)
              .scapVersion(SCAPVersion.V1_4).submissionFileLocation(testFile.toString()).run();

      Assert.assertTrue("An ARF containing unbundled OVAL 5.12.1 must reach the assessment phase",
          assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  /**
   * Copies a classpath fixture to a temp file, rewriting every {@code <oval:schema_version>} text
   * value equal to {@code fromVersion} to {@code toVersion}. The caller deletes the copy.
   */
  private static Path copyFixtureWithOvalVersion(String fixture, String fromVersion, String toVersion)
      throws IOException {
    Path source = new File(new URL("classpath:" + fixture).getFile()).toPath();
    String content = new String(Files.readAllBytes(source), StandardCharsets.UTF_8);
    String modifiedContent = content.replace(">" + fromVersion + "<", ">" + toVersion + "<");
    Assert.assertNotEquals("Fixture " + fixture + " must declare OVAL " + fromVersion, content, modifiedContent);
    Path copy = Files.createTempFile("scapval-oval-" + toVersion + "-", ".xml");
    Files.write(copy, modifiedContent.getBytes(StandardCharsets.UTF_8));
    return copy;
  }

  @Test
  public void SCAP14SourceExpectFail() throws Exception {
    boolean passed = true;
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates/scap-14/source_data_stream_collection_sample-NO-USE-CASE.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults
        = new SCAPValWrapper.Builder().submissionType(Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_4)
            .useCase("CONFIGURATION").submissionFileLocation(testFile).run();
    // assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        passed = false;
      }
    }
    if (passed) {
      Assert.fail("There should have been a failed assessment.");
    }
  }

}
