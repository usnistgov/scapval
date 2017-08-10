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

import gov.nist.decima.xml.document.JDOMDocument;
import gov.nist.scap.validation.SCAPVersion;
import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

public class SCAPUtilsTest {
    @Test
    public void getOVALComponentsFromSCAPContent() throws Exception {
        final File datastream11 = new File(new URL("classpath:src/test/resources/candidates/scap-11/combined/SCAP11CombinedContent.xml").getFile());
        List<Element> OVALElements11 = SCAPUtils.getOVALComponentsFromSCAPContent(new JDOMDocument(datastream11), SCAPVersion.V1_1);
        Assert.assertEquals(OVALElements11.size(), 3);

        final File datastream12 = new File(new URL("classpath:src/test/resources/candidates/scap-12/scap_gov.nist_USGCB-Windows-XP-firewall.xml").getFile());
        List<Element> OVALElements12 = SCAPUtils.getOVALComponentsFromSCAPContent(new JDOMDocument(datastream12), SCAPVersion.V1_2);
        Assert.assertEquals(OVALElements12.size(), 2);

        final File datastream13 = new File(new URL("classpath:src/test/resources/candidates/scap-13/source_data_stream_collection_sample.xml").getFile());
        List<Element> OVALElements13 = SCAPUtils.getOVALComponentsFromSCAPContent(new JDOMDocument(datastream13), SCAPVersion.V1_3);
        Assert.assertEquals(OVALElements13.size(), 2);
    }

}