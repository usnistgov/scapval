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
package gov.nist.scap.validation.datastream;

import gov.nist.decima.xml.assessment.schematron.SchematronHandler;
import gov.nist.decima.xml.schematron.Schematron;
import gov.nist.decima.xml.schematron.SchematronCompilationException;
import gov.nist.scap.validation.Application;
import gov.nist.scap.validation.SCAPValReqManager;
import gov.nist.scap.validation.SCAPVersion;
import gov.nist.scap.validation.SchematronSet;
import gov.nist.scap.validation.component.OVALVersion;
import gov.nist.scap.validation.requirements.decima.ComponentSchematronHandler;
import gov.nist.scap.validation.requirements.decima.SCAPSchematronHandler;

import javax.xml.transform.stream.StreamSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static gov.nist.decima.xml.DecimaXML.newSchematron;

/**
 * Provides SCAP 1.1 specific schema/schematron items required for SCAPVal validation.
 */
public class SCAP11DataStream implements ISCAPDataStream {

  private String id;
  private String useCase;
  private SCAPVersion scapVersion;
  private Application.ContentType contentType;
  private static final String SOURCE_SCHEMATRON_LOCATION = "classpath:rules/scap/source-data-stream-1.1.sch";
  private static final String RESULT_SCHEMATRON_LOCATION = "classpath:rules/scap/result-data-stream-1.1.sch";

  private static final String[] Schemas = {
          "classpath:xsd/nist/cpe/2.2/cpe-language_2.2a.xsd",
          "classpath:xsd/nist/cpe/2.2/cpe-dictionary_2.2.xsd",
          "classpath:xsd/nist/ocil/2.0/ocil-2.0.xsd",
          "classpath:xsd/nist/xccdf/1.1/xccdf-1.1.4.xsd",
          "classpath:xsd/nist/scap/1.1/scap-data-stream_0.2.xsd"
  };
  private static final String[] AdditionalResultSchemas = {
          "classpath:scapval-xsd/scap-result_1.1.xsd",
  };

  public SCAP11DataStream(String id, Application.ContentType contentType, String useCase) {
    Objects.requireNonNull(contentType, "contentType cannot be null.");
    Objects.requireNonNull(useCase, "useCase cannot be null.");
    this.id = id;
    this.useCase = useCase;
    this.scapVersion = SCAPVersion.V1_1;
    this.contentType = contentType;
  }

  @Override
  public SCAPVersion getScapVersion() {
    return scapVersion;
  }

  public LinkedList<SchematronSet> getSchematronSets() {
    LinkedList<SchematronSet> schematronSetList = new LinkedList<>();
    //populate the SchematronSets with everything required for creation of a SchematronAssessment
    try {
      if (contentType.equals(Application.ContentType.SOURCE)) {
        Schematron schematron = newSchematron(new URL(SOURCE_SCHEMATRON_LOCATION));
        SchematronHandler schematronHandler = new SCAPSchematronHandler(schematron);

        // create necessary params for SCAP Source Schematron
        Map<String, String> sourceParams = new HashMap<>();
        sourceParams.put("datafiles_directory", "classpath:data_feeds");

        // scap 11 Source Assessment, usecase is used to specify a phase which enables proper
          // schematron asserts
        String phase = this.useCase;

        // only scap 1.1 source schematron checks utilizes phases
        SchematronSet schematronSet = new SchematronSet(schematron, schematronHandler, phase,
            sourceParams);
        schematronSetList.add(schematronSet);

      } else if (contentType.equals(Application.ContentType.RESULT)) {
        Schematron schematron = newSchematron(new URL(RESULT_SCHEMATRON_LOCATION));
        SchematronHandler schematronHandler = new SCAPSchematronHandler(schematron);

        //there are no params and phases are not required for Result schematron rules
        SchematronSet schematronSet = new SchematronSet(schematron, schematronHandler, null, null);
        schematronSetList.add(schematronSet);
      }

      // handle the rest of the component specific schematrons. Historically xccdf-1.2.sch and
        // ocil-2.0.sch
      // have been used for all content and versions
      Schematron xccdfSchematron = newSchematron(new URL("classpath:rules/other/xccdf-1.2.sch"));
      SchematronHandler xccdfSchematronHandler = new ComponentSchematronHandler(SCAPValReqManager
          .RequirementMappings.SCHEMATRON_VALIDATION.getSCAPReqID(scapVersion, Application
              .ContentType.COMPONENT));
      //XCCDF schematron requires a phase
      String XCCDFPhase = contentType.equals(Application.ContentType.RESULT) ? "ARF-Check" :
          "Benchmark";
      SchematronSet xccdfSchematronSet = new SchematronSet(xccdfSchematron,
          xccdfSchematronHandler, XCCDFPhase, null);
      schematronSetList.add(xccdfSchematronSet);

      Schematron ocilSchematron = newSchematron(new URL("classpath:rules/other/ocil-2.0.sch"));
      SchematronHandler ocilSchematronHandler = new ComponentSchematronHandler(SCAPValReqManager
          .RequirementMappings.SCHEMATRON_VALIDATION.getSCAPReqID(scapVersion, Application
              .ContentType.COMPONENT));
      SchematronSet ocilSchematronSet = new SchematronSet(ocilSchematron, ocilSchematronHandler,
          null, null);
      schematronSetList.add(ocilSchematronSet);

    } catch (MalformedURLException e) {
      throw new RuntimeException("Unable to read a required Schematron File: " + e.getMessage());
    } catch (SchematronCompilationException e) {
      throw new RuntimeException("Problem compiling a required Schematron File: " + e.getMessage());
    }

    return schematronSetList;
  }

  @Override
  public LinkedList<StreamSource> getSchemas() {
    LinkedList<StreamSource> schemaList = new LinkedList<>();
    for (String schema : Schemas) {
      schemaList.add(new StreamSource(schema));
    }

    //add the additional required shemas for results
    if (contentType.equals(Application.ContentType.RESULT)) {
      for (String schema : AdditionalResultSchemas) {
        schemaList.add(new StreamSource(schema));
      }
    }

    //get and load all the appropriate OVAL schemas
    OVALVersion ovalVersion = OVALVersion.getByString(scapVersion.getOvalSupportedVersion()
        .getVersionString());
    schemaList.addAll(ovalVersion.getOVALSchemas(scapVersion, contentType));

    return schemaList;
  }

}
