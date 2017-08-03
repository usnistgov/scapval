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
 * SHALL NASA BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM, OR
 * IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */

package gov.nist.scap.validation;

import gov.nist.decima.core.Decima;
import gov.nist.decima.core.assessment.AssessmentException;
import gov.nist.decima.core.assessment.AssessmentExecutor;
import gov.nist.decima.core.assessment.AssessmentReactor;
import gov.nist.decima.core.assessment.result.AssessmentResultBuilder;
import gov.nist.decima.core.assessment.result.AssessmentResults;
import gov.nist.decima.core.assessment.util.*;
import gov.nist.decima.core.document.DocumentException;
import gov.nist.decima.core.requirement.RequirementsManager;
import gov.nist.decima.core.requirement.RequirementsParserException;
import gov.nist.decima.module.cli.CLIParser;
import gov.nist.decima.module.cli.commons.cli.OptionEnumerationValidator;
import gov.nist.decima.xml.assessment.result.ReportGenerator;
import gov.nist.decima.xml.assessment.result.XMLResultBuilder;
import gov.nist.decima.xml.document.JDOMDocument;
import gov.nist.decima.xml.document.XMLDocument;
import gov.nist.decima.xml.schematron.SchematronCompilationException;
import gov.nist.scap.validation.candidate.ZipExpander;
import gov.nist.scap.validation.component.IndividualComponent;
import gov.nist.scap.validation.component.OVALVersion;
import gov.nist.scap.validation.exceptions.ConfigurationException;
import gov.nist.scap.validation.exceptions.SCAPException;
import gov.nist.scap.validation.utils.FileUtils;
import gov.nist.scap.validation.utils.SCAPUtils;
import gov.nist.scap.validation.utils.XMLUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gov.nist.decima.module.cli.CLIParser.*;
import static gov.nist.scap.validation.NamespaceConstants.NS_SOURCE_DS_1_2;
import static gov.nist.scap.validation.NamespaceConstants.NS_SOURCE_DS_1_3;

public class Application {

  private static final String OPTION_COMPONENT_FILE = "componentfile";
  private static final String OPTION_COMBINED_CONTENT_OUTPUT = "combinedoutput";
  private static final String OPTION_DIR = "dir";
  private static final String OPTION_FILE = "file";
  private static final String OPTION_RESULT_DIR = "resultdir";
  private static final String OPTION_RESULT_FILE = "resultfile";
  private static final String OPTION_MAX_SIZE = "maxsize";
  private static final String OPTION_ONLINE = "online";
  private static final String OPTION_SCAP_VERSION = "scapversion";
  private static final String OPTION_SOURCE_DS = "sourceds";
  private static final String OPTION_USECASE = "usecase";

  public enum FileType {
    DIRECTORY, ZIP, XML
  }

  public enum ContentType {
    SOURCE, RESULT, COMPONENT
  }

  private static CLIParser cliParser;
  private static SCAPVersion scapVersion;
  private static File contentToCheckFile;
  private static FileType contentToCheckFileType;
  private static ContentType contentToCheckType;
  private static String contentToCheckFilename;
  private static File sourcedsFile;
  private static FileType sourcedsFileType;
  private static String sourcedsFilename;
  private static String scapUseCase;
  private static XMLDocument XMLContentToValidate;
  private static XMLDocument XMLsourceds;
  private static Integer maxDownloadSize = 50; // default max download size in MiB
  private static File combinedOutput;
  private static boolean isOnline = false;

  private static final Logger log = LogManager.getLogger(Application.class);

  /**
   * Runs the application.
   *
   * @param args the command line arguments, not null
   */
  public static void main(String[] args) {
    Objects.requireNonNull(args,"args cannot be null.");

    int result;
    try {
      result = new Application().runCLI(args);
    } catch (ParseException | ConfigurationException | SCAPException | DocumentException e) {
      // These exceptions are somewhat expected and a simple error message is sufficient
      System.out.println(e.getMessage());
      result = 1;
    } catch (SchematronCompilationException | AssessmentException | JDOMException | SAXException
        | URISyntaxException | IOException | RequirementsParserException | TransformerException |
        RuntimeException e) {
      log.error("SCAPVal has encountered a problem and cannot continue with this validation." +
          "" + " - " + e);
      result = -1;
    }
    System.exit(result);
  }

  /**
   * Starts validation from a CLI interface.
   * Logging is sent to the screen as well as a generated XML and HTML report.
   *
   * @param args the command line arguments, not null
   * @return the exit code of SCAPVal
   * @throws SchematronCompilationException
   * @throws DocumentException
   * @throws AssessmentException
   * @throws JDOMException
   * @throws SAXException
   * @throws URISyntaxException
   * @throws IOException
   * @throws RequirementsParserException
   * @throws ParseException
   * @throws TransformerException
   * @throws ConfigurationException
   * @throws SCAPException
   */

  public int runCLI(String[] args) throws SchematronCompilationException, DocumentException,
      AssessmentException, JDOMException, SAXException, URISyntaxException, IOException,
      RequirementsParserException, ParseException, TransformerException, ConfigurationException,
      SCAPException {

    Objects.requireNonNull(args, "args cannot be null.");

    //print scapval version initially
    Messages.printVersion();

    //parse and validate the CLI args. var 'cmd' will be used later for report generation
    CommandLine cmd = parseCLI(args);

    //prepare things like updating data feeds, hashing submitted content, gathering validation
    // variables
    preAssessmentProcessing();

    //create and execute all the assessments for the submitted content
    SCAPValAssessmentResults scapValAssessmentResults = executeAssessments();

    //create the XML and HTML results files. The bootstrap location will be default so null
    generateResultsReport(scapValAssessmentResults, cmd, null);

    return 0;
  }

