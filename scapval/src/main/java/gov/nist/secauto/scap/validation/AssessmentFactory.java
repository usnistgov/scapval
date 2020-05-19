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

import gov.nist.secauto.decima.core.Decima;
import gov.nist.secauto.decima.core.assessment.Assessment;
import gov.nist.secauto.decima.core.assessment.AssessmentExecutor;
import gov.nist.secauto.decima.core.assessment.ConcurrentAssessmentExecutor;
import gov.nist.secauto.decima.core.document.DocumentException;
import gov.nist.secauto.decima.xml.assessment.Factory;
import gov.nist.secauto.decima.xml.assessment.schema.SchemaAssessment;
import gov.nist.secauto.decima.xml.assessment.schematron.SchematronAssessment;
import gov.nist.secauto.decima.xml.assessment.schematron.SchematronHandler;
import gov.nist.secauto.decima.xml.document.XMLDocument;
import gov.nist.secauto.decima.xml.schematron.DefaultSchematronCompiler;
import gov.nist.secauto.decima.xml.schematron.Schematron;
import gov.nist.secauto.decima.xml.schematron.SchematronCompilationException;
import gov.nist.secauto.decima.xml.schematron.SchematronCompiler;
import gov.nist.secauto.scap.validation.component.IndividualComponent;
import gov.nist.secauto.scap.validation.component.OVALVersion;
import gov.nist.secauto.scap.validation.datastream.IScapDataStream;
import gov.nist.secauto.scap.validation.datastream.SCAP11DataStream;
import gov.nist.secauto.scap.validation.datastream.SCAP12DataStream;
import gov.nist.secauto.scap.validation.datastream.SCAP13DataStream;
import gov.nist.secauto.scap.validation.decima.requirements.ComponentSchematronHandler;
import gov.nist.secauto.scap.validation.exceptions.SCAPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.xml.transform.stream.StreamSource;

public class AssessmentFactory {

  private SCAPVersion scapVersion;
  private IScapDataStream dataStream;
  private String scapUseCase;
  private Application.ContentType contentToCheckType;
  private XMLDocument xmlContentToValidate;

  private SchemaAssessment schemaAssessment;
  private Assessment<XMLDocument> schematronAssessments;

  private static final Logger log = LogManager.getLogger(AssessmentFactory.class);

  /**
   * Creates the appropriate assessments for SCAPVal validation based on specified parameters.
   * StandaloneComponent methods are for individual Component file checking. SCAP methods are for any
   * complete SCAP content. OVAL methods handle special OVAL processing used in both SCAP and
   * Standalone Components.
   *
   * @param scapVersion
   *          specified in SCAP content. If component check this should be null
   * @param scapUseCase
   *          specified in SCAP content. If component check this should be null
   * @param contentType
   *          one of three content types, not null
   * @param documentToValidate
   *          an XMLDocument object loaded from user specified target, not null
   */

  public AssessmentFactory(SCAPVersion scapVersion, String scapUseCase, Application.ContentType contentType,
      XMLDocument documentToValidate) throws DocumentException, SCAPException {
    Objects.requireNonNull(contentType, "contentType cannot be null.");
    Objects.requireNonNull(documentToValidate, "documentToValidate cannot be null.");

    // setup the assessments for SCAP content validation
    if (!contentType.equals(Application.ContentType.COMPONENT)) {
      this.scapVersion = scapVersion;
      this.scapUseCase = scapUseCase;
      this.contentToCheckType = contentType;
      this.xmlContentToValidate = documentToValidate;
      switch (scapVersion) {
      case V1_1:
        this.dataStream
            = new SCAP11DataStream(documentToValidate.getOriginalLocation().getPath(), contentType, this.scapUseCase);
        break;
      case V1_2:
        this.dataStream = new SCAP12DataStream(documentToValidate.getOriginalLocation().getPath(), contentType);
        break;
      case V1_3:
        this.dataStream = new SCAP13DataStream(documentToValidate.getOriginalLocation().getPath(), contentType);
        break;
      default:
        throw new SCAPException("Unsupported SCAP Version: " + scapVersion);
      }

      this.schemaAssessment = createSCAPSchemaAssessment();
      this.schematronAssessments = createSCAPSchematronAssessments();
    } else {
      // setup the assessments for individual component validation
      // first determine the type of component
      String namespace = documentToValidate.getJDOMDocument().getRootElement().getNamespaceURI();
      IndividualComponent component = IndividualComponent.getByNamespace(namespace);
      if (component == null) {
        throw new SCAPException("Unsupported component found. SCAPVal will validate components with namespace of: "
            + IndividualComponent.getAllComponentNamespaces());
      }
      log.debug("Discovered component namespace: " + namespace);
      this.contentToCheckType = contentType;
      this.xmlContentToValidate = documentToValidate;

      this.schemaAssessment = createStandaloneComponentSchemaAssessment(component);
      this.schematronAssessments = createStandaloneComponentSchematronAssessments(component);
    }

  }

