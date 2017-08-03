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

import gov.nist.decima.testing.PathRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Collections;
import java.util.List;

@RunWith(PathRunner.class)
public class UnitIntegrationTest {
	public static List<File> paths() {
//		return Collections.singletonList(new File("src/test/resources/unit-tests/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.3/APPLICATION/requirement-A26-scenario-2.xml"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.2/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.2/GENERAL/requirement-262-scenario-1.xml"));
		return Collections.singletonList(new File("src/test/resources/unit-tests/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.1/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.1-results/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/ocil-2.0/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/oval-tests/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/oval-tests/results"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/oval-tests/definitions"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/tmsad-1.0/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/xccdf-1.2/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.3/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.3/SOURCE/requirement-216-scenario-8.xml"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.3/RESULT/"));
//		return Collections.singletonList(new File("src/test/resources/unit-tests/scap-1.3/SOURCE/requirement-171-scenario-1.xml"));
	}
}