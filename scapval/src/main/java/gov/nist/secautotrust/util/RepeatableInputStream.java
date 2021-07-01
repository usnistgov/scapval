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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a specialized InputStream. It is built because most of the source content for
 * hashing/signing is from the source datastream collection. Since the interface to the signing
 * component requires that an inputstream and XPath be specified, this class allows a regular
 * inputstream to be read once, and then appear to be duplicated over and over without duplicating
 * the data in memory. The source input stream is read into a buffer. The buffer is then passed
 * between subsequent instances of the class as long as the objects are built using the
 * duplicateStream method. This allows the source datastream collection stream to be read multiple
 * times, regardless of the actual InputStream type.
 * 
 * @author 540951
 */
public class RepeatableInputStream extends InputStream {

  private int pointer;
  private byte[] content;
  private static final int BLOCK_SIZE = 4096;

  public RepeatableInputStream(InputStream is) throws IOException {
    List<byte[]> list = new LinkedList<byte[]>();
    byte[] b = new byte[BLOCK_SIZE];

    int readCount;
    while ((readCount = is.read(b)) > 0) {
      list.add(Arrays.copyOf(b, readCount));
    }

    content = new byte[(list.size() - 1) * BLOCK_SIZE + list.get(list.size() - 1).length];
    for (int i = 0, size = list.size(); i < size; i++) {
      System.arraycopy(list.get(i), 0, content, i * BLOCK_SIZE, list.get(i).length);
    }
  }

  private RepeatableInputStream(byte[] content) {
    this.content = content;
  }

  @Override
  public int read() throws IOException {
    if (pointer < content.length) {
      return content[pointer++];
    }
    return -1;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (len == 0) {
      return 0;
    }
    int i = 0;
    int read;
    while (len-- != 0) {
      read = read();
      if (read != -1)
        b[off + i++] = (byte) read;
      else
        break;
    }
    if (i > 0)
      return i;
    else
      return -1;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  /**
   * @throws IOException
   */
  public RepeatableInputStream duplicateStream() throws IOException {
    return new RepeatableInputStream(content);
  }
}
