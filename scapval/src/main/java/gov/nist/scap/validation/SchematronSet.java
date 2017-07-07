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
 * SHALL NASA BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM, OR
 * IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */
package gov.nist.scap.validation;

import gov.nist.decima.xml.schematron.Schematron;
import gov.nist.decima.xml.assessment.schematron.SchematronHandler;

import java.util.Map;

/** A related set of Schematron items required for creation of a SchematronAssessment. */
public class SchematronSet {
  private final Schematron schematron;
  private final SchematronHandler schematronHandler;
  private final String schematronPhase;
  private final Map<String, String> schematronParams;
  private boolean hasParams = false;

  public SchematronSet(Schematron schematron, SchematronHandler schematronHandler, String
      schematronPhase, Map<String, String> params) {
    this.schematron = schematron;
    this.schematronHandler = schematronHandler;
    this.schematronPhase = schematronPhase;
    this.schematronParams = params;
    if (params != null) {
      hasParams = true;
    }
  }

  public Schematron getSchematron() {
    return schematron;
  }

  public SchematronHandler getSchematronHandler() {
    return schematronHandler;
  }

  public String getSchematronPhase() {
    return schematronPhase;
  }

  public Map<String, String> getSchematronParams() {
    return schematronParams;
  }

  public boolean hasSchematronParams() {
    return hasParams;
  }
}
