<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!-- This file was produced from *system-characteristics-schema.xsd files with the provided XSLT ExtractSchFromXSD.xsl from https://github
.com/OVALProject/Language/tree/5.11.2/tools and modified for SCAPVal specific use (see oval-rules-readme.txt for details)-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xs="http://www.w3.org/2001/XMLSchema"
            queryBinding="xslt">
    <sch:ns xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="oval"
            uri="http://oval.mitre.org/XMLSchema/oval-common-5"/>
    <sch:ns xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="oval-def"
            uri="http://oval.mitre.org/XMLSchema/oval-definitions-5"/>
    <sch:ns xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="oval-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"/>
    <sch:ns xmlns:aix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#aix"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="aix-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#aix"/>
    <sch:ns xmlns:android-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#android"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="android-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#android"/>
    <sch:ns xmlns:apache-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#apache"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="apache-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"/>
    <sch:ns xmlns:apple-ios-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#apple_ios"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="apple-ios-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#apple_ios"/>
    <sch:ns xmlns:asa-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#asa"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="asa-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#asa"/>
    <sch:ns xmlns:catos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#catos"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="catos-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#catos"/>
    <sch:ns xmlns:esx-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#esx"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="esx-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#esx"/>
    <sch:ns xmlns:freebsd-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#freebsd"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="freebsd-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#freebsd"/>
    <sch:ns xmlns:hpux-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#hpux"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="hpux-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#hpux"/>
    <sch:ns xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="ind-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"/>
    <sch:ns xmlns:ios-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#ios"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="ios-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#ios"/>
    <sch:ns xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="iosxe-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"/>
    <sch:ns xmlns:junos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#junos"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="junos-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#junos"/>
    <sch:ns xmlns:linux-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="linux-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux"/>
    <sch:ns xmlns:macos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#macos"
            xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="macos-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#macos"/>
    <sch:ns xmlns:netconf-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#netconf"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="netconf-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#netconf"/>
    <sch:ns xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:pixos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#pixos"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="pixos-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#pixos"/>
    <sch:ns xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:sp-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#sharepoint"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="sp-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#sharepoint"/>
    <sch:ns xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:sol-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#solaris"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="sol-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#solaris"/>
    <sch:ns xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:unix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="unix-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix"/>
    <sch:ns xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="win-sc"
            uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"/>
    <sch:ns xmlns:aix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#aix"
            xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            prefix="xsi"
            uri="http://www.w3.org/2001/XMLSchema-instance"/>
<!--ESX-->
    <sch:pattern xmlns:esx-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#esx"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="esx-sc_patchitempatch_number">
        <sch:rule context="esx-sc:patch_item/esx-sc:patch_number">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>
