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
package gov.nist.scap.validation.utils;

import gov.nist.decima.xml.document.XMLDocument;
import gov.nist.scap.validation.SCAPVersion;
import gov.nist.scap.validation.exceptions.SCAPException;
import org.jdom2.Element;
import org.jdom2.filter.Filters;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.util.List;
import java.util.Objects;

/** Provides SCAP processing related helper methods */
public class SCAPUtils {
  public static List<Element> getOVALComponentsFromSCAPContent(XMLDocument scapContent,
                                                               SCAPVersion scapVersion) throws
      SCAPException {
    Objects.requireNonNull(scapContent, "scapContent cannot be null.");
    Objects.requireNonNull(scapVersion, "scapVersion cannot be null.");

    //the SCAP version will determine where to pull the oval components from
    String getOvalComponentXPATH = "";
    switch (scapVersion) {
      case V1_1:
        //using the composite doc for 1.1 content, then pulling out oval components
        getOvalComponentXPATH = "//*[namespace-uri()='http://oval.mitre" +
            ".org/XMLSchema/oval-common-5' and local-name()" +
            "='schema_version']/ancestor-or-self::*:check-system-content/*";
        break;
      case V1_2:
        getOvalComponentXPATH = "//*[namespace-uri()='http://oval.mitre" +
            ".org/XMLSchema/oval-common-5' and local-name()" +
            "='schema_version']/ancestor-or-self::*[namespace-uri()='http://scap.nist" +
            ".gov/schema/scap/source/1.2' and local-name()='component']/*";
        break;
      case V1_3:
        //SCAP 1.3 still uses the 1.2 namespce.
        getOvalComponentXPATH = "//*[namespace-uri()='http://oval.mitre" +
            ".org/XMLSchema/oval-common-5' and local-name()" +
            "='schema_version']/ancestor-or-self::*[namespace-uri()='http://scap.nist" +
            ".gov/schema/scap/source/1.2' and local-name()='component']/*";
        break;
    }

    List<Element> OVALElements = null;
    try {
      OVALElements = scapContent.newXPathEvaluator().evaluate(getOvalComponentXPATH, Filters
          .element());
    } catch (XPathExpressionException | XPathFactoryConfigurationException e) {
      //no results found, returning null
    }
    return OVALElements;
  }

  public static List<Element> getOVALResultsFromSCAPContent(XMLDocument scapContent, SCAPVersion
      scapVersion) throws SCAPException {
    Objects.requireNonNull(scapContent, "scapContent cannot be null.");
    Objects.requireNonNull(scapVersion, "scapVersion cannot be null.");

    //the SCAP version will determine where to pull the oval components from
    String getOvalResultsXPATH = "";
    switch (scapVersion) {
      case V1_1:
        getOvalResultsXPATH = "//*[namespace-uri()='http://oval.mitre" +
            ".org/XMLSchema/oval-common-5' and local-name()" +
            "='schema_version']/ancestor-or-self::*:oval_results";
        break;
      case V1_2:
        getOvalResultsXPATH = "//*[namespace-uri()='http://oval.mitre" +
            ".org/XMLSchema/oval-common-5' and local-name()" +
            "='schema_version']/ancestor-or-self::*[namespace-uri()='http://scap.nist" +
            ".gov/schema/asset-reporting-format/1.1' and local-name()='content']/*:oval_results";
        break;
      case V1_3:
        getOvalResultsXPATH = "//*[namespace-uri()='http://oval.mitre" +
            ".org/XMLSchema/oval-common-5' and local-name()" +
            "='schema_version']/ancestor-or-self::*[namespace-uri()='http://scap.nist" +
            ".gov/schema/asset-reporting-format/1.1' and local-name()='content']/*:oval_results";
        break;
    }

    List<Element> OVALElements = null;
    try {
      OVALElements = scapContent.newXPathEvaluator().evaluate(getOvalResultsXPATH, Filters
          .element());
    } catch (XPathExpressionException | XPathFactoryConfigurationException e) {
      //no results found, returning null
    }
    return OVALElements;
  }
}
