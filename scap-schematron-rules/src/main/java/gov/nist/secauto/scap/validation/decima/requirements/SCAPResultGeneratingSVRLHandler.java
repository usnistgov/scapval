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
import gov.nist.secauto.decima.xml.assessment.schematron.AbstractIdAwareSVRLHandler;
import gov.nist.secauto.decima.xml.assessment.schematron.IdAwareSchematronHandler;
import gov.nist.secauto.decima.xml.document.XMLDocument;

import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

public class SCAPResultGeneratingSVRLHandler extends AbstractIdAwareSVRLHandler {
  // private static final Pattern PATTERN_DERIVED_REQUIREMENT_ID =
  // Pattern.compile("^\\s*([^|\\s]+).*$");
  //
  // private String parseId(String text) {
  // Matcher m = PATTERN_DERIVED_REQUIREMENT_ID.matcher(text);
  // if (m.matches()) {
  // return m.group(1);
  // }
  // throw new IllegalStateException("Unable to extract derived requirement id from: "+text);
  // }
  public SCAPResultGeneratingSVRLHandler(Assessment<? extends XMLDocument> assessment, XMLDocument sourceDocument,
      AssessmentResultBuilder assessmentResultBuilder, IdAwareSchematronHandler handler) throws AssessmentException {
    super(assessment, sourceDocument, assessmentResultBuilder, handler);
  }

  @Override
  public void handleSuccessfulReport(Element successfulReport) {
    String assertionId = successfulReport.getAttributeValue("id");
    TestStatus testStatus = TestStatus.INFORMATIONAL;
    String xpath = successfulReport.getAttributeValue("location");

    // The value text contains the derived requirement id + zero or more values for the message
    String valueText = successfulReport.getChildText("text", successfulReport.getNamespace());
    String[] valueArray = valueText.split("\\|", 2);
    String derivedRequirementId = valueArray[0];

    List<String> values = new LinkedList<>();
    if (valueArray.length > 1) {
      values = handleValues(valueArray[1]);
    }
    // Add the schematron test to the "Message" in the final report
    String test = " - TEST: " + successfulReport.getAttribute("test").getValue();
    values.add(test);
    handleAssertionResult(derivedRequirementId, assertionId, testStatus, xpath, values);
  }

  @Override
  public void handleFailedAssert(Element failedAssert) {
    String assertionId = failedAssert.getAttributeValue("id");
    TestStatus testStatus = TestStatus.FAIL;
    String xpath = failedAssert.getAttributeValue("location");

    // The value text contains the derived requirement id + zero or more values for the message
    String valueText = failedAssert.getChildText("text", failedAssert.getNamespace());
    String[] valueArray = valueText.split("\\|", 2);
    String derivedRequirementId = valueArray[0];

    List<String> values = new LinkedList<>();
    if (valueArray.length > 1) {
      values = handleValues(valueArray[1]);
    }
    // Add the schematron test to the "Message" in the final report
    // consider including the context
    String test = " - TEST: " + failedAssert.getAttribute("test").getValue();
    values.add(test);
    handleAssertionResult(derivedRequirementId, assertionId, testStatus, xpath, values);
  }

  protected List<String> handleValues(String values) {
    values = values.trim();
    List<String> retval = new LinkedList<>();
    if (!values.isEmpty()) {
      String[] split = values.split("\\|");
      for (String val : split) {
        retval.add(val);
      }
    }
    return retval;
  }
}
