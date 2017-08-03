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

import gov.nist.decima.core.assessment.AssessmentException;
import gov.nist.decima.core.document.DocumentException;
import gov.nist.decima.core.requirement.RequirementsParserException;
import gov.nist.decima.xml.schematron.SchematronCompilationException;
import gov.nist.scap.validation.Application.ContentType;
import gov.nist.scap.validation.exceptions.ConfigurationException;
import gov.nist.scap.validation.exceptions.SCAPException;
import org.apache.commons.cli.ParseException;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *  A wrapper class to run SCAPVal programmatically.
 */
public class SCAPValWrapper {
    /**
     * Runs SCAPVal with the provided arguments per CLI usage.
     * A Builder is provided to help create the arguments.
     *
     * @param args a String array of arguments
     * @return the AssessmentResults which includes a Collection of the results
     */
    public static SCAPValAssessmentResults run(String[] args, URI bootstrapLocation) throws IOException, ConfigurationException,
            URISyntaxException, AssessmentException, JDOMException, SchematronCompilationException, SAXException, RequirementsParserException,
            SCAPException, TransformerException, ParseException, DocumentException {
        Objects.requireNonNull(args, "args can not be null.");

            Application application = new Application();
            return application.runProgrammatic(args, bootstrapLocation);
    }

    /**
     * Used to create commonly used arguments and then run SCAPVal programmatically
     */
    public static class Builder {
        private String submissionFilePath;
        private String submissionDirPath;
        private String maxDownloadSize = "30"; // default max download size in MiB
        private String useCase;
        private String reportDirPath;
        private URI bootstrapLocation = null;
        private ContentType submissionType;
        private SCAPVersion scapVersion;
        private boolean isOnline = false;
        private boolean debugMessageLevel = false;

        /**
         * @param path of the submission file
         * @return the Builder
         */
        public Builder submissionFileLocation(String path) {
            submissionFilePath = path;
            return this;
        }

        /**
         * @param path of the submission directory
         * @return the Builder
         */
        public Builder submissionDirLocation(String path) {
            submissionDirPath = path;
            return this;
        }

        /**
         * @param downloadSize in (MiB), defaulted to 30 if not specified
         * @return the Builder
         */
        public Builder maxDownloadSize(String downloadSize) {
            maxDownloadSize = downloadSize;
            return this;
        }

        /**
         * @param online how SCAPVal should run online/offline
         * @return the Builder
         */
        public Builder isOnline(boolean online) {
            isOnline = online;
            return this;
        }

        /**
         * @param contentType the type of content as a SOURCE, RESULT, or COMPONENT
         * @return the Builder
         */
        public Builder submissionType(Application.ContentType contentType) {
            submissionType = contentType;
            return this;
        }

        /**
         * @param version the SCAPVersion under validation
         * @return the Builder
         */
        public Builder scapVersion(SCAPVersion version) {
            scapVersion = version;
            return this;
        }

        /**
         * @param setUseCase the Usecase as a string if this is an SCAP check
         * @return the Builder
         */
        public Builder useCase(String setUseCase) {
            useCase = setUseCase.toUpperCase();
            return this;
        }

        /**
         * @param path the directory to write out the optional validation XML/HTML reports
         * @return the Builder
         */
        public Builder reportOutputDirectory(String path) {
            reportDirPath = path;
            return this;
        }

        /**
         * @param location the URI where the HTML report's boostrap dependency resides
         * @return the Builder
         */
        public Builder bootstrapLocation(URI location) {
            bootstrapLocation = location;
            return this;
        }

        /**
         * @param level set to true for debug message level
         * @return the Builder
         */
        public Builder debugMessageLevel(boolean level) {
            debugMessageLevel = level;
            return this;
        }

        public SCAPValAssessmentResults run() throws Exception {
            ArrayList<String> args = new ArrayList<>();

            if (this.submissionType == null || (this.submissionFilePath == null && this.submissionDirPath == null)) {
                throw new Exception("submissionType and submissionFilePath or submissionDirPath " + "must be specified");
            }

            if (this.submissionFilePath != null && this.submissionDirPath != null) {
                throw new Exception("submissionFilePath or submissionDirPath must be specified");
            }

            switch (this.submissionType) {
                case SOURCE:
                    if (submissionFilePath != null) {
                        args.add("-file");
                        args.add(submissionFilePath);
                    } else if (submissionDirPath != null) {
                        args.add("-dir");
                        args.add(submissionDirPath);
                    }
                    if (this.scapVersion == null) {
                        throw new Exception("scapVersion must be specified for SOURCE or RESULT content.");
                    }
                    args.add("-scapversion");
                    args.add(scapVersion.getVersion());
                    break;
                case RESULT:
                    args.add("-resultfile");
                    args.add(submissionFilePath);
                    if (this.scapVersion == null) {
                        throw new Exception("scapVersion must be specified for SOURCE or RESULT content.");
                    }
                    args.add("-scapversion");
                    args.add(scapVersion.getVersion());
                    break;
                case COMPONENT:
                    args.add("-componentfile");
                    args.add(submissionFilePath);
                    break;
            }

            if (useCase != null) {
                args.add("-usecase");
                args.add(useCase);
            }

            if (isOnline) {
                args.add("-online");
            }

            //to specify where result/report files are written
            //if null, they will not be created
            if (reportDirPath != null) {
                //the xml result file
                args.add("-valresultfile");
                args.add(reportDirPath + "validation-result.xml");

                //the html report file
                args.add("-valreportfile");
                args.add(reportDirPath + "validation-report.html");
            }

            args.add("-maxsize");
            args.add(maxDownloadSize);

            if (debugMessageLevel) {
                args.add("-debug");
            }

            return SCAPValWrapper.run(args.toArray(new String[0]), bootstrapLocation);
        }
    }

}