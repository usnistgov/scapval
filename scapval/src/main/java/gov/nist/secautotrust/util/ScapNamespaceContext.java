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

package gov.nist.secautotrust.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class ScapNamespaceContext implements NamespaceContext {

  private static final Map<String, String> prefixNamespaceMap = new HashMap<String, String>() {
    private static final long serialVersionUID = 1L;

    {
      put("ds", "http://scap.nist.gov/schema/scap/source/1.2");
      put("arf", "http://scap.nist.gov/schema/asset-reporting-format/1.1");
      put("dsig", "http://www.w3.org/2000/09/xmldsig#");
      put("xccdf", "http://checklists.nist.gov/xccdf/1.2");
      put("cpe-dict", "http://cpe.mitre.org/dictionary/2.0");
      put("oval-res", "http://oval.mitre.org/XMLSchema/oval-results-5");
      put("dsig-trans", "http://www.w3.org/2002/06/xmldsig-filter2");
    }
  };

  private static final Map<String, String> namespacePrefixMap = new HashMap<String, String>();
  static {
    for (String key : prefixNamespaceMap.keySet()) {
      namespacePrefixMap.put(prefixNamespaceMap.get(key), key);
    }
  }

  @Override
  public Iterator<String> getPrefixes(String namespaceURI) {
    return Collections.singleton(namespacePrefixMap.get(namespaceURI)).iterator();
  }

  @Override
  public String getPrefix(String namespaceURI) {
    return namespacePrefixMap.get(namespaceURI);
  }

  @Override
  public String getNamespaceURI(String prefix) {
    return prefixNamespaceMap.get(prefix);
  }

  public static Map<String, String> createPrefixNamespaceMap() {
    Map<String, String> returnMap = new HashMap<String, String>();
    for (String s : prefixNamespaceMap.keySet()) {
      returnMap.put(s, prefixNamespaceMap.get(s));
    }
    return returnMap;
  }

  public static Map<String, String> createPrefixNamespaceMap(String... prefixes) {
    Map<String, String> returnMap = new HashMap<String, String>();
    for (String s : prefixes) {
      if (prefixNamespaceMap.containsKey(s))
        returnMap.put(s, prefixNamespaceMap.get(s));
    }
    return returnMap;
  }

}
