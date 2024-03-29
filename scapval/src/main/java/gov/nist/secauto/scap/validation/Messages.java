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

package gov.nist.secauto.scap.validation;

import org.apache.logging.log4j.LogManager;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides SCAPVal visioning messages.
 */
public class Messages {
  /**
   * Reads the messages properties file to generate argument based messages.
   *
   * @param key
   *          the messages key
   * @param arguments
   *          the arguments to populate in the message
   * @return the generated text
   */
  public static String getMessage(String key, Object... arguments) {
    String msg = null;
    try {
      msg = ResourceBundle.getBundle("messages", Locale.getDefault()).getString("scap.validation.version");
    } catch (MissingResourceException e) {
      msg = key;
    }
    return MessageFormat.format(msg, arguments);
  }

  /**
   * Displays the current SCAPVal version based the pom file.
   */
  public static void printVersion() {
    String message = Messages.getMessage("scap.validation.version", SCAPVersion.getVersionsSupported(), getVersion());
    final org.apache.logging.log4j.Logger log = LogManager.getLogger(Application.class);
    log.info(message);
  }

  public static String getVersion() {
    return ResourceBundle.getBundle("scapval-version", Locale.getDefault()).getString("Implementation-Version");
  }

}
