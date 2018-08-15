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

package gov.nist.scap.validation.utils;

import gov.nist.scap.validation.Application;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class FileUtilsTest {
  @Test
  public void getFileHash() throws Exception {
    final File datastream = new File(
        new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml")
            .getFile());
    Assert.assertEquals(FileUtils.getFileHash(datastream, FileUtils.DEFAULT_HASH_ALGORITHM),
        "31AFB52F56F827AFE8555397D2E6302127AB715EAE9316AFA4D509C2008E6857");
  }

  @Test
  public void determineFileType() throws Exception {
    final String datastream = new File(
        new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml")
            .getFile()).getAbsolutePath();
    Assert.assertEquals(FileUtils.determineSCAPFileType(datastream), Application.FileType.XML);
  }

  @Test
  public void decompressGZIPFile() throws Exception {
    final File compressedFile
        = new File(new URL("classpath:src/test/resources/candidates/gzip/test-content.gz").getFile());
    File decompressedFile = null;
    try {
      decompressedFile = FileUtils.decompressGZIPFile(compressedFile);
      Assert.assertEquals(decompressedFile.length(), 12);
    } finally {
      decompressedFile.delete();
    }
  }

}