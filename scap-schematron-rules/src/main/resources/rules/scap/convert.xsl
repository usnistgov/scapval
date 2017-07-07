<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:sch="http://purl.oclc.org/dsdl/schematron"
    xmlns="http://purl.oclc.org/dsdl/schematron"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="xml" indent="yes" />

    <xsl:template match="sch:*">
        <xsl:element name="{local-name()}" >
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/sch:schema">
        <xsl:element name="schema" namespace="http://purl.oclc.org/dsdl/schematron">
            <xsl:attribute name="queryBinding">xslt2</xsl:attribute>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="sch:assert">
        <xsl:text>  </xsl:text><xsl:comment> old id: <xsl:value-of select="@id" /></xsl:comment><xsl:text>&#10;        </xsl:text>
        <xsl:element name="assert" namespace="http://purl.oclc.org/dsdl/schematron">
            <xsl:choose>
                <xsl:when test="contains(text(),'|')">
                    <xsl:attribute name="id" >REQ-<xsl:value-of select="substring-before(text(),'|')"/></xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="id" >REQ-<xsl:value-of select="text()"/></xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:attribute name="test" select="@test"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="sch:assert/text()">
        <xsl:value-of select="substring-after(text(),'|')"/>
    </xsl:template>
</xsl:stylesheet>