  /**
   * Creates required SCAP schema assessment based on scapVersion. In order to validate properly all
   * schemas must be returned and included in a single assessment.
   *
   * @return SCAP SchemaAssessments based on this AssessmentFactory
   */

  protected SchemaAssessment createSCAPSchemaAssessment() throws SCAPException {

    SchemaAssessment scapSchemaAssessment = Factory.newSchemaAssessment(
        SCAPValReqManager.RequirementMappings.SCHEMA_VALIDATION.getSCAPReqID(scapVersion, contentToCheckType),
        this.dataStream.getSchemas());

    return scapSchemaAssessment;
  }

  /**
   * Creates required SCAP schematron assessments based on scapVersion.
   *
   * @return an SCAP Schematron Assessment based on this AssessmentFactory
   */
  protected Assessment<XMLDocument> createSCAPSchematronAssessments() throws SCAPException {
    List<Assessment<XMLDocument>> assessmentGroup = new LinkedList<>();

    // gather SCAP specific schematron assessments
    LinkedList<SchematronSet> schematronSets = dataStream.getSchematronSets();
    for (SchematronSet schematronSet : schematronSets) {
      SchematronAssessment schematronAssessment = Factory.newSchematronAssessment(schematronSet.getSchematron(),
          schematronSet.getSchematronPhase(), schematronSet.getSchematronHandler());
      // some schematrons have parameters which need to be set
      if (schematronSet.hasSchematronParams()) {
        schematronAssessment.addParameters(schematronSet.getSchematronParams());
      }
      assessmentGroup.add(schematronAssessment);
    }

    // gather OVAL schematron assessments
    OVALVersion ovalVersion = OVALVersion.getByString(scapVersion.getOvalSupportedVersion().getVersionString());
    if (ovalVersion == null) {
      throw new SCAPException("Unable to locate a supported oval version.");
    }
    List<Assessment<XMLDocument>> ovalSchematronAssessments
        = createOVALSchematronAssessments(ovalVersion, contentToCheckType);

    // The below utilize schematron <rule @context> so they should only fire if the specified
    // context element is found. This should prevent false positives from occurring
    assessmentGroup.addAll(ovalSchematronAssessments);
    Assessment<XMLDocument> scapSchematronAssessments = Decima.newAssessmentSequence(assessmentGroup);

    return scapSchematronAssessments;
  }

  /**
   * Creates schema assessments for Individual Component file validation.
   *
   * @param component
   *          an Individual component seperate from an SCAP check, not null
   * @return an component SchemaAssessment based on the IndividualComponent
   */
  protected SchemaAssessment createStandaloneComponentSchemaAssessment(IndividualComponent component)
      throws SCAPException {
    Objects.requireNonNull(component, "component cannot be null.");

    LinkedList<StreamSource> schemaList = new LinkedList<>();

    schemaList.add(new StreamSource("classpath:xsd/w3c/2009/01/xml.xsd"));
    schemaList.add(new StreamSource("classpath:xsd/oasis/catalog1.1.xsd"));
    schemaList.add(new StreamSource("classpath:xsd/oasis/docs/election/external/xAL.xsd"));
    schemaList.add(new StreamSource("classpath:xsd/oasis/docs/election/external/xNL.xsd"));
    switch (component) {
    case XCCDF_1_1_4:
      log.info("Discovered a XCCDF 1.1.4 file to validate");
      schemaList.add(new StreamSource("classpath:xsd/nist/xccdf/1.1/xccdf-1.1.4.xsd"));
      break;
    case XCCDF_1_2:
      log.info("Discovered a XCCDF 1.2 file to validate");
      schemaList.add(new StreamSource("classpath:xsd/nist/xccdf/1.2/xccdf_1.2.xsd"));
      schemaList.add(new StreamSource("classpath:xsd/nist/cpe/2.3/cpe-language_2.3.xsd"));
      break;
    case OCIL:
      log.info("Discovered an OCIL file to validate");
      schemaList.add(new StreamSource("classpath:xsd/nist/ocil/2.0/ocil-2.0.xsd"));
      break;
    case OVAL_DEF:
      // OVAL_DEF and OVAL_RES treated the same, fall through
    case OVAL_RES:
      log.info("Discovered an OVAL file to validate");
      // OVAL schemas are handled separately based on the schema_version specified
      return createOVALSchemaAssessment(xmlContentToValidate.getJDOMDocument().getRootElement());
    default:
      throw new SCAPException("Unsupported component found. SCAPVal will validate components with namespace of: "
          + IndividualComponent.getAllComponentNamespaces());
    }
    return new SchemaAssessment(SCAPValReqManager.RequirementMappings.SCHEMA_VALIDATION.getIndividualComponentReqID(),
        schemaList);
  }

