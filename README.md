# Security Content Automation Protocol Content Validator (SCAPVal)

> A command line tool to validate SCAP content.

## Overview

SCAPVal is a Java Command Line Application that provides information about whether SCAP content conforms to 
conventions and recommendations outlined in [NIST Special Publication 800-126 Rev.4],
The Technical Specification for the Security Content Automation Protocol (SCAP). With support for SCAP 1.2, 1.3, and 1.4

This repository contains the Security Content Automation Protocol Validator (SCAPVal) tool along with several required Java modules.

## Repository Contents

- create-file-list-mojo - A maven plugin required to build and run SCAPVal.
- scap-schematron-rules - Includes schematron rules required to validate with SCAPVal.
- scapval - The Java command line tool to validate SCAP content.

## Requirements

Requires Java Runtime Environment (JRE) 11 or higher.

If the JAVA_HOME variable is set, the JRE which is at that location will be used. Otherwise the JRE available on the path is used.

## Build Notes

SCAPVal utilizes Apache Maven. Once 'mvn' is available, from the root directory of the project, run:

`mvn clean install package`

This will build scapval and once complete will be ready for use in */scapval/target*

## License

[License Information]

## Feedback:

Please send tool defects reports, enhancement requests, and any other related comments by email to scap@nist.gov.
TODO: update the URL after the SP800-126r4 is final!
[NIST Special Publication 800-126 Rev.4]:https://csrc.nist.gov/publications/detail/sp/800-126/rev-4/final
[License Information]:https://github.com/usnistgov/scapval/blob/master/scapval/src/main/distro/NOTICE.txt