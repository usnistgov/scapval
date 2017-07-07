<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sch="http://purl.oclc.org/dsdl/schematron"
    queryBinding="xslt2" defaultPhase="GENERAL">
    <ns prefix="cpe-dict" uri="http://cpe.mitre.org/dictionary/2.0"/>
    <ns prefix="cpe-lang" uri="http://cpe.mitre.org/language/2.0"/>
    <ns prefix="dc" uri="http://purl.org/dc/elements/1.1/"/>
    <ns prefix="nvd-config" uri="http://scap.nist.gov/schema/feed/configuration/0.1"/>
    <ns prefix="ocil" uri="http://scap.nist.gov/schema/ocil/2.0"/>
    <ns prefix="oval-com" uri="http://oval.mitre.org/XMLSchema/oval-common-5"/>
    <ns prefix="oval-def" uri="http://oval.mitre.org/XMLSchema/oval-definitions-5"/>
    <ns prefix="oval-res" uri="http://oval.mitre.org/XMLSchema/oval-results-5"/>
    <ns prefix="oval-sc" uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"/>
    <ns prefix="scap" uri="http://scap.nist.gov/schema/data-stream/0.2"/>
    <ns prefix="xccdf" uri="http://checklists.nist.gov/xccdf/1.1"/>
    <ns prefix="xcf" uri="nist:scap:xslt:function"/>
    <ns prefix="xml" uri="http://www.w3.org/XML/1998/namespace"/>
    <ns prefix="xsd" uri="http://www.w3.org/2001/XMLSchema"/>

    <phase id="GENERAL">
        <active pattern="scap-general"/>
        <active pattern="scap-general-isolated-1"/>
        <active pattern="scap-additional-rules"/>
    </phase>
    <phase id="CONFIGURATION">
        <active pattern="scap-general"/>
        <active pattern="scap-general-isolated-1"/>
        <active pattern="scap-additional-rules"/>
        <active pattern="scap-use-case-conf-verification"/>
    </phase>
    <phase id="VULNERABILITY">
        <active pattern="scap-general"/>
        <active pattern="scap-general-isolated-1"/>
        <active pattern="scap-additional-rules"/>
        <active pattern="scap-use-case-vul-assess-xccdf-oval"/>
    </phase>
    <phase id="INVENTORY">
        <active pattern="scap-general"/>
        <active pattern="scap-general-isolated-1"/>
        <active pattern="scap-additional-rules"/>
        <active pattern="scap-use-case-inventory-collection"/>
    </phase>
    <phase id="OVAL_ONLY">
        <active pattern="scap-general"/>
        <active pattern="scap-general-isolated-1"/>
        <active pattern="scap-additional-rules"/>
    </phase>

	<let name="datafiles_directory" value="'datafiles'"/>

    <pattern id="scap-general">
        <rule id="scap-general-xccdf-benchmark" context="xccdf:Benchmark">
            <assert id="scap-general-xccdf-description"
                test="every $m in (. union .//xccdf:Profile union .//xccdf:Value union .//xccdf:Group union .//xccdf:Rule) satisfies ((exists($m/xccdf:description)) and (if( count($m/xccdf:description) gt 1 ) then count($m/xccdf:description) eq count($m/xccdf:description[@xml:lang]) else true()))"
                >SRC-10-1|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-no-xml-base"
                test="not(exists(@xml:base) or exists(.//*[@xml:base]))">SRC-111-1|xccdf:Benchmark <value-of select="@id"
                /></assert>
            <assert id="scap-general-xccdf-benchmark-cpe-platform-ref-dict"
                test="every $m in .//xccdf:platform[matches(@idref,'[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}')] satisfies exists(//cpe-dict:cpe-list/cpe-dict:cpe-item[@name eq $m/@idref])"
                >SRC-112-2|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-check-system-attribute"
                test="every $m in .//xccdf:check satisfies matches($m/@system,'^(http://oval.mitre.org/XMLSchema/oval-definitions-5|http://scap.nist.gov/schema/ocil/2)$')"
                >SRC-122-1|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-platform-cpe"
                test="every $m in (. union .//xccdf:Profile union .//xccdf:Value union .//xccdf:Group union .//xccdf:Rule) satisfies (every $n in $m/xccdf:platform satisfies matches($n/@idref,'[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}') or exists($n/ancestor::xccdf:Benchmark/cpe-lang:platform-specification/cpe-lang:platform[@id eq substring($n/@idref,2)]))"
                >SRC-14-1|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-cpe-deprecated"
                test="every $m in xccdf:platform[matches(@idref,'[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}')] satisfies not(exists(document(concat($datafiles_directory,'/official-cpe-dictionary_v2.2.xml'))/cpe-dict:cpe-list/cpe-dict:cpe-item[count(tokenize(@name,':')) ge count(tokenize($m/@idref,':')) and (tokenize(@name,':')[1] eq tokenize($m/@idref,':')[1] or tokenize($m/@idref,':')[1] eq '' or not(exists(tokenize($m/@idref,':')[1]))) and (tokenize(@name,':')[2] eq tokenize($m/@idref,':')[2] or tokenize($m/@idref,':')[2] eq '' or not(exists(tokenize($m/@idref,':')[2]))) and (tokenize(@name,':')[3] eq tokenize($m/@idref,':')[3] or tokenize($m/@idref,':')[3] eq '' or not(exists(tokenize($m/@idref,':')[3]))) and (tokenize(@name,':')[4] eq tokenize($m/@idref,':')[4] or tokenize($m/@idref,':')[4] eq '' or not(exists(tokenize($m/@idref,':')[4]))) and (tokenize(@name,':')[5] eq tokenize($m/@idref,':')[5] or tokenize($m/@idref,':')[5] eq '' or not(exists(tokenize($m/@idref,':')[5]))) and (tokenize(@name,':')[6] eq tokenize($m/@idref,':')[6] or tokenize($m/@idref,':')[6] eq '' or not(exists(tokenize($m/@idref,':')[6]))) and (tokenize(@name,':')[7] eq tokenize($m/@idref,':')[7] or tokenize($m/@idref,':')[7] eq '' or not(exists(tokenize($m/@idref,':')[7]))) and (tokenize(@name,':')[8] eq tokenize($m/@idref,':')[8] or tokenize($m/@idref,':')[8] eq '' or not(exists(tokenize($m/@idref,':')[8])))])) or exists(document(concat($datafiles_directory,'/official-cpe-dictionary_v2.2.xml'))/cpe-dict:cpe-list/cpe-dict:cpe-item[count(tokenize(@name,':')) ge count(tokenize($m/@idref,':')) and (tokenize(@name,':')[1] eq tokenize($m/@idref,':')[1] or tokenize($m/@idref,':')[1] eq '' or not(exists(tokenize($m/@idref,':')[1]))) and (tokenize(@name,':')[2] eq tokenize($m/@idref,':')[2] or tokenize($m/@idref,':')[2] eq '' or not(exists(tokenize($m/@idref,':')[2]))) and (tokenize(@name,':')[3] eq tokenize($m/@idref,':')[3] or tokenize($m/@idref,':')[3] eq '' or not(exists(tokenize($m/@idref,':')[3]))) and (tokenize(@name,':')[4] eq tokenize($m/@idref,':')[4] or tokenize($m/@idref,':')[4] eq '' or not(exists(tokenize($m/@idref,':')[4]))) and (tokenize(@name,':')[5] eq tokenize($m/@idref,':')[5] or tokenize($m/@idref,':')[5] eq '' or not(exists(tokenize($m/@idref,':')[5]))) and (tokenize(@name,':')[6] eq tokenize($m/@idref,':')[6] or tokenize($m/@idref,':')[6] eq '' or not(exists(tokenize($m/@idref,':')[6]))) and (tokenize(@name,':')[7] eq tokenize($m/@idref,':')[7] or tokenize($m/@idref,':')[7] eq '' or not(exists(tokenize($m/@idref,':')[7]))) and (tokenize(@name,':')[8] eq tokenize($m/@idref,':')[8] or tokenize($m/@idref,':')[8] eq '' or not(exists(tokenize($m/@idref,':')[8]))) and (not(exists(@deprecated)) or @deprecated eq 'false')])"
                >SRC-15-1|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-platform-cpe-reference-valid"
                test="every $m in xccdf:platform[matches(@idref,'[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}')] satisfies exists(document(concat($datafiles_directory,'/official-cpe-dictionary_v2.2.xml'))/cpe-dict:cpe-list/cpe-dict:cpe-item[count(tokenize(@name,':')) ge count(tokenize($m/@idref,':')) and (tokenize(@name,':')[1] eq tokenize($m/@idref,':')[1] or tokenize($m/@idref,':')[1] eq '' or not(exists(tokenize($m/@idref,':')[1]))) and (tokenize(@name,':')[2] eq tokenize($m/@idref,':')[2] or tokenize($m/@idref,':')[2] eq '' or not(exists(tokenize($m/@idref,':')[2]))) and (tokenize(@name,':')[3] eq tokenize($m/@idref,':')[3] or tokenize($m/@idref,':')[3] eq '' or not(exists(tokenize($m/@idref,':')[3]))) and (tokenize(@name,':')[4] eq tokenize($m/@idref,':')[4] or tokenize($m/@idref,':')[4] eq '' or not(exists(tokenize($m/@idref,':')[4]))) and (tokenize(@name,':')[5] eq tokenize($m/@idref,':')[5] or tokenize($m/@idref,':')[5] eq '' or not(exists(tokenize($m/@idref,':')[5]))) and (tokenize(@name,':')[6] eq tokenize($m/@idref,':')[6] or tokenize($m/@idref,':')[6] eq '' or not(exists(tokenize($m/@idref,':')[6]))) and (tokenize(@name,':')[7] eq tokenize($m/@idref,':')[7] or tokenize($m/@idref,':')[7] eq '' or not(exists(tokenize($m/@idref,':')[7]))) and (tokenize(@name,':')[8] eq tokenize($m/@idref,':')[8] or tokenize($m/@idref,':')[8] eq '' or not(exists(tokenize($m/@idref,':')[8])))])"
                >SRC-15-2|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-lang-required" test="exists(@xml:lang)">SRC-2-1|xccdf:Benchmark
                    <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-no-check-content-check" test="not(exists(.//xccdf:check-content))"
                >SRC-25-1|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-ref-cpe"
                test="every $m in current()//xccdf:platform[not(starts-with(@idref, '#'))]/@idref | current()//cpe-lang:fact-ref/@name satisfies exists(//scap:cpe-dictionary-content//cpe-dict:cpe-item[@name eq $m])"
                >SRC-272-1|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-style" test="@style eq 'SCAP_1.1'">SRC-4-1|xccdf:Benchmark <value-of select="@id"
                /></assert>
            <assert id="scap-general-xccdf-metadata-missing" test="exists(xccdf:metadata)">SRC-8-2|xccdf:Benchmark <value-of
                    select="@id"/></assert>
            <assert id="scap-general-xccdf-benchmark-metadata-source-uri"
                test="every $m in xccdf:metadata/dc:source satisfies matches($m,'^[a-zA-Z][a-zA-Z1-9\+\.-]*:')"
                >SRC-8-3|xccdf:Benchmark <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-metadata-populated"
                test="if( exists(xccdf:metadata) ) then exists(xccdf:metadata/dc:creator) and exists(xccdf:metadata/dc:publisher) and exists(xccdf:metadata/dc:contributor) and exists(xccdf:metadata/dc:source) else true()"
                >SRC-8-1|xccdf:Benchmark <value-of select="@id"/></assert>
        </rule>
        <sch:rule id="scap-general-xccdf-profile" context="xccdf:Profile">
            <sch:assert id="scap-general-xccdf-profile-unique" test="if (count(ancestor::scap:data-stream-collection//xccdf:Benchmark/xccdf:Profile[@id eq current()/@id]) gt 1) then false() else true()">A-25-1|xccdf:Profile <sch:value-of select="@id"/></sch:assert>
        </sch:rule>
        <rule id="scap-general-oval-cpe-dict-check"
            context="cpe-dict:cpe-list/cpe-dict:cpe-item/cpe-dict:check[@system eq 'http://oval.mitre.org/XMLSchema/oval-definitions-5']">
            <assert id="scap-general-oval-cpe-dict-check-ref-inventory"
                test="exists(//*[@id eq current()/@href and @content-type eq 'CPE_INVENTORY']//oval-def:definition[@id eq current()])"
                >SRC-112-1|cpe-dict:cpe-item <value-of select="@name"/></assert>
            <assert id="scap-general-oval-cpe-dict-check-ref-oval-def"
                test="exists(//*[@id eq current()/@href and @content-type eq 'CPE_INVENTORY']//oval-def:definition[@class eq 'inventory' and @id eq current()])"
                >SRC-273-1|cpe-dict:cpe-item <value-of select="@name"/></assert>
        </rule>
        <rule id="scap-general-oval-cpe-dict" context="cpe-dict:cpe-list/cpe-dict:cpe-item">
            <assert id="scap-general-xccdf-rule-official-dict"
                test="exists(document(concat($datafiles_directory,'/official-cpe-dictionary_v2.2.xml'))/cpe-dict:cpe-list/cpe-dict:cpe-item[count(tokenize(@name,':')) ge count(tokenize(current()/@name,':')) and (tokenize(@name,':')[1] eq tokenize(current()/@name,':')[1] or tokenize(current()/@name,':')[1] eq '' or not(exists(tokenize(current()/@name,':')[1]))) and (tokenize(@name,':')[2] eq tokenize(current()/@name,':')[2] or tokenize(current()/@name,':')[2] eq '' or not(exists(tokenize(current()/@name,':')[2]))) and (tokenize(@name,':')[3] eq tokenize(current()/@name,':')[3] or tokenize(current()/@name,':')[3] eq '' or not(exists(tokenize(current()/@name,':')[3]))) and (tokenize(@name,':')[4] eq tokenize(current()/@name,':')[4] or tokenize(current()/@name,':')[4] eq '' or not(exists(tokenize(current()/@name,':')[4]))) and (tokenize(@name,':')[5] eq tokenize(current()/@name,':')[5] or tokenize(current()/@name,':')[5] eq '' or not(exists(tokenize(current()/@name,':')[5]))) and (tokenize(@name,':')[6] eq tokenize(current()/@name,':')[6] or tokenize(current()/@name,':')[6] eq '' or not(exists(tokenize(current()/@name,':')[6]))) and (tokenize(@name,':')[7] eq tokenize(current()/@name,':')[7] or tokenize(current()/@name,':')[7] eq '' or not(exists(tokenize(current()/@name,':')[7]))) and (tokenize(@name,':')[8] eq tokenize(current()/@name,':')[8] or tokenize(current()/@name,':')[8] eq '' or not(exists(tokenize(current()/@name,':')[8])))])"
                >SRC-148-1|cpe-dict:cpe-item <value-of select="@name"/></assert>
            <assert id="scap-general-oval-cpe-inventory"
                test="every $ m in cpe-dict:check satisfies ends-with($m/@href,'cpe-oval.xml') and exists(//scap:check-system-content[@id eq $m/@href]//oval-def:definition[@id eq $m])"
                >SRC-72-1|cpe-dict:cpe-item <value-of select="@name"/></assert>
            <assert id="scap-general-oval-cpe-dict-title-match"
                test="every $m in cpe-dict:check satisfies (some $n in cpe-dict:title satisfies exists(//scap:check-system-content[@id eq $m/@href]//oval-def:definition[@id eq $m]/oval-def:metadata/oval-def:affected/oval-def:platform[. eq $n]))"
                >SRC-73-1|cpe-dict:cpe-item <value-of select="@name"/></assert>
        </rule>
        <rule id="scap-general-scap-content" context="scap:data-stream">
            <assert id="scap-general-scap-content-locator-prefix"
                test="every $m in *[not(@content-type eq 'OVAL_PATCH' and starts-with(@id,'http'))] satisfies if( exists($m/preceding-sibling::*[not(@content-type eq 'OVAL_PATCH' and starts-with(@id,'http'))]) ) then xcf:get-locator-prefix($m/@id) eq xcf:get-locator-prefix($m/preceding-sibling::*[not(@content-type eq 'OVAL_PATCH' and starts-with(@id,'http'))][1]/@id) else true()"
                >SRC-154-1</assert>
        </rule>
        <rule id="scap-general-xccdf-check-content-ref" context="xccdf:check-content-ref">
            <assert id="scap-general-xccdf-check-content-ref-patch-id-req"
                test="if( //*[@id eq current()/@href and @content-type eq 'OVAL_PATCH'] ) then exists(ancestor::xccdf:Rule[@id eq 'security_patches_up_to_date']) else true()"
                >SRC-169-1|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-check-content-ref-name-omit-patch"
                test="if( //*[@id eq current()/@href and @content-type eq 'OVAL_PATCH'] ) then not(exists(@name)) else true()"
                >SRC-171-1|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-check-content-ref-ocil-var-bound-req"
                test="if( ends-with(@href, 'ocil.xml') ) then every $m in xcf:get-ocil-var-ref(//scap:check-system-content[@id eq current()/@href]/ocil:ocil/ocil:questionnaires/ocil:questionnaire[@id eq current()/@name]) satisfies exists(preceding-sibling::xccdf:check-export[@export-name eq $m/@var_ref]) else true()"
                >SRC-252-3|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-check-content-ref-ref-ocil-questionnaire"
                test="if(//*[@id eq current()/@href and @content-type eq 'OCIL_QUESTIONNAIRE']) then //*[@id eq current()/@href and @content-type eq 'OCIL_QUESTIONNAIRE']//ocil:questionnaire[@id eq current()/@name] else true()"
                >SRC-252-1|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-check-content-ref-name-required"
                test="if( //*[@id eq current()/@href and @content-type ne 'OVAL_PATCH'] ) then exists(@name) else true()"
                >SRC-268-1|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
        </rule>
        <rule id="scap-general-xccdf-rule" context="xccdf:Rule">
            <assert id="scap-general-xccdf-rule-patch-only-one-check"
                test="if( @id eq 'security_patches_up_to_date' ) then (count(xccdf:check) eq 1) and exists(xccdf:check[@system eq 'http://oval.mitre.org/XMLSchema/oval-definitions-5']) else true()"
                >SRC-170-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-check-content-ref-req"
                test="every $m in .//xccdf:check satisfies exists($m/xccdf:check-content-ref)">SRC-175-1|xccdf:Rule
                    <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-cve-system"
                test="every $m in xccdf:ident[starts-with(.,'CVE')] satisfies matches($m/@system, '^(http://cve.mitre.org|CVE)$')"
                >SRC-224-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-cce-system"
                test="every $m in xccdf:ident[matches(.,'^CCE-\d+-\d$')] satisfies matches($m/@system, '^(http://cce.mitre.org|CCE)$')"
                >SRC-225-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-cpe-system"
                test="every $m in xccdf:ident[matches(.,'[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}')] satisfies matches($m/@system, '^(http://cpe.mitre.org|CPE)$')"
                >SRC-226-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-xccdf-oval-ref-match"
                test="every $m in current()//xccdf:check[starts-with(@system,'http://oval.mitre.org/XMLSchema/oval-definitions-5')]/xccdf:check-content-ref satisfies ((every $n in //*[@id eq $m/@href]//*[@id eq $m/@name]//oval-def:reference[matches(@source,'^(CCE|http://cce.mitre.org)$')] satisfies exists(current()/xccdf:ident[matches(@system,'^(CCE|http://cce.mitre.org)$') and . eq $n/@ref_id])) and (every $n in //*[@id eq $m/@href]//*[@id eq $m/@name]//oval-def:reference[matches(@source,'^(CVE|http://cve.mitre.org)$')] satisfies exists(current()/xccdf:ident[matches(@system,'^(CVE|http://cve.mitre.org)$') and . eq $n/@ref_id])) and (every $n in //*[@id eq $m/@href]//*[@id eq $m/@name]//oval-def:reference[matches(@source,'^(CPE|http://cpe.mitre.org)$')] satisfies exists(current()/xccdf:ident[matches(@system,'^(CPE|http://cpe.mitre.org)$') and . eq $n/@ref_id])))"
                >SRC-251-2|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-cve-cce-cpe"
                test="exists(xccdf:ident[matches(@system,'^(CCE|http://cce.mitre.org|CVE|http://cve.mitre.org|CPE|http://cpe.mitre.org)$')])"
                >SRC-251-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-ocil-ref-resolvable"
                test="every $m in current()//xccdf:check-content-ref[ends-with(@href,'ocil.xml') and @name] satisfies exists(//scap:check-system-content[@id eq $m/@href]//ocil:questionnaire[@id eq $m/@name])"
                >SRC-252-2|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-ident-cve-cce-cpe-order"
                test="every $m in xccdf:ident satisfies matches($m/@system,'^(CPE|CVE|CCE|http://cpe.mitre.org|http://cve.mitre.org|http://cce.mitre.org)$') or not(exists($m/following-sibling::xccdf:ident[matches(@system,'^(CPE|CVE|CCE|http://cpe.mitre.org|http://cve.mitre.org|http://cce.mitre.org)$')]))"
                >SRC-257-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-oval-embedded"
                test="every $m in current()//* satisfies $m/namespace-uri() ne 'http://oval.mitre.org/XMLSchema/oval-definitions-5' or $m/local-name() ne 'oval_definitions'"
                >SRC-31-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-rule-reference-oval-resolvable"
                test="every $m in current()//xccdf:check-content-ref[(ends-with(@href,'oval.xml') or ends-with(@href,'patches.xml') or ends-with(@href,'cpe-dictionary.xml') or ends-with(@href,'cpe-oval.xml'))] satisfies exists(//scap:check-system-content[@id eq $m/@href])"
                >SRC-32-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-general-xccdf-cce-exists"
                test="every $m in xccdf:ident[matches(@system, '^(CCE|http://cce.mitre.org)$')] satisfies exists(document(concat($datafiles_directory,'/nvdcce-0.1-feed.xml'))/nvd-config:nvd/nvd-config:entry[@id eq $m])"
                >SRC-74-1|xccdf:Rule <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-general-scap-cpe-dict-check" context="scap:cpe-dictionary-content">
            <assert id="scap-general-scap-cpe-dict-check-id-verify" test="ends-with(@id, '-cpe-dictionary.xml')"
                >SRC-203-3|scap:cpe-dictionary-content <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-general-oval-check" context="scap:check-system-content">
            <assert id="scap-general-oval-check-id-type-verify"
                test="(exists(oval-def:oval_definitions) and ((ends-with(@id, '-oval.xml') and (matches(@content-type,'OVAL_COMPLIANCE') or matches(@content-type,'OVAL_VULNERABILITY'))) or ((ends-with(@id, '-patches.xml') or starts-with(@id, 'http')) and matches(@content-type,'OVAL_PATCH')) or (ends-with(@id, '-cpe-oval.xml') and matches(@content-type,'CPE_INVENTORY')))) or (exists(ocil:ocil) and ends-with(@id, '-ocil.xml') and matches(@content-type,'OCIL_QUESTIONNAIRE'))"
                >SRC-203-1|scap:check-system-content <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-general-scap-xccdf-check" context="scap:xccdf-content">
            <assert id="scap-general-scap-xccdf-check-id-verify" test="ends-with(@id, '-xccdf.xml')"
                >SRC-203-2|scap:xccdf-content <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-general-oval-def" context="oval-def:definition">
            <assert id="scap-general-oval-def-rule-compliance-cce"
                test="if(@class eq 'compliance') then exists(oval-def:metadata/oval-def:reference[matches(@source,'^(http://cce.mitre.org|CCE)$')]) else true()"
                >SRC-207-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-compliance-extension"
                test="if(@class eq 'compliance') then every $m in xcf:get-all-parents(ancestor::oval-def:definitions,.) satisfies matches($m/@class,'^(compliance|inventory)$') else true()"
                >SRC-208-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-ref-cpe-inventory"
                test="if(@class eq 'inventory') then exists(oval-def:metadata/oval-def:reference[matches(@source,'^(http://cpe.mitre.org|CPE)$') and matches(@ref_id,'^[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\._\-~%]*){0,6}$')]) else true()"
                >SRC-209-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-inventory-extension"
                test="if(@class eq 'inventory') then every $m in xcf:get-all-parents(ancestor::oval-def:definitions,.) satisfies matches($m/@class,'^(inventory)$') else true()"
                >SRC-210-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-patch-ref-cve"
                test="if( @class eq 'patch' ) then exists(current()//oval-def:reference[matches(@source,'^(CVE|http://cve.mitre.org)$')]) else true()"
                >SRC-211-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-patch-extensions"
                test="if(@class eq 'patch') then every $m in xcf:get-all-parents(ancestor::oval-def:definitions,.) satisfies matches($m/@class,'^(patch|inventory)$') else true()"
                >SRC-213-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-vulnerability-cve-ref"
                test="if(@class eq 'vulnerability') then exists(oval-def:metadata/oval-def:reference[matches(@source,'^(http://cve.mitre.org|CVE)$')]) else true()"
                >SRC-214-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-vulnerability-extension"
                test="if(@class eq 'vulnerability') then every $m in xcf:get-all-parents(ancestor::oval-def:definitions,.) satisfies (if( generate-id(current()) ne generate-id($m) ) then matches($m/@class,'^(inventory|vulnerability)$') else true() ) else true()"
                >SRC-215-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-compliance-not-ref-cve-cpe"
                test="if(@class eq 'compliance') then not(exists(oval-def:metadata/oval-def:reference[matches(@source,'^(http://cve.mitre.org|CVE|http://cpe.mitre.org|CPE)$')])) else true()"
                >SRC-221-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-inventory-not-cve-cce"
                test="if(@class eq 'inventory') then not(exists(oval-def:metadata/oval-def:reference[matches(@source,'^(http://cve.mitre.org|CVE|http://cce.mitre.org|CCE)$')])) else true()"
                >SRC-222-1|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-general-oval-def-vuln-patch-not-cce-cpe"
                test="if(matches(@class,'^(vulnerability|patch)$')) then not(exists(oval-def:metadata/oval-def:reference[matches(@source,'^(http://cce.mitre.org|CCE|http://cpe.mitre.org|CPE)$')])) else true()"
                >SRC-223-1|oval-def:definition <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-general-oval-generator" context="oval-def:generator">
            <assert id="scap-general-oval-generator-version" test="matches(oval-com:schema_version,'5\.(3|4|5|6|7|8)')"
                >SRC-216-1</assert>
        </rule>
        <rule id="scap-general-ocil" context="ocil:ocil">
            <assert id="scap-general-ocil-schema-version" test="ocil:generator/ocil:schema_version eq '2.0'"
                >SRC-252-6</assert>
        </rule>
        <rule id="scap-general-xccdf-check-export" context="xccdf:Rule/xccdf:check/xccdf:check-export">
            <assert id="scap-general-xccdf-check-export-ocil-num-map"
                test="if( ends-with((current()/following-sibling::xccdf:check-content-ref)[1]/@href, 'ocil.xml') and matches(current()/ancestor::xccdf:Benchmark//xccdf:Value[@id eq current()/@value-id]/@type, '^number$')) then matches(//*[@id eq (current()/following-sibling::xccdf:check-content-ref)[1]/@href]//*[@id eq current()/@export-name]/@datatype, '^NUMERIC$') else true()"
                >SRC-252-4|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-check-export-ocil-text-map"
                test="if( ends-with((current()/following-sibling::xccdf:check-content-ref)[1]/@href, 'ocil.xml') and matches(current()/ancestor::xccdf:Benchmark//xccdf:Value[@id eq current()/@value-id]/@type, '^(string|boolean)$')) then matches(//*[@id eq (current()/following-sibling::xccdf:check-content-ref)[1]/@href]//*[@id eq current()/@export-name]/@datatype, '^TEXT$') else true()"
                >SRC-252-5|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-rule-value-binding-type-2"
                test="if( not(ends-with((current()/following-sibling::xccdf:check-content-ref)[1]/@href, 'ocil.xml')) and matches(current()/ancestor::xccdf:Benchmark//xccdf:Value[@id eq current()/@value-id]/@type, '^boolean$')) then matches(//*[@id eq (current()/following-sibling::xccdf:check-content-ref)[1]/@href]//*[@id eq current()/@export-name]/@datatype, '^boolean$') else true()"
                >SRC-38-2|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-rule-value-binding-type-1"
                test="if( not(ends-with((current()/following-sibling::xccdf:check-content-ref)[1]/@href, 'ocil.xml')) and matches(current()/ancestor::xccdf:Benchmark//xccdf:Value[@id eq current()/@value-id]/@type, '^number$')) then matches(//*[@id eq (current()/following-sibling::xccdf:check-content-ref)[1]/@href]//*[@id eq current()/@export-name]/@datatype, '^(int|float)$') else true()"
                >SRC-38-1|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert id="scap-general-xccdf-rule-value-binding-type-3"
                test="if( not(ends-with((current()/following-sibling::xccdf:check-content-ref)[1]/@href, 'ocil.xml')) and matches(current()/ancestor::xccdf:Benchmark//xccdf:Value[@id eq current()/@value-id]/@type, '^string$')) then matches(//*[@id eq (current()/following-sibling::xccdf:check-content-ref)[1]/@href]//*[@id eq current()/@export-name]/@datatype, '^(string|evr_string|version|ios_version|fileset_revision|binary)$') else true()"
                >SRC-38-3|xccdf:Rule <value-of select="ancestor::xccdf:Rule/@id"/></assert>
        </rule>
        <rule id="scap-general-xccdf-status-rule-value-date" context="xccdf:status">
            <assert id="scap-general-xccdf-status-rule-value-date-2" test="@date ne ''">SRC-5-2</assert>
            <assert id="scap-general-xccdf-status-rule-value-date-1" test=". eq 'draft' or . eq 'accepted'">SRC-5-1</assert>
        </rule>
        <rule id="scap-general-xccdf-version" context="xccdf:version">
            <assert id="scap-general-xccdf-version-update" test="exists(@update)">SRC-6-3</assert>
            <assert id="scap-general-xccdf-version-time" test="exists(@time)">SRC-6-2</assert>
        </rule>
    </pattern>
    <pattern id="scap-general-isolated-1">
        <rule id="scap-general-benchmark-profile-value-group"
            context="xccdf:Benchmark | xccdf:Profile | xccdf:Value | xccdf:Group">
            <assert id="scap-general-xccdf-title-3" test="exists(xccdf:title)">SRC-9-1|Element with @id <value-of
                    select="@id"/></assert>
            <assert id="scap-general-xccdf-title-1"
                test="if( count(xccdf:title) gt 1 ) then count(xccdf:title) eq count(xccdf:title[@xml:lang]) else true()"
                >SRC-9-2|Element with @id <value-of select="@id"/></assert>
        </rule>
    </pattern>
    <pattern id="scap-use-case-conf-verification">
        <rule id="scap-use-case-conf-verification-rule" context="xccdf:Rule">
            <assert id="scap-use-case-conf-verification-rule-ref-oval"
                test="some $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href and matches(@content-type,'^(OVAL_COMPLIANCE|OCIL_QUESTIONNAIRE|OVAL_PATCH)$')])"
                >SRC-236-2|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-use-case-conf-verification-rule-xccdf-ref-compliance"
                test="every $m in current()//xccdf:check-content-ref satisfies if(exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OVAL_COMPLIANCE'])) then exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OVAL_COMPLIANCE']//oval-def:definition[@id eq $m/@name and @class eq 'compliance']) else true()"
                >SRC-237-4|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-use-case-conf-verification-rule-ref-ocil-questionnaire"
                test="every $m in current()//xccdf:check-content-ref satisfies if(exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OCIL_QUESTIONNAIRE'])) then exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OCIL_QUESTIONNAIRE']//ocil:questionnaire[@id eq $m/@name]) else true()"
                >SRC-241-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-use-case-conf-verification-rule-refs-exist"
                test="every $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href])"
                >SRC-263-1|xccdf:Rule <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-use-case-conf-verification-content" context="/scap:data-stream">
            <assert id="scap-use-case-conf-verification-content-xccdf-req" test="exists(scap:xccdf-content/xccdf:Benchmark)"
                >SRC-236-1</assert>
            <assert id="scap-use-case-conf-verification-content-oval-compliance-one-oval-def"
                test="if(exists(scap:check-system-content[@content-type eq 'OVAL_COMPLIANCE'])) then exists(scap:check-system-content[@content-type eq 'OVAL_COMPLIANCE']//oval-def:definition[@class eq 'compliance']) else true()"
                >SRC-237-2</assert>
            <assert id="scap-use-case-conf-verification-content-ref-cpe-comp"
                test="if(exists(scap:xccdf-content//xccdf:ident[matches(@system,'^(http://cpe.mitre.org|CPE)$')])) then exists(scap:cpe-dictionary-content) else true()"
                >SRC-238-1</assert>
            <assert id="scap-use-case-conf-verification-content-cpe-ref-inventory"
                test="if(exists(scap:xccdf-content//xccdf:ident[matches(@system,'^(http://cpe.mitre.org|CPE)$')])) then exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY']) else true()"
                >SRC-239-1</assert>
            <assert id="scap-use-case-conf-verification-content-cpe-inventory-def"
                test="if(exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY'])) then exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY']//oval-def:definition[@class eq 'inventory']) else true()"
                >SRC-239-2</assert>
            <assert id="scap-use-case-conf-verification-content-patch-class-req"
                test="if(exists(scap:check-system-content[@content-type eq 'OVAL_PATCH'])) then exists(scap:check-system-content[@content-type eq 'OVAL_PATCH']//oval-def:definition[@class eq 'patch']) else true()"
                >SRC-240-1</assert>
        </rule>
        <rule id="scap-use-case-conf-verification-oval-def" context="oval-def:definition">
            <assert id="scap-use-case-conf-verification-oval-def-compliance-no-other-oval-def"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'OVAL_COMPLIANCE'])) then matches(@class, '^(compliance|inventory)$') else true()"
                >SRC-237-3|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-use-case-conf-verification-oval-def-cpe-comp-inventory"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'CPE_INVENTORY'])) then matches(@class, '^(inventory)$') else true()"
                >SRC-239-3|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-use-case-conf-verification-oval-def-patch-component"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'OVAL_PATCH'])) then matches(@class, '^(inventory|compliance|patch)$') else true()"
                >SRC-240-2|oval-def:definition <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-use-case-conf-verification-benchmark" context="xccdf:Benchmark">
            <assert id="scap-use-case-conf-verification-benchmark-one-rule-ref-oval-ocil"
                test="some $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href and matches(@content-type,'^(OVAL_COMPLIANCE|OCIL_QUESTIONNAIRE)$')])"
                >SRC-262-1|xccdf:Benchmark <value-of select="@id"/></assert>
        </rule>
    </pattern>
    <pattern id="scap-use-case-vul-assess-xccdf-oval">
        <rule id="scap-use-case-vul-assess-xccdf-oval-content" context="/scap:data-stream">
            <assert id="scap-use-case-vul-assess-xccdf-oval-content-xccdf-req" test="exists(scap:xccdf-content/xccdf:Benchmark)"
                >SRC-242-1</assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-content-one-vuln-req"
                test="if(exists(scap:check-system-content[@content-type eq 'OVAL_VULNERABILITY'])) then exists(scap:check-system-content[@content-type eq 'OVAL_VULNERABILITY']//oval-def:definition[@class eq 'vulnerability']) else true()"
                >SRC-243-2</assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-content-cpe-req"
                test="if(exists(scap:xccdf-content//xccdf:ident[matches(@system,'^(http://cpe.mitre.org|CPE)$')])) then exists(scap:cpe-dictionary-content) else true()"
                >SRC-244-1</assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-content-cpe-inv-inv-req"
                test="if(exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY'])) then exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY']//oval-def:definition[@class eq 'inventory']) else true()"
                >SRC-245-2</assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-content-cpe-inv-req"
                test="if(exists(scap:xccdf-content//xccdf:ident[matches(@system,'^(http://cpe.mitre.org|CPE)$')])) then exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY']) else true()"
                >SRC-245-1</assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-content-patch-def-req"
                test="if(exists(scap:check-system-content[@content-type eq 'OVAL_PATCH'])) then exists(scap:check-system-content[@content-type eq 'OVAL_PATCH']//oval-def:definition[@class eq 'patch']) else true()"
                >SRC-246-1</assert>
        </rule>
        <rule id="scap-use-case-vul-assess-xccdf-oval-rule" context="xccdf:Rule">
            <assert id="scap-use-case-vul-assess-xccdf-oval-rule-xccdf-oval-ref-req"
                test="some $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href and matches(@content-type,'^(OVAL_VULNERABILITY|OCIL_QUESTIONNAIRE|OVAL_PATCH)$')])"
                >SRC-242-2|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-rule-xccdf-rule-ref-vuln-def"
                test="every $m in current()//xccdf:check-content-ref satisfies if(exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OVAL_VULNERABILITY'])) then exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OVAL_VULNERABILITY']//oval-def:definition[@id eq $m/@name and @class eq 'vulnerability']) else true()"
                >SRC-243-4|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-rule-rule-ref-ocil-questionnaire"
                test="every $m in current()//xccdf:check-content-ref satisfies if(exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OCIL_QUESTIONNAIRE'])) then exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'OCIL_QUESTIONNAIRE']//ocil:questionnaire[@id eq $m/@name]) else true()"
                >SRC-247-1|xccdf:Rule <value-of select="@id"/></assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-rule-rule-ref-exist"
                test="every $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href])"
                >SRC-266-1|xccdf:Rule <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-use-case-vul-assess-xccdf-oval-oval-def" context="oval-def:definition">
            <assert id="scap-use-case-vul-assess-xccdf-oval-oval-def-allowed-classes"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'OVAL_VULNERABILITY'])) then matches(@class, '^(vulnerability|inventory)$') else true()"
                >SRC-243-3|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-oval-def-only-inv-allowed"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'CPE_INVENTORY'])) then matches(@class, '^(inventory)$') else true()"
                >SRC-245-3|oval-def:definition <value-of select="@id"/></assert>
            <assert id="scap-use-case-vul-assess-xccdf-oval-oval-def-patch-allowed-classes"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'OVAL_PATCH'])) then matches(@class, '^(inventory|compliance|patch)$') else true()"
                >SRC-246-2|oval-def:definition <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-use-case-vul-assess-xccdf-benchmark" context="xccdf:Benchmark">
            <assert id="scap-use-case-vul-assess-xccdf-benchmark-one-rule-ref-oval-ocil"
                test="some $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href and matches(@content-type,'^(OVAL_VULNERABILITY|OCIL_QUESTIONNAIRE)$')])"
                >SRC-265-1|xccdf:Benchmark <value-of select="@id"/></assert>
        </rule>
    </pattern>
    <pattern id="scap-use-case-inventory-collection">
        <rule id="scap-use-case-inventory-collection-content" context="/scap:data-stream">
            <assert id="scap-use-case-inventory-collection-content-xccdf-req" test="exists(scap:xccdf-content/xccdf:Benchmark)"
                >SRC-248-1</assert>
            <assert id="scap-use-case-inventory-collection-content-"
                test="exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY']//oval-def:definition[@class eq 'inventory'])"
                >SRC-249-3</assert>
            <assert id="scap-use-case-inventory-collection-content-cpe-inv-req"
                test="exists(scap:check-system-content[@content-type eq 'CPE_INVENTORY'])">SRC-249-1</assert>
        </rule>
        <rule id="scap-use-case-inventory-collection-xccdf-rule" context="xccdf:Rule">
            <assert id="scap-use-case-inventory-collection-xccdf-rule-ref-oval-def-req"
                test="some $m in current()//xccdf:check-content-ref satisfies exists(//scap:check-system-content[@id eq $m/@href and @content-type eq 'CPE_INVENTORY']//oval-def:definition[@id eq $m/@name])"
                >SRC-248-2|xccdf:Rule <value-of select="@id"/></assert>
        </rule>
        <rule id="scap-use-case-inventory-collection-oval-def" context="oval-def:definition">
            <assert id="scap-use-case-inventory-collection-oval-def-oval-def-inv-only"
                test="if(exists(ancestor::scap:check-system-content[@content-type eq 'CPE_INVENTORY'])) then matches(@class, '^(inventory)$') else true()"
                >SRC-249-2|oval-def:definition <value-of select="@id"/></assert>
        </rule>
    </pattern>
    <pattern id="scap-additional-rules">
        <rule id="scap-unused-oval-rule" context="oval-def:definition">
            <assert flag="WARNING"
                test="if(exists(//scap:xccdf-content)) then exists(ancestor::scap:check-system-content[@content-type eq 'OVAL_PATCH']) or exists(//xccdf:check-content-ref[@href eq current()/ancestor::scap:check-system-content/@id and (not(@name) or @name eq current()/@id)]) or exists(current()/ancestor::oval-def:oval_definitions//oval-def:extend_definition[@definition_ref eq current()/@id]) or exists(//cpe-dict:check[@href eq current()/ancestor::scap:check-system-content/@id and . eq current()/@id]) else true()"
                id="scap-unused-oval-assert">A-15-1|oval-def:definition <sch:value-of select="@id"/></assert>
        </rule>
        <rule id="scap-cce-check-rule-1" context="xccdf:ident">
            <assert flag="WARNING"
                test="if(@system eq 'http://cce.mitre.org' or @system eq 'CCE') then . ne '' else true()"
                id="scap-cce-check-assert-1">A-16-1|xccdf:Rule <sch:value-of select="ancestor::xccdf:Rule/@id"/></assert>
            <assert flag="ERROR"
                test="if(@system eq 'http://cce.mitre.org' or @system eq 'CCE') then matches(., '^CCE-\d+-\d$') else true()"
                id="scap-cce-check-assert-2">A-17-1|<sch:value-of select="."/></assert>
            <assert flag="ERROR"
                test="if((@system eq 'http://cce.mitre.org' or @system eq 'CCE') and matches(., '^CCE-\d+-\d$')) then (sum(for $j in (for $i in reverse(string-to-codepoints(concat(substring(.,5,string-length(.)-6),substring(.,string-length(.),1))))[position() mod 2 = 0] return ($i - 48) * 2, for $i in reverse(string-to-codepoints(concat(substring(.,5,string-length(.)-6),substring(.,string-length(.),1))))[position() mod 2 = 1] return ($i - 48)) return ($j mod 10, $j idiv 10)) mod 10) eq 0 else true()"
                id="scap-cce-check-assert-3">A-17-1|<sch:value-of select="."/></assert>
        </rule>
        <rule id="scap-cce-check-rule-2" context="oval-def:reference">
            <assert flag="WARNING"
                test="if(@source eq 'http://cce.mitre.org' or @source eq 'CCE') then @ref_id ne '' else true()"
                id="scap-cce-check-assert-5">A-16-1|oval-def:definition <sch:value-of
                    select="ancestor::oval-def:definition/@id"/></assert>
            <assert flag="ERROR"
                test="if(@source eq 'http://cce.mitre.org' or @source eq 'CCE') then matches(@ref_id, '^CCE-\d+-\d$') else true()"
                id="scap-cce-check-assert-6">A-17-1|<sch:value-of select="@ref_id"/></assert>
            <assert flag="ERROR"
                test="if((@source eq 'http://cce.mitre.org' or @source eq 'CCE') and matches(@ref_id, '^CCE-\d+-\d$')) then (sum(for $j in (for $i in reverse(string-to-codepoints(concat(substring(@ref_id,5,string-length(@ref_id)-6),substring(@ref_id,string-length(@ref_id),1))))[position() mod 2 = 0] return ($i - 48) * 2, for $i in reverse(string-to-codepoints(concat(substring(@ref_id,5,string-length(@ref_id)-6),substring(@ref_id,string-length(@ref_id),1))))[position() mod 2 = 1] return ($i - 48)) return ($j mod 10, $j idiv 10)) mod 10) eq 0 else true()"
                id="scap-cce-check-assert-7">A-17-1|<sch:value-of select="@ref_id"/></assert>
        </rule>
        <rule id="scap-check-system-content-match-rule" context="scap:check-system-content">
            <assert flag="ERROR"
                test="if(matches(@content-type,'^(OVAL_COMPLIANCE|OVAL_PATCH|CPE_INVENTORY|OVAL_VULNERABILITY)$')) then exists(oval-def:oval_definitions) else true()"
                id="scap-check-system-content-match-assert-1">A-18-1</assert>
            <assert flag="ERROR"
                test="if(matches(@content-type,'^(OCIL_QUESTIONS)$')) then exists(ocil:ocil) else true()"
                id="scap-check-system-content-match-assert-2">A-18-1</assert>
        </rule>
