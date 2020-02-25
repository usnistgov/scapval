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
package gov.nist.secauto.scap.validation.decima.requirements;

import gov.nist.secauto.decima.core.assessment.Assessment;
import gov.nist.secauto.decima.core.assessment.AssessmentException;
import gov.nist.secauto.decima.core.assessment.result.AssessmentResultBuilder;
import gov.nist.secauto.decima.core.assessment.result.TestStatus;
import gov.nist.secauto.decima.xml.assessment.schematron.AbstractSVRLHandler;
import gov.nist.secauto.decima.xml.document.XMLDocument;

import org.jdom2.Element;

import java.util.Arrays;

public class ComponentResultGeneratingSVRLHandler extends AbstractSVRLHandler {

  private String derivedRequirmentID;

  public ComponentResultGeneratingSVRLHandler(String derivedRequirmentID, Assessment<? extends XMLDocument> assessment,
      XMLDocument sourceDocument, AssessmentResultBuilder validationResultBuilder) throws AssessmentException {
    super(assessment, sourceDocument, validationResultBuilder);
    this.derivedRequirmentID = derivedRequirmentID;
  }

  @Override
  public void handleSuccessfulReport(Element successfulReport) {
    String assertionId = null;
    TestStatus testStatus = TestStatus.INFORMATIONAL;
    String xpath = successfulReport.getAttributeValue("location");
    String test = " - TEST: " + successfulReport.getAttribute("test").getValue();

    String valueText = successfulReport.getChildText("text", successfulReport.getNamespace());
    String[] valueArray = { valueText + test };

    String derivedRequirementId = derivedRequirmentID;

    handleAssertionResult(derivedRequirementId, assertionId, testStatus, xpath, Arrays.asList(valueArray));
  }

  @Override
  public void handleFailedAssert(Element failedAssert) {
    String assertionId = null;
    TestStatus testStatus = TestStatus.FAIL;
    // some component file asserts use this flag attribute to set level as warning
    if (failedAssert.getAttribute("flag") != null && failedAssert.getAttribute("flag").getValue().equals("WARNING")) {
      testStatus = TestStatus.WARNING;
    }
    String test = " - TEST: " + failedAssert.getAttribute("test").getValue();

    String xpath = failedAssert.getAttributeValue("location");

    String valueText = failedAssert.getChildText("text", failedAssert.getNamespace());
    String[] valueArray = { valueText + test };

    String derivedRequirementId = derivedRequirmentID;

    handleAssertionResult(derivedRequirementId, assertionId, testStatus, xpath, Arrays.asList(valueArray));
  }

}
