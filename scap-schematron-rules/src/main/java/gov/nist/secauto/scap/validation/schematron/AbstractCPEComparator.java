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
package gov.nist.secauto.scap.validation.schematron;

import gov.nist.secauto.cpe.common.WellFormedName;
import gov.nist.secauto.cpe.matching.CPENameMatcher;
import gov.nist.secauto.cpe.naming.CPENameUnbinder;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.value.StringValue;

import java.text.ParseException;

public abstract class AbstractCPEComparator extends ExtensionFunctionCall {
  // private static final long serialVersionUID = 4991578721196222282L;

  protected static final String NAMESPACE = "java:gov.nist.secauto.scap.validation.schematron";
  protected static final String PREFIX = "java-cpe";

  /**
   * Returns true if, and only if, cpe1 is equal or superset of cpe2, per CPE matching algorithm
   * 
   * @param cpe1
   *          A CPE 2.3 formatted string or URI
   * @param cpe2
   *          A CPE 2.3 formatted string or URI
   * @return {@code true} if cpe1 is equal or a superset of cpe2, or {@code false} otherwise
   */
  public static boolean isEqualOrSuperset(String cpe1, String cpe2) {
    BindingType type1 = getCpeType(cpe1);
    BindingType type2 = getCpeType(cpe2);
    if (type1 == null || type2 == null) {
      return false;
    }
    WellFormedName wfn1;
    WellFormedName wfn2;
    try {
      wfn1 = type1.unbind(cpe1);
      wfn2 = type2.unbind(cpe2);
    } catch (ParseException e) {
      return false;
    }
    if (wfn1 == null || wfn2 == null) {
      return false;
    }

    boolean isEqualOrSuperset = CPENameMatcher.isSuperset(wfn1, wfn2);
    isEqualOrSuperset = isEqualOrSuperset ? isEqualOrSuperset : CPENameMatcher.isEqual(wfn1, wfn2);
    return isEqualOrSuperset;
  }

  private static BindingType getCpeType(String cpe) {
    if (cpe.startsWith("cpe:2.3")) {
      return BindingType.FS;
    } else if (cpe.startsWith("cpe:/")) {
      return BindingType.URI;
    } else {
      return null;
    }
  }

  private static enum BindingType {
    FS(new IUnbindToWFN() {
      @Override
      public WellFormedName unbind(String cpe) throws ParseException {
        return CPENameUnbinder.unbindFS(cpe);
      }
    }),
    URI(new IUnbindToWFN() {
      @Override
      public WellFormedName unbind(String cpe) throws ParseException {
        return CPENameUnbinder.unbindURI(cpe);
      }
    });

    private IUnbindToWFN toWFN;

    private BindingType(IUnbindToWFN toWFN) {
      this.toWFN = toWFN;
    }

    private WellFormedName unbind(String cpe) throws ParseException {
      return toWFN.unbind(cpe);
    }
  }

  private interface IUnbindToWFN {
    public WellFormedName unbind(String cpe) throws ParseException;
  }

  protected Sequence createBooleanSequence(boolean bool) {
    return new StringValue(bool ? "true" : "");
  }
}
