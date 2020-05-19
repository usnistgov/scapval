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

import gov.nist.secauto.scap.validation.component.SCAP11Components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Inspects a folder to see if it is a folder of SCAP files based on file naming convention. Refer
 * to NIST SP-126 for the specification of naming SCAP files.
 */
public class ScapFolderInspector implements ICandidateFileCreator {

  private static final Logger log = LogManager.getLogger(ScapFolderInspector.class);

  /**
   * Inspects a directory to see if it represents an SCAP bundle, based on SCAP 1.1 file naming
   * conventions.
   *
   * @param builder
   *          The CandidateFile.Builer.
   * @return A CandidateFile object containing the SCAP content types; otherwise type UNKNOWN is
   *         specified
   */
  public CandidateFile createCandidate(final CandidateFile.Builder builder) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("Inspecting for SCAP %s", builder.getFile().getAbsolutePath()));
    }

    final File file = builder.getFile();

    if (!file.isDirectory()) {
      throw new IllegalStateException("File must be a directory");
    }

    final List<String> filenames = getXmlFilenames(file);

    final List<SCAP11Components> types = getScap11ContentTypes(filenames);

    // if contains OVAL or CPE OVAL, then consider it an SCAP bundle
    if (types.contains(SCAP11Components.OVAL_VULNERABILITY) || types.contains(SCAP11Components.OVAL_COMPLIANCE)
        || types.contains(SCAP11Components.OVAL_PATCH) || types.contains(SCAP11Components.CPE_DICTIONARY)) {
      return builder.setTypeScapBundle(types).createCandidateFile();
    }

    // not an SCAP bundle
    return builder.setTypeUnknown("Folder is not an SCAP bundle").createCandidateFile();
  }

  /**
   * Inspects a given folder to determine if it has SCAP 11 files based on the SCAP naming convention.
   *
   * @param filenames
   *          a list of String filenames
   * @return the list of scap 11 files found
   */
  public List<SCAP11Components> getScap11ContentTypes(final List<String> filenames) {

    final List<SCAP11Components> list = new LinkedList<SCAP11Components>();
    for (final SCAP11Components type : SCAP11Components.values()) {
      if (hasFileNamed(filenames, type)) {
        list.add(type);
      }
    }
    return list;
  }

  /**
   * Returns true if the list of filenames contains a file named such that it matches a given SCAP
   * content type.
   *
   * @param filenames
   *          The list of filenames.
   * @param type
   *          The SCAP content type.
   * @return True, if the list of files has a file of that type.
   */
  private boolean hasFileNamed(final List<String> filenames, final SCAP11Components type) {

    for (final String filename : filenames) {
      for (final String nameSuffix : type.getFileNameSuffixes()) {
        if (filename.endsWith(nameSuffix)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns list of filenames from folder.
   *
   * @param folder
   *          The folder.
   * @return The list of filenames.
   */
  private List<String> getXmlFilenames(final File folder) {

    final List<String> filenames = new ArrayList<String>();
    final File[] xmlFiles = folder.listFiles(new XmlFileFilter());
    for (final File xmlFile : xmlFiles) {
      filenames.add(xmlFile.getName());
    }
    return filenames;

  }

  /**
   * Filter non-directory, XML files.
   */
  static class XmlFileFilter implements FileFilter {

    public boolean accept(File pathname) {
      if (pathname == null) {
        return false;
      }
      return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".xml");
    }

  }

}
