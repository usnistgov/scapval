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

import gov.nist.secauto.decima.core.assessment.result.AssessmentResults;

import java.util.List;
import java.util.Objects;

/**
 * SCAPVal assessments include custom notes in its validation reports. This pairs Decima based
 * assessment results along with associated assessment notes.
 */
public class SCAPValAssessmentResults {
  private final AssessmentResults assessmentResults;
  private final List<String> assessmentNotes;

  /**
   * The SCAPValAssessmentResults contain the AssessmentResults from the Decima framework along with
   * some notes from the validation.
   *
   * @param assessmentResults
   *          the AssessmentResults of the SCAPVal evaluation.
   * @param assessmentNotes
   *          the list of notes for this evaluation.
   */
  public SCAPValAssessmentResults(AssessmentResults assessmentResults, List<String> assessmentNotes) {
    Objects.requireNonNull(assessmentResults, "assessmentResults can not be null");
    Objects.requireNonNull(assessmentNotes, "assessmentNotes can not be null");

    this.assessmentResults = assessmentResults;
    this.assessmentNotes = assessmentNotes;
  }

  public List<String> getAssessmentNotes() {
    return assessmentNotes;
  }

  public AssessmentResults getAssessmentResults() {
    return assessmentResults;
  }
}
