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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Opens a candidate file for validation.
 */
public class CanOpener implements ICandidateFileCreator {

  private static final Logger log = LogManager.getLogger(CanOpener.class);

  private CandidateFileList candidates = new CandidateFileList();

  private final ICandidateFileCreator scapFolderInspector = new ScapFolderInspector();
  private final ICandidateFileCreator scapXmlInspector = new ScapXmlInspector();
  private final ZipExpander zipExpander;

  public CanOpener(final int bufferSize) {
    this.zipExpander = new ZipExpander(bufferSize);
  }

  /**
   * Returns all the candidates in a file. The candidates may or may not be validate-able items.
   *
   * @param file
   *          The file we are inspecting.
   * @return The list of items to possibly validate.
   */
  public CandidateFileList findCandidateFiles(final File file) {
    Objects.requireNonNull(file, "file cannot be null.");
    this.candidates = new CandidateFileList();
    final CandidateFile.Builder builder = new CandidateFile.Builder(file);
    createCandidate(builder);
    return this.candidates;
  }

  /**
   * Takes a Candidate file builder and returns the candidate file
   *
   * @param builder
   *          The CandidateFile.Builder with information about a file.
   * @return the CandidateFile per the specified builder
   */
  public CandidateFile createCandidate(final CandidateFile.Builder builder) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("Processing %s", builder.getFile().getAbsolutePath()));
    }

    CandidateFile candidate = null;

    // if file is a ZIP file
    if (builder.isFileZip()) {

      // expand the file
      final File expanded = this.zipExpander.expand(builder.getFile());
      builder.setExpandedTo(expanded);
    }

    // if the file is a directory
    if (builder.isFileDirectory()) {

      // inspect for SCAP contents
      candidate = this.scapFolderInspector.createCandidate(builder);
    } else {

      // if file is XML
      if (builder.isFileXml()) {

        // inspect XML for SCAP or XCCDF content
        candidate = this.scapXmlInspector.createCandidate(builder);
      } else {

        // otherwise, the file type is unrecognized
        candidate = builder.setTypeUnknown("File is not recognized for validation.").createCandidateFile();
      }
    }

    // add candidate to list
    this.candidates.addCandidate(candidate);

    // process folder children, if candidate is not yet identified
    if (candidate.isUnrecognized() && builder.isFileDirectory()) {

      processChildren(candidate, builder.getFile());
    }

    return candidate;
  }

  private void processChildren(final CandidateFile parent, final File folder) {

    // recursively process every child file
    for (final File child : folder.listFiles()) {

      final CandidateFile.Builder folderChildBuilder = new CandidateFile.Builder(parent, child);

      createCandidate(folderChildBuilder);
    }
  }

  /**
   * Deletes expanded candidate files on the filesystem.
   *
   * @param candidates
   *          the CandidateFileList to be deleted
   */
  public void deleteExpandedFiles(final CandidateFileList candidates) {

    final List<CandidateFile> files = candidates.getCandidates();
    for (final CandidateFile file : files) {
      if (file.isExpanded()) {
        this.zipExpander.deleteDirectory(new File(file.getAbsolutePath()));
      }
    }
  }
}
