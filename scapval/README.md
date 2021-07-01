# SCAPVal
> A command line tool to validate SCAP content.

## Overview:
Security Content Automation Protocol Validator (SCAPVal) is a Java Command Line Application that provides information
about whether SCAP content conforms to conventions and recommendations outlined in [NIST Special Publication 800-126 
Rev.3],
The Technical Specification for the Security Content Automation Protocol (SCAP). With support for SCAP 1.1, 1.2, and 1.3

SCAPVal validates the data stream according to one of the use cases for an SCAP-validated tool listed in 800-126,
namely Compliance Checking, Vulnerability Scanning, Inventory Scanning, and Other. 
Additionally, SCAPVal checks components and data streams against appropriate schemas and uses Schematron to perform additional checks within and across component data streams.

Stand-alone XCCDF, OVAL, and OCIL files, separate from SCAP can also be validated using -componentfile.

SCAP XML content can be signed and signatures verified as well, see usage -h for details.

SCAPVal produces validation results in a report that conveys all error and warning conditions detected; results are output in both XML and HTML formats.

For a listing of the SCAP requirements this tool adheres to, refer to the files in the /requirements directory.

Requires Java Runtime Environment (JRE) 1.8 or higher.
If the JAVA_HOME environment variable is set, then the scapval launch script will use the version specified.
Otherwise, it will use whatever version is available from the current working directory.

The provided scapval.bat file should be used to run the tool in Windows and the scapval.sh for macOS or Linux/Unix.

## Example Usage:
For a 1.3 Source Data Stream running in Windows:

    `scapval.bat -scapversion 1.3 -file source_data_stream_collection_sample.xml`

For a 1.3 Result Data Stream running in macOS::

    `./scapval.sh -scapversion 1.3 -resultfile arf-result.xml`

For a 1.2 Source Data Stream with resolution of remote resources and verbose output running in Linux:

     `./scapval.sh -scapversion 1.2 -file datastream-12.xml -online -debug`

Results for the above are provided in validation-report.html and validation-result.xml

Usage Details:
----
`-h`

Use Notes:
----
Once the validation is complete, two result files will be created:

  *validation-result.xml* - An XML file containing the set of requirements used
      for validation, and the status of each requirement.
  *validation-report.html* - A human-readable report based on the validations
      results.

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

[NIST Special Publication 800-126 Rev.3]:http://csrc.nist.gov/publications/PubsDrafts.html
[License Information]:https://github.com/usnistgov/scapval/blob/master/scapval/src/main/distro/NOTICE.txt
[README.txt]:https://github.com/usnistgov/scapval/blob/master/scapval/src/main/distro/README.txt