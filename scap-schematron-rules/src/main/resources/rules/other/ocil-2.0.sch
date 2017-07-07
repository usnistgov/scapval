<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
  <ns uri="http://purl.oclc.org/dsdl/schematron" prefix="sch"/>
  <ns uri="http://scap.nist.gov/schema/ocil/2" prefix="ocil"/>
  <ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
  
  <pattern>
    <rule context="ocil:choice_question[exists(@default_answer_ref)]">
      <assert test="exists(ocil:choice[@id eq current()/@default_answer_ref]) or (some $m in ocil:choice_group_ref satisfies exists(ancestor::ocil:questions/ocil:choice_group[@id eq $m]/ocil:choice[@id eq current()/@default_answer_ref]))">The @default_answer_ref on &lt;choice_question @id='<value-of select="@id"/>'&gt; must specify an answer applicable for the question</assert>
    </rule>
    <rule context="ocil:choice_question_result[exists(ocil:answer/@choice_ref)]">
      <let name="choice_question" value="ancestor::ocil:ocil/ocil:questions/ocil:choice_question[@id eq current()/@question_ref]"/>
      <assert test="exists($choice_question/ocil:choice[@id eq current()/ocil:answer/@choice_ref]) or (some $m in $choice_question/ocil:choice_group_ref satisfies exists($choice_question/ancestor::ocil:questions/ocil:choice_group[@id eq $m]/ocil:choice[@id eq current()/ocil:answer/@choice_ref]))">The @choice_ref on &lt;answer&gt; in &lt;choice_question_result @question_ref='<value-of select="@question_ref"/>'&gt; must specify an answer applicable for the question</assert>
    </rule>
    <rule context="ocil:local_variable">
      <let name="question_type" value="ancestor::ocil:ocil/ocil:questions/ocil:*[@id eq current()/@question_ref]/local-name()"/>
      <let name="choice_question" value="ancestor::ocil:ocil/ocil:questions/ocil:choice_question[@id eq current()/@question_ref]"/>
      <assert test="if( $question_type eq 'choice_question' ) then @datatype eq 'TEXT' else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore must be @datatype='TEXT'</assert>
      <assert test="if( $question_type eq 'numeric_question' ) then @datatype eq 'NUMERIC' else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore must be @datatype='NUMERIC'</assert>
      <assert test="if( $question_type eq 'string_question' ) then @datatype eq 'TEXT' else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore must be @datatype='TEXT'</assert>
      <assert test="if( $question_type eq 'boolean_question' ) then every $m in ocil:set/* satisfies matches($m/local-name(),'(when_boolean)') else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore all expressions in &lt;set&gt; must be &lt;when_boolean&gt;</assert>
      <assert test="if( $question_type eq 'choice_question' ) then every $m in ocil:set/* satisfies matches($m/local-name(),'(when_choice)') else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore all expressions in &lt;set&gt; must be &lt;when_choice&gt;</assert>
      <assert test="if( $question_type eq 'numeric_question' ) then every $m in ocil:set/* satisfies matches($m/local-name(),'(when_pattern|when_range)') else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore all expressions in &lt;set&gt; must be &lt;when_pattern&gt; or &lt;when_range&gt;</assert>
      <assert test="if( $question_type eq 'string_question' ) then every $m in ocil:set/* satisfies matches($m/local-name(),'(when_pattern)') else true()">&lt;local_variable @id='<value-of select="@id"/>' references a question of type &lt;<value-of select="$question_type"/>&gt; and therefore all expressions in &lt;set&gt; must be &lt;when_pattern&gt;</assert>
      <assert test="if( $question_type eq 'choice_question' ) then every $l in ocil:set/ocil:when_choice satisfies exists($choice_question/ocil:choice[@id eq $l/@choice_ref]) or (some $m in $choice_question/ocil:choice_group_ref satisfies exists($choice_question/ancestor::ocil:questions/ocil:choice_group[@id eq $m]/ocil:choice[@id eq $l/@choice_ref])) else true()">The @choice_ref on every &lt;when_choice&gt; in &lt;set&gt; in &lt;local_variable @id='<value-of select="@id"/>'&gt; must specify a choice applicable for the question being referenced in @question_ref='<value-of select="@question_ref"/>'</assert>
    </rule>
  </pattern>
  <pattern>
    <rule context="ocil:question_results/*">
      <assert test="if( not(exists(@response)) or @response eq 'ANSWERED') then not(exists(ocil:answer[boolean(@xsi:nil) eq true()])) and (ocil:answer ne '' or exists(ocil:answer[exists(@choice_ref)])) else exists(ocil:answer[boolean(@xsi:nil) eq true()]) and not(exists(ocil:answer[exists(@choice_ref)]))">If @response on &lt;<value-of select="local-name()"/> @question_ref='<value-of select="@question_ref"/>'&gt; is not provided, or is set to 'ANSWERED', then &lt;answer&gt; must not be nil and a value must be provided (or @choice_ref specified if applicable), otherwise &lt;answer&gt; must be nil and no other attributes or values may be specified on &lt;answer&gt;</assert>
    </rule>
  </pattern>
</schema>