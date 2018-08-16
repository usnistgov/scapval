# scap-schematron-rules
> A collection of various schematron files used for validating SCAP content.

## Overview
These files are used by SCAPVal as part of its validation process.
The .sch schematron files include SCAP 1.1, 1.2, and 1.3 support along with various required components OVAL, OCIL, XCCDF, and TMSAD.
The schematron rules have SCAP requirement IDs embedded which are used in SCAPVal report generation.

## Contents
Schematron files (.sch) are found in */schematron/scap* (SCAP specific schematrons) 
and */schematron/other* (for component specific schematrons)

SCAPVal requirements are found in */requirements*

Included schematron files may have been modified from their original source to suit validation of SCAP per 800-126r3
See additional readmes below for changes and usage information.

[SCAP Rules Readme] 

[OVAL Rules Readme]

## Build Notes:
scap-schematron-rules requires *gov.nist:mitre-cpe*, *gov.nist.decima:decima-xml*, and *gov.nist.decima:decima-xml* 
These are not currently available in maven central so they must be acquired and installed to your local maven 
repository first

then

`mvn clean install`   

License:
---------
[License Information]

Feedback:
---------
Please send tool defects reports, enhancement requests, and any other related
comments by email to scap@nist.gov.

[SCAP Rules Readme]:https://github.com/usnistgov/scapval/blob/master/scap-schematron-rules/install/scap-rules-readme.txt
[OVAL Rules Readme]:https://github.com/usnistgov/scapval/blob/master/scap-schematron-rules/install/oval-rules-readme.txt
[License Information]:https://github.com/usnistgov/scapval/blob/master/scapval/src/main/distro/NOTICE.txt