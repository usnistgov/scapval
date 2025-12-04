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

import gov.nist.secauto.decima.core.assessment.Assessment;
import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.decima.core.requirement.BaseRequirement;
import gov.nist.secauto.decima.core.requirement.DefaultBaseRequirement;
import gov.nist.secauto.decima.core.requirement.RequirementsManager;
import gov.nist.secauto.decima.xml.assessment.schema.SchemaAssessment;
import gov.nist.secauto.decima.xml.document.JDOMDocument;
import gov.nist.secauto.decima.xml.document.XMLDocument;
import gov.nist.secauto.scap.validation.Application;
import gov.nist.secauto.scap.validation.AssessmentFactory;
import gov.nist.secauto.scap.validation.SCAPValReqManager;
import gov.nist.secauto.scap.validation.SCAPVersion;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

public class AssessmentFactoryTest {

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void createAssessmentFactory11() throws Exception {
    XMLDocument SCAP11 = new JDOMDocument(new File(
        new URL("classpath:src/test/resources/candidates/scap-11/combined/SCAP11CombinedContent.xml").getFile()));
    AssessmentFactory assessmentFactory
        = new AssessmentFactory(SCAPVersion.V1_1, "CONFIGURATION", Application.ContentType.SOURCE, SCAP11);
    SchemaAssessment scapDocumentSchemaAssessment = assessmentFactory.createSCAPSchemaAssessment();
    // currently should expect at least 40 schemas
    Assert.assertTrue(scapDocumentSchemaAssessment.getSchemaSources().size() > 40);

    Assessment<XMLDocument> scapDocumentSchematronAssessments = assessmentFactory.createSCAPSchematronAssessments();
    // currently should expect at least 4
    Assert.assertTrue(scapDocumentSchematronAssessments.getExecutableAssessments(SCAP11).size() > 3);
  }

  @Test
  public void createAssessmentFactory12() throws Exception {
    XMLDocument SCAP12 = new JDOMDocument(
        new File(new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml")
            .getFile()));
    AssessmentFactory assessmentFactory
        = new AssessmentFactory(SCAPVersion.V1_2, "CONFIGURATION", Application.ContentType.SOURCE, SCAP12);
    SchemaAssessment scapDocumentSchemaAssessment = assessmentFactory.createSCAPSchemaAssessment();
    // currently should expect at least 40 schemas
    Assert.assertTrue(scapDocumentSchemaAssessment.getSchemaSources().size() > 40);

    Assessment<XMLDocument> scapDocumentSchematronAssessments = assessmentFactory.createSCAPSchematronAssessments();
    // currently should expect at least 5
    Assert.assertTrue(scapDocumentSchematronAssessments.getExecutableAssessments(SCAP12).size() > 4);
  }

  @Test
  public void createAssessmentFactory13() throws Exception {
    XMLDocument SCAP13 = new JDOMDocument(new File(
        new URL("classpath:src/test/resources/candidates/scap-13/source_data_stream_collection_sample.xml").getFile()));
    AssessmentFactory assessmentFactory
        = new AssessmentFactory(SCAPVersion.V1_3, "CONFIGURATION", Application.ContentType.SOURCE, SCAP13);
    SchemaAssessment scapDocumentSchemaAssessment = assessmentFactory.createSCAPSchemaAssessment();
    // currently should expect at least 58 schemas
    Assert.assertTrue(scapDocumentSchemaAssessment.getSchemaSources().size() > 58);

    Assessment<XMLDocument> scapDocumentSchematronAssessments = assessmentFactory.createSCAPSchematronAssessments();
    // currently should expect at least 5
    Assert.assertTrue(scapDocumentSchematronAssessments.getExecutableAssessments(SCAP13).size() > 4);
  }

  @Test
  public void createAssessmentFactory14() throws Exception {
    XMLDocument SCAP14 = new JDOMDocument(new File(
        new URL("classpath:src/test/resources/candidates/scap-14/source_data_stream_collection_sample.xml").getFile()));
    AssessmentFactory assessmentFactory
        = new AssessmentFactory(SCAPVersion.V1_4, "CONFIGURATION", Application.ContentType.SOURCE, SCAP14);
    SchemaAssessment scapDocumentSchemaAssessment = assessmentFactory.createSCAPSchemaAssessment();
    // currently should expect at least 58 schemas
    Assert.assertTrue(scapDocumentSchemaAssessment.getSchemaSources().size() > 58);

    Assessment<XMLDocument> scapDocumentSchematronAssessments = assessmentFactory.createSCAPSchematronAssessments();
    // currently should expect at least 5
    Assert.assertTrue(scapDocumentSchematronAssessments.getExecutableAssessments(SCAP14).size() > 4);
  }

  @Test
  public void createAssessmentFactoryResult() throws Exception {
    XMLDocument SCAP12Result = new JDOMDocument(
        new File(new URL("classpath:src/test/resources/candidates/scap-12/arf/ARF-results.xml").getFile()));
    AssessmentFactory assessmentFactory
        = new AssessmentFactory(SCAPVersion.V1_2, null, Application.ContentType.RESULT, SCAP12Result);
    SchemaAssessment scapDocumentSchemaAssessment = assessmentFactory.createSCAPSchemaAssessment();
    // currently expecting up to 46
    Assert.assertTrue(scapDocumentSchemaAssessment.getSchemaSources().size() > 45);

    Assessment<XMLDocument> scapDocumentSchematronAssessments = assessmentFactory.createSCAPSchematronAssessments();
    // currently should expect at least 5
    Assert.assertTrue(scapDocumentSchematronAssessments.getExecutableAssessments(SCAP12Result).size() > 4);
  }

  @Test
  public void newRequirementsManager() throws Exception {
    // make sure req manager properly loads for each SCAP version
    checkRequirementsManager(SCAPValReqManager.getRequirements(SCAPVersion.V1_1), "scapval-scap-1.1-requirements.xml");
    checkRequirementsManager(SCAPValReqManager.getRequirements(SCAPVersion.V1_2), "scapval-scap-1.2-requirements.xml");
    checkRequirementsManager(SCAPValReqManager.getRequirements(SCAPVersion.V1_3), "scapval-scap-1.3-requirements.xml");
    checkRequirementsManager(SCAPValReqManager.getRequirements(SCAPVersion.V1_4), "scapval-scap-1.4-requirements.xml");
  }

  public void checkRequirementsManager(RequirementsManager requirementsManager, String expectedName) {
    Assert.assertTrue(
        requirementsManager.getRequirementDefinitions().get(0).getSchemeSpecificPart().contains(expectedName));
    Collection<BaseRequirement> baseRequirements = requirementsManager.getBaseRequirements();
    // just a rough estimate to ensure we're loading an appropriate amount
    Assert.assertTrue(requirementsManager.getBaseRequirements().size() > 120);
    Iterator<BaseRequirement> iterator = baseRequirements.iterator();
    while (iterator.hasNext()) {
      DefaultBaseRequirement requirement = (DefaultBaseRequirement) iterator.next();
      Assert.assertFalse(requirement.getId().isEmpty());
      Assert.assertFalse(requirement.getStatement().isEmpty());
    }
  }
}
