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

package gov.nist.secauto.scap.validation.datastream;

import gov.nist.secauto.decima.xml.assessment.Factory;
import gov.nist.secauto.decima.xml.assessment.schematron.SchematronHandler;
import gov.nist.secauto.decima.xml.schematron.Schematron;
import gov.nist.secauto.decima.xml.schematron.SchematronCompilationException;
import gov.nist.secauto.scap.validation.Application;
import gov.nist.secauto.scap.validation.SCAPValReqManager;
import gov.nist.secauto.scap.validation.SCAPVersion;
import gov.nist.secauto.scap.validation.SchematronSet;
import gov.nist.secauto.scap.validation.component.OVALVersion;
import gov.nist.secauto.scap.validation.decima.requirements.ComponentSchematronHandler;
import gov.nist.secauto.scap.validation.decima.requirements.SCAPSchematronHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.stream.StreamSource;

/**
 * Provides SCAP 1.4 specific schema/schematron items required for SCAPVal validation.
 */
public class SCAP14DataStream implements IScapDataStream {

    // TODO GK Check all the values
    private String id;
    private SCAPVersion scapVersion;
    private Application.ContentType contentType;
    private static final String SOURCE_SCHEMATRON_LOCATION = "classpath:rules/scap/source-data-stream-1.4.sch";
    private static final String RESULT_SCHEMATRON_LOCATION = "classpath:rules/scap/result-data-stream-1.4.sch";
    private static final String TMSAD_SCHEMATRON_LOCATION = "classpath:rules/other/tmsad-1.0.sch";
    // all schemas for 1.4 as specified in the 800-126Ar4 Annex Document
    private static final String[] Schemas = { "classpath:xsd/nist/cpe/2.3/cpe-language_2.3.xsd",
            "classpath:xsd/nist/cpe/2.3/cpe-dictionary_2.3.xsd",
            "classpath:xsd/nist/cpe/2.3/cpe-dictionary-extension_2.3.xsd", "classpath:xsd/nist/cpe/2.3/cpe-naming_2.3.xsd",
            "classpath:xsd/nist/ocil/2.0/ocil-2.0.xsd", "classpath:xsd/nist/scap/1.2/scap-constructs_1.2.xsd",
            "classpath:xsd/nist/scap/1.3/scap-source-data-stream_1.3.xsd", "classpath:xsd/iso/19770-2-SWID/schema.xsd",
            "classpath:xsd/nist/tmsad/1.0/tmsad_1.0.xsd", "classpath:xsd/nist/xccdf/1.2/xccdf_1.2.xsd" };
    private static final String[] AdditionalResultSchemas
            = { "classpath:xsd/nist/asset-identification/1.1/asset-identification_1.1.0.xsd",
            "classpath:xsd/nist/asset-reporting-format/1.1/asset-reporting-format_1.1.0.xsd" };

    /**
     * The class constructor requires a contentType.
     *
     * @param id
     *          a specified id for this datastream, can be null
     * @param contentType
     *          the ContentType within this DS
     */
    public SCAP14DataStream(String id, Application.ContentType contentType) {
        Objects.requireNonNull(contentType, "contentType cannot be null.");
        this.id = id;
        this.scapVersion = SCAPVersion.V1_4;
        this.contentType = contentType;
    }

    /**
     * Get the datastream's identifier.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    @Override
    public SCAPVersion getScapVersion() {
        return scapVersion;
    }

    @Override
    public LinkedList<SchematronSet> getSchematronSets() {
        LinkedList<SchematronSet> schematronSetList = new LinkedList<>();
        // populate the SchematronSets with everything required for creation of a
        // SchematronAssessment
        try {
            if (contentType.equals(Application.ContentType.SOURCE)) {
                Schematron schematron = Factory.newSchematron(new URL(SOURCE_SCHEMATRON_LOCATION));
                SchematronHandler schematronHandler = new SCAPSchematronHandler(schematron);

                // create necessary params for SCAP Source Schematrons
                Map<String, String> sourceParams = new HashMap<>();
                sourceParams.put("datafiles_directory", "classpath:data_feeds");

                // phases are not required for source schematron rules
                SchematronSet schematronSet = new SchematronSet(schematron, schematronHandler, null, sourceParams);
                schematronSetList.add(schematronSet);

            } else if (contentType.equals(Application.ContentType.RESULT)) {
                Schematron schematron = Factory.newSchematron(new URL(RESULT_SCHEMATRON_LOCATION));
                SchematronHandler schematronHandler = new SCAPSchematronHandler(schematron);
                // there are no params and phases are not required for Result schematron rules
                SchematronSet schematronSet = new SchematronSet(schematron, schematronHandler, null, null);
                schematronSetList.add(schematronSet);
            }

            // load the tmsad schematron (only for version 1.2+)
            Schematron tmsadSchematron = Factory.newSchematron(new URL(TMSAD_SCHEMATRON_LOCATION));
            SchematronHandler tmsadSchematronHandler
                    = new ComponentSchematronHandler(SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION
                    .getSCAPReqID(scapVersion, Application.ContentType.COMPONENT));
            SchematronSet tmsadSchematronSet = new SchematronSet(tmsadSchematron, tmsadSchematronHandler, null, null);
            schematronSetList.add(tmsadSchematronSet);

            // handle the rest of the component specific schematrons. Historically xccdf-1.2.sch and
            // ocil-2.0.sch
            // have been used for all content and versions
            Schematron xccdfSchematron = Factory.newSchematron(new URL("classpath:rules/other/xccdf-1.2.sch"));
            SchematronHandler xccdfSchematronHandler
                    = new ComponentSchematronHandler(SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION
                    .getSCAPReqID(scapVersion, Application.ContentType.COMPONENT));
            // XCCDF schematron requires a phase
            String xccdfPhase = contentType.equals(Application.ContentType.RESULT) ? "ARF-Check" : "Benchmark";
            SchematronSet xccdfSchematronSet = new SchematronSet(xccdfSchematron, xccdfSchematronHandler, xccdfPhase, null);
            schematronSetList.add(xccdfSchematronSet);

            Schematron ocilSchematron = Factory.newSchematron(new URL("classpath:rules/other/ocil-2.0.sch"));
            SchematronHandler ocilSchematronHandler
                    = new ComponentSchematronHandler(SCAPValReqManager.RequirementMappings.SCHEMATRON_VALIDATION
                    .getSCAPReqID(scapVersion, Application.ContentType.COMPONENT));
            SchematronSet ocilSchematronSet = new SchematronSet(ocilSchematron, ocilSchematronHandler, null, null);
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

        // add the additional required shemas for results
        if (contentType.equals(Application.ContentType.RESULT)) {
            for (String schema : AdditionalResultSchemas) {
                schemaList.add(new StreamSource(schema));
            }
        }

        // get and load all the appropriate OVAL schemas
        OVALVersion ovalVersion = OVALVersion.getByString(scapVersion.getOvalSupportedVersion().getVersionString());
        schemaList.addAll(ovalVersion.getOVALSchemas(scapVersion, contentType));

        return schemaList;
    }

}
