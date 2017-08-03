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
package gov.nist.scap.validation.candidate;

import gov.nist.scap.validation.NamespaceConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Takes an XML file and determines the content type (SCAP or XCCDF) by looking
 * at XML namespace of the root element.
 * <p>
 * For SCAP 1.2/1.3 content, can also find the SCAP use case by looking at the
 * use-case attribute of the data-stream element.
 */
public class ScapDocumentSniffer {

    private static final Logger log = LogManager.getLogger(ScapDocumentSniffer.class);
    /**
     * Call this method to find the content type of the given file.
     * 
     * @param filename The location of the file.
     * @return The namespace of the root element, if recognized; otherwise,
     *         null.
     */
    public String findContentType(final String filename) {

        final ScapUriHandler handler = new ScapUriHandler();

        parseFile(filename, handler);

        if (log.isDebugEnabled()) {
            log.debug(String.format(
                "%s SCAP use case is %s",
                filename,
                handler.getContentType()));
        }
        return handler.getContentType();
    }

    /**
     * For an SCAP 1.2/1.3 file, returns the use-case attribute of the data-stream
     * element.
     * 
     * @param filename The SCAP file.
     * @return The use case, e.g., CONFIGURATION.
     */
    public String findUseCase(final String filename) {

        final ScapUseCaseHandler handler = new ScapUseCaseHandler();

        parseFile(filename, handler);

        if (log.isDebugEnabled()) {
            log.debug(String.format(
                "%s SCAP use case is %s",
                filename,
                handler.getUseCase()));
        }
        return handler.getUseCase();
    }

    /**
     * Parses a particular XML file, and handles it in a certain way.
     * 
     * @param filename The XML file.
     * @param handler Handler determines what we are looking for in the file.
     */
    private void parseFile(final String filename, final DefaultHandler handler) {

        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(false);
        parserFactory.setNamespaceAware(true);

        FileInputStream in = null;
        try {
            final SAXParser parser = parserFactory.newSAXParser();
            in = new FileInputStream(new File(filename));
            final InputSource inputSource = new InputSource(in);
            parser.parse(inputSource, handler);
        } catch (Exception e) {
            log.error(String.format("Unable to process %s", filename), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("Unable to close " + filename);
                }
            }
        }
    }

    /**
     * Handles an XML document by inspecting the namespace of the root element
     * to determine if it is recognized as an SCAP namespace.
     */
    static class ScapUriHandler extends DefaultHandler {

        // short-circuit once root element is parsed
        private boolean isRootElementFound = false;

        // namespace of the root element, if recognized by SCAP
        private String contentType = null;

        // collection of all recognized SCAP namespaces
        private static final String[] SCAP_URIS = {
                NamespaceConstants.NS_CPE_DICT_2.getNamespaceString(),
                NamespaceConstants.NS_OCIL_2_0.getNamespaceString(),
                NamespaceConstants.NS_OVAL_DEF_5.getNamespaceString(),
                NamespaceConstants.NS_SOURCE_DS_1_1.getNamespaceString(),
                NamespaceConstants.NS_SOURCE_DS_1_2.getNamespaceString(),
                NamespaceConstants.NS_SOURCE_DS_1_3.getNamespaceString(),
                NamespaceConstants.NS_RESULTS_DS_1_2.getNamespaceString(),
                NamespaceConstants.NS_ARF_1_1.getNamespaceString(),
                NamespaceConstants.NS_XCCDF_1_1_4.getNamespaceString(),
                NamespaceConstants.NS_XCCDF_1_2.getNamespaceString(),
        };

        private static final Set<String> NAMESPACE_MAP = new HashSet<String>();
        static {
            for (final String uri : SCAP_URIS) {
                NAMESPACE_MAP.add(uri);
            }
        }

        @Override
        public void startElement(
                String uri,
                String localName,
                String qName,
                Attributes attributes) throws SAXException {

            // short-circuit after root element for performance
            if (!isRootElementFound) {

                if (log.isDebugEnabled()) {
                    log.debug(String.format("found root uri %s", uri));
                }

                // record namespace ONLY if it is SCAP
                if (NAMESPACE_MAP.contains(uri)) {
                    this.contentType = uri;
                }

                isRootElementFound = true;
            }

            super.startElement(uri, localName, qName, attributes);
        }

        public String getContentType() {
            return this.contentType;
        }
    }

    /**
     * Handles an XML document by searching for the SCAP 1.2/1.3 namespace, then
     * finding the use-case attribute of the data-stream element.
     */
    static class ScapUseCaseHandler extends DefaultHandler {

        // the value of the use-case attribute, if found
        private String useCase = null;

        @Override
        public void startElement(
                String uri,
                String localName,
                String qName,
                Attributes attributes) throws SAXException {

            // short-circuit for performance
            if (useCase == null) {

                // if stream is SCAP 1.2/1.3 and element is data-stream
                if ((NamespaceConstants.NS_SOURCE_DS_1_2.getNamespaceString().equals(uri)
                || NamespaceConstants.NS_SOURCE_DS_1_3.getNamespaceString().equals(uri))
                    && "data-stream".equals(localName)) {

                    // get value of the use-case attribute
                    this.useCase = attributes.getValue("use-case");
                    if (log.isDebugEnabled()) {
                        log.debug(String.format(
                            "found use-case %s",
                            this.useCase));
                    }
                }
            }

            super.startElement(uri, localName, qName, attributes);
        }

        public String getUseCase() {
            return this.useCase;
        }
    }
}