  /**
   * Starts validation from programmatic interface. This is utilized by SCAPValWrapper.
   * All results are returned in an AssessmentResults Object and an optional HTML report is
   * generated if -valreportfile is provided .
   *
   * @param args the command line arguments, not null
     @param bootstrapLocation a URI that will be used in the HTML report to locate the required bootstrap files, can be null
   * @return the AssessmentResults of the validation
   * @throws SchematronCompilationException
   * @throws DocumentException
   * @throws AssessmentException
   * @throws JDOMException
   * @throws SAXException
   * @throws URISyntaxException
   * @throws IOException
   * @throws RequirementsParserException
   * @throws ParseException
   * @throws TransformerException
   * @throws ConfigurationException
   * @throws SCAPException
   */
  protected SCAPValAssessmentResults runProgrammatic(String[] args, URI bootstrapLocation) throws
      SchematronCompilationException, DocumentException, AssessmentException, JDOMException,
      SAXException, URISyntaxException, IOException, RequirementsParserException, ParseException,
      TransformerException, ConfigurationException, SCAPException {

    Objects.requireNonNull(args, "args cannot be null.");

    //print scapval version initially
    Messages.printVersion();

    //parse and validate the CLI args.
    CommandLine cmd = parseCLI(args);

    //prepare things like updating data feeds, hashing submitted content, gathering validation
    // variables
    preAssessmentProcessing();

    //create and execute all the assessments for the submitted content
    SCAPValAssessmentResults scapValAssessmentResults = executeAssessments();

    //optionally generate XML results and HTML report
    if(cmd.getOptionValue(OPTION_VALIDATION_REPORT_FILE) != null) {
      generateResultsReport(scapValAssessmentResults, cmd, bootstrapLocation);
    }

    return scapValAssessmentResults;
  }

  /**
   * Performs the initial parsing and validation of command line arguments.
   * Several options are provided or handled by Decima
   *
   * @param args the command line arguments, not null
   * @return a CommandLine object containing the parsed params
   * @throws ParseException
   * @throws ConfigurationException
   * @throws SCAPException
   * @throws IOException
   * @throws DocumentException
   */
  protected CommandLine parseCLI(String[] args) throws ParseException, ConfigurationException,
      SCAPException, IOException, DocumentException {
    Objects.requireNonNull(args, "args cannot be null.");

    cliParser = new CLIParser("scapval <options>");
    cliParser.setVersion(Messages.getVersion());
    // required SCAP source or results content to check
    OptionGroup contentToCheck = new OptionGroup();
    contentToCheck.addOption(Option.builder(OPTION_DIR).desc("Directory of individual component "
        + "SCAP files. Provide if validating SCAP 1.1 source files only").hasArg().build());
    contentToCheck.addOption(Option.builder(OPTION_FILE).desc("SCAP Source XML file (SCAP 1.2, "
        + "1.3) or ZIP file (SCAP 1.1). Only provide if validating source files").hasArg().build());
    contentToCheck.addOption(Option.builder(OPTION_RESULT_DIR).desc("Directory of individual " +
        "component SCAP result files. Provide if validating SCAP 1.1 result files only").hasArg()
        .build());
    contentToCheck.addOption(Option.builder(OPTION_RESULT_FILE).desc("SCAP result XML file (SCAP " +
        "" + "1.2, 1.3) or ZIP file (SCAP 1.1). Only provide if validating result files").hasArg
        ().build());
    contentToCheck.addOption(Option.builder(OPTION_COMPONENT_FILE).desc("Validate an individual "
        + "component file. Currently XCCDF/OVAL/OCIL is supported").hasArg().build());
    cliParser.addOptionGroup(contentToCheck);

    // specified and supported SCAP version
    Option optionScapVersion = Option.builder(OPTION_SCAP_VERSION).desc("The SCAP version to " +
        "validate. 1.1, 1.2, and 1.3 are supported").hasArg().build();
    OptionEnumerationValidator scapVersionValidator = new OptionEnumerationValidator
        (optionScapVersion);
    scapVersionValidator.addAllowedValue("1.1");
    scapVersionValidator.addAllowedValue("1.2");
    scapVersionValidator.addAllowedValue("1.3");
    cliParser.addOption(scapVersionValidator);

    // other various options
    Option optionDatastreamOutput = Option.builder(OPTION_COMBINED_CONTENT_OUTPUT).desc("Creates " +
        "" + "an optional file for reference containing any combined remote resources processed " +
        "by " + "SCAPVal and any -sourceds Data Stream specified. " + "This file is a copy of the" +
        " final " + "content SCAPVal validates against.").hasArg().build();
    Option optionOnline = Option.builder(OPTION_ONLINE).desc("Enable download of latest " +
        "dictionaries and remote resolution of some components").hasArg(false).build();
    Option optionMaxSize = Option.builder(OPTION_MAX_SIZE).desc("Overwrites the default max " +
        "download size for remote references (specified in MiB)").hasArg().build();
    Option optionSourceDS = Option.builder(OPTION_SOURCE_DS).desc("Specifies the location of the " +
        "" + "source data stream to include with results. " + "For SCAP 1.1 it will be included " +
        "in the" + " 1.1 Data Stream, for SCAP 1.2 and 1.3 it will included in ARF Report")
        .hasArg().build();
    Option optionUseCase = Option.builder(OPTION_USECASE).desc("required for .ZIP files or " +
        "-directory of individual component SCAP files. " + "One of -file or -dir is required " +
        "option, the SCAP use case, one of " + "[[CONFIGURATION, VULNERABILITY_XCCDF_OVAL, " +
        "VULNERABILITY_OVAL, VULNERABILITY, OVAL_ONLY, OTHER, SYSTEM_INVENTORY, INVENTORY]] " +
        "" + "(use OVAL_ONLY for SCAP 1.1, and OTHER for SCAP 1.2/1.3)").hasArg().build();

    cliParser.addOption(optionDatastreamOutput).addOption(optionOnline).addOption(optionMaxSize)
        .addOption(optionSourceDS).addOption(optionUseCase);

    if (args.length == 0) {
      throw new ConfigurationException("Must provide command line arguments");
    }

    // account for the help params, if not the usage message will be displayed twice
    if (args[0].equals("-h") || args[0].equals("--help)")) {
      throw new ConfigurationException("");
    }

    // parse the args
    CommandLine commandLine = cliParser.parse(args);

    if (commandLine == null) {
      // CLIParser will display an error message before exiting
      throw new ConfigurationException("Unable to read command line arguments.");
    }
    // now validate all the args and store settings in Application instance variables.
    // If something is incorrect here the tools usage will be printed
    validateCLI(commandLine);

    return commandLine;
  }

