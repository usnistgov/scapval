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
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a professional HTML summary report for batch validation runs. The report includes
 * pass/fail status for each file, links to per-file HTML detail reports, and failure details
 * (requirement IDs, error messages, XPath locations) extracted from the XML result files.
 */
public class BatchSummaryReportGenerator {

  private static final Logger log = LogManager.getLogger(BatchSummaryReportGenerator.class);
  private static final Namespace RESULTS_NS
      = Namespace.getNamespace("res", "http://csrc.nist.gov/ns/decima/results/1.0");
  private static final String SUMMARY_FILENAME = "batch-validation-summary.html";

  /**
   * Data carrier for per-file batch validation results.
   */
  public static class BatchFileResult {
    private final String fileName;
    private final boolean passed;
    private final String htmlReportName;
    private final String xmlResultName;

    public BatchFileResult(String fileName, boolean passed, String htmlReportName, String xmlResultName) {
      this.fileName = fileName;
      this.passed = passed;
      this.htmlReportName = htmlReportName;
      this.xmlResultName = xmlResultName;
    }

    public String getFileName() {
      return fileName;
    }

    public boolean isPassed() {
      return passed;
    }

    public String getHtmlReportName() {
      return htmlReportName;
    }

    public String getXmlResultName() {
      return xmlResultName;
    }
  }

  /**
   * Represents a single failure detail extracted from an XML result file.
   */
  private static class FailureDetail {
    String requirementId;
    String message;
    String xpath;
    String line;
    String column;
  }

