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
package gov.nist.scap.schematron;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

public class SimpleExtensionFunctionDefinition extends ExtensionFunctionDefinition {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 8301620238792339638L;

  private final StructuredQName qname;
  private final SequenceType[] argType;
  private final SequenceType resultType;
  private final ExtensionFunctionCall efc;

  /**
   * Constructs a new {@link ExtensionFunctionDefinition} using a provided set of parameters that
   * define the namespace, arguments, result, and function to call to evaluate an extension function.
   * 
   * @param qname
   *          the qualified name for the function
   * @param argType
   *          the argument definition for the function
   * @param resultType
   *          the return type for the function
   * @param efc
   *          the callback object to use to evaluate the function
   */
  public SimpleExtensionFunctionDefinition(StructuredQName qname, SequenceType[] argType, SequenceType resultType,
      ExtensionFunctionCall efc) {
    this.qname = qname;
    this.argType = argType;
    this.resultType = resultType;
    this.efc = efc;
  }

  @Override
  public SequenceType[] getArgumentTypes() {
    return argType;
  }

  @Override
  public StructuredQName getFunctionQName() {
    return qname;
  }

  @Override
  public SequenceType getResultType(SequenceType[] arg0) {
    return resultType;
  }

  @Override
  public ExtensionFunctionCall makeCallExpression() {
    return efc;
  }

}
