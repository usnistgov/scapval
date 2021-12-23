Security Content Automation Protocol (SCAP) Validator ${project.build.finalName}

Overview:
---------
Security Content Automation Protocol Validator (SCAPVal) is a Java Command Line Application that provides information
about whether SCAP content conforms to conventions and recommendations outlined in NIST Special Publication 800-126,
The Technical Specification for the Security Content Automation Protocol (SCAP). With support for SCAP 1.1, 1.2, and 1.3

SCAPVal validates the data stream according to one of the use cases for an SCAP-validated tool listed in 800-126,
namely Compliance Checking, Vulnerability Scanning, Inventory Scanning, and Other.
Checks components and data streams against appropriate schemas. Uses Schematron to perform additional checks within and across component data streams.

Stand alone XCCDF, OVAL, and OCIL files, separate from SCAP can also be validated using -componentfile.

SCAPVal produces validation results in a report that conveys all error and warning conditions detected; results are output in both XML and HTML formats.

For a listing of the SCAP requirements this tool adheres to, refer to the files in the /requirements directory.

Requires Java Runtime Environment (JRE) 1.8 or higher.
If the JAVA_HOME environment variable is set then the scapval launch script will use the version specified.
Otherwise, it will use whatever version is available from the current working directory.

The provided scapval.bat file should be used to run the tool in Windows and the scapval.sh for macOS or Linux/Unix.

Example Usage:
--------------
For a 1.3 Source Data Stream running in Windows:
    "scapval.bat -scapversion 1.3 -file source_data_stream_collection_sample.xml"

For a 1.3 Result Data Stream running in macOS:
    "./scapval.sh -scapversion 1.3 -resultfile arf-result.xml"

For a 1.2 Source Data Stream with resolution of remote resources and verbose output running in Linux:
     "./scapval.sh -scapversion 1.2 -file datastream-12.xml -online -debug"

Results for the above are provided in validation-report.html and validation-result.xml

Usage Details:
----
scapval <options>
 -combinedoutput <arg>         Creates an optional file for reference
                               containing any combined remote resources
                               processed by SCAPVal and any -sourceds Data
                               Stream specified. This file is a copy of
                               the final content SCAPVal validates
                               against.
 -componentfile <arg>          Validate an individual component file.
                               Currently XCCDF/OVAL/OCIL is supported
 -createsigconfig <arg>        First step to sign content, creates a
                               signing configuration file. Requires 8
                               arguments, see README.txt for details
 -debug                        Enable verbose output
 -dir <arg>                    Directory of individual component SCAP
                               files. Provide if validating SCAP 1.1
                               source files only
 -file <arg>                   SCAP Source XML file (SCAP 1.2, 1.3) or ZIP
                               file (SCAP 1.1). Only provide if validating
                               source files
 -h,--help                     Display the available cli arguments
 -listcertificatealias <arg>   Lists available certificate by aliases.
                               Provide path to a Java Keystore (JKS) file,
                               "MSCAPI" to show the certificates installed
                               in Windows.
 -maxsize <arg>                Overwrites the default max download size
                               for remote references (specified in MiB)
 -online                       Enable download of latest dictionaries and
                               remote resolution of some components
 -quiet                        Silence console output
 -resultdir <arg>              Directory of individual component SCAP
                               result files. Provide if validating SCAP
                               1.1 result files only
 -resultfile <arg>             SCAP result XML file (SCAP 1.2, 1.3) or ZIP
                               file (SCAP 1.1). Only provide if validating
                               result files
 -scapversion <arg>            The SCAP version to validate. 1.1, 1.2, and
                               1.3 are supported
 -showcertificate <arg>        Shows a certificate. First argument is a
                               Java Keystore (JKS) file path, or specify
                               "MSCAPI" to show a certificate installed in
                               Windows. Second argument is a certificate
                               aliast name.
 -signcontent <arg>            Second and final step to sign content,
                               specify path to the configuration file
                               created in first step by -createsigconfig
 -sourceds <arg>               Specifies the location of the source data
                               stream to include with results. For SCAP
                               1.1 it will be included in the 1.1 Data
                               Stream, for SCAP 1.2 and 1.3 it will
                               included in ARF Report
 -usecase <arg>                The SCAP use case. For 1.1 content
                               CONFIGURATION, VULNERABILITY_XCCDF_OVAL,
                               SYSTEM_INVENTORY, OVAL_ONLY For 1.2/1.3
                               content CONFIGURATION, VULNERABILITY,
                               INVENTORY, OTHER This is required for
                               validation of .zip files or a directory of
                               component SCAP files
 -validatesignature <arg>      Checks for validity of signed content.
                               First argument must be a signed XML
                               document. Second argument must be a Java
                               Keystore (JKS) file, or specify "MSCAPI" to
                               use a certificate installed in Windows.
                               Third argument must be the alias name of
                               the certificate of the trusted root to use
                               in the keystore.
 -valreportfile <FILE>         The validation HTML report file location
                               (default: validation-report.html)
 -valresultfile <FILE>         The validation result file location
                               (default: validation-result.xml)
 -version                      Display the version of the tool