  /**
   * Generates the batch summary HTML report.
   *
   * @param results
   *          the list of per-file validation results
   * @param outputDir
   *          the directory to write the summary report into
   * @param scapvalVersion
   *          the SCAPVal version string
   * @param dirPath
   *          the path of the directory that was validated
   * @return the generated report File, or null if generation failed
   */
  public static File generate(List<BatchFileResult> results, File outputDir, String scapvalVersion, String dirPath) {
    File outputFile = new File(outputDir, SUMMARY_FILENAME);
    try (Writer writer
        = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8)) {

      long passedCount = results.stream().filter(BatchFileResult::isPassed).count();
      long failedCount = results.size() - passedCount;
      String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

      StringBuilder html = new StringBuilder();
      appendHeader(html, scapvalVersion, timestamp, dirPath, results.size(), passedCount, failedCount);
      appendResultsTable(html, results, outputDir);
      appendFailureDetails(html, results, outputDir);
      appendFooter(html);

      writer.write(html.toString());
      log.info("Batch summary report written to: " + outputFile.getAbsolutePath());
      return outputFile;

    } catch (IOException e) {
      log.error("Failed to write batch summary report: " + e.getMessage());
      return null;
    }
  }

  private static void appendHeader(StringBuilder html, String version, String timestamp, String dirPath,
      int total, long passed, long failed) {
    html.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
    html.append("<meta charset=\"UTF-8\">\n");
    html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
    html.append("<title>SCAPVal Batch Validation Summary</title>\n");
    html.append("<style>\n");
    html.append(CSS);
    html.append("</style>\n</head>\n<body>\n");

    // Header bar
    html.append("<div class=\"header\">\n");
    html.append("  <h1>SCAPVal Batch Validation Summary</h1>\n");
    html.append("  <div class=\"header-meta\">Version ").append(esc(version));
    html.append(" &bull; ").append(esc(timestamp)).append("</div>\n");
    html.append("</div>\n");

    // Info bar
    html.append("<div class=\"container\">\n");
    html.append("<div class=\"info-bar\">\n");
    html.append("  <span>Directory: <strong>").append(esc(dirPath)).append("</strong></span>\n");
    html.append("</div>\n");

    // Stats cards
    html.append("<div class=\"stats\">\n");
    html.append("  <div class=\"stat-card\"><div class=\"stat-num\">").append(total);
    html.append("</div><div class=\"stat-label\">Total Files</div></div>\n");
    html.append("  <div class=\"stat-card stat-pass\"><div class=\"stat-num\">").append(passed);
    html.append("</div><div class=\"stat-label\">Passed</div></div>\n");
    html.append("  <div class=\"stat-card stat-fail\"><div class=\"stat-num\">").append(failed);
    html.append("</div><div class=\"stat-label\">Failed</div></div>\n");
    html.append("</div>\n");
  }

  private static void appendResultsTable(StringBuilder html, List<BatchFileResult> results, File outputDir) {
    html.append("<h2>Validation Results</h2>\n");
    html.append("<table>\n<thead><tr>");
    html.append("<th>File Name</th><th>Result</th><th>Detail Report</th>");
    html.append("</tr></thead>\n<tbody>\n");

    for (BatchFileResult r : results) {
      html.append("<tr>");
      html.append("<td>").append(esc(r.getFileName())).append("</td>");
      if (r.isPassed()) {
        html.append("<td><span class=\"badge badge-pass\">Pass</span></td>");
      } else {
        html.append("<td><span class=\"badge badge-fail\">Fail</span></td>");
      }
      boolean reportExists = new File(outputDir, r.getHtmlReportName()).exists();
      if (reportExists) {
        html.append("<td><a href=\"").append(esc(r.getHtmlReportName())).append("\">View Report</a></td>");
      } else {
        html.append("<td><span class=\"no-report\">No report (validation error)</span></td>");
      }
      html.append("</tr>\n");
    }

    html.append("</tbody>\n</table>\n");
  }

  private static void appendFailureDetails(StringBuilder html, List<BatchFileResult> results, File outputDir) {
    boolean hasFailures = results.stream().anyMatch(r -> !r.isPassed());
    if (!hasFailures) {
      return;
    }

    html.append("<h2>Failure Details</h2>\n");

    for (BatchFileResult r : results) {
      if (r.isPassed()) {
        continue;
      }

      html.append("<details open>\n");
      html.append("<summary class=\"file-summary\">").append(esc(r.getFileName()));
      boolean reportExists = new File(outputDir, r.getHtmlReportName()).exists();
      if (reportExists) {
        html.append(" &mdash; <a href=\"").append(esc(r.getHtmlReportName())).append("\">Full Report</a>");
      }
      html.append("</summary>\n");

      File xmlResultFile = new File(outputDir, r.getXmlResultName());
      List<FailureDetail> failures = extractFailures(xmlResultFile);
      if (failures.isEmpty()) {
        if (!xmlResultFile.exists()) {
          html.append("<p class=\"no-details\">Validation encountered an error before results could be generated. ")
              .append("Check the console output above for details.</p>\n");
        } else {
          html.append("<p class=\"no-details\">No individual failure details found in result file.</p>\n");
        }
      } else {
        html.append("<table class=\"detail-table\">\n");
        html.append("<colgroup><col class=\"col-req\"><col class=\"col-msg\"><col class=\"col-loc\"></colgroup>\n");
        html.append("<thead><tr><th>Requirement</th><th>Message</th><th>Location</th></tr></thead>\n");
        html.append("<tbody>\n");
        String copyBtn = "<button class=\"copy-btn\" onclick=\"copyCell(this)\" title=\"Copy to clipboard\" aria-label=\"Copy to clipboard\">&#x2398;</button>";
        for (FailureDetail f : failures) {
          html.append("<tr>");
          html.append("<td class=\"req-id\">").append(esc(f.requirementId)).append(copyBtn).append("</td>");
          html.append("<td>").append(esc(f.message)).append(copyBtn).append("</td>");
          html.append("<td class=\"location\">");
          if (f.line != null) {
            html.append("Line ").append(esc(f.line));
            if (f.column != null) {
              html.append(", Col ").append(esc(f.column));
            }
            html.append("<br>");
          }
          if (f.xpath != null) {
            html.append("<code class=\"xpath\">").append(esc(f.xpath)).append("</code>");
          }
          html.append(copyBtn);
          html.append("</td>");
          html.append("</tr>\n");
        }
        html.append("</tbody>\n</table>\n");
      }

      html.append("</details>\n");
    }
  }

  private static void appendFooter(StringBuilder html) {
    html.append("</div>\n"); // close container
    html.append("<div class=\"footer\">Generated by SCAPVal &bull; NIST</div>\n");
    html.append("<script>\n");
    html.append("function copyCell(btn) {\n");
    html.append("  var td = btn.parentElement;\n");
    html.append("  var clone = td.cloneNode(true);\n");
    html.append("  var buttons = clone.querySelectorAll('.copy-btn');\n");
    html.append("  for (var i = 0; i < buttons.length; i++) buttons[i].remove();\n");
    html.append("  var text = clone.textContent.trim();\n");
    html.append("  function onSuccess() {\n");
    html.append("    btn.classList.add('copied');\n");
    html.append("    btn.innerHTML = '\\u2713';\n");
    html.append("    setTimeout(function() {\n");
    html.append("      btn.classList.remove('copied');\n");
    html.append("      btn.innerHTML = '\\u2398';\n");
    html.append("    }, 1500);\n");
    html.append("  }\n");
    html.append("  if (navigator.clipboard && navigator.clipboard.writeText) {\n");
    html.append("    navigator.clipboard.writeText(text).then(onSuccess).catch(function() {\n");
    html.append("      fallbackCopy(text) && onSuccess();\n");
    html.append("    });\n");
    html.append("  } else {\n");
    html.append("    fallbackCopy(text) && onSuccess();\n");
    html.append("  }\n");
    html.append("}\n");
    html.append("function fallbackCopy(text) {\n");
    html.append("  var ta = document.createElement('textarea');\n");
    html.append("  ta.value = text;\n");
    html.append("  ta.style.position = 'fixed';\n");
    html.append("  ta.style.opacity = '0';\n");
    html.append("  document.body.appendChild(ta);\n");
    html.append("  ta.select();\n");
    html.append("  var ok = document.execCommand('copy');\n");
    html.append("  document.body.removeChild(ta);\n");
    html.append("  return ok;\n");
    html.append("}\n");
    html.append("</script>\n");
    html.append("</body>\n</html>\n");
  }

  /**
   * Parses the XML result file to extract failure details.
   */
  private static List<FailureDetail> extractFailures(File xmlResultFile) {
    List<FailureDetail> failures = new ArrayList<>();
    if (!xmlResultFile.exists()) {
      log.warn("XML result file not found: " + xmlResultFile.getAbsolutePath());
      return failures;
    }

    try {
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(xmlResultFile);
      Element root = doc.getRootElement();
      Element resultsEl = root.getChild("results", RESULTS_NS);
      if (resultsEl == null) {
        return failures;
      }

      for (Element baseReq : resultsEl.getChildren("base-requirement", RESULTS_NS)) {
        String baseStatus = getChildText(baseReq, "status");
        if (!"FAIL".equals(baseStatus)) {
          continue;
        }

        for (Element derivedReq : baseReq.getChildren("derived-requirement", RESULTS_NS)) {
          String derivedStatus = getChildText(derivedReq, "status");
          if (!"FAIL".equals(derivedStatus)) {
            continue;
          }

          String reqId = derivedReq.getAttributeValue("id");

          for (Element test : derivedReq.getChildren("test", RESULTS_NS)) {
            String testStatus = getChildText(test, "status");
            if (!"FAIL".equals(testStatus)) {
              continue;
            }

            FailureDetail detail = new FailureDetail();
            detail.requirementId = reqId;
            detail.message = getChildText(test, "message");

            Element location = test.getChild("location", RESULTS_NS);
            if (location != null) {
              detail.xpath = location.getAttributeValue("xpath");
              detail.line = location.getAttributeValue("line");
              detail.column = location.getAttributeValue("column");
            }

            failures.add(detail);
          }
        }
      }
    } catch (Exception e) {
      log.warn("Error parsing XML result file " + xmlResultFile.getName() + ": " + e.getMessage());
    }

    return failures;
  }

  private static String getChildText(Element parent, String childName) {
    Element child = parent.getChild(childName, RESULTS_NS);
    return child != null ? child.getTextTrim() : null;
  }

  /**
   * HTML-escapes a string to prevent XSS and rendering issues.
   */
  private static String esc(String text) {
    if (text == null) {
      return "";
    }
    return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        .replace("\"", "&quot;").replace("'", "&#39;");
  }

  // @formatter:off
  private static final String CSS =
      "* { margin: 0; padding: 0; box-sizing: border-box; }\n"
    + "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;\n"
    + "  background: #f5f7fa; color: #333; line-height: 1.6; }\n"
    + ".header { background: linear-gradient(135deg, #1a365d, #2b6cb0); color: white;\n"
    + "  padding: 24px 32px; }\n"
    + ".header h1 { font-size: 1.6em; font-weight: 600; }\n"
    + ".header-meta { font-size: 0.9em; opacity: 0.85; margin-top: 4px; }\n"
    + ".container { max-width: 1200px; margin: 0 auto; padding: 24px; }\n"
    + ".info-bar { background: #edf2f7; border-radius: 6px; padding: 12px 16px;\n"
    + "  margin-bottom: 20px; font-size: 0.95em; color: #4a5568; }\n"
    + ".stats { display: flex; gap: 16px; margin-bottom: 28px; }\n"
    + ".stat-card { flex: 1; background: white; border-radius: 8px; padding: 20px;\n"
    + "  text-align: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }\n"
    + ".stat-num { font-size: 2em; font-weight: 700; }\n"
    + ".stat-label { font-size: 0.85em; color: #718096; text-transform: uppercase;\n"
    + "  letter-spacing: 0.05em; margin-top: 4px; }\n"
    + ".stat-pass .stat-num { color: #276749; }\n"
    + ".stat-fail .stat-num { color: #c53030; }\n"
    + "h2 { font-size: 1.25em; margin-bottom: 12px; color: #2d3748; }\n"
    + "table { width: 100%; border-collapse: collapse; background: white;\n"
    + "  border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1);\n"
    + "  margin-bottom: 28px; }\n"
    + "th { background: #edf2f7; text-align: left; padding: 12px 16px;\n"
    + "  font-weight: 600; font-size: 0.85em; text-transform: uppercase;\n"
    + "  letter-spacing: 0.05em; color: #4a5568; }\n"
    + "td { padding: 10px 16px; border-top: 1px solid #e2e8f0; font-size: 0.95em; }\n"
    + "tr:hover { background: #f7fafc; }\n"
    + ".badge { display: inline-block; padding: 3px 10px; border-radius: 12px;\n"
    + "  font-size: 0.8em; font-weight: 600; }\n"
    + ".badge-pass { background: #c6f6d5; color: #276749; }\n"
    + ".badge-fail { background: #fed7d7; color: #c53030; }\n"
    + "a { color: #2b6cb0; text-decoration: none; }\n"
    + "a:hover { text-decoration: underline; }\n"
    + "details { background: white; border-radius: 8px; margin-bottom: 12px;\n"
    + "  box-shadow: 0 1px 3px rgba(0,0,0,0.1); overflow: hidden; }\n"
    + ".file-summary { padding: 14px 16px; cursor: pointer; font-weight: 600;\n"
    + "  background: #fff5f5; border-left: 4px solid #c53030; }\n"
    + ".file-summary:hover { background: #fee; }\n"
    + ".detail-table { margin: 0; box-shadow: none; border-radius: 0; table-layout: fixed; }\n"
    + ".detail-table td { word-wrap: break-word; overflow-wrap: break-word;\n"
    + "  position: relative; padding-right: 30px; }\n"
    + ".copy-btn { position: absolute; top: 6px; right: 6px;\n"
    + "  background: #edf2f7; border: 1px solid #e2e8f0; border-radius: 4px;\n"
    + "  cursor: pointer; padding: 2px 5px; font-size: 0.75em; color: #718096;\n"
    + "  opacity: 0; transition: opacity 0.15s; line-height: 1; }\n"
    + ".detail-table td:hover .copy-btn { opacity: 1; }\n"
    + ".copy-btn:hover { background: #e2e8f0; color: #2d3748; }\n"
    + ".copy-btn.copied { background: #c6f6d5; color: #276749; border-color: #9ae6b4; }\n"
    + ".detail-table .col-req { width: 12%; }\n"
    + ".detail-table .col-msg { width: 44%; }\n"
    + ".detail-table .col-loc { width: 44%; }\n"
    + ".req-id { font-weight: 600; white-space: nowrap; color: #c53030; }\n"
    + ".location { font-size: 0.85em; color: #718096; }\n"
    + ".xpath { font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;\n"
    + "  font-size: 0.85em; word-break: break-all; background: #f7fafc;\n"
    + "  padding: 2px 6px; border-radius: 3px; display: inline-block; max-width: 100%; }\n"
    + ".no-details { padding: 16px; color: #a0aec0; font-style: italic; }\n"
    + ".no-report { color: #a0aec0; font-style: italic; font-size: 0.9em; }\n"
    + ".footer { text-align: center; padding: 20px; color: #a0aec0; font-size: 0.85em; }\n";
  // @formatter:on
}
