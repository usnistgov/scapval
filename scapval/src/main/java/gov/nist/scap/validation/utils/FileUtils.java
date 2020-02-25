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
package gov.nist.scap.validation.utils;

import gov.nist.scap.validation.Application;
import gov.nist.scap.validation.ValidationNotes;
import gov.nist.scap.validation.candidate.ZipExpander;
import gov.nist.scap.validation.exceptions.ConfigurationException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/**
 * Provides File related helper methods.
 */
public class FileUtils {
  public static final String PATH_SEPERATOR = java.nio.file.FileSystems.getDefault().getSeparator();
  public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
  public static final String DEFAULT_HASH_ALGORITHM = "SHA-256";
  public static final int BUFFER_SIZE = 64 * 1024;

  private static final Logger log = LogManager.getLogger(FileUtils.class);

  /**
   * Returns the filename only without its specified a file extension.
   *
   * @param filename
   *          the complete filename in question
   * @return the resultant String
   */
  public static String getFilenamePrefix(String filename) {
    Objects.requireNonNull(filename, "filename can not be null.");

    int sep = filename.lastIndexOf(PATH_SEPERATOR);
    int dot = filename.lastIndexOf('.');
    return filename.substring(sep + 1, dot);
  }

  /**
   * Returns the path of an appropriate temporary directory for reading/writing temp files.
   *
   * @return the path as a String
   */
  public static String getTmpDir() {
    String tmpDir = TMP_DIR;
    // some OS will return without trailing path separator make sure to include on prevent issues
    if (!tmpDir.endsWith(PATH_SEPERATOR)) {
      tmpDir = tmpDir.concat(PATH_SEPERATOR);
    }
    return tmpDir;
  }

  /**
   * Returns the filename only from a URL without its specified a file extension.
   *
   * @param url
   *          the complete url in question
   * @return the resultant String
   */
  public static String getFilenameFromURLNoExtension(String url) {
    Objects.requireNonNull(url, "filename can not be null.");

    return url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
  }

