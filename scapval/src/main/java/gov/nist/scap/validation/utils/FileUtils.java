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
package gov.nist.scap.validation.utils;

import gov.nist.scap.validation.Application;
import gov.nist.scap.validation.ValidationNotes;
import gov.nist.scap.validation.candidate.ZipExpander;
import gov.nist.scap.validation.exceptions.ConfigurationException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/** Provides File related helper methods */
public class FileUtils {
  public static final String PATH_SEPERATOR = java.nio.file.FileSystems.getDefault().getSeparator();
  public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
  public static final String DEFAULT_HASH_ALGORITHM = "SHA-256";
  public static final int BUFFER_SIZE = 64 * 1024;

  private static final Logger log = LogManager.getLogger(FileUtils.class);

  public static String getFilenamePrefix(String filename) {
    Objects.requireNonNull(filename, "filename can not be null.");

    int sep = filename.lastIndexOf(PATH_SEPERATOR);
    int dot = filename.lastIndexOf('.');
    return filename.substring(sep + 1, dot);
  }

  public static String getFilenameFromURLNoExtension(String filename) {
    Objects.requireNonNull(filename, "filename can not be null.");

    return filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf('.'));
  }

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
      throw new RuntimeException("Unable to read: " + file.getAbsolutePath() + " file stream for " +
          "hashing", e);
    } finally {
      try {
        bufferedInputStream.close();
      } catch (IOException e) {
        throw new RuntimeException("Unable to close: " + file.getAbsolutePath() + " file stream " +
            "for hashing", e);
      }
    }
    // encode as base hex, not base64
    return new String(Hex.encodeHex(messageDigest.digest())).toUpperCase();
  }

  public static Application.FileType determineFileType(String contentPath) throws
      ConfigurationException {
    Objects.requireNonNull(contentPath, "contentPath can not be null.");

    // error if the specified File or Directory does not exist
    File contentFile = new File(contentPath);
    if (!contentFile.isFile() && !contentFile.isDirectory()) {
      throw new ConfigurationException(contentFile.getAbsolutePath() + " is not valid file or " +
          "directory.");
    }
    if (contentFile.isDirectory()) {
      return Application.FileType.DIRECTORY;
    } else if (contentFile.isFile()) {
      // we could do file type content inspection here but its better for scapval to attempt to
        // validate
      // and return the reason for failure. So, we'll do a simple file extension sanity check
      String fileName = contentFile.getName();
      String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
      if (fileExtension.toLowerCase().equals("zip")) {
        return Application.FileType.ZIP;
      } else if (fileExtension.toLowerCase().equals("xml")) {
        return Application.FileType.XML;
      } else {
        throw new ConfigurationException("Found a file but does not contain a .xml or .zip " +
            "extension");
      }
    } else {
      return null;
    }
  }

  public static File downloadFile(URL url, int maxDownloadSizeInBytes) {
    Objects.requireNonNull(url, "url can not be null.");
    Objects.requireNonNull(maxDownloadSizeInBytes, "maxDownloadSizeInBytes can not be null.");

    InputStream is = null;
    try {
      log.info("Downloading: " + url.toString() + " ...");
      is = url.openStream();
    } catch (IOException e) {
      log.error("Unable to download: " + url.toString());
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
      return null;
    }
    OutputStream os = null;
    try {
      os = new BufferedOutputStream(new FileOutputStream(tempFile));
    } catch (FileNotFoundException e1) {
      log.error("Unable to write to temporary file: " + url);
      return null;
    }

    byte[] b = new byte[BUFFER_SIZE];
    int length = -1;
    try {
      length = is.read(b);
    } catch (IOException e2) {
      log.error("Unable to read temporary file: " + url);
      try {
        os.close();
      } catch (IOException e1) {
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
        log.error("Unable to download: " + url + " because the file size is larger than the " +
            "specified " + Integer.toString(maxDownloadSizeInBytes / 1024 / 1024) + " MiB");
        try {
          os.close();
        } catch (IOException e) {
        }
        tempFile.delete();
        return null;
      }
      try {
        os.write(b, 0, length);
      } catch (IOException e) {
        log.error("Unable to write to temporary file: " + url);
        try {
          os.close();
        } catch (IOException e1) {
        }
        tempFile.delete();
        return null;
      }
      try {
        length = is.read(b);
      } catch (IOException e) {
        log.error("Unable to read to temporary file: " + url);
        try {
          os.close();
        } catch (IOException e1) {
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
        e.printStackTrace();
      }
    }

    return tempFile;
  }

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
        gzis.close();
        out.close();
      } catch (IOException e) {
      }
    }
    return tempFile;
  }

  public static void deleteDirOnExit(File dir) {
    Objects.requireNonNull(dir, "dir can not be null.");

    final ZipExpander zipExpander = new ZipExpander(1024);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        zipExpander.deleteDirectory(dir);
        if (dir.exists()) {
          System.out.print("Problem cleaning up the extracted content: " + dir.getAbsolutePath());
        }
      }
    });
  }

  public static File getTempFileFromResource(String resourcePath){
    try {
      URL resourceLocation = new URL(resourcePath);
      InputStream in = resourceLocation.openConnection().getInputStream();
      //could not acquire the resource so return null
      if (in == null) {
        return null;
      }
      //create the file in a temp location
      File tempFile = new File(FileUtils.TMP_DIR + in.hashCode() + ".tmp");
      tempFile.createNewFile();
      //this temp file will be removed on exit of JVM
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
