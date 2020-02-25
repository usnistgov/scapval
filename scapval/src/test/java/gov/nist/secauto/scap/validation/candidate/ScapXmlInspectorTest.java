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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.scap.validation.SCAPVersion;
import gov.nist.secauto.scap.validation.candidate.CandidateFile;
import gov.nist.secauto.scap.validation.candidate.ScapXmlInspector;
import gov.nist.secauto.scap.validation.component.XccdfVersion;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class ScapXmlInspectorTest {

  private final ScapXmlInspector inspector = new ScapXmlInspector();

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void testXccdf() throws Exception {

    final File file = new File(
        new URL("classpath:src/test/resources/candidates/components/xccdf" + "/xccdf114-xml/fdcc-winvista-xccdf.xml")
            .getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertEquals(XccdfVersion.V1_1_4, candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.STANDALONE_XCCDF, candidate.getType());
    assertNull(candidate.getDisqualificaiton());
  }

  @Test
  public void testOval5() throws Exception {
    final File file
        = new File(new URL("classpath:src/test/resources/candidates/oval/fdcc-winxp" + "-oval-5-4.xml").getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.UNKNOWN, candidate.getType());
    assertNotNull(candidate.getDisqualificaiton());
  }

  @Test
  public void testFileNotFound() {
    final String name = "foobar.txt";
    final File file = new File(name);

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertEquals(name, candidate.getName());
    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.UNKNOWN, candidate.getType());
    assertNotNull(candidate.getDisqualificaiton());
    assertNull(candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
    assertNull(candidate.getXccdfVersion());
  }

  @Test
  public void testScap12() throws Exception {
    final File file = new File(
        new URL("classpath:src/test/resources/candidates/scap-12/scap_gov" + ".nist_USGCB-Windows-XP-firewall.xml")
            .getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.SCAP_COMBINED_FILE, candidate.getType());
    assertNull(candidate.getDisqualificaiton());
    assertEquals(SCAPVersion.V1_2, candidate.getScapVersion());
    assertEquals("CONFIGURATION", candidate.getScapUseCase());
  }

  @Test
  public void testScap12WithoutUseCase() throws Exception {
    final File file = new File(new URL(
        "classpath:src/test/resources/candidates/scap-12/scap_gov" + ".nist_USGCB-Windows-XP-firewall-NO-USE-CASE.xml")
            .getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.SCAP_COMBINED_FILE, candidate.getType());
    assertNull(candidate.getDisqualificaiton());
    assertEquals(SCAPVersion.V1_2, candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
  }

  @Test
  public void testScap12InvalidUseCase() throws Exception {
    final File file = new File(new URL("classpath:src/test/resources/candidates/scap-12/scap_gov"
        + ".nist_USGCB-Windows-XP-firewall-INVALID-USE-CASE.xml").getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.SCAP_COMBINED_FILE, candidate.getType());
    assertNull(candidate.getDisqualificaiton());

    assertEquals(SCAPVersion.V1_2, candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
  }

  @Test
  public void testScap13() throws Exception {
    final File file = new File(
        new URL("classpath:src/test/resources/candidates/scap-13/" + "source_data_stream_collection_sample.xml")
            .getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.SCAP_COMBINED_FILE, candidate.getType());
    assertNull(candidate.getDisqualificaiton());
    assertEquals(SCAPVersion.V1_3, candidate.getScapVersion());
    assertEquals("CONFIGURATION", candidate.getScapUseCase());
  }

  @Test
  public void testScap13WithoutUseCase() throws Exception {
    final File file = new File(new URL(
        "classpath:src/test/resources/candidates/scap-13/" + "source_data_stream_collection_sample-NO-USE-CASE.xml")
            .getFile());

    final CandidateFile candidate = this.inspector.createCandidate(new CandidateFile.Builder(file));

    assertNull(candidate.getXccdfVersion());
    assertEquals(CandidateFile.Type.SCAP_COMBINED_FILE, candidate.getType());
    assertNull(candidate.getDisqualificaiton());
    assertEquals(SCAPVersion.V1_3, candidate.getScapVersion());
    assertNull(candidate.getScapUseCase());
  }

}
