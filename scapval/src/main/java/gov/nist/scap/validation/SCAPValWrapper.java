/**
 * Portions of this software was developed by employees of the National Institute
 * of Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 17 United States Code Section 105, works of NIST employees are
 * not subject to copyright protection in the United States and are considered to
 * be in the public domain. Permission to freely use, copy, modify, and distribute
 * this software and its documentation without fee is hereby granted, provided that
 * this notice and disclaimer of warranty appears in all copies.
 * <p>
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

import gov.nist.decima.core.assessment.AssessmentException;
import gov.nist.decima.core.document.DocumentException;
import gov.nist.decima.core.requirement.RequirementsParserException;
import gov.nist.decima.xml.schematron.SchematronCompilationException;
import gov.nist.scap.validation.exceptions.ConfigurationException;
import gov.nist.scap.validation.exceptions.SCAPException;
import org.apache.commons.cli.ParseException;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *  A wrapper class to run SCAPVal programmatically
 */
public class SCAPValWrapper {
  /**
   * Runs SCAPVal with the required parameters per CLI usage.
   * A Builder is also provided for common usages.
   *
   * @param args a String array of arguments
   * @return the AssessmentResults which includes a Collection of the results
   */
  public static SCAPValAssessmentResults run(String[] args) throws IOException,
      ConfigurationException, URISyntaxException, AssessmentException, JDOMException,
      SchematronCompilationException, SAXException, RequirementsParserException, SCAPException,
      TransformerException, ParseException, DocumentException {
    Objects.requireNonNull(args, "args can not be null.");

    try {
      Application application = new Application();
      return application.runProgrammatic(args);
    } catch (ConfigurationException e) {
      //this needs to be handled here since it relies on the instantiated Application for its
        // error message
      throw new RuntimeException(e.getMessage());
    }
  }

  public static class Builder {
    private String submissionFileLocation;
    private String submissionDirLocation;
    private String maxDownloadSize = "30"; // default max download size in MiB
    private boolean isOnline = false;
    private Application.ContentType submissionType;
    private SCAPVersion scapVersion;
    private String useCase;
    private boolean debugMessageLevel = false;

    public Builder submissionFileLocation(String location) {
      submissionFileLocation = location;
      return this;
    }

    public Builder submissionDirLocation(String location) {
      submissionDirLocation = location;
      return this;
    }

    public Builder maxDownloadSize(String downloadSize) {
      maxDownloadSize = downloadSize;
      return this;
    }

    public Builder isOnline(boolean b) {
      isOnline = b;
      return this;
    }

    public Builder submissionType(Application.ContentType contentType) {
      submissionType = contentType;
      return this;
    }

    public Builder scapVersion(SCAPVersion scapVer) {
      scapVersion = scapVer;
      return this;
    }

    public Builder useCase(String setUseCase) {
      useCase = setUseCase.toUpperCase();
      return this;
    }

    public Builder debugMessageLevel(boolean b) {
      debugMessageLevel = b;
      return this;
    }

    public SCAPValAssessmentResults run() throws Exception {
      ArrayList<String> args = new ArrayList<>();

      if (this.submissionType == null || (this.submissionFileLocation == null && this
          .submissionDirLocation == null)) {
        throw new Exception("submissionType and submissionFileLocation or submissionDirLocation " +
            "must be specified");
      }

      if (this.submissionFileLocation != null && this.submissionDirLocation != null) {
        throw new Exception("submissionFileLocation or submissionDirLocation must be specified");
      }

      switch (this.submissionType) {
        case SOURCE:
          if (submissionFileLocation != null) {
            args.add("-file");
            args.add(submissionFileLocation);
          } else if (submissionDirLocation != null) {
            args.add("-dir");
            args.add(submissionDirLocation);
          }
          if (this.scapVersion == null) {
            throw new Exception("scapVersion must be specified for SOURCE or RESULT content.");
          }
          args.add("-scapversion");
          args.add(scapVersion.getVersion());
          break;
        case RESULT:
          args.add("-resultfile");
          args.add(submissionFileLocation);
          if (this.scapVersion == null) {
            throw new Exception("scapVersion must be specified for SOURCE or RESULT content.");
          }
          args.add("-scapversion");
          args.add(scapVersion.getVersion());
          break;
        case COMPONENT:
          args.add("-componentfile");
          args.add(submissionFileLocation);
          break;
      }

      if (useCase != null) {
        args.add("-usecase");
        args.add(useCase);
      }

      if (isOnline) {
        args.add("-online");
      }

      args.add("-maxsize");
      args.add(maxDownloadSize);

      if (debugMessageLevel) {
        args.add("-debug");
      }

      return SCAPValWrapper.run(args.toArray(new String[0]));
    }
  }

}
