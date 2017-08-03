<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="cpe-dict" uri="http://cpe.mitre.org/dictionary/2.0"/>
  <sch:ns prefix="cpe-lang" uri="http://cpe.mitre.org/language/2.0"/>
  <sch:ns prefix="dc" uri="http://purl.org/dc/elements/1.1/"/>
  <sch:ns prefix="nvd-config" uri="http://scap.nist.gov/schema/feed/configuration/0.1"/>
  <sch:ns prefix="ocil" uri="http://scap.nist.gov/schema/ocil/2.0"/>
  <sch:ns prefix="oval-com" uri="http://oval.mitre.org/XMLSchema/oval-common-5"/>
  <sch:ns prefix="oval-def" uri="http://oval.mitre.org/XMLSchema/oval-definitions-5"/>
  <sch:ns prefix="oval-res" uri="http://oval.mitre.org/XMLSchema/oval-results-5"/>
  <sch:ns prefix="oval-sc" uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"/>
  <sch:ns prefix="scap" uri="http://scap.nist.gov/schema/data-stream/0.2"/>
  <sch:ns prefix="xccdf" uri="http://checklists.nist.gov/xccdf/1.1"/>
  <sch:ns prefix="xcf" uri="nist:scap:xslt:function"/>
  <sch:ns prefix="xml" uri="http://www.w3.org/XML/1998/namespace"/>
  <sch:ns prefix="xsd" uri="http://www.w3.org/2001/XMLSchema"/>
  <sch:pattern id="scap-result-general">
    <sch:rule id="scap-result-general-xccdf-rule-result" context="xccdf:rule-result">
      <sch:assert id="scap-result-xccdf-rule-result-result-val" test="(every $m in xccdf:check satisfies if( current()/xccdf:result eq 'pass' ) then (//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'true' and matches(//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name]/@class,'^(compliance|inventory)$')) or (//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'false' and matches(//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name]/@class,'^(vulnerability|patch)$')) else false()) or (some $m in xccdf:check satisfies if( current()/xccdf:result eq 'error' ) then //result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'error' else if( current()/xccdf:result eq 'unknown' ) then //result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'unknown' else if( current()/xccdf:result eq 'notapplicable' ) then //result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'not applicable' else if( current()/xccdf:result eq 'notchecked' ) then //result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'not evaluated' else if( current()/xccdf:result eq 'fail' ) then (//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'false' and matches(//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name]/@class,'^(compliance|inventory)$')) or (//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'true' and matches(//result[@id eq $m//xccdf:check-content-ref[1]/@href]//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name]/@class,'^(vulnerability|patch)$')) else false())">RES-126-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-rule-result-check-content-ref-exists" test="if(current()/xccdf:result ne 'notapplicable' and current()/xccdf:result ne 'notchecked' and current()/xccdf:result ne 'notselected') then exists(current()//xccdf:check/xccdf:check-content-ref[exists(@href) and exists(@name)]) else true()">RES-260-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-rule-result-check-content-ref-valid" test="every $m in current()//xccdf:check-content-ref satisfies exists(//result[@id eq $m/@href]//*[@id eq $m/@name])">RES-271-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-rule-result-set-value" test="every $m in //source//xccdf:Rule[@id eq current()/@idref]//xccdf:check-export satisfies exists(preceding-sibling::xccdf:set-value[@idref eq $m/@value-id])">RES-41-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-rule-result-ref" test="exists(//source//xccdf:Rule[@id eq current()/@idref])">RES-43-1</sch:assert>
    </sch:rule>
    <sch:rule id="scap-result-general-xccdf-test-result" context="xccdf:TestResult">
      <sch:assert id="scap-result-general-xccdf-test-result-benchmark-req" test="exists(ancestor::xccdf:Benchmark) or exists(xccdf:benchmark)">RES-131-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-org-req" test="exists(xccdf:organization)">RES-132-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-start-end-time" test="exists(@start-time) and exists(@end-time)">RES-133-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-test-system" test="exists(@test-system) and matches(@test-system, '[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}')">RES-134-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-no-profile-selected" test="if (some $m in ancestor::node()//source//xccdf:Profile/xccdf:select satisfies $m[@selected eq 'true' or not(@selected)]) then (xccdf:profile/@idref eq ancestor::node()//source//xccdf:Profile/@id) else not(exists(xccdf:profile))">RES-135-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-target-address" test="count(xccdf:target) eq count(xccdf:target-address)">RES-136-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-target-address-content" test="every $m in xccdf:target-address satisfies matches($m, '([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))') or matches($m, '([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}')">RES-136-2</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-profile-rule-reference" test="every $m in xccdf:profile satisfies every $n in xcf:get-all-profile-parents(//source,//source//xccdf:Benchmark/xccdf:Profile[@id eq $m/@idref]) satisfies every $o in $n/xccdf:select[@selected cast as xsd:boolean] satisfies every $p in //source//xccdf:Benchmark//xccdf:Rule[@id eq $o/@idref] union xcf:get-all-group-parents(//source,//source//xccdf:Benchmark//xccdf:Group[@id eq $o/@idref])//xccdf:Rule satisfies exists(xccdf:rule-result[@idref eq $p/@id])">RES-40-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-profile-include" test="if(//source//xccdf:Profile/@id ne '') then exists(xccdf:profile[@idref eq //source//xccdf:Profile/@id]) else false()">RES-40-2</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-identity-provided" test="exists(xccdf:identity) and (every $m in xccdf:identity satisfies matches($m,'\S'))">RES-42-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-cve-carry" test="every $m in xccdf:rule-result satisfies every $n in //source//xccdf:Rule[@id eq $m/@idref]/xccdf:ident[matches(@system,'^(CVE|http://cve.mitre.org)$')] satisfies exists($m/xccdf:ident[@system eq $n/@system and string() eq $n/string()])">RES-44-1</sch:assert>
      <sch:assert id="scap-result-general-xccdf-test-result-cce-carry" test="every $m in xccdf:rule-result satisfies every $n in //source//xccdf:Rule[@id eq $m/@idref]/xccdf:ident[matches(@system,'^(CCE|http://cce.mitre.org)$')] satisfies exists($m/xccdf:ident[@system eq $n/@system and string() eq $n/string()])">RES-45-1</sch:assert>
    </sch:rule>
    <sch:rule id="scap-result-general-results" context="results">
      <sch:assert id="scap-result-general-results-locator-prefix" test="every $m in result satisfies if( exists($m/preceding-sibling::result) ) then xcf:get-locator-prefix-res($m/@id) eq xcf:get-locator-prefix-res($m/preceding-sibling::result[1]/@id) else true()">RES-177-1</sch:assert>
      <sch:assert id="scap-result-general-results-name-postfix" test="every $m in result satisfies matches($m/@id,'(-xccdf-res\.xml|-oval-res\.xml|-patches-res\.xml|-ocil-res\.xml|-cpe-oval-res\.xml)$') ">RES-234-1</sch:assert>
    </sch:rule>
    <sch:rule id="scap-result-general-oval-results" context="oval-res:oval_results">
      <sch:assert id="scap-result-oval-results-result-type" test="(count(oval-res:directives/*) eq 6) and (every $m in oval-res:directives/* satisfies(($m/@reported eq 'true') and (if(exists($m/following-sibling::*)) then (if ($m/@content eq 'thin') then ($m/following-sibling::*[1]/@content eq 'thin' or not(exists($m/following-sibling::*[1]/@content))) else if ($m/@content eq 'full' or not(exists($m/@content))) then ($m/following-sibling::*[1]/@content eq 'full' or not(exists($m/following-sibling::*[1]/@content))) else false()) else true())))">RES-179-1</sch:assert>
      <sch:assert id="scap-result-oval-results-sys-wo-char" test="if( oval-res:directives/*[1]/@content eq 'full' or not(exists(oval-res:directives/*[1]/@content))) then ((exists(.//oval-sc:oval_system_characteristics/oval-sc:collected_objects) and exists(.//oval-sc:oval_system_characteristics/oval-sc:system_data)) or ((not(exists(.//oval-sc:oval_system_characteristics/oval-sc:collected_objects)) and not(exists(.//oval-sc:oval_system_characteristics/oval-sc:system_data))))) else true()">RES-181-1</sch:assert>
      <sch:assert id="scap-result-general-oval-results-generator-schema-version" test="oval-res:generator/oval-com:schema_version eq '5.8'">RES-69-2</sch:assert>
    </sch:rule>
  </sch:pattern>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-profile-parents">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:if test="exists($node/@extends)">
      <xsl:sequence select="xcf:get-all-profile-parents($doc,$doc//xccdf:Benchmark//xccdf:Profile[@id eq $node/@extends])"/>
    </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-def-children">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:for-each select="$doc//oval-def:extend_definition[@definition_ref eq $node/@id]/ancestor::oval-def:definition">
      <xsl:sequence select="xcf:get-all-def-children($doc,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-parents">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:for-each select="$node//oval-def:extend_definition">
      <xsl:sequence select="xcf:get-all-parents($doc,ancestor::oval-def:oval_definitions//*[@id eq current()/@definition_ref])"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:get-ocil-var-ref">
    <xsl:param name="ocil_questionnaire"/>
    <xsl:variable name="initialSet">
      <xsl:for-each select="$ocil_questionnaire/ocil:actions/ocil:test_action_ref">
        <xsl:sequence select="ancestor::ocil:ocil/ocil:test_actions/ocil:numeric_question_test_action[@id eq current()]/ocil:when_equals[@var_ref]"/>
        <xsl:sequence select="ancestor::ocil:ocil/ocil:test_actions/ocil:string_question_test_action[@id eq current()]/ocil:when_pattern/ocil:pattern[@var_ref]"/>
        <xsl:sequence select="ancestor::ocil:ocil/ocil:test_actions/ocil:numeric_question_test_action[@id eq current()]/ocil:when_range/ocil:range/ocil:min[@var_ref]"/>
        <xsl:sequence select="ancestor::ocil:ocil/ocil:test_actions/ocil:numeric_question_test_action[@id eq current()]/ocil:when_range/ocil:range/ocil:max[@var_ref]"/>
        <xsl:for-each select="ancestor::ocil:ocil/ocil:test_actions/*[@id eq current()]">
          <xsl:sequence select="ancestor::ocil:ocil/ocil:questions/ocil:choice_question[@id eq current()/@question_ref]/ocil:choice[@var_ref]"/>
          <xsl:for-each select="ancestor::ocil:ocil/ocil:questions/ocil:choice_question[@id eq current()/@question_ref]/ocil:choice_group_ref">
            <xsl:sequence select="ancestor::ocil:questions/ocil:choice_group[@id eq current()]/ocil:choice[@var_ref]"/>
          </xsl:for-each>
        </xsl:for-each>
      </xsl:for-each>
    </xsl:variable>
    <xsl:for-each select="$initialSet/*">
      <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:pass-ocil-var-ref">
    <xsl:param name="ocil_questionnaire"/>
    <xsl:param name="var_ref"/>
    <xsl:sequence select="$var_ref"/>
    <xsl:for-each select="$ocil_questionnaire/ancestor::ocil:ocil/ocil:variables/ocil:local_variable[@id eq $var_ref/@var_ref]/ocil:set/ocil:when_range/ocil:min[@var_ref]">
      <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
    </xsl:for-each>
    <xsl:for-each select="$ocil_questionnaire/ancestor::ocil:ocil/ocil:variables/ocil:local_variable[@id eq $var_ref/@var_ref]/ocil:set/ocil:when_range/ocil:max[@var_ref]">
      <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-external-vars">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$doc//oval-def:external_variable[@id eq $node/@var_ref]"/>
    <xsl:for-each select="$doc//*[@id eq $node/@var_ref]//*[@var_ref]">
      <xsl:sequence select="xcf:get-all-external-vars($doc,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-group-parents">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:if test="exists($node/@extends)">
      <xsl:sequence select="xcf:get-all-group-parents($doc,$doc//xccdf:Benchmark//xccdf:Group[@id eq $node/@extends])"/>
    </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:get-locator-prefix">
    <xsl:param name="name"/>
    <xsl:variable name="subName" select="substring($name,1,string-length($name) - string-length(tokenize($name,'-')[last()]) - 1)"/>
    <xsl:choose>
      <xsl:when test="ends-with($subName,'cpe')">
        <xsl:value-of select="xcf:get-locator-prefix($subName)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="tokenize($subName,'/')[last()]"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="xcf:get-locator-prefix-res">
    <xsl:param name="name"/>
    <xsl:variable name="subName" select="substring($name,1,string-length($name) - string-length(tokenize($name,'-')[last()]) - 1)"/>
    <xsl:value-of select="xcf:get-locator-prefix($subName)"/>
  </xsl:function>
</sch:schema>
