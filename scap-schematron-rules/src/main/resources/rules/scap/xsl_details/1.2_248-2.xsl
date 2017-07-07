<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:ds="http://scap.nist.gov/schema/scap/source/1.2" xmlns:xcf="nist:scap:xslt:function"
  xmlns:xccdf="http://checklists.nist.gov/xccdf/1.2" xmlns:cat="urn:oasis:names:tc:entity:xmlns:xml:catalog"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
  xmlns:oval-def="http://oval.mitre.org/XMLSchema/oval-definitions-5" xmlns:ocil="http://scap.nist.gov/schema/ocil/2.0">
  <xsl:import href="functions.xsl"/>

  <xsl:template match="/">
    <xsl:element name="records">
      <xsl:apply-templates select=".//ds:data-stream[@use-case eq 'INVENTORY']"/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="ds:data-stream">
    <xsl:variable name="ds" select="."/>
    <xsl:for-each select="ds:checklists/ds:component-ref">
      <xsl:variable name="m" select="."/>
      <xsl:for-each
        select="xcf:get-component($m)//xccdf:Rule">
        <xsl:variable name="n" select="."/>
        <xsl:if
          test="not(some $o in $n/xccdf:check[@system eq 'http://oval.mitre.org/XMLSchema/oval-definitions-5' or @system eq 'http://scap.nist.gov/schema/ocil/2']/xccdf:check-content-ref[exists(@name) and not(xcf:is-external-ref($m/cat:catalog, @href) cast as xsd:boolean)] satisfies (exists(xcf:get-component(xcf:get-component-ref($m/cat:catalog, $o/@href))//oval-def:definition[@id eq $o/@name and matches(@class,'^(inventory)$')]) or exists(xcf:get-component(xcf:get-component-ref($m/cat:catalog, $o/@href))//ocil:questionnaire[@id eq $o/@name])))">
          <xsl:element name="record">
            <xsl:element name="cell">
              <xsl:value-of select="string('rule_ds')"/>
            </xsl:element>
            <xsl:element name="cell">
              <xsl:value-of select="$n/@id"/>
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