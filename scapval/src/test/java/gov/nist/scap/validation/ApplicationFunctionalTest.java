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

import org.junit.Test;

import static org.junit.Assert.fail;

public class ApplicationFunctionalTest {

  @Test
  public void CompleteSourceRun() {
    try {
      new Application().runCLI(new String[]{"-scapversion", "1.2", "-file",
          "src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml"});
    } catch (Exception e) {
      e.printStackTrace();
      fail("Encountered an unexpected Exception.");
    }
  }

  @Test
  public void CompleteResultRun() {

//        // handle when -sourceds and -crpath are specified
//        if (cmd.getOptionValue(OPTION_SOURCE_DS) != null || cmd.getOptionValue(OPTION_CR_PATH)
// != null) {

    try {
      new Application().runCLI(new String[]{"-scapversion", "1.2", "-resultfile",
          "src/test/resources/candidates/scap-12/arf/ARF-results.xml"});
    } catch (Exception e) {
      e.printStackTrace();
      fail("Encountered an unexpected Exception.");
    }
  }

  @Test
  public void CompleteComponentRun() {
    try {
      new Application().runCLI(new String[]{"-componentfile",
          "src/test/resources/candidates/components/oval/oval-vulnerability-remote-code-exec-5" +
              "-10.xml"});
    } catch (Exception e) {
      e.printStackTrace();
      fail("Encountered an unexpected Exception.");
    }
  }

  @Test
  public void IncompleteRun() {
    boolean failed = false;
    try {
      new Application().runCLI(new String[]{"-scapversion", ".4", "-file",
          "src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml"});
    } catch (Exception e) {
      //expected an exception, test passed
      failed = true;
    }
    if (!failed) {
      fail("Expected an exception for this test which did not occur.");
    }
  }
}
