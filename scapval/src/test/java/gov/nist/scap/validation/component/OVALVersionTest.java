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
package gov.nist.scap.validation.component;

import org.junit.Assert;
import org.junit.Test;

public class OVALVersionTest {
  @Test
  public void getByString() throws Exception {
    Assert.assertEquals(OVALVersion.getByString("5.11.1"), OVALVersion.V5_11_1);
  }

  @Test
  public void getVersionString() throws Exception {
    Assert.assertEquals(OVALVersion.V5_11_1.getVersionString(), "5.11.1");
  }

  @Test
  public void getSchemaDir() throws Exception {
    Assert.assertEquals(OVALVersion.V5_11_1.getSchemaDir(), "xsd/mitre/oval/oval_5.11.1");
  }

  @Test
  public void getDefinitionSchematron() throws Exception {
    Assert.assertEquals(OVALVersion.V5_11_1.getDefinitionSchematron(),
        "oval-definitions-schematron-5.11.sch");
  }

  @Test
  public void getResultSchematron() throws Exception {
    Assert.assertEquals(OVALVersion.V5_11_1.getResultSchematron(),
        "oval-results-schematron-5.11.sch");
  }

}