  /**
   * Performs various checks against user specified content to make sure the provided CLI params and
   * provided content compatible and correct before SCAPVal validation begins.
   *
   * @param cmd the parsed args as a CommandLine from parseCLI(), not null
   * @throws ConfigurationException
   * @throws DocumentException
   * @throws IOException
   * @throws SCAPException
   */
  protected void validateCLI(CommandLine cmd) throws ConfigurationException, DocumentException,
      IOException, SCAPException {
    Objects.requireNonNull(cmd, "cmd cannot be null.");
    // gather the type of check and file type to check.
    // The OptionGroup ensures only 1 of these can be set (mutually exclusive)
    if (cmd.getOptionValue(OPTION_DIR) != null) {
      contentToCheckType = ContentType.SOURCE;
      contentToCheckFilename = cmd.getOptionValue(OPTION_DIR);
    } else if (cmd.getOptionValue(OPTION_FILE) != null) {
      contentToCheckType = ContentType.SOURCE;
      contentToCheckFilename = cmd.getOptionValue(OPTION_FILE);
    } else if (cmd.getOptionValue(OPTION_RESULT_DIR) != null) {
      contentToCheckType = ContentType.RESULT;
      contentToCheckFilename = cmd.getOptionValue(OPTION_RESULT_DIR);
    } else if (cmd.getOptionValue(OPTION_RESULT_FILE) != null) {
      contentToCheckType = ContentType.RESULT;
      contentToCheckFilename = cmd.getOptionValue(OPTION_RESULT_FILE);
    } else if (cmd.getOptionValue(OPTION_COMPONENT_FILE) != null) {
      contentToCheckType = ContentType.COMPONENT;
      contentToCheckFilename = cmd.getOptionValue(OPTION_COMPONENT_FILE);
    }

    //get the scap version from the command line
    scapVersion = SCAPVersion.getByString(cmd.getOptionValue(OPTION_SCAP_VERSION));

    //For validating individual XCCDF, OVAL, and OCIL component files.
    if (contentToCheckType.equals(ContentType.COMPONENT)) {
      if (cmd.getOptionValue(OPTION_SCAP_VERSION) != null) {
        throw new ConfigurationException("-scapversion cannot be specified with -componentfile");
      }
      if (cmd.getOptionValue(OPTION_SOURCE_DS) != null) {
        throw new ConfigurationException("-sourceds cannot be specified with -componentfile");
      }
    } else {
      if (scapVersion == null) {
        throw new ConfigurationException("-scapversion must be specified");
      }
    }

    // first make sure we can read the specified content to check
    contentToCheckFile = new File(contentToCheckFilename);
    if (!contentToCheckFile.canRead()) {
      throw new ConfigurationException("Unable to read the specified content to validate: " +
          contentToCheckFilename);
    }

    // per scapval 1.2, is SCAP 1.1 results are checked a -sourceds must be present
    if ((cmd.getOptionValue(OPTION_RESULT_DIR) != null || cmd.getOptionValue(OPTION_RESULT_FILE)
        != null) && cmd.getOptionValue(OPTION_SOURCE_DS) == null && scapVersion.equals(SCAPVersion.V1_1)) {
      throw new ConfigurationException("When -resultfile or -resultdir and SCAP 1.1 is specified," +
          "" + " the source content is also required with -sourceds");
    }

    // if -sourceds is specified, make sure we can read it and its file type is appropriate
    if (cmd.getOptionValue(OPTION_SOURCE_DS) != null) {
      sourcedsFilename = cmd.getOptionValue(OPTION_SOURCE_DS);
      sourcedsFile = new File(sourcedsFilename);
      if (!sourcedsFile.canRead()) {
        throw new ConfigurationException("Unable to read the -sourceds content specified: " +
            sourcedsFilename);
      }
      sourcedsFileType = FileUtils.determineFileType(sourcedsFilename);
      if (scapVersion.equals(SCAPVersion.V1_1)) {
        //1.1 sourceds must be zip or dir
        if (!sourcedsFileType.equals(FileType.ZIP) && !sourcedsFileType.equals(FileType
            .DIRECTORY)) {
          throw new ConfigurationException("For SCAP 1.1 content the specified -sourceds must be " +
              "" + "a ZIP file or Directory");
        }

      } else {
        //sourceds for 1.2 and 1.3 must be an xml file
        if (!sourcedsFileType.equals(FileType.XML)) {
          throw new ConfigurationException("For SCAP 1.2 or 1.3 content the specified -sourceds "
              + "must be a XML file");
        }
      }
    }

    // make sure the content specified matches the correct arg type
    contentToCheckFileType = FileUtils.determineFileType(contentToCheckFilename);

    switch (contentToCheckFileType) {
      case DIRECTORY:
        if (cmd.getOptionValue(OPTION_FILE) != null || cmd.getOptionValue(OPTION_RESULT_FILE) !=
            null || cmd.getOptionValue(OPTION_COMPONENT_FILE) != null) {
        throw new ConfigurationException(contentToCheckFilename + " -resultfile is specified " +
              "so" + " it must be a file, not a directory");
        }
        if (cmd.getOptionValue(OPTION_USECASE) == null) {
        throw new ConfigurationException("When checking directory contents, -usecase must be "
              + "provided");
        }
        if (!scapVersion.equals(SCAPVersion.V1_1)) {
          throw new ConfigurationException("Directory checks are only supported with SCAP 1.1");
        }
        if (contentToCheckType.equals(ContentType.COMPONENT)) {
          throw new ConfigurationException("Only -file can be used to specify individual " +
              "component files to validate");
        }
        break;
      case ZIP:
        if (cmd.getOptionValue(OPTION_FILE) == null && cmd.getOptionValue(OPTION_RESULT_FILE) ==
            null && scapVersion != SCAPVersion.V1_1) {
          throw new ConfigurationException(contentToCheckFilename + " is a .zip file. Only " +
              "version SCAP 1.1 checks supports zip");
        }
        if (cmd.getOptionValue(OPTION_USECASE) == null) {
          throw new ConfigurationException("When checking .zip file contents, -usecase must be "
              + "provided");
        }
        if (contentToCheckType.equals(ContentType.COMPONENT)) {
          throw new ConfigurationException("Only -file can be used to specify individual " +
              "component files to validate");
        }
        break;
      case XML:
        if (scapVersion == SCAPVersion.V1_1) {
          throw new ConfigurationException(contentToCheckFilename + " SCAP 1.1 file checks " +
              "cannot" + " be XML");
        }
        if (cmd.getOptionValue(OPTION_DIR) != null || cmd.getOptionValue(OPTION_RESULT_DIR) !=
            null) {
          throw new ConfigurationException(contentToCheckFilename + " must be a directory, not a " +
              "" + "file");
        }
        break;
      default:
        throw new ConfigurationException(contentToCheckFilename + " is not valid");
    }

    if (cmd.hasOption(OPTION_ONLINE)) {
      isOnline = true;
    }

    // make sure usecase specified is valid
    if (cmd.getOptionValue(OPTION_USECASE) != null) {
      scapUseCase = cmd.getOptionValue(OPTION_USECASE).toUpperCase();
      boolean useCaseIsValid = false;
      for (String useCase : scapVersion.getUseCases()) {
        if (useCase.equals(scapUseCase)) useCaseIsValid = true;
      }
      if (!useCaseIsValid) {
        throw new ConfigurationException("the usecase " + scapUseCase + " is not valid for SCAP "
            + "version " + scapVersion.getVersion());
      }
    }

    if (contentToCheckType.equals(ContentType.COMPONENT) && cmd.getOptionValue(OPTION_USECASE) !=
        null) {
      throw new ConfigurationException("-usecase is not specified when validating individual " +
          "component files");
    }

    // if maxsize is included, make sure its valid and convert to int
    if (cmd.getOptionValue(OPTION_MAX_SIZE) != null) {
      try {
        maxDownloadSize = Integer.parseInt(cmd.getOptionValue(OPTION_MAX_SIZE));
      } catch (NumberFormatException nfe) {
        throw new ConfigurationException("-maxsize must be a number (specified in MiB)");
      }
    }

    // handle when -XMLsourceds is specified
    if (cmd.getOptionValue(OPTION_SOURCE_DS) != null) {
      //this option should only be specified when checking result content
      if (!contentToCheckType.equals(ContentType.RESULT)) {
        throw new ConfigurationException("-XMLsourceds can only be specified when validation " +
            "result content");
      }

      // the specified XML sourceds will ultimately be combined with the result content
      if (cmd.getOptionValue(OPTION_COMBINED_CONTENT_OUTPUT) != null) {
        combinedOutput = new File(cmd.getOptionValue(OPTION_COMBINED_CONTENT_OUTPUT));
        if (!combinedOutput.exists()) {
          if (!combinedOutput.createNewFile() || !combinedOutput.canWrite()) {
            throw new IOException("Cannot write to: " + combinedOutput + " Please specify a " +
                "valid" + " location to " + "write the combined file to");
          }
        }
      } else {
        // user has not specified -combinedoutput we will set it by default and make sure we can
        // write to it
        File sourcedsCombinedOuput = new File(FileUtils.getFilenamePrefix(contentToCheckFilename)
            + "-with-data-stream.xml");
        if (!sourcedsCombinedOuput.exists()) {
          if (!sourcedsCombinedOuput.createNewFile() || !sourcedsCombinedOuput.canWrite()) {
            throw new IOException("Cannot write to: " + sourcedsCombinedOuput.getAbsolutePath() +
                " Please specify a valid location to " + "write the combined file to with " +
                "-combinedoutput");
          }
        }
        combinedOutput = sourcedsCombinedOuput;
      }

      if (scapVersion.equals(SCAPVersion.V1_1)) {
        //1.1 the specified XM Lsourceds must be a zip file or dir
        if (!FileUtils.determineFileType(cmd.getOptionValue(OPTION_SOURCE_DS)).equals(FileType
            .ZIP) && !FileUtils.determineFileType(cmd.getOptionValue(OPTION_SOURCE_DS)).equals
            (FileType.DIRECTORY)) {
          throw new ConfigurationException("-sourceds for 1.1 content must be an ZIP file or " +
              "Directory");
        }
      } else {
        //1.2 and 1.3 source ds must be an xml file
        if (!FileUtils.determineFileType(cmd.getOptionValue(OPTION_SOURCE_DS)).equals(FileType
            .XML)) {
          throw new ConfigurationException("-XMLsourceds must be an .XML file containing a " +
              "source-data-stream");
        }
      }
    }

    // handle when -combinedoutput is specified
    if (cmd.getOptionValue(OPTION_COMBINED_CONTENT_OUTPUT) != null) {
      // make sure it hasn't already been set (in the case of -XMLsourceds)
      if (combinedOutput == null) {
        combinedOutput = new File(cmd.getOptionValue(OPTION_COMBINED_CONTENT_OUTPUT));
        if (!combinedOutput.exists()) {
          if (!combinedOutput.createNewFile() || !combinedOutput.canWrite()) {
            throw new IOException("Cannot write to: " + combinedOutput + " Please specify a " +
                "valid" + " location to " + "write the combined file to");
          }
        }
      }
    }
  }

