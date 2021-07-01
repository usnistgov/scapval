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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

public class Util {
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

  public static String generateId(String idRoot) {
    return idRoot + "-" + sdf.format(new Date()) + "-" + Integer.toString(Math.abs((new Random()).nextInt()) % 100);
  }

  // This is needed because xmlsec-1.5.1 uses getElementById when a fragment
  // is specified
  // NOTE: this is a very naive implementation, and should be fixed if time
  // allows
  public static void setIdOnDOM(Node n, Set<String> ids) {
    if (n instanceof Element) {
      Element e = (Element) n;
      String value = e.getAttribute("Id");
      if (value.length() > 0) {
        if (!ids.contains(value)) {
          ids.add(value);
          e.setIdAttribute("Id", true);
        }
      } else if ((value = e.getAttribute("ID")).length() > 0) {
        if (!ids.contains(value)) {
          ids.add(value);
          e.setIdAttribute("ID", true);
        }
      } else if ((value = e.getAttribute("id")).length() > 0) {
        if (!ids.contains(value)) {
          ids.add(value);
          e.setIdAttribute("id", true);
        }
      }
    }
    for (int i = 0, size = n.getChildNodes().getLength(); i < size; i++) {
      setIdOnDOM(n.getChildNodes().item(i), ids);
    }
  }
}
