<?xml version="1.0" standalone="yes"?>
<!-- This file was produced from oval-system-characteristics-schema.xsd with the provided XSLT
     ExtractSchFromXSD.xsl from https://github.com/OVALProject/Language/tree/5.11.2/tools
     and generated against the OVAL 5.12.2 schemas for SCAPVal use. -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <sch:ns xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5" xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1" prefix="oval-sc" uri="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5"/>
  <sch:ns xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5" xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1" prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
  <sch:ns xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5" prefix="oval" uri="http://oval.mitre.org/XMLSchema/oval-common-5"/>
  <sch:pattern xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:oval="http://oval.mitre.org/XMLSchema/oval-common-5" xmlns:oval-sc="http://oval.mitre.org/XMLSchema/oval-system-characteristics-5" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:tns="http://scap.nist.gov/schema/asset-identification/1.1" id="oval-sc_collected_objects_must_not_be_empty">
    <sch:rule context="oval-sc:oval_system_characteristics/oval-sc:collected_objects">
      <sch:assert test="oval-sc:collected_object">A valid OVAL System Characteristics document must contain at least one collected_object element.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
