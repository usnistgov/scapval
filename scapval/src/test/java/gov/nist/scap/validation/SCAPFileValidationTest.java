/**
 * Portions of this software was developed by employees of the National Institute
 * of Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 17 United States Code Section 105, works of NIST employees are
 * not subject to copyright protection in the United States and are considered to
 * be in the public domain. Permission to freely use, copy, modify, and distribute
 * this software and its documentation without fee is hereby granted, provided that
 * this notice and disclaimer of warranty appears in all copies.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS' WITHOUT ANY WARRANTY OF ANY KIND, EITHER
 * EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY
 * THAT THE SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND FREEDOM FROM
 * INFRINGEMENT, AND ANY WARRANTY THAT THE DOCUMENTATION WILL CONFORM TO THE
 * SOFTWARE, OR ANY WARRANTY THAT THE SOFTWARE WILL BE ERROR FREE. IN NO EVENT
 * SHALL NIST BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM, OR
 * IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */
package gov.nist.scap.validation;

import gov.nist.decima.core.assessment.result.BaseRequirementResult;
import gov.nist.decima.core.assessment.result.ResultStatus;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class SCAPFileValidationTest {
  @Test
  public void SCAP11DIRSourceExpectPass() throws Exception {
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-11/source-folder/").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_1).useCase("CONFIGURATION")
        .isOnline(false).submissionDirLocation(testFile).run();
    //assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        //this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP11ZIPSourceExpectPass() throws Exception {
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-11-zip/R1100-scap11.zip").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_1).useCase("CONFIGURATION")
        .submissionFileLocation(testFile).run();
    //assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        //this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP11SourceExpectFail() throws Exception {
    boolean passed = true;
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-11-zip/R1100-scap11-ERROR.zip").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_1).useCase("CONFIGURATION")
        .isOnline(false).submissionFileLocation(testFile).run();        //assure many results
    // were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
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
  public void SCAP12SourceExpectPass() throws Exception {
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_2).useCase("CONFIGURATION")
        .submissionFileLocation(testFile).run();
    //assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        //this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP12SourceExpectFail() throws Exception {
    boolean passed = true;
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall-ERROR.xml").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_2).useCase("CONFIGURATION")
        .isOnline(false).submissionFileLocation(testFile).run();        //assure many results
    // were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
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
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-13/source_data_stream_collection_sample.xml").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_3).useCase("CONFIGURATION")
        .isOnline(true).submissionFileLocation(testFile).run();
    //assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        //this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void SCAP13SourceExpectFail() throws Exception {
    boolean passed = true;
    String testFile = new File(new URL("classpath:src/test/resources/candidates" +
        "/scap-13/source_data_stream_collection_sample-NO-USE-CASE.xml").getFile())
        .getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder().submissionType
        (Application.ContentType.SOURCE).scapVersion(SCAPVersion.V1_3).useCase("CONFIGURATION")
        .submissionFileLocation(testFile).run();
    //assure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size()
        > 0);
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
