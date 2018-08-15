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

package gov.nist.scap.validation.candidate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.nist.scap.validation.SCAPVersion;
import gov.nist.scap.validation.component.SCAP11Components;
import gov.nist.scap.validation.component.XccdfVersion;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CandidateFileTest {
  String property = "java.io.tmpdir";
  String tmpDir = System.getProperty(property);

  @Test
  public void testTypeUnknown() {

    final File file = new File(tmpDir + "myzip.zip");

    final CandidateFile.Builder builder
        = new CandidateFile.Builder(file).setTypeUnknown("Some " + "reason why this is not SCAP");

    final CandidateFile candidate = builder.createCandidateFile();

    assertEquals("myzip.zip", candidate.getName());
    assertFalse(candidate.isExpanded());
    assertEquals(tmpDir + "myzip.zip", candidate.getAbsolutePath());
    assertEquals("myzip.zip", candidate.getDisplayPath());
    assertEquals("Some reason why this is not SCAP", candidate.getDisqualificaiton());

    assertEquals(CandidateFile.Type.UNKNOWN, candidate.getType());
    assertNull(candidate.getXccdfVersion());
    assertNull(candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
    assertTrue(candidate.getScapContentTypes().isEmpty());
  }

  // @Test
  // public void testTypeEmbeddedXCCDF() {
  //
  // final File file = new File("C:\\Users\\user\\Downloads\\U_Windows_2008_MS_V6R36_STIG.zip");
  //
  //
  // final int BUFFER_SIZE = 2048;
  //
  // final CanOpener opener = new CanOpener(BUFFER_SIZE);
  //
  // final CandidateFileList result =
  // opener.findCandidateFiles(file);
  // final List<CandidateFile> candidates =
  // result.getValidatableCandidates();
  //
  // candidates.contains("s");
  //
  // }

  @Test
  public void testTypeXccdf() {

    final File file = new File(tmpDir + "xccdf.xml");

    final CandidateFile.Builder builder = new CandidateFile.Builder(file).setTypeXccdf(XccdfVersion.V1_2);

    final CandidateFile candidate = builder.createCandidateFile();

    assertEquals("xccdf.xml", candidate.getName());
    assertFalse(candidate.isExpanded());
    assertEquals(tmpDir + "xccdf.xml", candidate.getAbsolutePath());
    assertEquals("xccdf.xml", candidate.getDisplayPath());
    assertNull(candidate.getDisqualificaiton());

    assertEquals(CandidateFile.Type.STANDALONE_XCCDF, candidate.getType());
    assertEquals(XccdfVersion.V1_2, candidate.getXccdfVersion());
    assertNull(candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
    assertTrue(candidate.getScapContentTypes().isEmpty());
  }

  @Test
  public void testTypeScapBundle() {

    final File file = new File(tmpDir + "scapfolder");

    final List<SCAP11Components> types = new ArrayList<SCAP11Components>();
    types.add(SCAP11Components.OVAL_COMPLIANCE);

    final CandidateFile.Builder builder = new CandidateFile.Builder(file).setTypeScapBundle(types);

    final CandidateFile candidate = builder.createCandidateFile();

    assertEquals("scapfolder", candidate.getName());
    assertFalse(candidate.isExpanded());
    assertEquals(tmpDir + "scapfolder", candidate.getAbsolutePath());
    assertEquals("scapfolder", candidate.getDisplayPath());
    assertNull(candidate.getDisqualificaiton());

    assertEquals(CandidateFile.Type.SCAP_BUNDLE, candidate.getType());
    assertNull(candidate.getXccdfVersion());
    assertNull(candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
    assertEquals(1, candidate.getScapContentTypes().size());
    assertEquals(SCAP11Components.OVAL_COMPLIANCE, candidate.getScapContentTypes().get(0));
  }

  @Test
  public void testTypeScapCombinedFile() {

    final File file = new File(tmpDir + "scap.xml");

    final CandidateFile.Builder builder
        = new CandidateFile.Builder(file).setTypeScapCombinedFile(SCAPVersion.V1_2, "CONFIGURATION");

    final CandidateFile candidate = builder.createCandidateFile();

    assertEquals("scap.xml", candidate.getName());
    assertFalse(candidate.isExpanded());
    assertEquals(tmpDir + "scap.xml", candidate.getAbsolutePath());
    assertEquals("scap.xml", candidate.getDisplayPath());
    assertNull(candidate.getDisqualificaiton());

    assertEquals(CandidateFile.Type.SCAP_COMBINED_FILE, candidate.getType());
    assertNull(candidate.getXccdfVersion());
    assertEquals(SCAPVersion.V1_2, candidate.getScapVersion());
    assertEquals("CONFIGURATION", candidate.getScapUseCase());
    assertTrue(candidate.getScapContentTypes().isEmpty());
  }

  @Test
  public void testExpanded() {

    final File file = new File(tmpDir + "myzip.zip");

    final CandidateFile.Builder builder = new CandidateFile.Builder(file).setExpandedTo(new File(tmpDir + "myzip0"))
        .setTypeUnknown("Unknown expanded file");

    final CandidateFile candidate = builder.createCandidateFile();

    assertEquals("myzip.zip", candidate.getName());
    assertTrue(candidate.isExpanded());
    assertEquals(tmpDir + "myzip0", candidate.getAbsolutePath());
    assertEquals("myzip.zip", candidate.getDisplayPath());
    assertEquals("Unknown expanded file", candidate.getDisqualificaiton());

    assertEquals(CandidateFile.Type.UNKNOWN, candidate.getType());
    assertNull(candidate.getXccdfVersion());
    assertNull(candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
    assertTrue(candidate.getScapContentTypes().isEmpty());
  }

  @Test
  public void testParent() {

    final File parentFile = new File(tmpDir + "myzip.zip");

    final CandidateFile.Builder parentBuilder = new CandidateFile.Builder(parentFile)
        .setExpandedTo(new File(tmpDir + "myzip0")).setTypeUnknown("Unknown expanded file");

    final CandidateFile parent = parentBuilder.createCandidateFile();

    final File childFile = new File(tmpDir + "myzip0/myxccdf.xml");

    final CandidateFile.Builder childBuilder
        = new CandidateFile.Builder(parent, childFile).setTypeXccdf(XccdfVersion.V1_1_4);

    final CandidateFile candidate = childBuilder.createCandidateFile();

    assertEquals("myxccdf.xml", candidate.getName());
    assertFalse(candidate.isExpanded());
    assertEquals(tmpDir + "myzip0\\myxccdf.xml", candidate.getAbsolutePath());
    assertEquals("myzip.zip\\myxccdf.xml", candidate.getDisplayPath());
    assertNull(candidate.getDisqualificaiton());

    assertEquals(CandidateFile.Type.STANDALONE_XCCDF, candidate.getType());
    assertEquals(XccdfVersion.V1_1_4, candidate.getXccdfVersion());
    assertNull(candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
    assertTrue(candidate.getScapContentTypes().isEmpty());
  }

  @Test
  public void testTypeRequired() {

    try {
      new CandidateFile.Builder(new File("foobar.txt")).createCandidateFile();
      fail("Expected exception if builder type is not set");
    } catch (IllegalStateException e) {
      return;
    }
  }

  @Test
  public void testCannotExpandTwice() {

    final CandidateFile.Builder builder
        = new CandidateFile.Builder(new File("foobar.zip")).setExpandedTo(new File("foobar"));
    try {
      builder.setExpandedTo(new File("foobar2"));
      fail("Expected exception if expand called twice");
    } catch (IllegalStateException e) {
      return;
    }
  }

}
