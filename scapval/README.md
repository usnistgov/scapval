# SCAPVal
> A command line tool to validate SCAP content.

## Overview:
Security Content Automation Protocol Validator (SCAPVal) is a Java Command Line Application that provides information
about whether SCAP content conforms to conventions and recommendations outlined in [NIST Special Publication 800-126 
Rev.4],
The Technical Specification for the Security Content Automation Protocol (SCAP). With support for SCAP 1.1, 1.2, 1.3, and 1.4

SCAPVal validates the data stream according to one of the use cases for an SCAP-validated tool listed in 800-126,
namely Compliance Checking, Vulnerability Scanning, Inventory Scanning, and Other. 
Additionally, SCAPVal checks components and data streams against appropriate schemas and uses Schematron to perform additional checks within and across component data streams.

Stand-alone XCCDF, OVAL, and OCIL files, separate from SCAP can also be validated using -componentfile.

The `-auto` option accepts any SCAP XML file and automatically detects whether it is source content, result content, or a standalone component.
The `-batchdir` option validates all XML files in a directory, auto-detecting the content type and SCAP version of each file.

SCAP XML content can be signed and signatures verified as well, see usage -h for details.

SCAPVal produces validation results in a report that conveys all error and warning conditions detected; results are output in both XML and HTML formats.

For a listing of the SCAP requirements this tool adheres to, refer to the files in the /requirements directory.

Requires Java Runtime Environment (JRE) 11 or higher.
If the JAVA_HOME environment variable is set, then the scapval launch script will use the version specified.
Otherwise, it will use the java executable available on your PATH.
On macOS and Linux/Unix, `scapval.sh` verifies that the selected Java runtime is version 11 or higher before launch and fails fast with an error if the runtime is missing, invalid, or too old.
On Windows, `scapval.bat` verifies that the selected Java runtime is version 11 or higher before launch and fails fast with an error if the runtime is missing, invalid, or too old.

Diagnostics:
Optional diagnostics output can be enabled for troubleshooting startup and runtime issues by setting SCAPVAL_DIAGNOSTICS (or system property scapval.diagnostics) to `1`, `true`, `yes`, or `on`.
When enabled, SCAPVal prints Java/runtime, classpath, and launch-context information.

The provided scapval.bat file should be used to run the tool in Windows and the scapval.sh for macOS or Linux/Unix.

## Example Usage:
The `-scapversion` parameter is optional. SCAPVal will auto-detect the SCAP version from the content if not specified.

For a Source Data Stream running in Windows (version auto-detected):

    `scapval.bat -file source_data_stream_collection_sample.xml`

For a Source Data Stream running in macOS (version auto-detected):

    `./scapval.sh -file source_data_stream_collection_sample.xml`

For a Result Data Stream running in macOS (version auto-detected):

    `./scapval.sh -resultfile arf-result.xml`

The `-scapversion` parameter can still be specified explicitly if desired:

    `scapval.bat -scapversion 1.4 -file source_data_stream_collection_sample.xml`
    `./scapval.sh -scapversion 1.3 -resultfile arf-result.xml`

Auto-detect content type (source, result, or component) and SCAP version:

    `scapval.bat -auto any-scap-file.xml`
    `./scapval.sh -auto arf-result.xml`

Validate all XML files in a directory (auto-detect each file):

    `scapval.bat -auto /path/to/scap-content/`
    `./scapval.sh -auto /path/to/scap-content/`
    `scapval.bat -batchdir /path/to/scap-content/`

For a Source Data Stream with resolution of remote resources and verbose output running in Linux:

     `./scapval.sh -file datastream.xml -online -debug`

Output filenames are derived from the input filename (e.g., `my-content-validation-result.xml` and `my-content-validation-report.html`).
Use `-valresultfile` and `-valreportfile` to override the output filenames.

Usage Details:
----
`-h`

Use Notes:
----
Once the validation is complete, two result files will be created based on the input filename:

  *\<input-filename\>-validation-result.xml* - An XML file containing the set of requirements used
      for validation, and the status of each requirement.
  *\<input-filename\>-validation-report.html* - A human-readable report based on the validations
      results.

Use `-valresultfile` and `-valreportfile` to specify custom output filenames.

If remote resources are defined in content, SCAPVal will attempt to resolve them when ran with the -online parameter.
The remote content will be downloaded and automatically combined with the local content before validation begins.
When validating result content and the source content is specified with -source_ds, SCAPVal will combine the two before validation begins.
SCAPVal will provide a combined content file for reference when using the -combinedcontent parameter to specify an output file location.

As of SCAPVal 1.3.6 SCAP content signing and signature validation has be integrated from https://sourceforge.net/projects/secautotrust/
See [README.txt] for details and example usage.

License:
---------
[License Information]

Feedback:
---------
Please send tool defects reports, enhancement requests, and any other related
comments by email to scap@nist.gov.

[NIST Special Publication 800-126 Rev.4]:http://csrc.nist.gov/publications/PubsDrafts.html
[License Information]:https://github.com/usnistgov/scapval/blob/master/scapval/src/main/distro/NOTICE.txt
[README.txt]:https://github.com/usnistgov/scapval/blob/master/scapval/src/main/distro/README.txt
