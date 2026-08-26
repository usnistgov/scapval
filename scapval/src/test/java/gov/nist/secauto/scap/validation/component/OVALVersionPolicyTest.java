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

package gov.nist.secauto.scap.validation.component;

import gov.nist.secauto.decima.core.classpath.ClasspathHandler;
import gov.nist.secauto.scap.validation.Application;
import gov.nist.secauto.scap.validation.SCAPVersion;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.stream.StreamSource;

/**
 * Keeps the Java OVAL version gate ({@link OVALVersion#getByString(String)}) in agreement with the
 * OVAL version policy encoded elsewhere: the SRC-216-1 schematron rule per SCAP revision, the
 * OVAL family approved by the SCAP 1.4 annex, and the schema bundles on the classpath.
 * <p>
 * The gate runs in Application.validateSCAPContent() before any assessment starts. A version it
 * rejects aborts the run with no report, so it must never be stricter than the schematron rule
 * that is supposed to judge OVAL versions.
 */
public class OVALVersionPolicyTest {

  private static final Namespace SCH = Namespace.getNamespace("sch", "http://purl.oclc.org/dsdl/schematron");
  private static final String SRC_216_1 = "SRC-216-1";
  private static final Pattern MATCHES_CALL = Pattern.compile("matches\\([^,]+,'([^']+)'\\)");
  private static final int MAX_MINOR = 20;
  private static final int MAX_PATCH = 20;

  @BeforeClass
  public static void initialize() {
    ClasspathHandler.initialize();
  }

  @Test
  public void javaGateResolvesEveryVersionAcceptedBySrc216() throws Exception {
    for (SCAPVersion scapVersion : SCAPVersion.values()) {
      Pattern accepted = src216CoreVersionPattern(scapVersion);
      List<String> rejected = new ArrayList<>();
      for (String candidate : candidateVersions()) {
        if (accepted.matcher(candidate).find() && OVALVersion.getByString(candidate) == null) {
          rejected.add(candidate);
        }
      }
      Assert.assertTrue("SCAP " + scapVersion.getVersion() + ": SRC-216-1 accepts OVAL versions that "
          + "OVALVersion.getByString() rejects, so validation would abort before the rule runs: " + rejected,
          rejected.isEmpty());
    }
  }

  @Test
  public void scap14ResolvesTheAnnexApproved512FamilyToA512Bundle() {
    // SP 800-126Ar4 sect. 2.2 approves "OVAL 5.12.x" for SCAP 1.4. Every member of the family,
    // including patch levels with no bundle of their own, must resolve to a 5.12 bundle.
    String familyDir = "xsd/mitre/oval/oval_5.12";
    for (String version : new String[] { "5.12", "5.12.0", "5.12.1", "5.12.2", "5.12.3", "5.12.4" }) {
      OVALVersion resolved = OVALVersion.getByString(version);
      Assert.assertNotNull("OVAL " + version + " is approved for SCAP 1.4 by SP 800-126Ar4 and must resolve",
          resolved);
      Assert.assertTrue("OVAL " + version + " resolved to " + resolved.getSchemaDir() + ", outside the 5.12 family",
          resolved.getSchemaDir().startsWith(familyDir));
    }
  }

  @Test
  public void everyOvalVersionHasASchemaBundleOnTheClasspath() throws Exception {
    for (OVALVersion version : OVALVersion.values()) {
      Set<String> systemIds = new HashSet<>();
      for (StreamSource source : version.getOVALSchemas(null, Application.ContentType.COMPONENT)) {
        systemIds.add(source.getSystemId());
      }
      // Older bundles are partial (several carry no results or system-characteristics schema), so
      // anchor on the two files every bundle must have for source-component validation.
      String dir = "classpath:" + version.getSchemaDir();
      for (String anchor : new String[] { "oval-common-schema.xsd", "oval-definitions-schema.xsd" }) {
        String systemId = dir + anchor;
        Assert.assertTrue(version + ": " + systemId + " is not in the bundled schema list", systemIds.contains(systemId));
        try (InputStream stream = new URL(systemId).openStream()) {
          Assert.assertNotNull(version + ": " + systemId + " is listed but not on the classpath", stream);
        }
      }
    }
  }

  /**
   * Extracts the core (non-platform) OVAL version pattern from the SRC-216-1 assert of the
   * source-data-stream schematron for the given SCAP version.
   */
  private static Pattern src216CoreVersionPattern(SCAPVersion scapVersion) throws Exception {
    String location = "classpath:rules/scap/source-data-stream-" + scapVersion.getVersion() + ".sch";
    Document schematron = new SAXBuilder().build(new URL(location));
    for (Element assertion : schematron.getDescendants(Filters.element("assert", SCH))) {
      if (!assertion.getTextTrim().startsWith(SRC_216_1)) {
        continue;
      }
      Matcher matcher = MATCHES_CALL.matcher(assertion.getAttributeValue("test"));
      Assert.assertTrue(location + ": SRC-216-1 assert has no matches() call to extract", matcher.find());
      return Pattern.compile(matcher.group(1));
    }
    throw new AssertionError(location + ": no SRC-216-1 assert found");
  }

  /**
   * Every "5.minor" and "5.minor.patch" string up to the configured bounds. Wide enough to cover
   * each version the schematron rules currently accept plus future patch levels.
   */
  private static List<String> candidateVersions() {
    List<String> candidates = new ArrayList<>();
    for (int minor = 0; minor <= MAX_MINOR; minor++) {
      candidates.add("5." + minor);
      for (int patch = 0; patch <= MAX_PATCH; patch++) {
        candidates.add("5." + minor + "." + patch);
      }
    }
    return candidates;
  }
}
