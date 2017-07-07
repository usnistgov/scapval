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
package gov.nist.scap.validation.candidate;

import gov.nist.scap.validation.NamespaceConstants;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScapDocumentSnifferTest {

  private final ScapDocumentSniffer sniffer = new ScapDocumentSniffer();

  @Test
  public void testXccdf() throws Exception {
    final File xccdfFile = new File(new URL
        ("classpath:src/test/resources/candidates/components/xccdf/xccdf114-xml/fdcc-winvista" +
            "-xccdf.xml").getFile());

    assertEquals(NamespaceConstants.NS_XCCDF_1_1_4.getNamespaceString(), this.sniffer
        .findContentType(xccdfFile.getAbsolutePath()));
  }

  @Test
  public void testOval5() throws Exception {
    final File ovalFile = new File(new URL
        ("classpath:src/test/resources/candidates/components/oval/fdcc-winxp-oval-5-4.xml")
        .getFile());

    assertEquals(NamespaceConstants.NS_OVAL_DEF_5.getNamespaceString(), this.sniffer
        .findContentType(ovalFile.getAbsolutePath()));
  }

  @Test
  public void testFileNotFound() {
    assertNull(this.sniffer.findContentType("foobar.txt"));
  }

  @Test
  public void testScap12() throws Exception {
    final File scap12File = new File(new URL
        ("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall" +
            ".xml").getFile());

    assertEquals(NamespaceConstants.NS_SOURCE_DS_1_2.getNamespaceString(), this.sniffer
        .findContentType(scap12File.getAbsolutePath()));

    assertEquals("CONFIGURATION", this.sniffer.findUseCase(scap12File.getAbsolutePath()));

    final File scap12FileNoUseCase = new File(new URL
        ("classpath:src/test/resources/candidates/scap-12/scap_gov" +
            ".nist_USGCB-Windows-XP-firewall-NO-USE-CASE.xml").getFile());

    assertEquals(NamespaceConstants.NS_SOURCE_DS_1_2.getNamespaceString(), this.sniffer
        .findContentType(scap12FileNoUseCase.getAbsolutePath()));

    assertNull(this.sniffer.findUseCase(scap12FileNoUseCase.getAbsolutePath()));
  }

  @Test
  public void testScap13() throws Exception {
    final File scap13File = new File(new URL
        ("classpath:src/test/resources/candidates/scap-13/source_data_stream_collection_sample" +
            ".xml").getFile());

    assertEquals(NamespaceConstants.NS_SOURCE_DS_1_3.getNamespaceString(), this.sniffer
        .findContentType(scap13File.getAbsolutePath()));

    assertEquals("CONFIGURATION", this.sniffer.findUseCase(scap13File.getAbsolutePath()));

    final File scap13FileNoUseCase = new File(new URL
        ("classpath:src/test/resources/candidates/scap-13/source_data_stream_collection_sample-NO" +
            "-USE-CASE.xml").getFile());

    assertEquals(NamespaceConstants.NS_SOURCE_DS_1_3.getNamespaceString(), this.sniffer
        .findContentType(scap13FileNoUseCase.getAbsolutePath()));

    assertNull(this.sniffer.findUseCase(scap13FileNoUseCase.getAbsolutePath()));
  }

}
