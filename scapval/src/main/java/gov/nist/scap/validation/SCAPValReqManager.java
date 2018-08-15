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

package gov.nist.scap.validation;

import gov.nist.decima.core.requirement.DefaultRequirementsManager;
import gov.nist.decima.core.requirement.MutableRequirementsManager;
import gov.nist.decima.core.requirement.RequirementsParserException;
import gov.nist.decima.xml.requirement.XMLRequirementsParser;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import javax.xml.transform.stream.StreamSource;

/**
 * Handles requirements related tasks of loading and processing SCAPVal *requirements.xml files.
 * Provides a RequirementMappings enum for clarification of certain requirements.
 */
public class SCAPValReqManager {
  private static final String REQUIREMENTS_SCHEMA = "classpath:scapval-xsd/scapval-requirements-ext.xsd";
  private static final String COMPONENTS_REQUIREMENTS
      = "classpath:requirements/scapval-individual-component-requirements.xml";
  private static final String SCAP11_REQUIREMENTS = "classpath:requirements/scapval-scap-1.1-requirements.xml";
  private static final String SCAP12_REQUIREMENTS = "classpath:requirements/scapval-scap-1.2-requirements.xml";
  private static final String SCAP13_REQUIREMENTS = "classpath:requirements/scapval-scap-1.3-requirements.xml";

  /**
   * Collects, parses, and provides the SCAPVal requirements defined in *requirements.xml files
   * Utilizes Decima's requirements features.
   *
   * @param scapVersion
   *          can be null if a component check
   * @return a requirements manager with parsed reqs
   */
  public static MutableRequirementsManager getRequirements(SCAPVersion scapVersion) {
    MutableRequirementsManager requirementsManager = new DefaultRequirementsManager();
    try {
      XMLRequirementsParser parser
          = new XMLRequirementsParser(Collections.singletonList(new StreamSource(REQUIREMENTS_SCHEMA)));

      if (scapVersion == null) {
        // if scap verison is null, this must be a component file check
        requirementsManager.load(new URL(COMPONENTS_REQUIREMENTS), parser);
      } else {
        switch (scapVersion) {
        case V1_1:
          requirementsManager.load(new URL(SCAP11_REQUIREMENTS), parser);
          break;
        case V1_2:
          requirementsManager.load(new URL(SCAP12_REQUIREMENTS), parser);
          break;
        case V1_3:
          requirementsManager.load(new URL(SCAP13_REQUIREMENTS), parser);
          break;
        default:
        }
      }

    } catch (IOException | RequirementsParserException | URISyntaxException | JDOMException | SAXException e) {
      throw new RuntimeException(e);
    }
    return requirementsManager;
  }

  /**
   * Because several requirements have overlap, along with differences from legacy SCAPVal
   * requirements implementation, it may not be clear exactly which requirements IDs are responsible
   * for Schema and Schematron validation. These mappings store the requirement ID that will be used
   * in SCAPVal 1.3. This will be primarily used to define which Requirement ID will be reported on a
   * Component schematron error since components have no embedded requirement IDs on their own. For
   * SCAP Source/Result Schematrons, their embedded ID will be reported.
   *
   *
   * 1. Individual Component Validation 2. SCAP 1.1 Contained Components 3. SCAP 1.1 Source Content 4.
   * SCAP 1.1 Result Content 5. SCAP 1.2 Contained Components 6. SCAP 1.2 Source Content 7. SCAP 1.2
   * Result Content 8. SCAP 1.3 Contained Components 9. SCAP 1.3 Source Content 10.SCAP 1.3 Result
   * Content
   */
  public enum RequirementMappings {

    SCHEMA_VALIDATION(
        new String[] { "COMP-1-1", // Component
            "A-10-1", "A-10-1", "A-10-1", // SCAP 1.1
            "A-10-1", "SRC-329-1", "RES-363-1", // SCAP 1.2
            "A-10-1", "SRC-329-1", "RES-363-1" }), // SCAP 1.3

    SCHEMATRON_VALIDATION(
        new String[] { "COMP-1-2", // Component
            "A-14-1", "A-14-1", "A-14-1", // SCAP 1.1
            "SRC-330-3", "SRC-330-1", "SRC-330-1", // SCAP 1.2
            "SRC-330-3", "SRC-330-1", "RES-363-2" }); // SCAP 1.3

    private String[] reqID;

    RequirementMappings(String[] reqID) {
      this.reqID = reqID;
    }

    public String getIndividualComponentReqID() {
      return this.reqID[0];
    }

    /**
     * Returns the appropriate requirement ID from this enum based on SCAP version the content type.
     *
     * @param scapVersion
     *          the applicable scapVersion
     * @param contentType
     *          the applicable contentType for this req
     * @return the requirement ID as a String
     */
    public String getSCAPReqID(SCAPVersion scapVersion, Application.ContentType contentType) {
      switch (scapVersion) {
      case V1_1:
        switch (contentType) {
        case COMPONENT:
          return this.reqID[1];
        case SOURCE:
          return this.reqID[2];
        case RESULT:
          return this.reqID[3];
        default:
          return null;
        }
      case V1_2:
        switch (contentType) {
        case COMPONENT:
          return this.reqID[4];
        case SOURCE:
          return this.reqID[5];
        case RESULT:
          return this.reqID[6];
        default:
          return null;
        }
      case V1_3:
        switch (contentType) {
        case COMPONENT:
          return this.reqID[7];
        case SOURCE:
          return this.reqID[8];
        case RESULT:
          return this.reqID[9];
        default:
          return null;
        }
      default:
        return null;
      }
    }

  }
}
