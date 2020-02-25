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
package gov.nist.scap.validation.candidate;

import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

import gov.nist.decima.core.classpath.ClasspathHandler;

import org.junit.BeforeClass;

public class ZipExpanderTest {

    private final ZipExpander expander = new ZipExpander(1024);

    @BeforeClass
    public static void initialize() {
      ClasspathHandler.initialize();
    }

    @Test
    public void testExpand() throws Exception {
        File file = null;
        File result = null;
        try {
            file = new File(new URL("classpath:src/test/resources/candidates/scap-11-zip/SharePoint" + ".zip").getFile());
            result = this.expander.expand(file);
            assertTrue(result.exists());

            final File[] children = result.listFiles();
            assertEquals(6, children.length);

            //the order of listFiles is different per OS so we will just iterate through a list of expected files and use a flag
            String[] expectedFileNames = {
                    "Documentation",
                    "sp2007-cpe-dictionary.xml",
                    "sp2007-cpe-oval.xml",
                    "sp2007-ocil.xml",
                    "sp2007-oval.xml",
                    "sp2007-xccdf.xml"
            };

            for (String expectedFileName : expectedFileNames) {
                Boolean hasExpectedFile = false;

                for (File child : children) {
                    if (expectedFileName.equals(child.getName())) {
                        hasExpectedFile = true;
                    }
                }
                if (!hasExpectedFile) {
                    //if we get here it was not found
                    throw new AssertionError("Did not find expected file " + expectedFileName);
                }
            }

        } finally {
            // clean up
            this.expander.deleteDirectory(result);
        }
    }

    @Test
    public void testExpand2() throws Exception {
        File file = null;
        File result = null;
        try {
            file = new File(new URL(
                    "classpath:src/test/resources/candidates/scap-11-zip-with-extra" + "-files/R1100-scap11-extra-file.zip")
                    .getFile());
            result = this.expander.expand(file);
            assertTrue(result.exists());

            final File[] children = result.listFiles();
            assertEquals(7, children.length);

        } finally {
            // clean up
            expander.deleteDirectory(result);
        }
    }

    @Test
    public void testZipExceptions() throws Exception {
        // not a ZIP file
        final File file = new File(
                new URL("classpath:src/test/resources/candidates/components/xccdf" + "/xccdf-114-xml/fdcc-winvista-xccdf.xml")
                        .getFile());
        try {
            this.expander.expand(file);
            fail("Should have throw exception, file is not zip");
        } catch (IllegalStateException e) {
            // expected
        }

        File conflict = null;
        File target = null;
        try {
            // target folder already exists
            final String name = "classpath:src/test/resources/candidates/scap-11-zip";
            // TDOD: Do not create test files on the classpath
            final File parent = new File(new URL(name).getFile());
            conflict = new File(parent, "SharePoint");
            conflict.mkdirs();
            target = this.expander.expand(new File(new URL(String.format("%s/SharePoint.zip", name)).getFile()));

            assertEquals("SharePoint0", target.getName());
        } finally {
            this.expander.deleteDirectory(target);
            this.expander.deleteDirectory(conflict);
        }

    }

}
