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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Takes a zip file and expands it to the file system. By convention the file is expanded to a
 * folder next to the ZIP file, and the folder is named just like the ZIP file but without the .zip
 * file extension.
 */
public class ZipExpander {
  private static final Logger log = LogManager.getLogger(ZipExpander.class);

  private static final int MIN_BUFFER_SIZE = 1024;

  private final int bufferSize;

  /**
   * Constructs a ZipExpander with a given output buffer size.
   *
   * @param bufferSize
   *          The buffer size in bytes.
   */
  public ZipExpander(final int bufferSize) {

    if (bufferSize < MIN_BUFFER_SIZE) {
      throw new IllegalArgumentException(String.format("Minimum buffer size is %d", MIN_BUFFER_SIZE));
    }
    this.bufferSize = bufferSize;
  }

  public File expand(final File compressedFile) {
    return expand(compressedFile, null);
  }

  /**
   * Expands a ZIP file and returns the new folder where it was expanded to.
   *
   * @param compressedFile
   *          The original ZIP file.
   * @return The new folder with expanded contents. Null if there is a problem
   */
  public File expand(final File compressedFile, final File targetDirFile) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Expandidng %s", compressedFile.getAbsolutePath()));
    }
    final File targetFolder = createTarget(compressedFile, targetDirFile);

    final byte[] buffer = new byte[this.bufferSize];
    ZipInputStream zipInputStream = null;
    try {

      // get the zip file content
      zipInputStream = new ZipInputStream(new FileInputStream(compressedFile));

      // write the entries
      writeZipEntries(targetFolder, zipInputStream, buffer);

      if (log.isDebugEnabled()) {
        log.debug("Finished expanding file.");
      }

      return targetFolder;

    } catch (IOException e) {
      log.error(String.format("Unable to write to folder %s", targetFolder.getAbsolutePath()), e);
      return null;
    } finally {
      if (zipInputStream != null) {
        try {
          zipInputStream.closeEntry();
        } catch (IOException e) {
          log.error(String.format("Unable to close entry in %s", compressedFile.getAbsolutePath()), e);
        }
        try {
          zipInputStream.close();
        } catch (IOException e) {
          log.error(String.format("Unable to close input stream for %s", compressedFile.getAbsolutePath()), e);
        }
      }
    }

  }

  /**
   * Writes out all the entries in a zip file.
   *
   * @param targetFolder
   *          Where we are writing to.
   * @param zipInputStream
   *          The ZIP input stream.
   * @param buffer
   *          A buffer for reading.
   * @throws IOException
   *           If unable to read a ZIP entry.
   */
  private void writeZipEntries(final File targetFolder, final ZipInputStream zipInputStream, final byte[] buffer)
      throws IOException {

    // get the next zip entry
    ZipEntry zipEntry = zipInputStream.getNextEntry();

    while (zipEntry != null) {

      // if the zip entry is a directory
      if (zipEntry.isDirectory()) {

        // make the directory
        writeFolder(targetFolder, zipEntry);
      } else {
        // otherwise, write the file
        writeFile(targetFolder, zipEntry, zipInputStream, buffer);
      }

      // get the next zip entry
      zipEntry = zipInputStream.getNextEntry();
    }

  }

  private void writeFolder(final File target, final ZipEntry ze) {
    final File newFolder = new File(target, ze.getName());
    if (log.isDebugEnabled()) {
      log.debug(String.format("Creating folder %s", newFolder.getAbsolutePath()));
    }
    if (!newFolder.mkdirs()) {
      log.error("Unable to create folder: " + newFolder.getAbsolutePath());
    }
  }

  /**
   * Writes a single zip entry out to the file system.
   *
   * @param target
   *          Where we are writing out to.
   * @param zipEntry
   *          The zip entry.
   * @param zipInputStream
   *          The zip input stream.
   * @param buffer
   *          A buffer for writing.
   */
  private void writeFile(final File target, final ZipEntry zipEntry, final ZipInputStream zipInputStream,
      final byte[] buffer) {

    final File newFile = new File(target, zipEntry.getName());

    if (log.isDebugEnabled()) {
      log.debug(String.format("Unzipping %s", newFile.getAbsolutePath()));
    }

    // create all non existing folders
    new File(newFile.getParent()).mkdirs();

    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(newFile);
      int len;
      while ((len = zipInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
      }
      zipInputStream.closeEntry();
    } catch (FileNotFoundException e) {
      log.error(String.format("FileNotFoundException while writing %s", newFile.getAbsolutePath()), e);
    } catch (IOException e) {
      log.error(String.format("IOException writing %s", newFile.getAbsolutePath()), e);
    } finally {

      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
          log.error(String.format("Unable to close output for %s", newFile.getAbsolutePath()), e);
        }
      }
    }

  }

  /**
   * Creates target folder where we are unzipping to.
   *
   * @param compressedFile
   *          The original ZIP filename upon which the target folder name is based.
   * @return The target folder where we can unzip to.
   */
  private File createTarget(final File compressedFile, final File targetDir) {

    if (compressedFile.isDirectory()) {
      throw new IllegalStateException(
          String.format("Unable to unzip a directory: %s", compressedFile.getAbsolutePath()));
    }

    final File target = getTargetDirectory(compressedFile, targetDir);

    target.mkdirs();
    return target;
  }

  /**
   * For completeness, call this method to delete an expanded folder.
   *
   * @param path
   *          The folder.
   */
  public void deleteDirectory(final File path) {

    if (path.exists()) {
      File[] files = path.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
          deleteDirectory(files[i]);
        } else {
          if (!files[i].delete()) {
            log.error(String.format("Cannot delete %s", files[i].getAbsolutePath()));
          }
        }
      }
    }
    if (!path.delete()) {
      log.error(String.format("Cannot delete %s", path.getAbsolutePath()));
    }
  }

  /**
   * Returns the target folder name based on the original ZIP file. Folder name is constructed by
   * removing .zip from the filename.
   *
   * @param compressedFile
   *          The original compressed ZIP file.
   * @return The new target folder name.
   */
  private File getTargetDirectory(final File compressedFile, final File targetDir) {
    final String compressedFilePath;
    if (targetDir == null) {
      compressedFilePath = compressedFile.getAbsolutePath();
    } else {
      compressedFilePath = compressedFile.getName();
    }
    final int pos = compressedFilePath.toLowerCase().lastIndexOf(".zip");

    if (pos < 1) {
      throw new IllegalStateException(
          String.format("Unable to construct target unzip directory from %s", compressedFilePath));
    }

    final String targetPath = compressedFilePath.substring(0, pos);
    File target = new File(targetDir, targetPath);

    int index = -1;
    while (target.exists()) {

      target = new File(targetDir, targetPath + String.valueOf(++index));
      if (index > 999) {
        throw new IllegalStateException("Unable to generate target unzip directory");
      }
    }
    return target;
  }

}