Use Notes:
----
Once the validation is completed, two result files will be created:

  validation-result.xml - An XML file containing the set of requirements used
      for validation, and the status of each requirement.
  validation-report.html - A human-readable report based on the validations
      results.

If remote resources are defined in content, SCAPVal will attempt to resolve them when when ran with the -online parameter.
The remote content will be downloaded and automatically combined with the local content before validation begins.
When validating result content and the source content is specified with -source_ds, SCAPVal will combine the two before validation begins.
SCAPVal will provide a combined content file for reference when using the -combinedcontent parameter to specify an output file location.

As of SCAPVal 1.3.6 SCAP content signing and signature validation has be integrated from https://sourceforge.net/projects/secautotrust/
To Sign content, a 2 step process is required:
    First, use the -createsigconfig option followed by 8 arguments:
        The first argument MUST be a file path where the output configuration is written.
        The second argument MUST be an SCAP 1.2 data stream.
        The third argument MUST be a file path where the signed SCAP data stream will be written to.
        The fourth argument MUST be the digest algorithm to use. Valid values are SHA1, SHA256, SHA512
        The fifth argument MUST be the signature algorithm to use. Valid values are DSA_SHA1, RSA_SHA1, RSA_SHA256
        The sixth argument MUST be a Java Keystore (JKS) file, or specify "MSCAPI" to use a certificate installed in Windows.
        The seventh argument MUST be the alias of the certificate to use to sign the content.
        The eighth argument MUST be "true" or "false", to indicate if external references should be signed.
    e.g. scapval -createsigconfig working_dir\scap-config.xml scap-data-stream.xml working_dir\scap-data-stream-signed.xml SHA256 RSA_SHA256 test.jks test false

    Second, use the -signcontent option followed by the path to the signing configuration file created from the step above. Provide passwords as required.
    e.g. scapval -signcontent working_dir\scap-config.xml

To Validate the signature of signed SCAP content, use the -validatesignature option followed by 3 arguments:
    The first argument must be the path to a signed XML document.
    The second argument must be the path to a Java Keystore (JKS) file, or specify "MSCAPI" to use a certificate installed in Windows.
    The third argument must be the alias name of the certificate of the trusted root to use in the keystore. Provide passwords as required.
    e.g. scapval -validatesignature working_dir\scap-data-stream-signed.xml test.jks test-alias

To View certificate details, use -showcertificate followed by an argument with the specified
    Java Keystore (JKS) file path, or specify "MSCAPI" to show the certificates installed in Windows then a second argument with the alias name
    e.g. scapval -showcertificate test.jks test-alias

To View and list certificates aliases, use -listcertificatealias followed by a single argument with the specified
    Java Keystore (JKS) file path, or specify "MSCAPI" to show the certificates installed in Windows.
    e.g. scapval test.jks
    e.g. scapval MSCAPI

Feedback:
---------
Please send tool defects reports, enhancement requests, and any other related
comments by email to scap@nist.gov.

Changelog:
----------
Version 1.3.6
- Integrated content signing and validation functionality from https://sourceforge.net/projects/secautotrust/
- Added OVAL systems characteristics schematron rules for 5.11.2 content
- Updated several dependencies to resolve security and functionality issues

Version 1.3.5
- Including user provided arguments in the SCAPVal report now.
- Fixes to A-27-1 schematron assert

Version 1.3.4
- Improved messaging when problems detected with remote components.
- Removed .xml file extension check for remote references.
- Now utilizing xercesImpl-2.12.0 dependency as implemented in Decima

Version 1.3.3
- Added Bash launch script for macOS and *NIX usage
- Resolved an error when running with later versions of Java
- Updated various OVAL rules per OVALProject/Language on GitHub

Version 1.3.2
- Generated and added OVAL 5.11.1 schematron files and improved handling of 5.11.x OVAL schematrons.

Version 1.3.1
- Support for SCAP 1.3 added including new requirements from 800-126r3.
- Support for legacy SCAP 1.0 dropped.
- SCAPVal has been rewritten and now utilizing the NIST Decima Framework
- New HTML reporting format.

