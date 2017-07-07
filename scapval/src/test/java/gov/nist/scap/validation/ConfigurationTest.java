/**
 * Portions of this software was developed by employees of the National Institute
 * of Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 17 United States Code Section 105, works of NIST employees are
 * not subject to copyright protection in the United States and are considered to
 * be in the public domain. Permission to freely use, copy, modify, and distribute
 * this software and its documentation without fee is hereby granted, provided that
 * this notice and disclaimer of warranty appears in all copies.
 * <p>
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

package gov.nist.scap.validation;

import gov.nist.decima.core.document.DocumentException;
import gov.nist.scap.validation.exceptions.ConfigurationException;
import gov.nist.scap.validation.exceptions.SCAPException;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.fail;

public class ConfigurationTest {
  File testfile1;

  public ConfigurationTest() {
    try {
      testfile1 = new File(new URL("classpath:src/test/resources/candidates/scap-12/scap_gov" +
          ".nist_USGCB-Windows-XP-firewall.xml").getFile());
    } catch (MalformedURLException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGoodOptions1() {
    try {
      new Application().parseCLI(new String[]{"-scapversion", "1.2", "-usecase", "CONFIGURATION",
          "-file", testfile1.getAbsolutePath()});
    } catch (ParseException | ConfigurationException | SCAPException | IOException |
        DocumentException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGoodOptions2() {
    try {
      new Application().parseCLI(new String[]{"-scapversion", "1.2", "-file",
          testfile1.getAbsolutePath()});
    } catch (ParseException | ConfigurationException | SCAPException | IOException |
        DocumentException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGoodOptions3() {
    try {
      new Application().parseCLI(new String[]{"-scapversion", "1.2", "-file",
          testfile1.getAbsolutePath(), "-debug"});
    } catch (ParseException | ConfigurationException | SCAPException | IOException |
        DocumentException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGoodOptions4() {
    try {
      new Application().parseCLI(new String[]{"-componentfile", testfile1.getAbsolutePath(),
          "-debug"});
    } catch (ParseException | ConfigurationException | SCAPException | IOException |
        DocumentException e) {
      fail(e.getMessage());
    }
  }

  @Test(expected = ParseException.class)
  public void testBadOptions1() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.0", "-usecase", "CONFIGURATION",
        "-file", testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions2() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-usecase", "CONFIGURATION", "-file",
        testfile1.getAbsolutePath()});
  }

  @Test(expected = ParseException.class)
  public void testBadOptions3() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.0", "-usecase", "CONFIGURATION",
        "-file", testfile1.getAbsolutePath(), "-dir", testfile1.getAbsolutePath()});
  }

  @Test(expected = ParseException.class)
  public void testBadOptions4() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.2", "-usecase", "CONFIGURATION",
        "-file", testfile1.getAbsolutePath(), "-cats", testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions5() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.3", "-usecase", "CARS", "-file",
        testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions6() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.2", "-usecase", "CONFIGURATION",
        "-file", "whatfile.xml"});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions7() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.1", "-usecase", "CONFIGURATION",
        "-file", testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions8() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.1",
        // "-usecase", "CONFIGURATION",
        "-file", testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions9() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.3", "-usecase", "OVAL_ONLY",
        "-file", testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions10() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.2", "-usecase", "CONFIGURATION",
        "-dir", testfile1.getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions11() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.2", "-usecase", "CONFIGURATION",
        "-dir", testfile1.getParentFile().getAbsolutePath()});
  }

  @Test(expected = ConfigurationException.class)
  public void testBadOptions12() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-scapversion", "1.2", "-componentfile",
        testfile1.getAbsolutePath()});
  }

  @Test(expected = AlreadySelectedException.class)
  public void testBadOptions13() throws ParseException, ConfigurationException, SCAPException,
      IOException, DocumentException {
    new Application().parseCLI(new String[]{"-file", testfile1.getAbsolutePath(),
        "-componentfile", testfile1.getAbsolutePath()});
  }
}
