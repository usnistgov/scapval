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

import static gov.nist.decima.module.cli.CLIParser.DEFAULT_VALIDATION_REPORT_FILE;
import static gov.nist.decima.module.cli.CLIParser.DEFAULT_VALIDATION_RESULT_FILE;

import gov.nist.decima.core.assessment.result.BaseRequirementResult;
import gov.nist.decima.core.assessment.result.ResultStatus;
import gov.nist.scap.validation.utils.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class SCAPValWrapperTest {
  @Test
  public void runSCAP() throws Exception {

    final String datastream = new File(
        new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall" + ".xml")
            .getFile()).getAbsolutePath();

    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.SOURCE).submissionFileLocation(datastream).scapVersion(SCAPVersion.V1_2)
        .isOnline(false).debugMessageLevel(false).maxDownloadSize("40").useCase("CONFIGURATION").run();

    // ensure many results were generated
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 180);

    // ensure at least 1 note generated
    Assert.assertTrue(assessmentResults.getAssessmentNotes().size() > 0);

    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        // this particular case should have no status FAIL
        Assert.fail("Should not have had a result with FAIL.");
      }
    }
  }

  @Test
  public void runComponentWithReportAndLogFile() throws Exception {
    final String component = new File(new URL(
        "classpath:src/test/resources/candidates/components/oval/oval-vulnerability-remote-code" + "-exec-5-10.xml")
            .getFile()).getAbsolutePath();

    SCAPValAssessmentResults assessmentResults;
    File tmpReport = null;
    File tmpResults = null;
    File tmpLog = null;

    try {
      URI logFileURI = new File(FileUtils.getTmpDir() + "test.log").toURI();

      assessmentResults = new SCAPValWrapper.Builder().submissionType(Application.ContentType.COMPONENT)
          .submissionFileLocation(component).reportOutputDirectory(FileUtils.getTmpDir()).logFileLocation(logFileURI)
          .run();

      // confirm the html report was created
      tmpReport = new File(FileUtils.getTmpDir() + DEFAULT_VALIDATION_REPORT_FILE);
      // and populated
      Assert.assertTrue(tmpReport.length() > 1);

      // confirm the xml results file was created
      tmpResults = new File(FileUtils.getTmpDir() + DEFAULT_VALIDATION_RESULT_FILE);
      // and populated
      Assert.assertTrue(tmpResults.length() > 1);

      // confirm the log was created
      tmpLog = new File(FileUtils.getTmpDir() + "test.log");
      // and populated
      Assert.assertTrue(tmpLog.length() > 1);

    } catch (Exception e) {
      throw e;
    } finally {
      // clean up the tmp files
      if (tmpReport != null) {
        tmpReport.deleteOnExit();
      }
      if (tmpResults != null) {
        tmpResults.deleteOnExit();
      }
      if (tmpLog != null) {
        tmpLog.deleteOnExit();
      }
    }

    // there should be at least one result
    Assert.assertTrue(assessmentResults.getAssessmentResults().getBaseRequirementResults().size() > 0);

    // ensure at least 1 note generated
    Assert.assertTrue(assessmentResults.getAssessmentNotes().size() > 0);

    for (BaseRequirementResult baseRequirementResult : assessmentResults.getAssessmentResults()
        .getBaseRequirementResults()) {
      if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
        if (baseRequirementResult.getStatus().equals(ResultStatus.FAIL)) {
          // this particular case should have no status FAIL
          Assert.fail("Should not have had a result with FAIL.");
        }
      }
    }
  }
}