<!--    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"-->
<!--                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"-->
<!--                 id="oval_schema_version_core_matches_platforms">-->
<!--        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">-->
<!--            <sch:let name="core_version_portion"-->
<!--                     value="parent::oval-def:generator/oval:schema_version[not(@platform)]"/>-->
<!--            <sch:assert test="starts-with(.,$core_version_portion)">This platform's version (<sch:value-of select="."/>) MUST match the core version being used: <sch:value-of select="$core_version_portion"/>.</sch:assert>-->
<!--        </sch:rule>-->
<!--    </sch:pattern>-->
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
<!--FREEBSD-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--HPUX-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--Independent-->
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_filehash_item_dep">
        <sch:rule context="ind-sc:filehash_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_environmentvariable_item_dep">
        <sch:rule context="ind-sc:environmentvariable_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_ldap57_item_dep">
        <sch:rule context="ind-sc:ldap57_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_ldap57_itemvalue">
        <sch:rule context="ind-sc:ldap57_item/ind-sc:value">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the value entity of a ldap57_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_sql_item_dep">
        <sch:rule context="ind-sc:sql_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_sql57_itemresult">
        <sch:rule context="ind-sc:sql57_item/ind-sc:result">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the result entity of a sql57_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_tfc_i">
        <sch:rule context="win-sc:textfilecontent_item/win-sc:instance">
            <sch:assert test="string-length(.) = 0 or number(.) &lt; 1">
                <sch:value-of select="../@id"/> - the value of instance must be greater than one</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_txtitemline">
        <sch:rule context="ind-sc:textfilecontent_item/ind-sc:line">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_ldaptype_timestamp_value_dep">
        <sch:rule context="oval-sc:oval_system_characteristics/oval-sc:system_data/ind-sc:ldap_item/ind-sc:ldaptype|oval-sc:oval_system_characteristics/oval-sc:system_data/ind-sc:ldap57_item/ind-sc:ldaptype">
            <sch:report test=".='LDAPTYPE_TIMESTAMP'">
                DEPRECATED ELEMENT VALUE IN: ldap_item ELEMENT VALUE: <sch:value-of select="."/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ind-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ind-sc_ldaptype_email_value_dep">
        <sch:rule context="oval-sc:oval_system_characteristics/oval-sc:system_data/ind-sc:ldap_item/ind-sc:ldaptype|oval-sc:oval_system_characteristics/oval-sc:system_data/ind-sc:ldap57_item/ind-sc:ldaptype">
            <sch:report test=".='LDAPTYPE_EMAIL'">
                DEPRECATED ELEMENT VALUE IN: ldap_item ELEMENT VALUE: <sch:value-of select="."/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--IOS-->
    <sch:pattern xmlns:ios-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#ios"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ios-sc_interfaceitemnoipdbc">
        <sch:rule context="ios-sc:interface_item/ios-sc:no_ip_directed_broadcast_command">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ios-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#ios"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ios-sc_interfaceitem_urpf_command_dep">
        <sch:rule context="ios-sc:interface_item/ios-sc:urpf_command">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it was replaced by the ipv4_urpf_command and ipv6_urpf_command entities.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ios-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#ios"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ios-sc_versionitemmajor_release">
        <sch:rule context="ios-sc:version_item/ios-sc:major_release">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ios-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#ios"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="ios-sc_versionitemtrain_number">
        <sch:rule context="ios-sc:version_item/ios-sc:train_number">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--IOSXE-->
    <sch:pattern xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="iosxe-sc_versionitem_platform">
        <sch:rule context="iosxe-sc:version_item/iosxe-sc:platform">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it cannot be reliably collected.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="iosxe-sc_versionitem_rp">
        <sch:rule context="iosxe-sc:version_item/iosxe-sc:rp">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it cannot be reliably collected.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="iosxe-sc_versionitem_pkg">
        <sch:rule context="iosxe-sc:version_item/iosxe-sc:pkg">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it cannot be reliably collected.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="iosxe-sc_versionitem_ios_release">
        <sch:rule context="iosxe-sc:version_item/iosxe-sc:ios_release">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it is irrelevant to the IOS-XE version.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="iosxe-sc_versionitem_ios_train">
        <sch:rule context="iosxe-sc:version_item/iosxe-sc:ios_train">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it is irrelevant to the IOS-XE version.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:iosxe-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#iosxe"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="iosxe-sc_interfaceitem_urpf_command_dep">
        <sch:rule context="iosxe-sc:interface_item/iosxe-sc:urpf_command">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it was replaced by the ipv4_urpf_command and ipv6_urpf_command entities.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--JUNOS-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--Linux-->
    <sch:pattern xmlns:linux-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="linux-sc_dpkginfoitemevrstring_id">
        <sch:rule context="linux-sc:dpkginfo_item/linux-sc:evr">
            <sch:report test="@datatype='evr_string'">
                <sch:value-of select="../@id"/> Warning: There are differences in the algorithms for how the version strings of Debian and RPM packages are compared. As a result, a new debian_evr_string datatype was added to the OVAL Language and should be used, for this entity, instead of the evr_string datatype.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:linux-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="linux-sc_rpmverify_item_dep">
        <sch:rule context="linux-sc:rpmverify_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:linux-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="linux-sc_rpmverifypackage_dicp_dep">
        <sch:rule context="linux-sc:rpmverifypackage_item/linux-sc:digest_check_passed">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="../@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:linux-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="linux-sc_rpmverifypackage_sigcp_dep">
        <sch:rule context="linux-sc:rpmverifypackage_item/linux-sc:signature_check_passed">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="../@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--MACOS-->
    <sch:pattern xmlns:macos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#macos"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="macos-sc_inetlisteningserveritem_dep">
        <sch:rule context="macos-sc:inetlisteningserver_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:macos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#macos"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="macos-sc_plistitem_dep">
        <sch:rule context="macos-sc:plist_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:macos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#macos"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="macos-sc_pwpolicy_item_dep">
        <sch:rule context="macos-sc:pwpolicy_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--NETCONF-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--OVALSC-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--PIXOS-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--SHAREPOINT-->
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:sp-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#sharepoint"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="sp-sc_spjobdefinition_item_dep">
        <sch:rule context="sp-sc:spjobdefinition_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--SOLARIS-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--UNIX-->
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:unix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="unix-sc_file_gid">
        <sch:rule context="unix-sc:file_item/unix-sc:group_id">
            <sch:assert test="string-length(.) = 0 or number(.) &lt; 0">
                <sch:value-of select="../@id"/> - the value of group_id must be greater than zero</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:unix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="unix-sc_file_uid">
        <sch:rule context="unix-sc:file_item/unix-sc:user_id">
            <sch:assert test="string-length(.) = 0 or number(.) &lt; 0">
                <sch:value-of select="../@id"/> - the value of user_id must be greater than zero</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:unix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="unix-sc_processitem_dep">
        <sch:rule context="unix-sc:process_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:unix-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="unix-sc_sccsitem_dep">
        <sch:rule context="unix-sc:sccs_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--WINDOWS-->
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_accesstoken_item_dep">
        <sch:rule context="win-sc:accesstoken_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_activedirectory57_item_dep">
        <sch:rule context="win-sc:activedirectory57_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_activedirectory57_itemvalue">
        <sch:rule context="win-sc:activedirectory57_item/win-sc:value">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the value entity of a activedirectory57_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_auditeventpolicysubcategoriesitemkerberos_ticket_events">
        <sch:rule context="win-sc:auditeventpolicysubcategories_item/win-sc:kerberos_ticket_events">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_cmdletitemparameters">
        <sch:rule context="win-sc:cmdlet_item/win-sc:parameters">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the parameters entity of a cmdlet_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_cmdletitemselect">
        <sch:rule context="win-sc:cmdlet_item/win-sc:select">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the select entity of a cmdlet_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_cmdletitemvalue">
        <sch:rule context="win-sc:cmdlet_item/win-sc:value">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the value entity of a cmdlet_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_fileaudititemtrustee_name">
        <sch:rule context="win-sc:fileauditedpermissions_item/win-sc:trustee_name">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_feritemtrustee_name">
        <sch:rule context="win-sc:fileeffectiverights_item/win-sc:trustee_name">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_group_item_dep">
        <sch:rule context="win-sc:group_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_lpi_fl">
        <sch:rule context="win-sc:lockoutpolicy_item/win-sc:force_logoff">
            <sch:assert test="string-length(.) = 0 or number(.) &gt;= 0">
                <sch:value-of select="../@id"/> - the value of force_logoff must be greater than or equal to zero</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_lpi_ld">
        <sch:rule context="win-sc:lockoutpolicy_item/win-sc:lockout_duration">
            <sch:assert test="string-length(.) = 0 or number(.) &gt;= 0">
                <sch:value-of select="../@id"/> - the value of lockout_duration must be greater than or equal to zero</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_pwp_mpa">
        <sch:rule context="win-sc:passwordpolicy_item/win-sc:max_passwd_age">
            <sch:assert test="number(.) &gt;= 0">
                <sch:value-of select="../@id"/> - the value of max_passwd_age must be greater than or equal to zero</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_rapitemtrustee_name">
        <sch:rule context="win-sc:regkeyauditedpermissions_item/win-sc:trustee_name">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_rapitemstandard_synchronize">
        <sch:rule context="win-sc:regkeyauditedpermissions_item/win-sc:standard_synchronize">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_reritemtrustee_name">
        <sch:rule context="win-sc:regkeyeffectiverights_item/win-sc:trustee_name">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_reritemstandard_synchronize">
        <sch:rule context="win-sc:regkeyeffectiverights_item/win-sc:standard_synchronize">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_user_item_dep">
        <sch:rule context="win-sc:user_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_wmi_item_dep">
        <sch:rule context="win-sc:wmi_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:win-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="win-sc_wmi57_itemresult">
        <sch:rule context="win-sc:wmi57_item/win-sc:result">
            <sch:assert test="@datatype='record'">
                <sch:value-of select="../@id"/> - datatype attribute for the result entity of a wmi57_item must be 'record'</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
