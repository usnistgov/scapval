<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:ds="http://scap.nist.gov/schema/scap/source/1.2" xmlns:xcf="nist:scap:xslt:function"
  xmlns:xccdf="http://checklists.nist.gov/xccdf/1.2"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:oval-def="http://oval.mitre.org/XMLSchema/oval-definitions-5" xmlns:ocil="http://scap.nist.gov/schema/ocil/2.0">
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-all-profile-parents">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:if test="exists($node/@extends)">
      <xsl:sequence
        select="xcf:get-all-profile-parents($doc,$doc//xccdf:Benchmark//xccdf:Profile[@id eq $node/@extends])"/>
    </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-component">
    <xsl:param name="component-ref"/>
    <xsl:sequence
      select="$component-ref/ancestor::ds:data-stream-collection//ds:component[@id eq substring($component-ref/@xlink:href,2)]"
    />
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-def-children">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:for-each select="$doc//oval-def:extend_definition[@definition_ref eq $node/@id]/ancestor::oval-def:definition">
      <xsl:sequence select="xcf:get-all-def-children($doc,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-all-parents">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:for-each select="$node//oval-def:extend_definition">
      <xsl:sequence
        select="xcf:get-all-parents($doc,ancestor::oval-def:oval_definitions//*[@id eq current()/@definition_ref])"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-ocil-var-ref">
    <xsl:param name="ocil_questionnaire"/>
    <xsl:variable name="initialSet">
      <xsl:for-each select="$ocil_questionnaire/ocil:actions/ocil:test_action_ref">
        <xsl:sequence
          select="ancestor::ocil:ocil/ocil:test_actions/ocil:numeric_question_test_action[@id eq current()]/ocil:when_equals[@var_ref]"/>
        <xsl:sequence
          select="ancestor::ocil:ocil/ocil:test_actions/ocil:string_question_test_action[@id eq current()]/ocil:when_pattern/ocil:pattern[@var_ref]"/>
        <xsl:sequence
          select="ancestor::ocil:ocil/ocil:test_actions/ocil:numeric_question_test_action[@id eq current()]/ocil:when_range/ocil:range/ocil:min[@var_ref]"/>
        <xsl:sequence
          select="ancestor::ocil:ocil/ocil:test_actions/ocil:numeric_question_test_action[@id eq current()]/ocil:when_range/ocil:range/ocil:max[@var_ref]"/>
        <xsl:for-each select="ancestor::ocil:ocil/ocil:test_actions/*[@id eq current()]">
          <xsl:sequence
            select="ancestor::ocil:ocil/ocil:questions/ocil:choice_question[@id eq current()/@question_ref]/ocil:choice[@var_ref]"/>
          <xsl:for-each
            select="ancestor::ocil:ocil/ocil:questions/ocil:choice_question[@id eq current()/@question_ref]/ocil:choice_group_ref">
            <xsl:sequence select="ancestor::ocil:questions/ocil:choice_group[@id eq current()]/ocil:choice[@var_ref]"/>
          </xsl:for-each>
        </xsl:for-each>
      </xsl:for-each>
    </xsl:variable>
    <xsl:for-each select="$initialSet/*">
      <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-component-ref">
    <xsl:param name="catalog"/>
    <xsl:param name="uri"/>
    <xsl:variable name="component-ref-uri" select="xcf:resolve-in-catalog($catalog/*[1],$uri)"/>
    <xsl:if test="$component-ref-uri ne ''">
      <xsl:sequence select="$catalog/ancestor::ds:data-stream//ds:component-ref[@id eq substring($component-ref-uri,2)]"
      />
    </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:pass-ocil-var-ref">
    <xsl:param name="ocil_questionnaire"/>
    <xsl:param name="var_ref"/>
    <xsl:sequence select="$var_ref"/>
    <xsl:for-each
      select="$ocil_questionnaire/ancestor::ocil:ocil/ocil:variables/ocil:local_variable[@id eq $var_ref/@var_ref]/ocil:set/ocil:when_range/ocil:min[@var_ref]">
      <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
    </xsl:for-each>
    <xsl:for-each
      select="$ocil_questionnaire/ancestor::ocil:ocil/ocil:variables/ocil:local_variable[@id eq $var_ref/@var_ref]/ocil:set/ocil:when_range/ocil:max[@var_ref]">
      <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
                <xsl:value-of
                  select="concat($resolver-node/@rewritePrefix,substring($uri,string-length($resolver-node/@uriStartString)+1))"
                />
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
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-external-vars">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$doc//oval-def:external_variable[@id eq $node/@var_ref]"/>
    <xsl:for-each select="$doc//*[@id eq $node/@var_ref]//*[@var_ref]">
      <xsl:sequence select="xcf:get-all-external-vars($doc,current())"/>
    </xsl:for-each>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-all-group-parents">
    <xsl:param name="doc"/>
    <xsl:param name="node"/>
    <xsl:sequence select="$node"/>
    <xsl:if test="exists($node/@extends)">
      <xsl:sequence select="xcf:get-all-group-parents($doc,$doc//xccdf:Benchmark//xccdf:Group[@id eq $node/@extends])"/>
    </xsl:if>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
    </xsl:choose>
  </xsl:function>
  <xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    name="xcf:get-locator-prefix-res">
    <xsl:param name="name"/>
    <xsl:variable name="subName"
      select="substring($name,1,string-length($name) - string-length(tokenize($name,'-')[last()]) - 1)"/>
    <xsl:value-of select="xcf:get-locator-prefix($subName)"/>
  </xsl:function>

</xsl:stylesheet>