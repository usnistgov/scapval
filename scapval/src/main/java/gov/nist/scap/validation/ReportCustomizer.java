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
package gov.nist.scap.validation;

import gov.nist.decima.core.document.DocumentException;
import gov.nist.decima.xml.document.JDOMDocument;
import gov.nist.scap.validation.component.IndividualComponent;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

/**
 * This modifies the .XSL file used to create the HTML validation report.
 * Depending on the validation type, this modifies the .XSL for the desired appearance.
 */

public class ReportCustomizer {
  File scapXSLTemplateExtention;
  SCAPValAssessmentResults scapValAssessmentResults;

  public ReportCustomizer(File XSLTemplateExtension, SCAPValAssessmentResults
      scapValAssessmentResults) {
    Objects.requireNonNull(XSLTemplateExtension, "XSLTemplateExtension cannot be null");
    Objects.requireNonNull(scapValAssessmentResults, "scapValAssessmentResults cannot be null");

    this.scapXSLTemplateExtention = XSLTemplateExtension;
    this.scapValAssessmentResults = scapValAssessmentResults;
  }

  /**
   *
   * @param contentType for this validation, not null
   * @param scapVersion can be null in the case of component check
   * @param individualComponent the IndividualComponent if a component check. Otherwise can be
   *                            null
   */
  public void customize(Application.ContentType contentType, SCAPVersion scapVersion,
                        IndividualComponent individualComponent) {
    Objects.requireNonNull(contentType, "contentType can not be null");

    String scapVersionString = null;
    String scapvalVersionString = Messages.getVersion();
    String contentTypeString = null;
    String headerString = null;

    Element ULElement = new Element("ul");
    //String notesString = "<ul>";
    for (String note : this.scapValAssessmentResults.getAssessmentNotes()) {
      ULElement.addContent(new Element("li").addContent(note));
    }
    // notesString += "</ul>";
    if (scapVersion != null) {
      switch (scapVersion) {
        case V1_1:
          scapVersionString = "1.1";
          break;
        case V1_2:
          scapVersionString = "1.2";
          break;
        case V1_3:
          scapVersionString = "1.3";
          break;
      }
    }
    switch (contentType) {
      case SOURCE:
        headerString = "SCAP";
        contentTypeString = "SCAP " + scapVersionString + " Source";
        break;
      case RESULT:
        headerString = "SCAP";
        contentTypeString = "SCAP " + scapVersionString + " Result";
        break;
      case COMPONENT:
        headerString = "Component";
        contentTypeString = individualComponent.getName() + " File";
        break;
    }

    JDOMDocument scapvalXSLTemplateDoc;
    try {
      scapvalXSLTemplateDoc = new JDOMDocument(scapXSLTemplateExtention);
    } catch (DocumentException | FileNotFoundException e) {
      throw new RuntimeException("Problem loading: " + scapXSLTemplateExtention.getPath());
    }

    Element originalRootElement = scapvalXSLTemplateDoc.getJDOMDocument(true).getRootElement();

    Iterator<Element> descendants = originalRootElement.getDescendants(new ElementFilter());
    Element headerTitleElement = null;
    Element contentTypeElement = null;
    Element scapvalVersionElement = null;
    Element notesElement = null;
    while (descendants.hasNext()) {
      Element element = descendants.next();
      if (element.getAttributeValue("id") != null && element.getAttributeValue("id").equals
          ("header-title")) {
        headerTitleElement = element;
      } else if (element.getAttributeValue("id") != null && element.getAttributeValue("id")
          .equals("content-type-title")) {
        contentTypeElement = element;
      } else if (element.getAttributeValue("id") != null && element.getAttributeValue("id")
          .equals("scapval-version")) {
        scapvalVersionElement = element;
      } else if (element.getAttributeValue("id") != null && element.getAttributeValue("id")
          .equals("notes-content")) {
        notesElement = element;
      }
    }

    headerTitleElement.setContent(new Text(headerString));
    contentTypeElement.setContent(new Text(contentTypeString));
    scapvalVersionElement.setContent(new Text(scapvalVersionString));
    notesElement.setContent(ULElement);

    JDOMDocument newXSLTemplateFile = null;
    try {
      //create the updated XSL Document
      newXSLTemplateFile = new JDOMDocument(scapvalXSLTemplateDoc.getJDOMDocument().setContent
          (originalRootElement.detach()), scapvalXSLTemplateDoc.getOriginalLocation());
      //overwrite the existing XSL file
      //File existingXSL = new File(scapvalXSLTemplateURL.openConnection().getURL().getPath());
      newXSLTemplateFile.copyTo(scapXSLTemplateExtention);

    } catch (DocumentException | IOException e) {
      throw new RuntimeException("There was a problem updating the XSL for HTML report generation" +
          " -" + e.getMessage());
    }
  }
}
