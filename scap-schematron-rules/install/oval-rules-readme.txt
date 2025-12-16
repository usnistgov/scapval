To validate OVAL content, SCAP uses OVAL Definition Schematron schemas that have been modified to use XPath 1.0 instead of XPath 2.0.
To use XPath 1.0, the queryBinding="xslt2" attributes were removed from the root element of the schematron schemas.
The modification was made to avoid an XPath exception in the floor() function when the current element being processed is not a node.
Version 1.4.0 Updates:
- Updates to SCAPVal Embedded OVAL System Characteristics definitions 5.12.2:
Version 1.3.7 Updates:
- Updates to SCAPVal Embedded OVAL System Characteristics definitions 5.11.2:
    Modifications:
        - Updated schematron rule win-sc:lockoutpolicy_item/win-sc:force_logoff to check for >= 0
        - Updated schematron rule win-sc:lockoutpolicy_item/win-sc:lockout_duration to check for >= 0
        - Updated schematron rule win-sc:passwordpolicy_item/win-sc:max_passwd_age to check for >= 0

Version 1.3.6 Updates:
- Include OVAL System Characteristics definitions 5.11.2:
    - Extracted from OVAL *.system-characteristics.xsd on github via the provided XSLT ExtractSchFromXSD.xsl from https://github.com/OVALProject/Language/tree/5.11.2/tools
    the output files were then merged into a single .sch file

    Modifications:
        - Removed some duplicate rules as a result of merging the extracted schematron from several XSDs.
        - Commented out pattern oval_schema_version_core_matches_platforms until fix is provided.

Version 1.3.3 Updates:
- Fixes to OVAL schematron rules from official github repository per https://github.com/balleman/Language/commit/f54e24132b99a1ea72290635590850e985942d6c

Version 1.3.2 Updates:
- Include OVAL definitions 5.11.2:
    - Extracted from OVAL *.definitions-schema.xsd on github via the provided XSLT ExtractSchFromXSD.xsl from https://github.com/OVALProject/Language/tree/5.11.2/tools
    the output files were then merged into a single .sch file
    Modifications:
        - Removed some duplicate rules as a result of merging the extracted schematron from several XSDs.
        - Commented out pattern oval_schema_version_core_matches_platforms until fix is provided.

- Include OVAL results 5.11.2:
    - Extracted from OVAL oval-results-schema.xsd schema github via the provided XSLT ExtractSchFromXSD.xsl from https://github.com/OVALProject/Language/tree/5.11.2/tools
    Modifications:
        - Prepend the XPath Axes ancestor-or-self:: to several asserts to allow for testing OVAL content embedded in ARF reports
        - Commented out pattern oval_schema_version_core_matches_platforms until fix is provided.

- Include OVAL definitions 5.11.1:
    - Extracted from OVAL *.definitions-schema.xsd on github via the provided XSLT ExtractSchFromXSD.xsl from https://github.com/OVALProject/Language/tree/5.11.2/tools
    the output files were then merged into a single .sch file
    Modifications:
        - Removed some duplicate rules as a result of merging the extracted schematron from several XSDs.
        - Commented out pattern oval_schema_version_core_matches_platforms until fix is provided.

- Include OVAL results 5.11.1:
    - Extracted from OVAL oval-results-schema.xsd schema github via the provided XSLT ExtractSchFromXSD.xsl from https://github.com/OVALProject/Language/tree/5.11.2/tools
    Modifications:
        - Prepend the XPath Axes ancestor-or-self:: to several asserts to allow for testing OVAL content embedded in ARF reports
        - Commented out pattern oval_schema_version_core_matches_platforms until fix is provided.

- Include OVAL definitions 5.11:
    - From oval.mitre.org provided .sch file
    Modifications:
        - Fix for apparent bug where the Namespace prefix 'x-macos-def' has not been declared
        - Update to a couple of linux rules on line 2409 and 2415 which were causing verbose SAXON warnings.

- Include OVAL results 5.11:
    - From oval.mitre.org provided .sch file
    Modifications:
        - Prepend the XPath Axes ancestor-or-self:: to several asserts to allow for testing OVAL content embedded in ARF reports

- File oval-results-schematron-5.8.sch and oval-results-schematron-5.10.sch - pattern id oval-res_directives_include_oval_definitions has been updated to match version 5.10.1+ to address a bug.

- Schematron rules checking for @content = 'full' were modified to also account for @content optionally missing with default value "full".

- Schematron rules checking for @datatype = 'string' were modified to also account for @datatype optionally missing with default value "string".

