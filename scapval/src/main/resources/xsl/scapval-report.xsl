<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:res="http://decima.nist.gov/xml/assessment-results/0.1"
    xmlns:swid-ext="https://csrc.nist.gov/ns/scapval/scapval-requirements-ext/1.3"
    version="2.0">

    <xsl:import href="classpath:xsl/result.xsl"/>
    
    <xsl:param name="has-requirement-categorizations" select="false()"/>

    <xsl:template name="process-header">
        <h1><span id="header-title"></span><small> Validation Report</small></h1>
    </xsl:template>

    <xsl:template name="process-categorizations"/>
    <xsl:template name="process-validation-details">
        <dt>Type:</dt>
        <dd><span id="content-type-title"></span></dd>
        <dt>Version:</dt>
        <dd><span id="scapval-version"></span></dd>
        <dt>Notes:</dt>
        <dd><span id="notes-content"></span></dd>
    </xsl:template>
    <xsl:template name="process-validation-summary">
    </xsl:template>
</xsl:stylesheet>