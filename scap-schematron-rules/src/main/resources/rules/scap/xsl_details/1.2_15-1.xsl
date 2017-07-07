<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:ds="http://scap.nist.gov/schema/scap/source/1.2" xmlns:xcf="nist:scap:xslt:function"
  xmlns:xccdf="http://checklists.nist.gov/xccdf/1.2" xmlns:cat="urn:oasis:names:tc:entity:xmlns:xml:catalog"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
  xmlns:oval-def="http://oval.mitre.org/XMLSchema/oval-definitions-5" xmlns:ocil="http://scap.nist.gov/schema/ocil/2.0"
  xmlns:cpe-dict="http://cpe.mitre.org/dictionary/2.0" xmlns:java="java:gov.nist.scap.schematron"
  xmlns:cpe-lang="http://cpe.mitre.org/language/2.0">
  <xsl:import href="functions.xsl"/>
  
  <xsl:template match="/">
    <xsl:element name="records">
      <xsl:apply-templates select=".//ds:data-stream"/>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="ds:data-stream">
    <xsl:variable name="ds" select="."/>
      <xsl:for-each select="ds:checklists/ds:component-ref">
        <xsl:variable name="m" select="."/>
        <xsl:for-each select="xcf:get-component($m)//xccdf:platform[not(starts-with(@idref,'#'))]">
          <xsl:variable name="n" select="."/>
          <xsl:if
            test="not(some $o in $ds/ds:dictionaries/ds:component-ref satisfies some $p in xcf:get-component($o)//cpe-dict:cpe-item satisfies java:isEqualOrSuperset($n/@idref,$p/@name))">
            <xsl:element name="record">
              <xsl:element name="cell">
                <xsl:value-of select="string('platform_ds')"/>
              </xsl:element>
              <xsl:element name="cell">
                <xsl:value-of select="$n/@idref"/>
              </xsl:element>
              <xsl:element name="cell">
                <xsl:value-of select="$ds/@id"/>
              </xsl:element>
            </xsl:element>
          </xsl:if>
        </xsl:for-each>
        <xsl:for-each select="xcf:get-component($m)//cpe-lang:fact-ref">
          <xsl:variable name="q" select="."/>
          <xsl:if
            test="not(some $r in $ds/ds:dictionaries/ds:component-ref satisfies some $s in xcf:get-component($r)//cpe-dict:cpe-item satisfies java:isEqualOrSuperset($q/@name,$s/@name))">
            <xsl:element name="record">
              <xsl:element name="cell">
                <xsl:value-of select="string('platform_ds')"/>
              </xsl:element>
              <xsl:element name="cell">
                <xsl:value-of select="string('15-1')"/>
              </xsl:element>
              <xsl:element name="cell">
                <xsl:value-of select="$q/@name"/>
              </xsl:element>
              <xsl:element name="cell">
                <xsl:value-of select="$ds/@id"/>
              </xsl:element>
            </xsl:element>
          </xsl:if>
        </xsl:for-each>
      </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>