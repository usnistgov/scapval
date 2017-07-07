<?xml version="1.0" encoding="UTF-8"?>
<!--These rules are for informational purposes only and DO NOT supersede the requirements in NIST SP 800-126 Rev 2.-->
<!--These rules may be revised at anytime. Comments/feedback on these rules are welcome.-->
<!--Private comments may be sent to scap@nist.gov.  Public comments may be sent to scap-dev@nist.gov.-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <ns prefix="ai" uri="http://scap.nist.gov/schema/asset-identification/1.1"/>
  <ns prefix="arf"
       uri="http://scap.nist.gov/schema/asset-reporting-format/1.1"/>
  <ns prefix="cat" uri="urn:oasis:names:tc:entity:xmlns:xml:catalog"/>
  <ns prefix="cpe-dict" uri="http://cpe.mitre.org/dictionary/2.0"/>
  <ns prefix="cpe-lang" uri="http://cpe.mitre.org/language/2.0"/>
  <ns prefix="dc" uri="http://purl.org/dc/elements/1.1/"/>
  <ns prefix="ds" uri="http://scap.nist.gov/schema/scap/source/1.2"/>
  <ns prefix="dsig" uri="http://www.w3.org/2000/09/xmldsig#"/>
  <ns prefix="java" uri="java:gov.nist.scap.schematron"/>
  <ns prefix="nvd-config"
       uri="http://scap.nist.gov/schema/feed/configuration/0.1"/>
  <ns prefix="ocil" uri="http://scap.nist.gov/schema/ocil/2.0"/>
  <ns prefix="oval-com" uri="http://oval.mitre.org/XMLSchema/oval-common-5"/>
  <ns prefix="oval-def"
       uri="http://oval.mitre.org/XMLSchema/oval-definitions-5"/>
  <ns prefix="oval-res" uri="http://oval.mitre.org/XMLSchema/oval-results-5"/>
  <ns prefix="oval-sc"
       uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"/>
  <ns prefix="rc" uri="http://scap.nist.gov/schema/reporting-core/1.1"/>
  <ns prefix="scap" uri="http://scap.nist.gov/schema/scap/source/1.2"/>
  <ns prefix="scap-con" uri="http://scap.nist.gov/schema/scap/constructs/1.2"/>
  <ns prefix="tmsad" uri="http://scap.nist.gov/schema/xml-dsig/1.0"/>
  <ns prefix="xccdf" uri="http://checklists.nist.gov/xccdf/1.2"/>
  <ns prefix="xcf" uri="nist:scap:xslt:function"/>
  <ns prefix="xinclude" uri="http://www.w3.org/2001/XInclude"/>
  <ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
  <ns prefix="xml" uri="http://www.w3.org/XML/1998/namespace"/>
  <ns prefix="xsd" uri="http://www.w3.org/2001/XMLSchema"/>
  <pattern id="scap-result-general">
      <rule id="scap-result-general-xccdf-rule-result" context="xccdf:rule-result">
        <!-- old id: scap-result-xccdf-rule-result-result-val-->
        <assert id="REQ-126-1"
                 test="((not(exists(./ancestor::arf:asset-report-collection[1]//arf:report-requests))) or (some $m in xccdf:check satisfies if( current()/xccdf:result eq 'error' ) then ./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'error' else if( current()/xccdf:result eq 'unknown' ) then ./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'unknown' else if( current()/xccdf:result eq 'notapplicable' ) then ./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'not applicable' else if( current()/xccdf:result eq 'notchecked' ) then ./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'not evaluated' else if( current()/xccdf:result eq 'fail' ) then (./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'false' and matches(./ancestor::arf:asset-report-collection[1]//arf:report-request//oval-def:oval_definitions//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name][1]/@class,'^(compliance|inventory)$')) or (./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'true' and matches(./ancestor::arf:asset-report-collection[1]//arf:report-request//oval-def:oval_definitions//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name][1]/@class,'^(vulnerability|patch)$')) else if( current()/xccdf:result eq 'notselected') then true() else false()) or (every $m in xccdf:check satisfies if( current()/xccdf:result eq 'pass' ) then (./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'true' and matches(./ancestor::arf:asset-report-collection[1]//arf:report-request//oval-def:oval_definitions//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name][1]/@class,'^(compliance|inventory)$')) or(./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]/@result eq 'false' and matches(./ancestor::arf:asset-report-collection[1]//arf:report-request//oval-def:oval_definitions//oval-def:definition[@id eq $m//xccdf:check-content-ref[1]/@name][1]/@class,'^(vulnerability|patch)$')) else false()) or (every $m in xccdf:check satisfies count(./ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m//xccdf:check-content-ref[1]/@href]//oval-res:definition[@definition_id eq $m//xccdf:check-content-ref[1]/@name]) ne 1))"/>
        <!-- old id: scap-result-general-xccdf-rule-result-check-content-ref-exists-->
        <assert id="REQ-260-1"
                 test="if( xccdf:result ne 'notapplicable' and xccdf:result ne 'notchecked' and xccdf:result ne 'notselected') then exists(xccdf:check/xccdf:check-content-ref[exists(@href) and exists(@name)]) else true()"/>
        <!-- old id: scap-result-general-xccdf-rule-result-check-ref-hash-->
        <assert id="REQ-370-1"
                 test="(every $m in .//xccdf:check-content-ref satisfies current()/ancestor::arf:asset-report-collection[1]//arf:report[concat('#',@id) eq $m/@href]) and (every $m in .//xccdf:check-content-ref satisfies parent::*/parent::*/parent::*[concat('#',@id) ne $m/@href])"/>
        <!-- old id: scap-result-general-xccdf-rule-result-check-ref-valid-->
        <assert id="REQ-370-1"
                 test="every $m in .//xccdf:check-content-ref satisfies if (contains($m/parent::xccdf:check/@system,'oval')) then current()/ancestor::arf:asset-report-collection[1]//arf:report[@id eq translate($m/@href,'#','')]/arf:content/*[local-name() eq 'oval_results'] else if (contains($m/parent::xccdf:check/@system,'ocil')) then current()/ancestor::arf:asset-report-collection[1]//arf:report[@id eq translate($m/@href,'#','')]/arf:content/*[local-name() eq 'ocil'] else false()"/>
      </rule>
      <rule id="scap-result-general-report" context="arf:report">
        <!-- old id: scap-result-general-report-xccdf-use-test-result-->
        <assert id="REQ-131-1"
                 test="if( namespace-uri(arf:content/*[1]) eq 'http://checklists.nist.gov/xccdf/1.2' ) then exists(arf:content/xccdf:TestResult) else true()">
            <value-of select="@id"/>
         </assert>
      </rule>
      <rule id="scap-result-general-xccdf-test-result" context="xccdf:TestResult">
        <!-- old id: scap-result-general-xccdf-test-result-start-end-time-->
        <assert id="REQ-133-1" test="exists(@start-time) and exists(@end-time)"/>
        <!-- old id: scap-result-general-xccdf-test-result-target-address-->
        <assert id="REQ-136-1"
                 test="count(xccdf:target) &gt; 0 and count(xccdf:target-address) &gt; 0"/>
        <!-- old id: scap-result-general-xccdf-test-result-target-address-content-->
        <assert id="REQ-136-2"
                 test="every $m in xccdf:target-address satisfies matches($m, '([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))') or matches($m, '([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}')"/>
        <!-- old id: scap-result-general-xccdf-test-result-benchmark-id-req-->
        <assert id="REQ-253-1" test="exists(xccdf:benchmark[exists(@id)])"/>
        <!-- old id: scap-result-general-xccdf-test-result-target-id-ref-->
        <assert id="REQ-304-1"
                 test="exists(.//xccdf:target-id-ref[@system eq 'http://scap.nist.gov/schema/asset-identification/1.1' and @href eq '' and @name eq current()/ancestor::arf:asset-report-collection[1]/rc:relationships/rc:relationship[@subject eq current()/ancestor::arf:report[1]/@id and resolve-QName(@type,.) eq QName('http://scap.nist.gov/vocabulary/arf/relationships/1.0#','isAbout')]/rc:ref[1]])"/>
        <!-- old id: scap-result-general-xccdf-test-result-identity-provided-->
        <assert id="REQ-42-1"
                 test="exists(xccdf:identity) and (every $m in xccdf:identity satisfies matches($m,'\S'))"/>
      </rule>
      <rule id="scap-result-general-oval-results" context="oval-res:oval_results">
        <!-- old id: scap-result-oval-results-result-type-->
        <assert id="REQ-179-1"
                 test="(count(oval-res:directives/*) eq 6) and (every $m in oval-res:directives/* satisfies ((boolean($m/@reported) eq true()) and (if(exists($m/following-sibling::*)) then (if ($m/@content eq 'thin') then $m/following-sibling::*[1]/@content eq 'thin' else ($m/following-sibling::*[1]/@content eq 'full' or not(exists($m/following-sibling::*[1]/@content)))) else true())))"/>
        <!-- old id: scap-result-oval-results-sys-wo-char-->
        <assert id="REQ-181-1"
                 test="if( oval-res:directives/*[1]/@content eq 'full' or not(exists(oval-res:directives/*[1]/@content)) ) then exists(.//oval-sc:oval_system_characteristics/oval-sc:collected_objects) and exists(.//oval-sc:oval_system_characteristics/oval-sc:system_data) else true()"/>
      </rule>
      <rule id="scap-result-general-ai-asset" context="arf:asset">
        <!-- old id: scap-result-general-ai-asset-certain-fields-req-->
        <assert id="REQ-299-1"
                 test="(exists(.//ai:ip-v4) or exists(.//ai:ip-v6)) and exists(.//ai:mac-address) and (.//ai:fqdn) and (.//ai:hostname)">
            <value-of select="@id"/>
         </assert>
      </rule>
      <rule id="scap-result-general-report-collection"
            context="arf:asset-report-collection">
        <!-- old id: scap-result-general-report-collection-include-report-request-->
        <assert id="REQ-300-1" test="exists(.//arf:report-request)">
            <value-of select="@id"/>
         </assert>
	        <!-- old id: scap-result-general-report-collection-report-request-not-included-->
        <assert id="REQ-A24" test="exists(.//arf:report-request)">
            <value-of select="@id"/>
         </assert>
        <!-- old id: scap-result-general-signature-sig-counter-sign-remove-orig-->
        <assert id="REQ-316-1"
                 test="every $m in .//arf:extended-info/dsig:Signature satisfies count($m/ancestor::arf:asset-report-collection[1]//dsig:Signature[@Id eq $m/@Id]) eq 1">
            <value-of select="@id"/>
         </assert>
        <!-- old id: scap-result-general-report-collection-sig-report-request-->
        <assert id="REQ-323-1"
                 test="if( exists(.//arf:extended-info/dsig:Signature) ) then exists(.//arf:report-request) else true() ">
            <value-of select="@id"/>
         </assert>
        <!-- old id: scap-result-general-xccdf-rule-ident-->
        <assert id="REQ-44-1"
                 test="deep-equal(.//xccdf:Rule/xccdf:ident/text(), .//xccdf:rule-result/xccdf:ident/text())"/>
      </rule>
      <rule id="scap-result-general-oval-sys"
            context="oval-sc:oval_system_characteristics">
        <!-- old id: scap-result-general-oval-sys-system-info-->
        <assert id="REQ-306-1"
                 test="exists(oval-sc:system_info/scap-con:asset-identification/arf:object-ref[@ref-id eq current()/ancestor::arf:asset-report-collection[1]/rc:relationships/rc:relationship[@subject eq current()/ancestor::arf:report[1]/@id and resolve-QName(@type,.) eq QName('http://scap.nist.gov/vocabulary/arf/relationships/1.0#','isAbout')]/rc:ref[1]])"/>
      </rule>
      <rule id="scap-result-general-signature-sig"
            context="arf:extended-info/dsig:Signature">
        <!-- old id: scap-result-general-signature-sig-sig-properties-->
        <assert id="REQ-311-1" test="exists(.//tmsad:signature-info)">
            <value-of select="@Id"/>
         </assert>
        <!-- old id: scap-result-general-signature-sig-first-ref-->
        <assert id="REQ-312-1"
                 test="some $m in .//dsig:Reference[1] satisfies ($m/@URI eq '' or exists(current()//dsig:Object[concat('#',@Id) eq $m/@URI]))">
            <value-of select="@Id"/>
         </assert>
        <!-- old id: scap-result-general-signature-sig-xpath-filter-->
        <assert id="REQ-313-1"
                 test="count(.//dsig:Reference[1]//dsig:Transform[@Algorithm eq 'http://www.w3.org/2002/06/xmldsig-filter2']) eq 2">
            <value-of select="@Id"/>
         </assert>
        <!-- old id: scap-result-general-signature-sig-sig-property-->
        <assert id="REQ-314-1"
                 test="exists(.//dsig:SignatureProperties[concat('#',@Id) eq current()/dsig:SignedInfo/dsig:Reference[2]/@URI])">
            <value-of select="@Id"/>
         </assert>
        <!-- old id: scap-result-general-signature-sig-key-info-->
        <assert id="REQ-315-1" test="exists(dsig:KeyInfo)">
            <value-of select="@Id"/>
         </assert>
      </rule>
  </pattern>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-all-profile-parents">
      <xsl:param name="doc"/>
      <xsl:param name="node"/>
      <xsl:sequence select="$node"/>
      <xsl:if test="exists($node/@extends)">
         <xsl:sequence select="xcf:get-all-profile-parents($doc,$doc//xccdf:Benchmark//xccdf:Profile[@id eq $node/@extends])"/>
      </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-component">
      <xsl:param name="component-ref"/>
      <xsl:sequence select="$component-ref/ancestor::ds:data-stream-collection//ds:component[@id eq substring($component-ref/@xlink:href,2)]"/>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-all-def-children">
      <xsl:param name="doc"/>
      <xsl:param name="node"/>
      <xsl:sequence select="$node"/>
      <xsl:for-each select="$doc//oval-def:extend_definition[@definition_ref eq $node/@id]/ancestor::oval-def:definition">
         <xsl:sequence select="xcf:get-all-def-children($doc,current())"/>
      </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:ident-mapping">
      <xsl:param name="in-string"/>
      <xsl:choose>
         <xsl:when test="$in-string eq 'http://cce.mitre.org'">
            <xsl:value-of select="string('^(CCE|http://cce.mitre.org)$')"/>
         </xsl:when>
         <xsl:when test="$in-string eq 'http://cve.mitre.org'">
            <xsl:value-of select="string('^(CVE|http://cve.mitre.org)$')"/>
         </xsl:when>
         <xsl:when test="$in-string eq 'http://cpe.mitre.org'">
            <xsl:value-of select="string('^(CPE|http://cpe.mitre.org)$')"/>
         </xsl:when>
      </xsl:choose>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-all-parents">
      <xsl:param name="doc"/>
      <xsl:param name="node"/>
      <xsl:sequence select="$node"/>
      <xsl:for-each select="$node//oval-def:extend_definition">
         <xsl:sequence select="xcf:get-all-parents($doc,ancestor::oval-def:oval_definitions//*[@id eq current()/@definition_ref])"/>
      </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-ocil-var-ref">
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
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-component-ref">
      <xsl:param name="catalog"/>
      <xsl:param name="uri"/>
      <xsl:variable name="component-ref-uri"
                    select="xcf:resolve-in-catalog($catalog/*[1],$uri)"/>
      <xsl:if test="$component-ref-uri ne ''">
         <xsl:sequence select="$catalog/ancestor::ds:data-stream//ds:component-ref[@id eq substring($component-ref-uri,2)]"/>
      </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:pass-ocil-var-ref">
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
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:resolve-in-catalog">
      <xsl:param name="resolver-node"/>
      <xsl:param name="uri"/>
      <xsl:choose>
         <xsl:when test="starts-with($uri,'#')">
            <xsl:value-of select="$uri"/>
         </xsl:when>
         <xsl:when test="exists($resolver-node)">
            <xsl:choose>
               <xsl:when test="$resolver-node/local-name() eq 'uri'">
                  <xsl:choose>
                     <xsl:when test="$resolver-node/@name eq $uri">
                        <xsl:value-of select="$resolver-node/@uri"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="xcf:resolve-in-catalog($resolver-node/following-sibling::*[1], $uri)"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:when>
               <xsl:when test="$resolver-node/local-name() eq 'rewriteURI'">
                  <xsl:choose>
                     <xsl:when test="starts-with($uri,$resolver-node/@uriStartString)">
                        <xsl:value-of select="concat($resolver-node/@rewritePrefix,substring($uri,string-length($resolver-node/@uriStartString)+1))"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="xcf:resolve-in-catalog($resolver-node/following-sibling::*[1], $uri)"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:when>
            </xsl:choose>
         </xsl:when>
      </xsl:choose>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-all-external-vars">
      <xsl:param name="doc"/>
      <xsl:param name="node"/>
      <xsl:sequence select="$doc//oval-def:external_variable[@id eq $node/@var_ref]"/>
      <xsl:for-each select="$doc//*[@id eq $node/@var_ref]//*[@var_ref]">
         <xsl:sequence select="xcf:get-all-external-vars($doc,current())"/>
      </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:is-external-ref">
      <xsl:param name="catalog"/>
      <xsl:param name="uri"/>
      <xsl:variable name="comp-ref" select="xcf:get-component-ref($catalog,$uri)"/>
      <xsl:choose>
         <xsl:when test="exists($comp-ref)">
            <xsl:value-of select="not(starts-with($comp-ref/@xlink:href,'#'))"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="false()"/>
         </xsl:otherwise>
      </xsl:choose>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-all-group-parents">
      <xsl:param name="doc"/>
      <xsl:param name="node"/>
      <xsl:sequence select="$node"/>
      <xsl:if test="exists($node/@extends)">
         <xsl:sequence select="xcf:get-all-group-parents($doc,$doc//xccdf:Benchmark//xccdf:Group[@id eq $node/@extends])"/>
      </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-locator-prefix">
      <xsl:param name="name"/>
      <xsl:variable name="subName"
                    select="substring($name,1,string-length($name) - string-length(tokenize($name,'-')[last()]) - 1)"/>
      <xsl:choose>
         <xsl:when test="ends-with($subName,'cpe')">
            <xsl:value-of select="xcf:get-locator-prefix($subName)"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="tokenize($subName,'/')[last()]"/>
         </xsl:otherwise>
      </xsl:choose>0
  </xsl:function>
  <xsl:function xmlns:xcf="nist:scap:xslt:function"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                 name="xcf:get-locator-prefix-res">
      <xsl:param name="name"/>
      <xsl:variable name="subName"
                    select="substring($name,1,string-length($name) - string-length(tokenize($name,'-')[last()]) - 1)"/>
      <xsl:value-of select="xcf:get-locator-prefix($subName)"/>
  </xsl:function>
</schema>