  /**
   * Creates schematron assessments for Individual Component file validation.
   *
   * @param component
   *          an Individual component seperate from an SCAP check, not null
   * @return a component Schematron Assessment based on the IndividualComponent
   */
  protected Assessment<XMLDocument> createStandaloneComponentSchematronAssessments(IndividualComponent component) {
    Objects.requireNonNull(component, "component cannot be null.");

    Assessment<XMLDocument> xmlDocumentAssessments;
    List<Assessment<XMLDocument>> assessmentGroup = new LinkedList<>();

    Schematron xccdfSchematron;
    Schematron ocilSchematron;
    SchematronHandler schematronHandler = new ComponentSchematronHandler(
        SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION.getIndividualComponentReqID());

    try {
      switch (component) {
      case XCCDF_1_1_4:
        // No schematron avail
        break;
      case XCCDF_1_2:
        xccdfSchematron = Factory.newSchematron(new URL("classpath:rules/other/xccdf-1.2.sch"));
        // the xccdf schematron has phases to account for
        SchematronAssessment xccdfSchematronAssessment = null;
        switch (contentToCheckType) {
        case SOURCE:
          xccdfSchematronAssessment = Factory.newSchematronAssessment(xccdfSchematron, "Benchmark", schematronHandler);
          break;
        case RESULT:
          xccdfSchematronAssessment = Factory.newSchematronAssessment(xccdfSchematron, "ARF-Check", schematronHandler);
          break;
        case COMPONENT:
          xccdfSchematronAssessment = Factory.newSchematronAssessment(xccdfSchematron, "Benchmark", schematronHandler);
          break;
        default:
        }
        assessmentGroup.add(xccdfSchematronAssessment);
        break;
      case OCIL:
        ocilSchematron = Factory.newSchematron(new URL("classpath:rules/other/ocil-2.0.sch"));
        SchematronAssessment ocilSchematronAssessment
            = Factory.newSchematronAssessment(ocilSchematron, null, schematronHandler);
        assessmentGroup.add(ocilSchematronAssessment);
        break;
      case OVAL_DEF:
        // OVAL_DEF and OVAL_RES treated the same, fall through
      case OVAL_RES:
        Element ovalComponent = xmlContentToValidate.getJDOMDocument().getRootElement();
        OVALVersion ovalVersion = OVALVersion.getOVALVersion(ovalComponent);
        if (ovalVersion == null) {
          throw new SCAPException("Unable to locate a supported oval version via <schema_version> element.");
        }
        assessmentGroup.addAll(createOVALSchematronAssessments(ovalVersion, contentToCheckType));
        break;
      default:
      }

      xmlDocumentAssessments = Decima.newAssessmentSequence(assessmentGroup);

    } catch (MalformedURLException | SchematronCompilationException | SCAPException e) {
      // this should not happen if the classpath is resolvable and valid
      throw new RuntimeException(e);
    }
    return xmlDocumentAssessments;
  }

