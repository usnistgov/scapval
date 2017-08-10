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
package gov.nist.scap.validation.candidate;

import gov.nist.scap.validation.SCAPVersion;
import gov.nist.scap.validation.component.SCAP11Components;
import gov.nist.scap.validation.component.XCCDFVersion;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CandidateFile has information about a File (xml, zip, folder, etc) which may
 * or may not be eligible for validation by the SCAPVAL tools.
 * <p>
 * There are four types of CandidateFile:
 * <li>SCAP_BUNDLE is a folder or zip file containing SCAP 1.1 files.</li>
 * <li>SCAP_COMBINED_FILE is an SCAP 1.2/1.3 XML file.</li>
 * <li>STANDALONE_XCCDF is an XCCDF XML file.</li>
 * <li>UNKNOWN is an unrecognized file, not of the above types.</li>
 * <p>
 * If the type is SCAP_BUNDLE, then it has one or more SCAP content types.
 * <p>
 * If the type is SCAP_COMBINED_FILE, then it has an SCAP version, and may have
 * a use case.
 * <p>
 * If the type is XCCDF, then it has an XCCDF version.
 */
public class CandidateFile {

    public enum Type {
        SCAP_BUNDLE,
        SCAP_COMBINED_FILE,
        STANDALONE_XCCDF,
        UNKNOWN
    }

    // type of candidate
    private Type type;

    // the file under consideration
    private String absolutePath;

    // user-friendly path hides absolute path
    private String displayPath;

    // the simple file name
    private String name;

    // conveys why this file is not eligible for validation, if disqualified
    private String disqualificaiton;

    // the XCCDF version, if applicable
    private XCCDFVersion xccdfVersion;

    // the SCAP version, if applicable
    private SCAPVersion scapVersion;

    // the SCAP use case, if known
    private String scapUseCase;

    // the SCAP content types, if found
    private List<SCAP11Components> scapContentTypes = new ArrayList<>();

    // true if file was ZIP and was expanded
    private boolean isExpanded;

    /**
     * Private constructor, use Builder.
     */
    private CandidateFile() {
        super();
    }

    public Type getType() {
        return this.type;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public String getName() {
        return this.name;
    }

    public boolean isUnrecognized() {
        return this.type == Type.UNKNOWN;
    }

    public String getDisqualificaiton() {
        return this.disqualificaiton;
    }

    public List<SCAP11Components> getScapContentTypes() {
        return Collections.unmodifiableList(this.scapContentTypes);
    }

    public SCAPVersion getScapVersion() {
        return this.scapVersion;
    }

    public String getScapUseCase() {
        return this.scapUseCase;
    }

    public XCCDFVersion getXccdfVersion() {
        return xccdfVersion;
    }

    public boolean isExpansionRequired() {
        return (this.type == Type.UNKNOWN) && this.isZipFile();
    }

    public boolean isZipFile() {
        return this.name.toLowerCase().endsWith(".zip");
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public String getDisplayPath() {
        return displayPath;
    }

    static class Builder {

        private final String name;
        private final String displayPath;
        private final CandidateFile parent;

        private File file;

        private Type type;
        private String disqualification;
        private XCCDFVersion xccdfVersion;
        private SCAPVersion scapVersion;
        private String scapUseCase;
        private List<SCAP11Components> scapContentTypes;

        private boolean isExpanded;

        public Builder(final CandidateFile parent, final File file) {
            this.parent = parent;
            this.file = file;

            if (this.file == null) {
                throw new IllegalStateException("File cannot be null");
            }
            this.name = this.file.getName();
            this.displayPath = computeDisplayPath();
        }

        public Builder(final File file) {
            this(null, file);
        }

        public boolean isFileDirectory() {
            return this.file.isDirectory();
        }

        public boolean isFileXml() {
            return this.file.getName().toLowerCase().endsWith(".xml");
        }

        public boolean isFileZip() {
            return this.file.getName().toLowerCase().endsWith(".zip");
        }

        public CandidateFile createCandidateFile() {

            if (this.type == null) {
                throw new IllegalStateException(
                    "Unable to create candidate file, CandidateFile.Type is null");
            }

            final CandidateFile candidate = new CandidateFile();

            candidate.absolutePath = file.getAbsolutePath();
            candidate.isExpanded = this.isExpanded;

            candidate.name = this.name;
            candidate.displayPath = this.displayPath;
            candidate.type = this.type;

            candidate.disqualificaiton = this.disqualification;

            candidate.xccdfVersion = this.xccdfVersion;
            candidate.scapContentTypes =
                this.scapContentTypes == null ? new ArrayList<>() : this.scapContentTypes;
            candidate.scapUseCase = this.scapUseCase;
            candidate.scapVersion = this.scapVersion;

            return candidate;
        }

        private String computeDisplayPath() {
            final StringBuilder builder = new StringBuilder();
            if (this.parent != null) {
                builder.append(this.parent.displayPath);
                builder.append(File.separatorChar);
            }
            builder.append(this.file.getName());
            return builder.toString();
        }

        private void setType(final Type type) {
            if (this.type != null) {
                throw new IllegalStateException(String.format(
                    "Unable to set type to %s, type was already %s",
                    type.name(),
                    this.type.name()));
            }
            this.type = type;
        }

        public Builder setTypeUnknown(final String message) {
            this.disqualification = message;
            setType(Type.UNKNOWN);
            return this;
        }

        public Builder setTypeXccdf(final XCCDFVersion version) {
            this.xccdfVersion = version;
            setType(Type.STANDALONE_XCCDF);
            return this;
        }

        public Builder setTypeScapCombinedFile(
                final SCAPVersion version,
                final String useCase) {
            this.scapVersion = version;
            this.scapUseCase = useCase;
            setType(Type.SCAP_COMBINED_FILE);
            return this;
        }

        public Builder setTypeScapBundle(final List<SCAP11Components> types) {
            this.scapContentTypes = types;
            setType(Type.SCAP_BUNDLE);
            return this;
        }

        public Builder setExpandedTo(final File expandedTo) {
            if (isExpanded) {
                throw new IllegalStateException("Unable to expand file twice.");
            }
            this.isExpanded = true;
            this.file = expandedTo;
            return this;
        }

        public File getFile() {
            return file;
        }
    }
}