  /**
   * Returns a file hash value as a String, based on the provided file and hash algorithm
   *
   * @param file
   *          a File object to be hashed
   * @param algorithm
   *          file hash algorithm. Scapval uses SHA-256 exclusively
   * @return a String hash value
   */
  public static String getFileHash(File file, String algorithm) throws FileNotFoundException {
    Objects.requireNonNull(file, "file can not be null.");
    Objects.requireNonNull(algorithm, "algorithm can not be null.");

    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(algorithm);
      DigestInputStream digestStream = new DigestInputStream(bufferedInputStream, messageDigest);
      while (digestStream.read() != -1) {
      }
      messageDigest = digestStream.getMessageDigest();
      digestStream.close();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException("Unable to read: " + file.getAbsolutePath() + " file stream for hashing", e);
    } finally {
      try {
        bufferedInputStream.close();
      } catch (IOException e) {
        throw new RuntimeException("Unable to close: " + file.getAbsolutePath() + " file stream for hashing", e);
      }
    }
    // encode as base hex, not base64
    return new String(Hex.encodeHex(messageDigest.digest())).toUpperCase();
  }

  /**
   * Returns the FileType (DIRECTORY, ZIP, XML) found at a given Path.
   *
   * @param filePath
   *          a String containing the path to the file to check
   * @return the FileType (DIRECTORY, ZIP, XML) detected or an exception thrown
   * @throws ConfigurationException
   *           if no valid FileType is found
   */
  public static Application.FileType determineSCAPFileType(String filePath) throws ConfigurationException {
    Objects.requireNonNull(filePath, "contentPath can not be null.");

    // error if the specified File or Directory does not exist
    File contentFile = new File(filePath);
    if (!contentFile.isFile() && !contentFile.isDirectory()) {
      throw new ConfigurationException(contentFile.getAbsolutePath() + " is not valid file or directory.");
    }
    if (contentFile.isDirectory()) {
      return Application.FileType.DIRECTORY;
    } else if (contentFile.isFile()) {
      // we could do file type content inspection here but its better for scapval to attempt
      // to
      // validate
      // and return the reason for failure. So, we'll do a simple file extension sanity check
      String fileName = contentFile.getName();
      String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
      if (fileExtension.toLowerCase().equals("zip")) {
        return Application.FileType.ZIP;
      } else if (fileExtension.toLowerCase().equals("xml")) {
        return Application.FileType.XML;
      } else {
        throw new ConfigurationException("Found a file but does not contain a .xml or .zip extension");
      }
    } else {
      return null;
    }
  }

  /**
   * Begins downloading a specified file.
   *
   * @param url
   *          to download from
   * @param maxDownloadSizeInBytes
   *          the download maxsize
   * @return the downloaded File
   */
  public static File downloadFile(URL url, int maxDownloadSizeInBytes) {
    Objects.requireNonNull(url, "url can not be null.");
    Objects.requireNonNull(maxDownloadSizeInBytes, "maxDownloadSizeInBytes can not be null.");

    InputStream is = null;
    try {
      log.info("Downloading: " + url.toString() + " ...");
      is = url.openStream();
    } catch (IOException e) {
      log.error("Unable to download: " + url.toString());
      log.debug("Unable to download: " + url.toString() + " - " + e);
      ValidationNotes.getInstance().createValidationNote("Unable to download: " + url.toString());
      return null;
    }
    return downloadFile(is, url.toString(), maxDownloadSizeInBytes);
  }

  private static File downloadFile(InputStream is, String url, int maxDownloadSizeInBytes) {
    Objects.requireNonNull(is, "is can not be null.");
    Objects.requireNonNull(url, "url can not be null.");
    Objects.requireNonNull(maxDownloadSizeInBytes, "maxDownloadSizeInBytes can not be null.");

    File tempFile = null;
    try {
      tempFile = File.createTempFile(Long.toString(new Date().getTime()), null);
      tempFile.deleteOnExit();
    } catch (IOException e) {
      log.error("Unable to create temporary file: " + url);
      log.debug("Unable to create temporary file: " + url + " - " + e);
      return null;
    }
    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {

      byte[] bytes = new byte[BUFFER_SIZE];
      int length = -1;
      try {
        length = is.read(bytes);
      } catch (IOException e2) {
        log.error("Unable to read temporary file: " + url);
        try {
          os.close();
        } catch (IOException e1) {
          log.debug("Unable to close Output Stream: " + e1);
        }
        tempFile.delete();
        return null;
      }
      int totalBytesDownloaded = 0;
      if (length == -1) {
        log.error("Unable to download: " + url.toString());
        ValidationNotes.getInstance().createValidationNote("Unable to download: " + url.toString());
        return null;
      }
      while (length >= 0) {
        totalBytesDownloaded += length;
        if (maxDownloadSizeInBytes > 0 && totalBytesDownloaded > maxDownloadSizeInBytes) {
          log.error("Unable to download: " + url + " because the file size is larger than the specified "
              + Integer.toString(maxDownloadSizeInBytes / 1024 / 1024) + " MiB");
          try {
            os.close();
          } catch (IOException e) {
            log.debug("Unable to close Output Stream: " + e);
          }
          tempFile.delete();
          return null;
        }
        try {
          os.write(bytes, 0, length);
        } catch (IOException e) {
          log.error("Unable to write to temporary file: " + url);
          try {
            os.close();
          } catch (IOException e1) {
            log.debug("Unable to close Output Stream: " + e1);
          }
          tempFile.delete();
          return null;
        }
        try {
          length = is.read(bytes);
        } catch (IOException e) {
          log.error("Unable to read to temporary file: " + url);
          try {
            os.close();
          } catch (IOException e1) {
            log.debug("Unable to close Output Stream: " + e1);
          }
          tempFile.delete();
          return null;
        }
      }
      try {
        os.flush();
        os.close();
      } catch (IOException e) {
        log.error("Unable to write to temporary file: " + url);
        tempFile.delete();
        return null;
      } finally {
        try {
          os.close();
        } catch (IOException e) {
          log.error("Unable to close stream: " + e);
        }
      }
    } catch (FileNotFoundException e) {
      log.error("Unable to open temporary file: " + url, e);
      return null;
    } catch (IOException e) {
      log.error("Unable to write to temporary file: " + url, e);
      return null;
    }
    return tempFile;
  }

  /**
   * Takes a gzip compressed file, decompresses it and returns the result.
   *
   * @param compressedFile
   *          a gzip compressed file
   * @return the decompressed file or null if there is a problem
   */
  public static File decompressGZIPFile(File compressedFile) {
    Objects.requireNonNull(compressedFile, "compressedFile can not be null.");

    File tempFile = null;
    try {
      tempFile = File.createTempFile(Long.toString(new Date().getTime()), null);
    } catch (IOException e) {
      log.error("Unable to create temp file: " + compressedFile.getName());
      return null;
    }
    byte[] buffer = new byte[1024];
    GZIPInputStream gzis = null;
    FileOutputStream out = null;
    try {
      gzis = new GZIPInputStream(new FileInputStream(compressedFile));
      out = new FileOutputStream(tempFile);
      int len;
      while ((len = gzis.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      gzis.close();
      out.close();
    } catch (IOException e) {
      log.error("Unable to extract: " + compressedFile.getAbsolutePath());
    } finally {
      try {
        if (gzis != null) {
          gzis.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        log.debug("Unable to close a stream: " + e);
      }
    }
    return tempFile;
  }

  /**
   * Attempts to delete the specified directory on JVM shutdown.
   *
   * @param dir
   *          the directory to delete
   */
  public static void deleteDirOnExit(File dir) {
    Objects.requireNonNull(dir, "dir can not be null.");

    final ZipExpander zipExpander = new ZipExpander(1024);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        zipExpander.deleteDirectory(dir);
        if (dir.exists()) {
          log.error("Problem cleaning up the extracted content: " + dir.getAbsolutePath());
        }
      }
    });
  }

  /**
   * Creates a File objects from a specified Java resource path.
   *
   * @param resourcePath
   *          the Java resource path to pull a File from
   * @return the File from the resource path. It will be deleted on JVM exit
   */
  public static File getTempFileFromResource(String resourcePath) {
    try {
      URL resourceLocation = new URL(resourcePath);
      InputStream in = resourceLocation.openConnection().getInputStream();
      // could not acquire the resource so return null
      if (in == null) {
        return null;
      }
      // create the file in a temp location
      File tempFile = new File(FileUtils.getTmpDir() + in.hashCode() + ".tmp");
      tempFile.createNewFile();
      // this temp file will be removed on exit of JVM
      tempFile.deleteOnExit();

      try (FileOutputStream out = new FileOutputStream(tempFile)) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
        }
      }
      return tempFile;
    } catch (IOException e) {
      log.error("Problem trying to load file from - " + resourcePath);
      return null;
    }

  }

}
