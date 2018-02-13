Various schematron files are used in the SCAP validation process. The associated version specific Source or Result schematron files are included.
These schematron rules will not retrieve remote resources and assume a data-stream under validation is fully populated with all content.

Each schematron assert includes an ID in its content with prefixes SRC-, RES-, and A-.
These asserts correspond to specific SCAP requirements found in the requirements/ directory.

Change Log:
1.3.2
- Added SCAP 1.3 support with result-data-stream-1.3.sch and source-data-stream-1.3.sch schematron files.
- All SCAP schematron assert content IDs are modified to include a RES, SRC-, A- prefix per Decima integration requirements.
- Added application requirement A-26 to warn users when optional attributes are missing in some SCAP content.
- Added application requirement A-27 to check for @href and @id-ref value of <cpe:check-fact-ref> elements.
- Updated RES-260-1 for SCAP 1.1 results to account for 'notapplicable', 'notchecked', or 'notselected' results.
- For SCAP 1.1 updated RES-126-1 to properly check for result of 'unknown' instead of 'errunknownr'
- Added Warnings RES-258-1 and RES-366-1 for SCAP 1.2 and 1.3 checks.
- Fixed the SRC-169-1 check for 1 or more class patch oval definitions for SCAP 1.3 content
- Previous unused versions of source-data-stream-1.2_* have been removed
- result-data-stream-1.2.1.sch renamed to result-data-stream-1.2.sch

#########################
Previous Legacy Version Updates:
1.2.6     12/16/2016
- Improved and updates to schematron rules 126-1, 126-2, 126-3, 126-4, and 260-1

1.2.5     08/05/2016
- Improved and updates to schematron rules 3, 8, 31, 38, 40, 44, 126, 134, 135, 179-181, 236, 242, 248, 260, 262, 265, 341, 370.

1.2.0     06/19/2013

- Added Derived Requirements 118-2, 118-3 and 3-3
   118-2: @system on &lt;cpe-dict:check&gt; MUST be "http://oval.mitre.org/XMLSchema/oval-definitions-5" or http://scap.nist.gov/schema/ocil/2
   118-3: @system on &lt;cpe-lang:check-fact-ref&gt; MUST be "http://oval.mitre.org/XMLSchema/oval-definitions-5" or http://scap.nist.gov/schema/ocil/2
   3-3: The @id and &lt;xccdf:version&gt; together MUST uniquely identify an xccdf:Benchmark in a &lt;scap:data-stream-collection&gt;
   
   All of the checks have been added to source-data-stream-1.2._1.2.sch; content must update @schematron-version to 1.2 in order to use this update

- XCCDF results that are not applicable would not have a xccdf:check-content-ref
- The dsig:Signature checking needed to be more targeted

0.5	05/01/2012

- Removed requirement 211-1 as it was erroneous
- Removed requirements 275-1 and 171-1 based on SCAP Errata E4
- Added source-data-stream-1.2_1.1.sch with the above changes
- Added requirement text that SCAPVal will not load external XML schemas
- Renamed source-data-stream-1.2.1.sch to source-data-stream-1.2_1.0.sch


0.4     01/06/2012

This package includes the SCAP 1.2 Schematron rules and related files.

source-data-stream-1.2.1.sch : The Schematron rules for an SCAP 1.2 source file
result-data-stream-1.2.1.sch : The Schematron rules for an SCAP 1.2 result file
scap-val-requirements-1.2.html : The documented requirements for SCAP 1.2, exported from SP 800-126 Rev 2, with derived requirements
1.2 (folder) : Contains the source XML from which scap-val-requirements-1.2.html was generated
xsl_details (folder) : Contains XSLT files for select requirements where the Schematron does not provide a detailed error location
lib (folder) : Contains JAR files to include on the classpath when running the rules.
data (folder) : Support files for the rules.

Usage:
Schematron rules should be processed in accordance with the instructions found at http://www.schematron.com/.  The Schematron rules are written using XPath 2.0, and therefore they should be processed as XSLT 2.0, with an XSLT 2.0 compatible processor. Some .sch files leverage custom Java functions for select rules.  If the Java functions are not enabled when running the Schematron checks, a warning will be issued stating that the functions are not accessible, but no further consequences are expected.

These are the steps to run the included .sch schematron files using Saxon-HE (free) for Java.
Prerequisite: Java 6 or higher is installed and available on the system path

1) Download Saxon-HE 9.8 for Java from http://saxon.sourceforge.net/ and unzip it.

2) Download ISO Schematron XSLT 2.0 files from https://github.com/Schematron/schematron/releases/download/2017-02-09/iso-schematron-xslt2.zip and unzip them.

3) Compile the SCAP Schematron Rules by running the following command (choose proper .sch file as necessary):
	java -cp %SAXON_HOME%\saxon9he.jar net.sf.saxon.Transform -s:source-data-stream-1.3.sch -xsl:%SCHEMATRON_HOME%\iso_svrl_for_xslt2.xsl -o:compiled.sch.xsl

4) Run the resulting XSLT against the target SCAP content using the following command:
	java -cp %SCAP_SCHEMATRON_HOME%\lib\scap-schematron-rules-1.3.2.jar;%mitre-cpe-1.0.7.jar;%SAXON_HOME%\saxon9he.jar net.sf.saxon.Transform -init:gov.nist.scap.schematron.Initializer -s:scap-source-content.xml -xsl:compiled.sch.xsl -o:results.xml

Each failed assertion will contain a requirement number (the requirement and derived requirement is separated by a dash (-)) which maps to the associated requirement file in /requirements, as well as the failed test condition and location of the failed item.
