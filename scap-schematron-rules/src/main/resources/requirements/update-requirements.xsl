<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:scapval-ext="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3"
    xmlns:req="http://csrc.nist.gov/ns/decima/requirements/1.0">
    <xsl:template match="@*|*|processing-instruction()|comment()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@scapval-ext:check-type">
<!--        <xsl:copy/>-->
        <xsl:choose>
            <xsl:when test=". eq 'APPLICATION'">
                <xsl:attribute name="check-implementation" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">CODE</xsl:attribute>
                <xsl:call-template name="check-scope"/>
                <xsl:call-template name="check-source"/>
            </xsl:when>
            <xsl:when test=". eq 'NOT_CHECKED'">
                <xsl:attribute name="check-implementation" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">NOT_IMPLEMENTED</xsl:attribute>
                <xsl:call-template name="check-scope"/>
                <xsl:call-template name="check-source"/>
            </xsl:when>
            <xsl:when test=". eq 'SCHEMATRON'">
                <xsl:attribute name="check-implementation" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">SCHEMATRON</xsl:attribute>
                <xsl:call-template name="check-scope"/>
                <xsl:call-template name="check-source"/>
            </xsl:when>
            <xsl:when test=". eq 'SCHEMA'">
                <xsl:attribute name="check-implementation" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">SCHEMA</xsl:attribute>
                <xsl:call-template name="check-scope"/>
                <xsl:call-template name="check-source"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="check-source">
        <xsl:choose>
            <xsl:when test="starts-with(ancestor::req:derived-requirement/@id, 'A-')">
                <xsl:attribute name="check-source" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">APPLICATION</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="check-source" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">SPECIFICATION</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="check-scope">
        <xsl:choose>
            <xsl:when test="ancestor::req:requirements/req:resource[@id='component']">
                <xsl:attribute name="check-scope" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">COMPONENT</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="check-scope" namespace="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3">DATASTREAM</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>