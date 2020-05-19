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

import static gov.nist.secauto.scap.validation.utils.FileUtils.BUFFER_SIZE;

import gov.nist.secauto.scap.validation.utils.FileUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Fetches the current CCE and CPE dictionaries at the start of the validation process. If the
 * file's hash matches the latest online version, then the download is skipped. If the user is
 * working off-line, then the download is skipped and a warning is issued. Currently it is an
 * unrecoverable error if the local directory is missing as it should have been created when the
 * tool was deployed. Download URLs are currently hard-coded but can be overridden with system
 * properties.
 */
public class DataFeedDownloader {
  static class URLHolder {
    private final String baseUrl;
    private final String compressedUrl;
    private final String metaUrl;
    private final String localName;
    private byte[] onlineSHA256Hash;

    public URLHolder(String baseUrl, String compressedUrl, String metaUrl, String localName) {
      this.baseUrl = baseUrl;
      this.compressedUrl = compressedUrl;
      this.metaUrl = metaUrl;
      this.localName = localName;
    }

    public String getBaseUrl() {
      return baseUrl;
    }

    public String getCompressedUrl() {
      return compressedUrl;
    }

    public String getMetaUrl() {
      return metaUrl;
    }

    public String getLocalName() {
      return localName;
    }

    public void setOnlineSHA256Hash(byte[] onlineSHA256Hash) {
      this.onlineSHA256Hash = onlineSHA256Hash;
    }

    public byte[] getOnlineSHA256Hash() {
      getOnlineFileHash(this);
      return onlineSHA256Hash;
    }
  }

  /**
   * Array of files to download with system property keys to override.
   */

  static final URLHolder[] dictionaryUrls = new URLHolder[] {
      new URLHolder("https://static.nvd.nist.gov/feeds/xml/cce/nvdcce-0.1-feed.xml",
          "https://static.nvd.nist.gov/feeds/xml/cce/nvdcce-0.1-feed.xml.gz",
          "https://static.nvd.nist.gov/feeds/xml/cce/nvdcce-0.1-feed.meta", "nvdcce-0.1-feed.xml"),

      new URLHolder("https://static.nvd.nist.gov/feeds/xml/cpe/dictionary/official-cpe-dictionary_v2.2.xml",
          "https://static.nvd.nist.gov/feeds/xml/cpe/dictionary/official-cpe-dictionary_v2.2.xml.gz",
          "https://static.nvd.nist.gov/feeds/xml/cpe/dictionary/official-cpe-dictionary_v2.2.meta",
          "official-cpe-dictionary_v2.2.xml") };

  private static final Logger log = LogManager.getLogger(DataFeedDownloader.class);