  /**
   * OVAL schematrons require extra handling. This method is used when validating SCAP content as well
   * as individual OVAL files.
   *
   * @param ovalVersion
   *          the OVALVersion for this assessment, not null
   * @param contentType
   *          the ContentType for this assessment, not null
   * @return a List of OVAL schematron Assessments
   */
  protected List<Assessment<XMLDocument>> createOVALSchematronAssessments(OVALVersion ovalVersion,
      Application.ContentType contentType) {
    Objects.requireNonNull(ovalVersion, "ovalVersion cannot be null.");
    Objects.requireNonNull(contentType, "contentType cannot be null.");

    List<Assessment<XMLDocument>> ovalComponentAssessments = new LinkedList<>();
    SchematronCompiler schematronCompiler = null;

    // all component schematron errors are handled as a single req
    // provide the correct derivedRequirementID, this could vary based on scap version or component
    String derivedRequirementID = "";
    if (contentType.equals(Application.ContentType.COMPONENT)) {
      derivedRequirementID = SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION.getIndividualComponentReqID();
    } else {
      derivedRequirementID = SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION.getSCAPReqID(scapVersion,
          Application.ContentType.COMPONENT);
    }
    try {
      schematronCompiler = new DefaultSchematronCompiler();
    } catch (SchematronCompilationException e) {
      throw new RuntimeException(e);
    }

    try {
      Schematron ovalSchematronDefinitions;
      Schematron ovalSchematronResults;
      SchematronAssessment ovalSchematronResultsAssessment;
      SchematronAssessment ovalSchematronDefinitionsAssessment;
      switch (contentType) {
      // this is an SCAP result content validation. Add the OVAL results schematron to check
      case RESULT:
        ovalSchematronResults
            = schematronCompiler.newSchematron(new URL("classpath:rules/other/" + ovalVersion.getResultSchematron()));
        ovalSchematronResultsAssessment = new SchematronAssessment(ovalSchematronResults, null,
            new ComponentSchematronHandler(derivedRequirementID));
        ovalComponentAssessments.add(ovalSchematronResultsAssessment);
        break;
      // this is an SCAP source content validation. Add the OVAL definitions schematron to check
      case SOURCE:
        ovalSchematronDefinitions = schematronCompiler
            .newSchematron(new URL("classpath:rules/other/" + ovalVersion.getDefinitionSchematron()));
        ovalSchematronDefinitionsAssessment = new SchematronAssessment(ovalSchematronDefinitions, null,
            new ComponentSchematronHandler(derivedRequirementID));
        ovalComponentAssessments.add(ovalSchematronDefinitionsAssessment);
        break;
      // this is standalone OVAL content validation. Add both the OVAL results and definitions
      // schematron to check
      case COMPONENT:
        ovalSchematronResults
            = schematronCompiler.newSchematron(new URL("classpath:rules/other/" + ovalVersion.getResultSchematron()));
        ovalSchematronResultsAssessment = new SchematronAssessment(ovalSchematronResults, null,
            new ComponentSchematronHandler(derivedRequirementID));
        ovalComponentAssessments.add(ovalSchematronResultsAssessment);
        ovalSchematronDefinitions = schematronCompiler
            .newSchematron(new URL("classpath:rules/other/" + ovalVersion.getDefinitionSchematron()));
        ovalSchematronDefinitionsAssessment = new SchematronAssessment(ovalSchematronDefinitions, null,
            new ComponentSchematronHandler(derivedRequirementID));
        ovalComponentAssessments.add(ovalSchematronDefinitionsAssessment);
        break;
      default:
      }

    } catch (MalformedURLException | SchematronCompilationException e) {
      throw new RuntimeException(e);
    }
    return ovalComponentAssessments;
  }

  /**
   * OVAL schema assessments require extra handling.
   *
   * @param ovalComponent
   *          the OVAL Element under assessment, not null
   * @return an OVAL SchemaAssessment based on the Oval Component
   */

  protected SchemaAssessment createOVALSchemaAssessment(Element ovalComponent) throws SCAPException {
    Objects.requireNonNull(ovalComponent, "ovalComponent cannot be null.");
    OVALVersion ovalVersion = OVALVersion.getOVALVersion(ovalComponent);
    if (ovalVersion == null) {
      throw new SCAPException("Unable to locate a supported oval version via <schema_version> element.");
    }
    LinkedList<StreamSource> schemaList = ovalVersion.getOVALSchemas(scapVersion, contentToCheckType);
    schemaList.add(new StreamSource("classpath:xsd/w3c/TR/xmldsig-core/xmldsig-core-schema.xsd"));
    // provide the correct derivedRequirementID, this will vary based on SCAP version or
    // individual component
    String derivedRequirementID = "";
    if (contentToCheckType.equals(Application.ContentType.COMPONENT)) {
      derivedRequirementID = SCAPValReqManager.RequirementMappings.SCHEMA_VALIDATION.getIndividualComponentReqID();
    } else {
      derivedRequirementID = SCAPValReqManager.RequirementMappings.SCHEMA_VALIDATION.getSCAPReqID(scapVersion,
          Application.ContentType.COMPONENT);
    }

    return new SchemaAssessment(derivedRequirementID, schemaList);
  }

  /**
   * Creates the Decima AssessmentExecutor after the schema/schematron assessments have been defined.
   *
   * @param executorService
   *          The Decima ExecutorService which should contain the threadPool count
   * @return The AssessmentExecutor for the SCAPVal XML content under validation.
   */
  public AssessmentExecutor<XMLDocument> newAssessmentExecutor(ExecutorService executorService) {
    Objects.requireNonNull(executorService, "executorService cannot be null.");

    List<Assessment<XMLDocument>> assessments = new ArrayList<>();

    assessments.add(schemaAssessment);
    assessments.add(schematronAssessments);
    AssessmentExecutor<XMLDocument> executor = new ConcurrentAssessmentExecutor<>(executorService, assessments);
    return executor;
  }

  public XMLDocument getXmlContentToValidate() {
    return xmlContentToValidate;
  }

}
