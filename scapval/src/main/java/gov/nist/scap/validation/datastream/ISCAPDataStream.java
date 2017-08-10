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
package gov.nist.scap.validation.datastream;

import gov.nist.scap.validation.SCAPVersion;
import gov.nist.scap.validation.SchematronSet;

import javax.xml.transform.stream.StreamSource;
import java.util.LinkedList;

/**
 * An abstraction for an SCAP data stream to handle the gathering of required
 * Schemas and Schematron for SCAP validation.
 */
public interface ISCAPDataStream {
  /**
   * @return the SCAPVersion of a particular ISCAPDataStream
   */
  SCAPVersion getScapVersion();

  /**
   * The Schematrons required to perform SCAP validation depends on the version of SCAP.
   *
   * @return a list of Schematron items required for creation of a SchematronAssessment.
   */
  LinkedList<SchematronSet> getSchematronSets();

  /**
   * The XML schemas required to perform SCAP validation depends on the version of SCAP.
   * Every schemas must be part of the same assessment for Decima to properly validate.
   *
   * @return a LinkedList of all the Source from applicable schema XSDs
   */
  LinkedList<StreamSource> getSchemas();
}
