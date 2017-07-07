<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" schemaVersion="1.0">
  <sch:p>
    Specification: NIST IR 7802 - Trust Model for Security Automation Data (TMSAD) Version 1.0
    Authors: Adam Halbardier, Harold Booth
    Version: 1.0
    Date: 2011-09-22
  </sch:p>
  <sch:ns uri="http://www.w3.org/2000/09/xmldsig#" prefix="dsig"/>
  <sch:ns uri="http://scap.nist.gov/schema/xml-dsig/1.0" prefix="tmsad"/>
  <sch:pattern>
    <sch:rule context="dsig:Signature//dsig:SignatureMethod">
      <sch:assert flag="WARNING"
        test="@Algorithm eq 'http://www.w3.org/2000/09/xmldsig#dsa-sha1' 
                or @Algorithm eq 'http://www.w3.org/2000/09/xmldsig#rsa-sha1'
                or @Algorithm eq 'http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'
                or @Algorithm eq 'http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256'"
        >dsig:SignatureMethod SHOULD contain one of 'http://www.w3.org/2000/09/xmldsig#dsa-sha1',
        'http://www.w3.org/2000/09/xmldsig#rsa-sha1', 'http://www.w3.org/2001/04/xmldsig-more#rsa-sha256',
        'http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256'</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature//dsig:DigestMethod">
      <sch:assert flag="WARNING"
        test="@Algorithm eq 'http://www.w3.org/2001/04/xmlenc#sha256' 
                or @Algorithm eq 'http://www.w3.org/2001/04/xmldsig-more#sha384'
                or @Algorithm eq 'http://www.w3.org/2001/04/xmlenc#sha512'"
        >dsig:DigestMethod @Algorithm SHOULD contain one of 'http://www.w3.org/2001/04/xmlenc#sha256',
        'http://www.w3.org/2001/04/xmldsig-more#sha384', 'http://www.w3.org/2001/04/xmlenc#sha512'</sch:assert>
      <sch:assert flag="WARNING" test="@Algorithm ne 'http://www.w3.org/2000/09/xmldsig#sha1'">dsig:DigestMethod
        @Algorithm SHOULD NOT contain 'http://www.w3.org/2000/09/xmldsig#sha1'</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature//dsig:DigestValue">
      <sch:let name="algorithm" value="preceding-sibling::dsig:DigestMethod[1]/@Algorithm"/>
      <sch:assert flag="ERROR"
        test="if( $algorithm eq 'http://www.w3.org/2001/04/xmlenc#sha256' ) then matches(replace(.,'\s+',''),'^[a-zA-z0-9/\+]{43}=$') else true()"
        >When dsig:DigestMethod @Algorithm equals 'http://www.w3.org/2001/04/xmlenc#sha256' then dsig:DigestValue MUST
        be a 256 bit Base64 value</sch:assert>
      <sch:assert flag="ERROR"
        test="if( $algorithm eq 'http://www.w3.org/2001/04/xmldsig-more#sha384' ) then matches(replace(.,'\s+',''),'^[a-zA-z0-9/\+]{64}$') else true()"
        >When dsig:DigestMethod @Algorithm equals 'http://www.w3.org/2001/04/xmldsig-more#sha384' then dsig:DigestValue
        MUST be a 384 bit Base64 value</sch:assert>
      <sch:assert flag="ERROR"
        test="if( $algorithm eq 'http://www.w3.org/2001/04/xmlenc#sha512' ) then matches(replace(.,'\s+',''),'^[a-zA-z0-9/\+]{86}={2}$') else true()"
        >When dsig:DigestMethod @Algorithm equals 'http://www.w3.org/2001/04/xmlenc#sha512' then dsig:DigestValue MUST
        be a 512 bit Base64 value</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature/dsig:SignedInfo">
      <sch:assert flag="WARNING" test="count(dsig:Reference) gt 1">At least two references SHOULD be provided on
        dsig:SignedInfo. One reference SHOULD be to the content being signed and the other reference SHOULD be to a
        dsig:SignatureProperties</sch:assert>
      <sch:assert flag="WARNING"
        test="if( count(dsig:Reference) gt 1 ) then some $m in dsig:Reference satisfies exists($m/ancestor::dsig:Signature[1]//dsig:SignatureProperties[concat('#',@Id) eq $m/@URI]) else true()"
        >If more than one reference is supplied on dsig:SignedInfo, then at least one of the references SHOULD be to a
        dsig:SignatureProperties</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature//dsig:Manifest">
      <sch:assert flag="ERROR"
        test="exists(ancestor::dsig:Signature[1]/dsig:SignedInfo/dsig:Reference[@URI eq concat('#',current()/@Id)])"
        >Every dsig:Manifest supplied on a signature MUST be referenced by a reference on the
        dsig:SignedInfo</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature">
      <sch:assert flag="WARNING" test="exists(dsig:Object/dsig:SignatureProperties)">A dsig:SignatureProperties SHOULD
        be included on a dsig:Signature</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature//dsig:SignatureProperties">
      <sch:assert flag="WARNING" test="exists(dsig:SignatureProperty/tmsad:signature-info)">A dsig:SignatureProperties
        SHOULD include a dsig:SignatureProperty that includes a tmsad:signature-info</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature//dsig:Reference">
      <sch:let name="ref-obj" value="ancestor::dsig:Signature[1]//dsig:*[concat('#',@Id) eq current()/@URI]"/>
      <sch:assert flag="ERROR"
        test="if( exists($ref-obj) and local-name($ref-obj) eq 'Object' ) then @Type eq 'http://www.w3.org/2000/09/xmldsig#Object' else true()"
        >If a reference points to a dsig:Object, the @Type MUST be populated on the reference with
        'http://www.w3.org/2000/09/xmldsig#Object'</sch:assert>
      <sch:assert flag="ERROR"
        test="if( exists($ref-obj) and local-name($ref-obj) eq 'Manifest' ) then @Type eq 'http://www.w3.org/2000/09/xmldsig#Manifest' else true()"
        >If a reference points to a dsig:Manifest, the @Type MUST be populated on the reference with
        'http://www.w3.org/2000/09/xmldsig#Manifest'</sch:assert>
      <sch:assert flag="ERROR"
        test="if( exists($ref-obj) and local-name($ref-obj) eq 'SignatureProperties' ) then @Type eq 'http://www.w3.org/2000/09/xmldsig#SignatureProperties' else true()"
        >If a reference points to a dsig:SignatureProperties, the @Type MUST be populated on the reference with
        'http://www.w3.org/2000/09/xmldsig#SignatureProperties'</sch:assert>
    </sch:rule>
    <sch:rule context="dsig:Signature//dsig:Transform">
      <sch:assert flag="WARNING" test="@Algorithm ne 'http://www.w3.org/TR/1999/REC-xpath-19991116'">Only XPath Filter
        2.0 XPath transforms SHOULD be used</sch:assert>
      <sch:assert flag="WARNING" test="@Algorithm ne 'http://www.w3.org/TR/1999/REC-xslt-19991116'">Unnamed XSLT
        transforms SHOULD be avoided</sch:assert>
      <sch:assert flag="WARNING"
        test="@Algorithm eq 'http://www.w3.org/2006/12/xml-c14n11' or @Algorithm eq 'http://www.w3.org/2006/12/xml-c14n11#WithComments'"
        >Canonical XML 1.1 transform SHOULD be used over Canonical XML 1.0</sch:assert>
    </sch:rule>
  </sch:pattern>
  
  <!-- 
  Change Log
  
  2012-05-01 - Edited assertion from "@Algorithm ne 'http://www.w3.org/TR/2001/REC-xml-c14n-20010315' and @Algorithm ne 'http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments'"
               to "@Algorithm eq 'http://www.w3.org/2006/12/xml-c14n11' or @Algorithm eq 'http://www.w3.org/2006/12/xml-c14n11#WithComments'"
  2012-05-01 - In rule "dsig:Signature//dsig:DigestValue", changed all three assertions from "...matches(.,..." to "matches(replace(.,'\s+',''),..."
  -->

</sch:schema>
