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

package gov.nist.secautotrust.signer;

import gov.nist.secautotrust.signature.UriResolver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;

/**
 * A URIDereferencer which handles the mapping between a URI and an input stream.
 */
public class MappedURIDereferencer implements URIDereferencer {

  private URIDereferencer defaultDeref;
  private UriResolver resolver;

  public MappedURIDereferencer(URIDereferencer defaultDeref, UriResolver resolver) {
    this.defaultDeref = defaultDeref;
    this.resolver = resolver;
  }

  @Override
  public Data dereference(URIReference uriRef, XMLCryptoContext context) throws URIReferenceException {
    if (uriRef == null) {
      throw new NullPointerException("uriRef cannot be null");
    }
    if (context == null) {
      throw new NullPointerException("context cannot be null");
    }
    InputStream mapped = resolver.getInputStream(uriRef.getURI());
    if (mapped != null) {
      return new OctetStreamData(mapped, uriRef.getURI(), "text/xml");
    }
    URI uri = URI.create(uriRef.getURI());
    if ("file".equals(uri.getScheme())) {
      try {
        return new OctetStreamData(new BufferedInputStream(new FileInputStream(new File(uri))), uriRef.getURI(),
            "text/xml");
      } catch (FileNotFoundException e) {
        throw new URIReferenceException(e);
      }
    }
    return defaultDeref.dereference(uriRef, context);
  }
}