  /**
   * Starts loading user specified content to check for errors, updates online files and combines
   * remote resources
   * in before validation assessments begin.
   *
   * @throws IOException
   * @throws DocumentException
   * @throws SCAPException
   * @throws ConfigurationException
   * @throws URISyntaxException
   */
  protected static void preAssessmentProcessing() throws IOException, DocumentException,
      SCAPException, ConfigurationException, URISyntaxException {
    //first update any data feeds if online
    if (isOnline) {
      log.info("SCAPVal is in online mode. Checking for updates to CCE and CPE dictionary.");
      ValidationNotes.getInstance().createValidationNote("Validation occurred while in ONLINE " +
          "mode.");
      DataFeedDownloader.download(maxDownloadSize);
    } else {
      ValidationNotes.getInstance().createValidationNote("Validation occurred while in OFFLINE "
          + "mode.");
    }

    // All specified args should be validated now, load the SCAP doc/docs to assess
    switch (contentToCheckFileType) {
      case XML: //xml source for 1.2 and 1.3 only
        if (contentToCheckType.equals(ContentType.COMPONENT)) {
          log.info("Validating Component File: " + contentToCheckFilename + " (SHA-256: " +
              FileUtils.getFileHash(contentToCheckFile, FileUtils.DEFAULT_HASH_ALGORITHM) + ")");
        } else {
          log.info("Validating SCAP Content: " + contentToCheckFilename + " (SHA-256: " +
              FileUtils.getFileHash(contentToCheckFile, FileUtils.DEFAULT_HASH_ALGORITHM) + ")" +
              " with SCAP Version: " + scapVersion.getVersion());
        }
        XMLContentToValidate = new JDOMDocument(new File(contentToCheckFilename));
        //check for -sourceds (it will be an XML file will be 1.2 and 1.3 content only)
        if (sourcedsFile != null && contentToCheckType.equals(ContentType.RESULT)) {
          log.info("Including -sourceds File: " + sourcedsFilename + " (SHA-256: " + FileUtils
              .getFileHash(sourcedsFile, FileUtils.DEFAULT_HASH_ALGORITHM) + ")");
          XMLsourceds = new JDOMDocument(sourcedsFile);
          //merge the XMLsourceds with ARF
          ContentCombiner.mergeARFWithDS(XMLContentToValidate, XMLsourceds, combinedOutput);
          XMLContentToValidate = new JDOMDocument(combinedOutput);
        }
        break;
      case DIRECTORY: //only for SCAP 1.1
        log.info("Validating Directory: " + contentToCheckFilename);
        try {
          //if -sourceds is specified and a result check, include the source
          if (sourcedsFile != null && contentToCheckType.equals(ContentType.RESULT)) {
            if (sourcedsFileType.equals(FileType.DIRECTORY)) {
              log.info("Including -sourceds Directory: " + sourcedsFilename);
              // create the 1.1 DS from the specified source DIR to include with the result content
              XMLDocument sourceDSCombined = ContentCombiner.combineSCAP11(sourcedsFile,
                  scapUseCase, maxDownloadSize, isOnline, null);
              //now include this source ds with the results
              XMLContentToValidate = ContentCombiner.combineSCAP11(contentToCheckFile,
                  scapUseCase, maxDownloadSize, isOnline, sourceDSCombined);
              ValidationNotes.getInstance().createValidationNote("SCAP 1.1 content was merged " +
                  "into single file for validation: " + XMLContentToValidate.getSystemId() + " "
                  + "The is a temporary " + "file used for validation and is removed after " +
                  "validation. If you would like to keep it for reference use the " +
                  "-combinedoutput" + " option.");
            } else if (sourcedsFileType.equals(FileType.ZIP)) {
              //the specified -sourceds will need to be extracted first
              log.info("Including -sourceds ZIP File: " + sourcedsFile.getAbsolutePath());
              // only for SCAP 1.1, extract the files and then essentially run as directory
              // afterwards
              final ZipExpander zipExpander = new ZipExpander(1024);
              File sourceDSExtractDir = null;
              sourceDSExtractDir = zipExpander.expand(sourcedsFile);
              // create the 1.1 DS from the specified source ZIP to include with the result content
              XMLDocument sourceDSCombined = ContentCombiner.combineSCAP11(sourceDSExtractDir,
                  scapUseCase, maxDownloadSize, isOnline, null);
              //now include this source ds with the results
              XMLContentToValidate = ContentCombiner.combineSCAP11(contentToCheckFile,
                  scapUseCase, maxDownloadSize, isOnline, sourceDSCombined);
              ValidationNotes.getInstance().createValidationNote("SCAP 1.1 content was merged " +
                  "into single file for validation: " + XMLContentToValidate.getSystemId() + " "
                  + "The is a temporary " + "file used for validation and is removed after " +
                  "validation. If you would like to keep it for reference use the " +
                  "-combinedoutput" + " option.");
              //clean up the extracted files on JVM exit
              FileUtils.deleteDirOnExit(sourceDSExtractDir);
            }
          } else {
            // SCAP 1.1
            XMLContentToValidate = ContentCombiner.combineSCAP11(contentToCheckFile, scapUseCase,
                maxDownloadSize, isOnline, null);
            ValidationNotes.getInstance().createValidationNote("SCAP 1.1 content was merged into " +
                "" + "single file for validation: " + XMLContentToValidate.getSystemId() + " The " +
                "is a " + "temporary " + "file used for validation and is removed after " +
                "validation. If you " + "would like to keep it for reference use the " +
                "-combinedoutput option.");
          }
        } catch (JDOMException e) {
          throw new IOException("Problem with the directory file contents " + e.getMessage());
        }
        break;
      case ZIP: // only for SCAP 1.1, extract the files and then essentially run as directory
        // afterwards
        log.info("Validating ZIP File: " + contentToCheckFilename + " (SHA-256: " + FileUtils
            .getFileHash(contentToCheckFile, FileUtils.DEFAULT_HASH_ALGORITHM) + ")");
        final ZipExpander zipExpander = new ZipExpander(1024);
        File extractDir = null;
        try {
          File contentToCheckFile = new File(contentToCheckFilename);
          extractDir = zipExpander.expand(contentToCheckFile);
          //if -sourceds is specified and a result check, include the source
          if (sourcedsFile != null && contentToCheckType.equals(ContentType.RESULT)) {
            if (sourcedsFileType.equals(FileType.DIRECTORY)) {
              log.info("Including -sourceds Directory: " + sourcedsFilename);
              // create the 1.1 DS from the specified source DIR to include with the result content
              XMLDocument sourceDSCombined = ContentCombiner.combineSCAP11(sourcedsFile,
                  scapUseCase, maxDownloadSize, isOnline, null);
              //now include this source ds with the results
              XMLContentToValidate = ContentCombiner.combineSCAP11(extractDir, scapUseCase,
                  maxDownloadSize, isOnline, sourceDSCombined);
              ValidationNotes.getInstance().createValidationNote("SCAP 1.1 content was merged " +
                  "into single file for validation: " + XMLContentToValidate.getSystemId() + " "
                  + "The is a temporary " + "file used for validation and is removed after " +
                  "validation. If you would like to keep it for reference use the " +
                  "-combinedoutput" + " option.");
            } else if (sourcedsFileType.equals(FileType.ZIP)) {
              //the specified -sourceds will need to be extracted first
              log.info("Including -sourceds ZIP File: " + sourcedsFile.getAbsolutePath());
              // only for SCAP 1.1, extract the files and then essentially run as directory
              // afterwards
              File sourceDSExtractDir = null;
              sourceDSExtractDir = zipExpander.expand(sourcedsFile);
              // create the 1.1 DS from the specified source ZIP to include with the result content
              XMLDocument sourceDSCombined = ContentCombiner.combineSCAP11(sourceDSExtractDir,
                  scapUseCase, maxDownloadSize, isOnline, null);
              //now include this source ds with the results
              XMLContentToValidate = ContentCombiner.combineSCAP11(extractDir, scapUseCase,
                  maxDownloadSize, isOnline, sourceDSCombined);
              ValidationNotes.getInstance().createValidationNote("SCAP 1.1 content was merged " +
                  "into single file for validation: " + XMLContentToValidate.getSystemId() + " "
                  + "The is a temporary " + "file used for validation and is removed after " +
                  "validation. If you would like to keep it for reference use the " +
                  "-combinedoutput" + " option.");
              //clean up the extracted files on JVM exit
              FileUtils.deleteDirOnExit(sourceDSExtractDir);
            }
          } else {
            //otherwise process the content with no extra source content
            XMLContentToValidate = ContentCombiner.combineSCAP11(extractDir, scapUseCase,
                maxDownloadSize, isOnline, null);
            ValidationNotes.getInstance().createValidationNote("SCAP 1.1 content was merged into " +
                "" + "single file for validation: " + XMLContentToValidate.getSystemId() + " The " +
                "is a " + "temporary " + "file used for validation and is removed after " +
                "validation. If you " + "would like to keep it for reference use the " +
                "-combinedoutput option.");
          }
        } catch (IOException e) {
          throw new IOException("Problem extracting zip file " + e.getMessage());
        } catch (JDOMException e) {
          throw new IOException("Problem with the zip file contents " + e.getMessage());
        } finally {
          // The extracted files are referenced later in the in report generation process
          // make sure the files are available until when the JVM shuts down, then clean them up
          final File extractedDir = extractDir.getAbsoluteFile();
          FileUtils.deleteDirOnExit(extractedDir);
        }
        break;
      default:
        throw new ConfigurationException("Unable to determine the specified file type");
    }

    //make sure scap version specified matches the scap-version found in content
    String xpath = "//*[local-name()='data-stream']/@scap-version";
    List<Attribute> results = XMLUtils.getXpathAttributes(XMLContentToValidate, xpath);
    if (results == null) {
      throw new SCAPException("@scap-version not found in the specified content:" +
          contentToCheckFilename);
    }
    //check each data-stream for the specified scap version.
    for (Attribute attribute : results) {
      if (!scapVersion.getVersion().equals(attribute.getValue())) {
        throw new ConfigurationException("SCAP version specified on command line " + scapVersion
            .getVersion() + " Does not match what was found in the specified content " + results);
      }
    }

    //Additional SCAP content checks
    if (!contentToCheckType.equals(ContentType.COMPONENT)) {

      //attempt to resolve remote components references in SCAP data-stream
      if (isOnline) {
        ContentCombiner.mergeRemoteResourcesInDS(XMLContentToValidate, scapVersion,
            maxDownloadSize);
      }

      //make sure the namespace is correct for expected validation content
      String namespace = XMLContentToValidate.getJDOMDocument().getRootElement().getNamespaceURI();
      if (contentToCheckType.equals(ContentType.SOURCE)) {
        switch (scapVersion) {
          case V1_1:
            if (!namespace.equals(NamespaceConstants.NS_SOURCE_DS_1_1.getNamespaceString())) {
              throw new SCAPException("Unable to find the expected namespace for source content: " +
                  "" + "" + NamespaceConstants.NS_SOURCE_DS_1_1.getNamespaceString());
            }
            break;
          case V1_2:
            if (!namespace.equals(NS_SOURCE_DS_1_2.getNamespaceString())) {
              throw new SCAPException("Unable to find the expected namespace for source content: " +
                  "" + "" + NS_SOURCE_DS_1_2.getNamespaceString());
            }
            break;
          case V1_3:
            if (!namespace.equals(NS_SOURCE_DS_1_3.getNamespaceString())) {
              throw new SCAPException("Unable to find the expected namespace for source content: " +
                  "" + "" + NS_SOURCE_DS_1_3.getNamespaceString());
            }
            break;
        }
      } else if (contentToCheckType.equals(ContentType.RESULT)) {
        switch (scapVersion) {
          case V1_1:
            //there is no namespace for 1.1 results, only components
            break;
          case V1_2:
            if (!namespace.equals(NamespaceConstants.NS_RESULTS_DS_1_2.getNamespaceString())) {
              throw new SCAPException("Unable to find the expected namespace for result content: " +
                  "" + "" + NamespaceConstants.NS_RESULTS_DS_1_2.getNamespaceString());
            }
            break;
          case V1_3:
            if (!namespace.equals(NamespaceConstants.NS_RESULTS_DS_1_2.getNamespaceString())) {
              throw new SCAPException("Unable to find the expected namespace for result content: " +
                  "" + "" + NamespaceConstants.NS_RESULTS_DS_1_2.getNamespaceString());
            }
            break;
        }
      }

      //make sure included OVAL component versions are valid
      List<Element> ovalComponents = SCAPUtils.getOVALComponentsFromSCAPContent
          (XMLContentToValidate, scapVersion);
      if (ovalComponents == null) {
        //SCAP content should at least contain an OVAL component
        throw new SCAPException("Unable to locate valid OVAL components in: " +
            XMLContentToValidate.getOriginalLocation().getPath());
      } else {
        for (Element ovalComponent : ovalComponents) {
          OVALVersion ovalVersion = OVALVersion.getOVALVersion(ovalComponent);
          if (ovalVersion == null) {
            throw new SCAPException("Unable to find valid OVAL version in: " +
                XMLContentToValidate.getOriginalLocation().getPath());
          }
        }
      }

      //also check the oval_results OVAL version if this is a RESULT check
      if (contentToCheckType.equals(Application.ContentType.RESULT)) {
        List<Element> ovalResults = SCAPUtils.getOVALResultsFromSCAPContent(XMLContentToValidate,
            scapVersion);
        if (ovalResults == null) {
          log.info("Unable to locate valid OVAL results in: " + XMLContentToValidate
              .getOriginalLocation().getPath());
        } else {
          for (Element ovalComponent : ovalResults) {
            OVALVersion ovalVersion = OVALVersion.getOVALVersion(ovalComponent);
            if (ovalVersion == null) {
              throw new SCAPException("Unable to find valid OVAL results version in: " +
                  XMLContentToValidate.getOriginalLocation().getPath());
            }
          }
        }
      }
    }
  }