<!--AIX-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--ANDROID-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--APACHE-->
    <sch:pattern xmlns:apache-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#apache"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="apache-sc_httpd_item_dep">
        <sch:rule context="apache-sc:httpd_item">
            <sch:report test="true()">DEPRECATED ITEM: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--APPLE-->
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--ASA-->
    <sch:pattern xmlns:asa-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#asa"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="asa-sc_interfaceitem_urpf_command_dep">
        <sch:rule context="asa-sc:interface_item/asa-sc:urpf_command">
            <sch:report test="true()">Warning: DEPRECATED ENTITY: <sch:value-of select="name()"/>. This entity has been deprecated because it was replaced by the ipv4_urpf_command and ipv6_urpf_command entities.</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
<!--CATOS-->
    <sch:pattern xmlns:catos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#catos"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="catos-sc_versionitemcatos_major_release">
        <sch:rule context="catos-sc:version_item/catos-sc:catos_major_release">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:catos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#catos"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="catos-sc_versionitemcatos_individual_release">
        <sch:rule context="catos-sc:version_item/catos-sc:catos_individual_release">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:catos-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#catos"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="catos-sc_versionitemcatos_version_id">
        <sch:rule context="catos-sc:version_item/catos-sc:catos_version_id">
            <sch:report test="true()">DEPRECATED ELEMENT: <sch:value-of select="name()"/> ID: <sch:value-of select="@id"/>
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
                 xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"
                 xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval-sc_entity_rules">
        <sch:rule context="oval-sc:system_data/*/*|oval-sc:system_data/*/*/*">
            <sch:assert flag="WARNING" test="not(@status) or @status='exists' or .=''">Warning: item <sch:value-of select="../@id"/> - a value for the <sch:value-of select="name()"/> entity should only be supplied if the status attribute is 'exists'</sch:assert>
            <!--<sch:assert test="if (@datatype='binary') then (matches(., '^[0-9a-fA-F]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of binary.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='boolean') then (matches(., '^true$|^false$|^1$|^0$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of boolean.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='evr_string') then (matches(., '^[^:\-]*:[^:\-]*-[^:\-]*$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of evr_string.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='float') then (matches(., '^[+\-]?[0-9]+([\.][0-9]+)?([eE][+\-]?[0-9]+)?$|^NaN$|^INF$|^\-INF$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of float.</sch:assert>-->
            <!--<sch:assert test="if (@datatype='int') then (matches(., '^[+\-]?[0-9]+$')) else (1=1)"><sch:value-of select="../@id"/> - A value of '<sch:value-of select="."/>' for the <sch:value-of select="name()"/> entity is not valid given a datatype of int.</sch:assert>-->
        </sch:rule>
        <sch:rule context="oval-sc:system_data/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']|oval-sc:system_data/*/*/*[not((@xsi:nil='1' or @xsi:nil='true')) and @datatype='int']">
            <sch:assert test="(not(contains(.,'.'))) and (number(.) = floor(.))">
                <sch:value-of select="../@id"/> - The datatype for the <sch:value-of select="name()"/> entity is 'int' but the value is not an integer.</sch:assert>
            <!--  Must test for decimal point because number(x.0) = floor(x.0) is true -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_one_core_element">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator">
            <sch:assert test="count(oval:schema_version[not(@platform)]) = 1">One (and only one) schema_version element MUST be present and omit the platform attribute to represent the core version.</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_schema_version_empty_platform">
        <sch:rule context="oval-def:oval_definitions/oval-def:generator/oval:schema_version[@platform]">
            <sch:report test="@platform = ''">Warning: The platform attribute should be set to the URI of the target namespace for this platform extension.</sch:report>
        </sch:rule>
    </sch:pattern>

    <sch:pattern xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                 id="oval_none_exist_value_dep">
        <sch:rule context="oval-def:oval_definitions/oval-def:tests/child::*">
            <sch:report test="@check='none exist'">
                DEPRECATED ATTRIBUTE VALUE IN: <sch:value-of select="name()"/> ATTRIBUTE VALUE:
            </sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:diagnostics/>
</sch:schema>


