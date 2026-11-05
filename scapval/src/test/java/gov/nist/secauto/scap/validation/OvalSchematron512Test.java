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
import gov.nist.secauto.decima.core.assessment.result.DefaultAssessmentResultBuilder;
import gov.nist.secauto.decima.core.assessment.result.DerivedRequirementResult;
import gov.nist.secauto.decima.core.assessment.result.ResultStatus;
import gov.nist.secauto.decima.core.assessment.result.TestResult;
import gov.nist.secauto.decima.core.assessment.result.TestStatus;
import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.decima.xml.assessment.schematron.SchematronAssessment;
import gov.nist.secauto.decima.xml.document.JDOMDocument;
import gov.nist.secauto.decima.xml.document.XMLDocument;
import gov.nist.secauto.decima.xml.schematron.DefaultSchematronCompiler;
import gov.nist.secauto.decima.xml.schematron.Schematron;
import gov.nist.secauto.scap.validation.component.IndividualComponent;
import gov.nist.secauto.scap.validation.component.OVALVersion;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.transform.JDOMSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OvalSchematron512Test {
  private static final OVALVersion[] VERSIONS = { OVALVersion.V5_12_2, OVALVersion.V5_12_3 };
  private static final String CANDIDATES = "classpath:src/test/resources/candidates/";
  private static final String FIXTURES = "components/oval/schematron/";
  private static final Namespace OVAL_COMMON
      = Namespace.getNamespace("oval", "http://oval.mitre.org/XMLSchema/oval-common-5");

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void every512RuleFileResolvesAndCompilesWithSaxon() throws Exception {
    DefaultSchematronCompiler compiler = new DefaultSchematronCompiler();
    for (OVALVersion version : VERSIONS) {
      for (String filename : new String[] { version.getDefinitionSchematron(), version.getResultSchematron(),
          version.getSystemCharacteristicsSchematron() }) {
        Schematron schematron = compiler.newSchematron(new URL("classpath:rules/other/" + filename));
        Assert.assertTrue(filename, schematron.getTransformerFactory().getClass().getName().startsWith("net.sf.saxon."));
        // Compile the generated XSLT as well as the Schematron transformation pipeline.
        Assert.assertNotNull(filename, schematron.getTransformerFactory()
            .newTemplates(new JDOMSource(schematron.getCompiledSchematron(null))));
      }
    }
  }

  @Test
  public void invalidObjectReferenceFailsSchematron() throws Exception {
    assertFixtureFails("invalid-object-reference.xml", "must reference a show_object");
  }

  @Test
  public void invalidStateReferenceFailsSchematron() throws Exception {
    assertFixtureFails("invalid-state-reference.xml", "must reference a show_state");
  }

  @Test
  public void invalidFilterStateFailsSchematron() throws Exception {
    assertFixtureFails("invalid-filter-state.xml", "is of the wrong type");
  }

  @Test
  public void invalidSemanticConstraintFailsSchematron() throws Exception {
    assertFixtureFails("invalid-semantic-constraint.xml", "operation attribute for the xpath entity");
  }

  @Test
  public void validReferencesHaveNoSchematronFailure() throws Exception {
    for (OVALVersion version : VERSIONS) {
      assertNoFailure(assessFixture("valid-references.xml", version));
    }
  }

  @Test
  public void deprecatedElementIsInformational() throws Exception {
    for (OVALVersion version : VERSIONS) {
      DerivedRequirementResult result = assessFixture("deprecated-element.xml", version);
      assertNoFailure(result);
      Assert.assertTrue("Missing deprecation report", result.getTestResults().stream()
          .anyMatch(test -> test.getStatus() == TestStatus.INFORMATIONAL
              && test.getResultValues().toString().contains("DEPRECATED")));
    }
  }

  @Test
  public void appleIosProfileAcceptsCorrectFilterState() throws Exception {
    for (OVALVersion version : VERSIONS) {
      assertNoFailure(assessFixture("apple-ios-profile-filter.xml", version));
    }
  }

  @Test
  public void independentObjectRulesFire() throws Exception {
    assertFixtureFails("independent-variable-reference.xml", "referenced variable");
  }

  @Test
  public void embeddedArfResultsHaveNoOvalResultsFailure() throws Exception {
    XMLDocument document = document("scap-14/ARF-results-oval5123.xml");
    AssessmentFactory factory = new AssessmentFactory(SCAPVersion.V1_4, null, Application.ContentType.RESULT, document);
    List<Assessment<XMLDocument>> resultsAssessments = new ArrayList<>();
    for (Assessment<XMLDocument> assessment
        : factory.createOVALSchematronAssessments(OVALVersion.V5_12_3, Application.ContentType.RESULT)) {
      if (((SchematronAssessment) assessment).getSchematron().getPath()
          .endsWith(OVALVersion.V5_12_3.getResultSchematron())) {
        resultsAssessments.add(assessment);
      }
    }
    Assert.assertEquals(1, resultsAssessments.size());
    assertNoFailure(execute(resultsAssessments, document, SCAPVersion.V1_4, Application.ContentType.COMPONENT));
  }

  @Test
  public void scap14SourceIncludesOvalDefinitionsAndSystemCharacteristics() throws Exception {
    assertScapAssessments(Application.ContentType.SOURCE, "scap-14/source_data_stream_collection_sample.xml",
        OVALVersion.V5_12_3.getDefinitionSchematron(), OVALVersion.V5_12_3.getSystemCharacteristicsSchematron());
  }

  @Test
  public void scap14ResultIncludesOvalResultsAndSystemCharacteristics() throws Exception {
    assertScapAssessments(Application.ContentType.RESULT, "scap-14/ARF-results-oval5123.xml",
        OVALVersion.V5_12_3.getResultSchematron(), OVALVersion.V5_12_3.getSystemCharacteristicsSchematron());
  }

  private static void assertScapAssessments(Application.ContentType type, String path, String... expected)
      throws Exception {
    XMLDocument document = document(path);
    AssessmentFactory factory = new AssessmentFactory(SCAPVersion.V1_4,
        type == Application.ContentType.SOURCE ? "CONFIGURATION" : null, type, document);
    List<Assessment<XMLDocument>> assessments = factory.createSCAPSchematronAssessments()
        .getExecutableAssessments(document);
    Assert.assertEquals(6, assessments.size());
    Set<String> ovalFiles = new HashSet<>();
    for (Assessment<XMLDocument> assessment : assessments) {
      String pathName = ((SchematronAssessment) assessment).getSchematron().getPath();
      String filename = pathName.substring(pathName.lastIndexOf('/') + 1);
      if (filename.startsWith("oval-")) {
        ovalFiles.add(filename);
      }
    }
    Assert.assertEquals(new HashSet<>(Arrays.asList(expected)), ovalFiles);
  }

  private static void assertFixtureFails(String filename, String message) throws Exception {
    for (OVALVersion version : VERSIONS) {
      DerivedRequirementResult result = assessFixture(filename, version);
      Assert.assertEquals(filename + " " + version, ResultStatus.FAIL, result.getStatus());
      Assert.assertTrue(filename + ": " + result.getTestResults(), result.getTestResults().stream()
          .anyMatch(test -> test.getStatus() == TestStatus.FAIL
              && test.getResultValues().toString().contains(message)));
    }
  }

  private static DerivedRequirementResult assessFixture(String filename, OVALVersion version) throws Exception {
    XMLDocument document = document(FIXTURES + filename);
    Element root = document.getJDOMDocument().getRootElement();
    // Upstream fixtures isolate Schematron and omit the generator required by version discovery.
    Element generator = new Element("generator", root.getNamespace());
    generator.addContent(new Element("schema_version", OVAL_COMMON).setText(version.getVersionString()));
    root.addContent(0, generator);
    AssessmentFactory factory = new AssessmentFactory(null, null, Application.ContentType.COMPONENT, document);
    Assessment<XMLDocument> assessment
        = factory.createStandaloneComponentSchematronAssessments(IndividualComponent.OVAL_DEF);
    List<Assessment<XMLDocument>> assessments = assessment.getExecutableAssessments(document);
    Assert.assertEquals(filename + " " + version, 3, assessments.size());
    return execute(assessments, document, null, Application.ContentType.COMPONENT);
  }

  private static DerivedRequirementResult execute(List<Assessment<XMLDocument>> assessments, XMLDocument document,
      SCAPVersion scapVersion, Application.ContentType requirementType) throws Exception {
    DefaultAssessmentResultBuilder builder = new DefaultAssessmentResultBuilder();
    builder.start();
    for (Assessment<XMLDocument> assessment : assessments) {
      assessment.execute(document, builder);
    }
    builder.end();
    String requirement = scapVersion == null
        ? SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION.getIndividualComponentReqID()
        : SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION.getSCAPReqID(scapVersion, requirementType);
    DerivedRequirementResult result = builder.build(SCAPValReqManager.getRequirements(scapVersion))
        .getDerivedRequirementResult(requirement);
    Assert.assertNotNull(requirement, result);
    Assert.assertNotEquals(requirement, ResultStatus.NOT_TESTED, result.getStatus());
    return result;
  }

  private static void assertNoFailure(DerivedRequirementResult result) {
    Assert.assertNotEquals(result.getTestResults().toString(), ResultStatus.FAIL, result.getStatus());
    for (TestResult test : result.getTestResults()) {
      Assert.assertNotEquals(test.getResultValues().toString(), TestStatus.FAIL, test.getStatus());
    }
  }

  private static XMLDocument document(String path) throws Exception {
    return new JDOMDocument(new File(new URL(CANDIDATES + path).getFile()));
  }
}