#########################
Previous Legacy Version Updates:
To validate Windows content, the OVAL 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, and 5.9 schema/schematron were modified to allow the following oval-def:platforms (the versions where a platform was added because it does exist in the original Schematron is noted):
oval-def:platform='Microsoft Windows Server 2008' (5.3, 5.4)
oval-def:platform='Microsoft Windows 7' (5.3, 5.4, 5.5)
oval-def:platform='Microsoft Windows Server 2008 R2' (5.3, 5.4, 5.5, 5.6, 5.7)
oval-def:platform='Microsoft Windows 8'
oval-def:platform='Microsoft Windows Server 2012'
oval-def:platform='Microsoft Windows 8.1'
oval-def:platform='Microsoft Windows Server 2012 R2'
oval-def:platform='Microsoft Windows 10'
oval-def:platform='Microsoft Windows Server 2016'

In OVAL 5.10 and 5.10.1 a pattern with ID = "macos-def_plist510objfilepath" updated wording from 'should' to 'shall'.

In OVAL results 5.8, 5.10, and 5.10.1 various patterns were prefixed with "ancestor-or-self::" in order to be used for ARF validation where OVAL results are embeeded in ARF content.

In OVAL 5.3, a pattern with ID = "fileobjfilename" has a single rule with an erroneous assertion. The test was corrected in OVAL 5.5, therefore the test from OVAL 5.5 is used in place of the OVAL 5.3 test.
Original test:
test="(not(@operation) or @operation='equals' or @operation='not equal')
		and not(contains(.,'\') or contains(.,'/') or contains(.,':') or
		contains(.,'*') or contains(.,'?')) or contains(.,'>')
		or contains(.,'|')"
New test:
test="(not(contains(.,'\') or contains(.,'/') or contains(.,':')
		or contains(.,'*') or contains(.,'?') or contains(.,'>')
		or contains(.,'|'))) or (@operation='pattern match')"


In OVAL 5.3, a pattern with ID = "regstevalue" has a single rule with an erroneous assertion. The assertion was removed in OVAL 5.4 and later. In order to fix the assertion in the OVAL 5.3 Schematron, the test was updated for this tool.
Original test:
test="((@datatype='binary' and (not(@operation) or @operation='equals'
	  or @operation='not equal')) or (@datatype='int' and (not(@operation)
	  or @operation='equals' or @operation='not equal'
	  or @operation='greater than' or @operation='greater than or equal'
	  or @operation='less than' or @operation='less than or equal'
	  or @operation='bitwise and' or @operation='bitwise or'))
	  or ((@datatype='string' or not(@datatype)) and (not(@operation)
	  or @operation='equals' or @operation='not equal'
	  or @operation='pattern match')) or (@datatype='version'
	  and (not(@operation) or @operation='equals' or @operation='not equal'
	  or @operation='greater than' or @operation='greater than or equal'
	  or @operation='less than' or @operation='less than or equal')))"
New test:
test="((@datatype='binary' and (not(@operation) or @operation='equals'
	  or @operation='not equal')) or (@datatype='int' and (not(@operation)
	  or @operation='equals' or @operation='not equal'
	  or @operation='greater than' or @operation='greater than or equal'
	  or @operation='less than' or @operation='less than
	  or equal' or @operation='bitwise and' or @operation='bitwise or'))
	  or ((@datatype='string' or not(@datatype)) and (not(@operation)
	  or @operation='equals' or @operation='not equal'
	  or @operation='pattern match')) or (@datatype='version'
	  and (not(@operation) or @operation='equals' or @operation='not equal'
	  or @operation='greater than' or @operation='greater than or equal'
	  or @operation='less than' or @operation='less than or equal'))
	  or (@datatype='boolean' and (not(@operation) or @operation='equals'
	  or @operation='not equal')) or (@datatype='evr_string'
	  and (not(@operation) or @operation='equals' or @operation='not equal'
	  or @operation='greater than' or @operation='greater than or equal'
	  or @operation='less than' or @operation='less than or equal'))
	  or (@datatype='float' and (not(@operation) or @operation='equals'
	  or @operation='not equal' or @operation='greater than'
	  or @operation='greater than or equal' or @operation='less than'
	  or @operation='less than or equal')) or (@datatype='ios_version'
	  and (not(@operation) or @operation='equals' or @operation='not equal'
	  or @operation='greater than' or @operation='greater than or equal'
	  or @operation='less than' or @operation='less than or equal')))"


In OVAL 5.4, pattern with ID = "entityrules" has an error in the first assertion.
Original test:
test="...(@datatype='string' and (not(@operation) or @operation='equals'
		   or @operation='not equal' or @operation='pattern match'))..."
New test:
test="...(@datatype='string' and (not(@operation) or @operation='equals'
		   or @operation='not equal' or @operation='case insensitive equals'
		   or @operation='case insensitive not equal'
		   or @operation='pattern match'))..."

The official OVAL schema and schematron can be found at http://oval.mitre.org/.