  /**
   * Creates and begins the required validation assessments after all user parameters have
   * been parsed and checked for initial correctness.
   *
   * @return the results of the combined assessments
   * @throws SCAPException
   * @throws DocumentException
   * @throws IOException
   */
  protected static SCAPValAssessmentResults executeAssessments() throws SCAPException,
      DocumentException, IOException {
    // First create the AssessmentFactory to manage requirements and assessments based on target
    // content
    AssessmentFactory assessmentFactory = new AssessmentFactory(scapVersion, scapUseCase,
        contentToCheckType, XMLContentToValidate);

    RequirementsManager requirementsManager = SCAPValReqManager.getRequirements(scapVersion);
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    AssessmentExecutor<XMLDocument> assessmentExecutor = assessmentFactory.newAssessmentExecutor
        (executorService);

    try {
      // The AssessmentReactor handles the execution of the assessments
      AssessmentReactor assessmentReactor = new AssessmentReactor(requirementsManager);
      // load overall document assessment this will include the SCAP file or Individual Component
      // file content
      assessmentReactor.pushAssessmentExecution(assessmentFactory.getXMLContentToValidate(),
          assessmentExecutor);

      // create the result builder and setup logging
      LoggingHandler loggingHandler = new TestResultLoggingHandler(requirementsManager);
      loggingHandler = new AssessmentLoggingHandler(Level.INFO, loggingHandler);
      loggingHandler = new AssessmentSummarizingLoggingHandler(Level.INFO, loggingHandler);
      loggingHandler = new OverallSummaryLoggingHandler(Level.INFO, loggingHandler);
      AssessmentResultBuilder builder = Decima.newAssessmentResultBuilder();
      builder.setLoggingHandler(loggingHandler);
      builder.addAssessmentTarget(XMLContentToValidate);
      if (combinedOutput != null) {
        //optionally write out the final content used in validation
        XMLContentToValidate.copyTo(combinedOutput);
      }

      AssessmentResults assessmentResults = assessmentReactor.react(builder);
      if(assessmentResults == null) {
        throw new RuntimeException("There was a problem generating assessment results.");
      }

      //validation is complete and all notes should be populated
      List<String> assessmentNotes = ValidationNotes.getInstance().getValidationNotes();

      return new SCAPValAssessmentResults(assessmentResults, assessmentNotes);

    } catch (AssessmentException e) {
      throw new RuntimeException("An error occurred while performing the assessment", e);
    } finally {
      executorService.shutdown();
    }
  }