<!--        
        <rule id="scap-xccdf-profile-check-rule" context="xccdf:Benchmark">
            <assert flag="ERROR"
                test="if($profile ne '') then exists(current()//xccdf:Profile[@id eq $profile]) else true()"
                id="scap-xccdf-profile-check-asset">A20|xccdf:Benchmark <sch:value-of select="@id"/></assert>
        </rule>
-->        
        <rule id="scap-oval-tests" context="oval-def:tests/*">
            <assert flag="INFO"
                test="exists(document(concat($datafiles_directory,'/validation_program_oval_test_types.xml'))/test-types-collection/test_types[@style='SCAP_1.1']/test_type[@namespace eq namespace-uri(current()) and @name eq local-name(current())])"
                id="scap-oval-tests-validation-program-test-types">A-21-1|OVAL test <sch:value-of select="@id"/></assert>
        </rule>
    </pattern>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        name="xcf:get-all-profile-parents">
        <xsl:param name="doc"/>
        <xsl:param name="node"/>
        <xsl:sequence select="$node"/>
        <xsl:if test="exists($node/@extends)">
            <xsl:sequence
                select="xcf:get-all-profile-parents($doc,$doc//xccdf:Benchmark//xccdf:Profile[@id eq $node/@extends])"/>
        </xsl:if>
    </xsl:function>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-def-children">
        <xsl:param name="doc"/>
        <xsl:param name="node"/>
        <xsl:sequence select="$node"/>
        <xsl:for-each
            select="$doc//oval-def:extend_definition[@definition_ref eq $node/@id]/ancestor::oval-def:definition">
            <xsl:sequence select="xcf:get-all-def-children($doc,current())"/>
        </xsl:for-each>
    </xsl:function>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        name="xcf:get-all-parents">
        <xsl:param name="doc"/>
        <xsl:param name="node"/>
        <xsl:sequence select="$node"/>
        <xsl:for-each select="$node//oval-def:extend_definition">
            <xsl:sequence
                select="xcf:get-all-parents($doc,ancestor::oval-def:oval_definitions//*[@id eq current()/@definition_ref])"
            />
        </xsl:for-each>
    </xsl:function>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
                        <xsl:sequence
                            select="ancestor::ocil:questions/ocil:choice_group[@id eq current()]/ocil:choice[@var_ref]"
                        />
                    </xsl:for-each>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:variable>
        <xsl:for-each select="$initialSet/*">
            <xsl:sequence select="xcf:pass-ocil-var-ref($ocil_questionnaire,current())"/>
        </xsl:for-each>
    </xsl:function>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns="http://www.w3.org/1999/XSL/Transform" name="xcf:get-all-external-vars">
        <xsl:param name="doc"/>
        <xsl:param name="node"/>
        <xsl:sequence select="$doc//oval-def:external_variable[@id eq $node/@var_ref]"/>
        <xsl:for-each select="$doc//*[@id eq $node/@var_ref]//*[@var_ref]">
            <xsl:sequence select="xcf:get-all-external-vars($doc,current())"/>
        </xsl:for-each>
    </xsl:function>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        name="xcf:get-all-group-parents">
        <xsl:param name="doc"/>
        <xsl:param name="node"/>
        <xsl:sequence select="$node"/>
        <xsl:if test="exists($node/@extends)">
            <xsl:sequence
                select="xcf:get-all-group-parents($doc,$doc//xccdf:Benchmark//xccdf:Group[@id eq $node/@extends])"/>
        </xsl:if>
    </xsl:function>
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
    <xsl:function xmlns:xcf="nist:scap:xslt:function" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        name="xcf:get-locator-prefix-res">
        <xsl:param name="name"/>
        <xsl:variable name="subName"
            select="substring($name,1,string-length($name) - string-length(tokenize($name,'-')[last()]) - 1)"/>
        <xsl:value-of select="xcf:get-locator-prefix($subName)"/>
    </xsl:function>
</schema>
