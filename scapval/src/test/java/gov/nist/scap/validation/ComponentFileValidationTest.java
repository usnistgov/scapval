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
package gov.nist.scap.validation;

import gov.nist.decima.core.assessment.result.BaseRequirementResult;
import gov.nist.decima.core.assessment.result.ResultStatus;
import gov.nist.decima.core.classpath.ClasspathHandler;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class ComponentFileValidationTest {

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }
  
  @Test
  public void OVALPassTest() throws Exception {
    String testFile = new File(new URL(
        "classpath:src/test/resources/candidates" + "/components/oval/oval-vulnerability-remote-code-exec-5-10.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void OVALFailTest() throws Exception {
    boolean passed = true;
    String testFile = new File(new URL("classpath:src/test/resources/candidates"
        + "/components/oval/oval-vulnerability-remote-code-exec-5-10-ERROR.xml").getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void OCILPassTest() throws Exception {
    String testFile
        = new File(new URL("classpath:src/test/resources/candidates" + "/components/ocil/R2100-OCIL.xml").getFile())
            .getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void OCILFailTest() throws Exception {
    boolean passed = true;
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates" + "/components/ocil/R2100-OCIL-ERROR.xml").getFile())
            .getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void XCCDF11PassTest() throws Exception {
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates" + "/components/xccdf/xccdf114-xml/fdcc-winvista-xccdf.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void XCCDF11FailTest() throws Exception {
    boolean passed = true;
    String testFile = new File(new URL(
        "classpath:src/test/resources/candidates" + "/components/xccdf/xccdf114-xml/fdcc-winvista-xccdf-ERROR.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void XCCDF12PassTest() throws Exception {
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates" + "/components/xccdf/xccdf12-xml/macos-xccdf1.2.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
  public void XCCDF12FailTest() throws Exception {
    boolean passed = true;
    String testFile = new File(
        new URL("classpath:src/test/resources/candidates" + "/components/xccdf/xccdf12-xml/macos-xccdf1.2-ERROR.xml")
            .getFile()).getAbsolutePath();
    SCAPValAssessmentResults assessmentResults = new SCAPValWrapper.Builder()
        .submissionType(Application.ContentType.COMPONENT).submissionFileLocation(testFile).run();
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