  /**
   * Provides the required information to Decima for validation report generation and
   * customizes the SCAPVal report .xsl to augment the report as necessary.
   *
   * @param scapValAssessmentResults the results of validation, not null
   * @param cmd                      the CommandLine object storing parsed user params, not null
   * @param bootstrapLocation        an optional URI containing the location of the bootstrap dependency
   *                                 for the HTML report, overriding the default. This can be null
   * @throws IOException
   * @throws URISyntaxException
   * @throws TransformerException
   */
  protected static void generateResultsReport(SCAPValAssessmentResults scapValAssessmentResults,
                                              CommandLine cmd, URI bootstrapLocation) throws IOException,
      URISyntaxException, TransformerException {
    Objects.requireNonNull(scapValAssessmentResults, "scapValAssessmentResults cannot be null.");
    Objects.requireNonNull(cmd, "cmd cannot be null.");

    File validationResultFile;
    {
      String fileValue = cmd.getOptionValue(OPTION_VALIDATION_RESULT_FILE,
          DEFAULT_VALIDATION_RESULT_FILE);
      validationResultFile = new File(fileValue);
      File parentDir = validationResultFile.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        if (!parentDir.mkdirs()) {
          throw new RuntimeException("Problem creating folder specified for Result File: " +
              parentDir.getAbsolutePath());
        }
      }
    }