  /**
   * Checks for out of date local data feed files then downloads and updates the local files.
   *
   * @param maxDownloadSizeInMiB
   *          if the download surpasses this value it will fail.
   * @return a boolean true if a successful download occurred, false if not
   */
  public static boolean download(int maxDownloadSizeInMiB) {
    Objects.requireNonNull(maxDownloadSizeInMiB, "maxDownloadSizeInMiB can not be null");

    String dataFeedsDirectoryPath = null;
    File dataFeedsDirectoryFile = null;

    try {
      dataFeedsDirectoryPath = new URL("classpath:data_feeds/").openConnection().getURL().getPath();
      // remove any URL encoding present which could corrupt the path
      dataFeedsDirectoryPath = URLDecoder.decode(dataFeedsDirectoryPath, "UTF-8");

      if (dataFeedsDirectoryPath.contains(".jar!")) {
        // we are running from a packaged version and would get something like
        // scapval-1.3.0\scapval-1.3.0.jar!\data_feeds
        // so in order to use the proper data_feeds dir and account for running the .bat
        // file from
        // a different current working dir,
        // we'll gather the location with some path parsing here. Since the path is obtained
        // from
        // URL.openConnection()
        // the seperator should always be "/"
        int dirsInPath = dataFeedsDirectoryPath.split("/").length;
        StringBuilder packagedPath = new StringBuilder("");
        for (int i = 0; i < dirsInPath - 2; i++) {
          packagedPath.append(dataFeedsDirectoryPath.split("/")[i] + "/");
        }
        // remove the prepended file: protocol added by URL
        dataFeedsDirectoryPath = packagedPath.toString().replace("file:", "") + "data_feeds";
      }

      dataFeedsDirectoryFile = new File(dataFeedsDirectoryPath);

      // makes sure the data_feeds dir is accessible
      if (!dataFeedsDirectoryFile.isDirectory() && dataFeedsDirectoryFile.canRead()) {
        throw new IOException("Cannot read directory: " + dataFeedsDirectoryPath);
      }
    } catch (IOException e) {
      String errorText = "Unable to access the data_feeds directory." + System.lineSeparator() + "CCE and CPE "
          + "dictionaries will not be updated." + System.lineSeparator() + "If problem persists, try to "
          + "re-extract SCAPVal. " + e.getMessage();
      log.error(errorText);
      ValidationNotes.getInstance().createValidationNote(errorText);
      return false;
    }

    List<URLHolder> filesNeedUpdating = filesNeedUpdating(dataFeedsDirectoryFile);
    // if one or more files need updating
    if (!filesNeedUpdating.isEmpty()) {
      // download the files
      for (URLHolder url : filesNeedUpdating) {
        try {
          if (url.getOnlineSHA256Hash() == null) {
            log.error("Failed to locate the hash value online. File will not be downloaded.");
            continue;
          }
          // download the file and convert the max MB specified to bytes
          File compressedFile
              = FileUtils.downloadFile(new URL(url.getCompressedUrl()), maxDownloadSizeInMiB * 1024 * 1024);
          if (compressedFile == null) {
            log.error("Failed to download file locally. File will not be used.");
            continue;
          }
          // decompress file here
          File decompressedFile = FileUtils.decompressGZIPFile(compressedFile);

          if (!compressedFile.delete()) {
            log.error("Unable to clean up the downloaded file: " + compressedFile.getAbsolutePath());
          }

          if (decompressedFile == null) {
            log.error("Failed to decompress the file.");
            continue;
          } else {
            // check the published hash compared to downloaded file
            if (!doesSHA256Match(new BufferedInputStream(new FileInputStream(decompressedFile)),
                url.getOnlineSHA256Hash())) {
              log.error("Downloaded temp file does not match expected hash.  File will not be used.");
              if (!decompressedFile.delete()) {
                log.error("Unable to delete file: " + decompressedFile.getAbsolutePath());
              }
              continue;
            }
            File permFile = new File(dataFeedsDirectoryFile, url.getLocalName());

            if (permFile.exists() && !permFile.delete()) {
              log.error("Permanent location file could not be deleted.");
              decompressedFile.delete();
            }
            // use Java nio to move the file, success will be determined by the path
            // returned
            Path tmp = Files.move(decompressedFile.toPath(), permFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (tmp != permFile.toPath()) {
              log.error("Could not move temp file to permanent location.");
              decompressedFile.delete();
            }
          }
        } catch (IOException e) {
          log.error("Unable to download and update " + url.getLocalName());
          ValidationNotes.getInstance().createValidationNote("Unable to download and update " + url.getLocalName());
          return false;
        } catch (NoSuchAlgorithmException e) {
          log.error("SHA-256 not supported");
          return false;
        }

      }
    }

    return true;
  }

  /**
   * Returns a list of URLs for files that need updating.
   */
  static List<URLHolder> filesNeedUpdating(File dir) {
    Objects.requireNonNull(dir, "dir can not be null");

    List<URLHolder> list = new LinkedList<URLHolder>();
    for (URLHolder url : dictionaryUrls) {
      if (getOnlineFileHash(url) && isUpdateRecommended(dir, url)) {
        list.add(url);
      }
    }
    return list;
  }

  /**
   * Returns a file that is missing or out of date.
   *
   * @param dir
   *          the file directory where to look for the file. Also used to log messages, not null
   * @param url
   *          The URL where to download the file, not null
   * @return The file if it needs updating, or null if it is current.
   */
  static boolean isUpdateRecommended(File dir, URLHolder url) {
    Objects.requireNonNull(dir, "dir cannot be null");
    Objects.requireNonNull(url, "url cannot be null");
    File file = new File(dir, url.getLocalName());
    // update is recommended if file at URL has a different hash (i.e its newer)
    try {
      BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
      byte[] onlineFileSHA256Hash = url.getOnlineSHA256Hash();

      if (!doesSHA256Match(bufferedInputStream, onlineFileSHA256Hash)) {
        // report that file is not current
        log.info("There is a newer version of: " + file.getAbsolutePath());
        return true;
      }
      log.info(file + " (SHA-256: " + FileUtils.getFileHash(file, FileUtils.DEFAULT_HASH_ALGORITHM) + ") is current.");
    } catch (NoSuchAlgorithmException e) {
      // The algorithm is set as final var set in FileUtils.DEFAULT_HASH_ALGORITHM
    } catch (IOException e) {
      // the local file is not available, attempt to replace it
      log.info("Unable to access: " + file.getAbsolutePath() + " an update will attempt to replace it.");
      return true;
    }
    return false;
  }

  private static byte[] getSHA256Hash(InputStream metafile) throws IOException {
    Objects.requireNonNull(metafile, "metafile can not be null");

    BufferedReader br = new BufferedReader(new InputStreamReader(metafile));

    String line;
    byte[] baseDigest = null;
    while ((line = br.readLine()) != null) {
      if (line.startsWith("sha256:")) {
        String sha256String = line.substring("sha256:".length());
        baseDigest = new byte[(int) Math.ceil(sha256String.length() / 2)];
        for (int i = 0, size = baseDigest.length; i < size; i++) {
          baseDigest[i] = (byte) Integer.parseInt(sha256String.substring(i * 2, i * 2 + 2), 16);
        }
      }
    }
    br.close();
    return baseDigest;
  }

  private static boolean doesSHA256Match(InputStream basefile, byte[] baseDigest)
      throws IOException, NoSuchAlgorithmException {

    int length = BUFFER_SIZE;
    List<byte[]> basefileList = new LinkedList<byte[]>();
    while (length == BUFFER_SIZE) {
      byte[] bytes = new byte[BUFFER_SIZE];
      length = basefile.read(bytes);
      basefileList.add(bytes);
    }
    basefile.close();
    byte[] fileArray = new byte[BUFFER_SIZE * (basefileList.size() - 1) + length];
    int index = 0;
    for (int i = 0, size = basefileList.size(); i < size - 1; i++) {
      System.arraycopy(basefileList.get(i), 0, fileArray, index, BUFFER_SIZE);
      index += BUFFER_SIZE;
    }
    System.arraycopy(basefileList.get(basefileList.size() - 1), 0, fileArray, index, length);
    basefileList = null;

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] digest = md.digest(fileArray);

    if (digest.length != baseDigest.length) {
      return false;
    }

    for (int i = 0, size = baseDigest.length; i < size; i++) {
      if (baseDigest[i] != digest[i]) {
        return false;
      }
    }
    return true;
  }

  private static boolean getOnlineFileHash(URLHolder urlHolder) {
    Objects.requireNonNull(urlHolder, "urlHolder can not be null");
    try {
      if (urlHolder.getMetaUrl() != null) {
        URL url = new URL(urlHolder.getMetaUrl());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        byte[] onlineHash = getSHA256Hash(bufferedInputStream);
        urlHolder.setOnlineSHA256Hash(onlineHash);
      } else {
        return false;
      }
    } catch (MalformedURLException e) {
      return false;
    } catch (IOException e) {
      log.info("Unable to reach the server for metadata on the latest file online. Local file: "
          + urlHolder.getLocalName() + " will not be updated.");
      return false;
    }
    return true;
  }
}
