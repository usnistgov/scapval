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

package gov.nist.secauto.scap.validation.candidate;

import gov.nist.secauto.scap.validation.NamespaceConstants;
import gov.nist.secauto.scap.validation.SCAPVersion;
import gov.nist.secauto.scap.validation.component.XccdfVersion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Inspects an XML file to see if it is an SCAP 1.2/1.3/1.4 file, or an XCCDF file.
 */
public class ScapXmlInspector implements ICandidateFileCreator {

  private static final Logger log = LogManager.getLogger(ScapXmlInspector.class);

  // delegate to ScapDocumentSniffer to check for XML schema reference
  private final ScapDocumentSniffer xmlSniffer = new ScapDocumentSniffer();

  /**
   * Returns CandidateFile information about a file.
   *
   * @param builder
   *          The CandidateFile.Builder that represents the file.
   */
  public CandidateFile createCandidate(final CandidateFile.Builder builder) {

    // determine the root schema
    final String contentType = this.xmlSniffer.findContentType(builder.getFile().getAbsolutePath());

    // SCAP 1.2 XML
    if (NamespaceConstants.NS_SOURCE_DS_1_2.getNamespaceString().equals(contentType)
        && this.xmlSniffer.findSCAPVersion(builder.getFile().getAbsolutePath()).equals("1.2")) {
      return createScap12Candidate(builder);
    }

    // SCAP 1.3 XML
    if (NamespaceConstants.NS_SOURCE_DS_1_3.getNamespaceString().equals(contentType)
        && this.xmlSniffer.findSCAPVersion(builder.getFile().getAbsolutePath()).equals("1.3")) {
      return createScap13Candidate(builder);
    }

    // SCAP 1.4 XML
    if (NamespaceConstants.NS_SOURCE_DS_1_4.getNamespaceString().equals(contentType)
        && this.xmlSniffer.findSCAPVersion(builder.getFile().getAbsolutePath()).equals("1.4")) {
      return createScap14Candidate(builder);
    }

    // XCCDF 1.2 XML
    if (NamespaceConstants.NS_XCCDF_1_2.getNamespaceString().equals(contentType)) {
      return createXccdfCandidate(builder, XccdfVersion.V1_2);
    }

    // XCCDF 1.1.4 XML
    if (NamespaceConstants.NS_XCCDF_1_1_4.getNamespaceString().equals(contentType)) {
      return createXccdfCandidate(builder, XccdfVersion.V1_1_4);
    }

    return builder.setTypeUnknown("XML is not an SCAP file").createCandidateFile();
  }

  /**
   * Builds an SCAP 1.2 candidate file, including the use case, if found in document.
   *
   * @param builder
   *          The CandidateFile.Builder which represents the file.
   * @return The candidate file.
   */
  private CandidateFile createScap12Candidate(final CandidateFile.Builder builder) {
    final SCAPVersion version = SCAPVersion.V1_2;

    if (log.isDebugEnabled()) {
      log.debug(String.format("%s is %s", builder.getFile().getName(), version.name()));
    }

    // search for use case in SCAP document
    final String useCase = findUseCase(builder.getFile(), SCAPVersion.V1_2);

    // create candidate file
    return builder.setTypeScapCombinedFile(version, useCase).createCandidateFile();
  }

  /**
   * Builds an SCAP 1.3 candidate file, including the use case, if found in document.
   *
   * @param builder
   *          The CandidateFile.Builder which represents the file.
   * @return The candidate file.
   */
  private CandidateFile createScap13Candidate(final CandidateFile.Builder builder) {
    final SCAPVersion version = SCAPVersion.V1_3;

    if (log.isDebugEnabled()) {
      log.debug(String.format("%s is %s", builder.getFile().getName(), version.name()));
    }

    // search for use case in SCAP document
    final String useCase = findUseCase(builder.getFile(), SCAPVersion.V1_3);

    // create candidate file
    return builder.setTypeScapCombinedFile(version, useCase).createCandidateFile();
  }

  /**
   * Builds an SCAP 1.4 candidate file, including the use case, if found in document.
   *
   * @param builder
   *          The CandidateFile.Builder which represents the file.
   * @return The candidate file.
   */
  private CandidateFile createScap14Candidate(final CandidateFile.Builder builder) {
    final SCAPVersion version = SCAPVersion.V1_4;

    if (log.isDebugEnabled()) {
      log.debug(String.format("%s is %s", builder.getFile().getName(), version.name()));
    }

    // search for use case in SCAP document
    final String useCase = findUseCase(builder.getFile(), SCAPVersion.V1_4);

    // create candidate file
    return builder.setTypeScapCombinedFile(version, useCase).createCandidateFile();
  }

  /**
   * Inspects an SCAP file to determine the use case.
   *
   * @param file
   *          The SCAP file.
   * @return The use case, or null if not found.
   */
  private String findUseCase(final File file, final SCAPVersion scapVersion) {

    String useCase = null;
    final String useCaseName = this.xmlSniffer.findUseCase(file.getAbsolutePath());

    if (useCaseName != null) {

      try {
        if (scapVersion.isUseCaseValid(useCaseName)) {
          useCase = useCaseName;
        }

      } catch (IllegalArgumentException e) {
        log.warn(String.format("%s has invalid SCAP use case %s", file.getName(), useCaseName));
      }
    }
    return useCase;
  }

  /**
   * Builds an XCCDF candidate file.
   *
   * @param builder
   *          The CandidateFile.Builder which represents the file.
   * @param version
   *          The XCCDF version.
   * @return The candidate file.
   * @return The candidate file.13
   */
  private CandidateFile createXccdfCandidate(final CandidateFile.Builder builder, final XccdfVersion version) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("%s is %s", builder.getFile().getName(), version.name()));
    }

    return builder.setTypeXccdf(version).createCandidateFile();
  }

}