    File validationReportFile;
    {
      String fileValue = cmd.getOptionValue(OPTION_VALIDATION_REPORT_FILE,
          DEFAULT_VALIDATION_REPORT_FILE);
      validationReportFile = new File(fileValue);
      File parentDir = validationReportFile.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        if (!parentDir.mkdirs()) {
          throw new RuntimeException("Problem creating folder specified for Result Report: " +
              parentDir.getAbsolutePath());
        }
      }
    }

    XMLResultBuilder writer = new XMLResultBuilder();
    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(validationResultFile))) {
      log.info("Generating results XML report to: " + validationResultFile.getAbsolutePath());
      writer.write(scapValAssessmentResults.getAssessmentResults(), os);
    }

    ReportGenerator reportGenerator = new ReportGenerator();

    // because the packaged version of scapval is launched from a script which could be executed
    // from various current working dirs, check for location of a jar file, otherwise we'll use the
    // current working directory
    StringBuilder packagedPath = null;
    File bootsrapDirFile = null;
    File scapvalBaseXSLTemplateFile = null;
    String bootstrapDirPath = "";

    //Gather the bootstrap location used when generating the HTML report
    if (bootstrapLocation != null) {
      //if user specified location, just us that
      reportGenerator.setBootstrapPath(bootstrapLocation);
    }else{
      try {
        bootstrapDirPath = new URL("classpath:bootstrap/").openConnection().getURL().getPath();
        if (bootstrapDirPath.contains(".jar!")) {
        /* we are running from a packaged version and would get something like
        scapval-1.3.0\scapval-1.3.0.jar!\bootstrap
          so in order to use the proper bootstrap dir and account for running the .bat file from
          a different current working dir,
          we'll gather the location with some path parsing here. Since the path is obtained from
          URL.openConnection() the seperator should always be "/" */
          int dirsInPath = bootstrapDirPath.split("/").length;
          packagedPath = new StringBuilder("");
          for (int i = 0; i < dirsInPath - 2; i++) {
            packagedPath.append(bootstrapDirPath.split("/")[i] + "/");
          }
          //remove the prepended file: protocol added by URL
          bootstrapDirPath = packagedPath.toString().replace("file:", "") + "bootstrap";
        }
      } catch (IOException e) {
        //we are not running from a packaged version, try from current working directory
        bootstrapDirPath = "bootstrap";
      }

      bootsrapDirFile = new File(bootstrapDirPath);
    }

    if (bootsrapDirFile != null){
      try {
        reportGenerator.setBootstrapPath(bootsrapDirFile);
        } catch (IOException e) {
        throw new RuntimeException("Unable to locate bootstrap location for HTML report " +
                "generation:" + " " + bootsrapDirFile + " - " + e);
        }
      }

    //Gather the base scapval-report.xsl for customization
      scapvalBaseXSLTemplateFile = FileUtils.getTempFileFromResource("classpath:xsl/scapval-report.xsl");

      reportGenerator.setIgnoreNotTestedResults(false);
      reportGenerator.setIgnoreOutOfScopeResults(false);
      reportGenerator.setHtmlTitle("SCAPVal Validation Report");
      reportGenerator.setTestResultLimit(10);
      reportGenerator.setIgnoreNotTestedResults(true);

      //customize the xsl template which will be provided to Decima for HTML report generation
      ReportCustomizer customizer = new ReportCustomizer(scapvalBaseXSLTemplateFile, scapValAssessmentResults);
      customizer.customize(contentToCheckType, scapVersion, IndividualComponent.getByXML(XMLContentToValidate));
      //this XSL file should be appropriately customized after ReportCustomizer().customize() above
      reportGenerator.setXslTemplateExtension(scapvalBaseXSLTemplateFile);

    try {
      log.info("Generating results HTML report to: " + validationReportFile.getAbsolutePath());
      reportGenerator.generate(validationResultFile, validationReportFile);
    }
    catch (IOException | TransformerException e){
      throw new RuntimeException("Problems generating HTML report - " + e);
    }

  }

  /**
   * Writes out the command line tool usage.
   */
  public static void showHelp() {
    cliParser.doShowHelp();
  }

}