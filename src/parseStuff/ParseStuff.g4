grammar ParseStuff;

//import Term;

options {
	language=Java;
}

@parser::header {
//  from Term.g4
    import org.antlr.v4.runtime.Parser;
    import term.*;
    import p_term.*;
    import formula.*;
    import p_formula.*;
	import p_axiomatization.*;
    import formulaBuilder.*;
}


//  parser rules


//unit
//	:	( sformula ';' )* EOF
//;


//  the grammar for sformulas


sformula returns [P_SFormula f] locals [Token t, P_Axiomatization a]
    :   {($t=_input.LT(1)).getType()==Identifier && ($a=P_Axiomatization$.MODULE$.lookupAxiomatization($t.getText()))!=null}? axiomatization
                                # SFormula_type_axiomatization
    |   formula                 # SFormula_type_formula
;

axiomatization
    :   Identifier
;


//  the grammar for formulas


formula returns [Formula f]
	:	implication
	        ( Biconditional_connective formula )?
;

implication returns [Formula f]
	:	disjunction
	        ( Conditional_connective formula )?
;

disjunction returns [Formula f]
	:	conjunction
	        ( Disjunction_connective conjunction )*
;

conjunction returns [Formula f]
	:	atom
	        ( Conjunction_connective atom )*
;

atom returns [Formula f]
	:	'(' formula ')'         # Atom_type_formula
	|	proposition             # Atom_type_proposition
	|	predicate_application   # Atom_type_predicate
	|	negation                # Atom_type_negation
	|	relation                # Atom_type_relation
	|   quantified_formula      # Atom_type_quantified
;

proposition returns [Formula f] locals [P_Proposition p]
    :   Identifier {($p=p_formula.package$.MODULE$.lookupProposition($Identifier.getText()))!=null}?<fail='axiomatization not permitted'>
;

predicate_application returns [Formula f]
	:	Identifier applicand
;

negation returns [Formula f]
	:	Negation_connective atom
;

relation returns [Formula f] locals [p_formula.P_Predicate p]
    :	term
        (
                '\u2208'        {$p=parseStuff.package$.MODULE$.predicateIsElementOf();}
                                //  ∈  is element of
            |	'='             {$p=parseStuff.package$.MODULE$.predicateIsEqualTo();}
                                //  =  is equal to
            |	'\u2260'        {$p=parseStuff.package$.MODULE$.predicateIsNotEqualTo();}
            |   '!='            //  ≠  is not equal to
            |	'<'             {$p=parseStuff.package$.MODULE$.predicateIsLessThan();}
                                //  <  is less than
            |	'\u2264'        {$p=parseStuff.package$.MODULE$.predicateIsLessThanOrEqualTo();}
            |   '<='            //  ≤  is less than or equal to
            |	'>'             {$p=parseStuff.package$.MODULE$.predicateIsGreaterThan();}
                                //  >  is greater than
            |	'\u2265'        {$p=parseStuff.package$.MODULE$.predicateIsGreaterThanOrEqualTo();}
            |   '>='            //  ≥  is greater than or equal to
        )
        term
;

quantified_formula returns [Formula f]
	:	(
	            quantified_variable '.'
            |   '(' quantified_variable ')'
        ) atom
                                # Quantified_formula_type_simple
    |   quantified_variable quantified_formula
                                # Quantified_formula_type_compound
;

quantified_variable returns [scala.Tuple2<scala.Enumeration.Value,java.lang.String> q]
	:	(
                '\u2200'        {$q=p_formula.QuantificationKind.Universal();}
                                //  ∀  for all
	        |	'\u2203'        {$q=p_formula.QuantificationKind.Existential();}
                                //  ∃  for some
        )
        variable
;


//  the grammars for terms and applicands


term returns [Term t]
	:	product ( Addition_operator product )*
;

product returns [Term t]
	:	particle ( Multiplication_operator particle )*
;

particle returns [Term t]
	:	'(' term ')'            # Particle_type_term
    |   term_variable           # Particle_type_variable
	|	function_application    # Particle_type_application
	|   successor_application   # Particle_type_successor
	|   Zero                    # Particle_type_zero
//	|   Natural_numbers_origin_zero
//	|   Natural_numbers_origin_one
;

term_variable returns [Term t]
	:	variable
;

function_application returns [Term t]
	:	Identifier applicand
;

successor_application returns [Term t]
	:	Successor_operator term
;

variable returns [P_VarName v]
	:	Identifier
;

applicand returns [Term[] a]
	:	'('
	        ( term ( ',' term )* )?
	    ')'
;


//  lexer tokens for formulas


Conjunction_connective
    :   '\u2227'                //  ∧  logical and
    |   '/\\'
;

Disjunction_connective
    :   '\u2228'                //  ∨  logical or
    |   '\\/'
;

Negation_connective
    :   '\u00AC'                //  ¬  logical not
    |   '~'
;

Conditional_connective
    :   '\u2192'                //  →  implies
    |   '->'
;

Biconditional_connective
    :   '\u2194'                //  ↔  if and only if
    |   '<->'
;

Qualification_sign
    :   '|'                     //  |  such that
;


//  lexer tokens for terms


Identifier
	:	[a-zA-Z] [a-zA-Z0-9_]*
;

Whitespace
	:	[ \t\r\n\uFEFF]+ -> skip
;                               //  includes the BOM for UTF-8

Successor_operator
    :   '/S'                    //  S  successor of
;

Addition_operator
    :   '+'                     //  +  sum of
;

Multiplication_operator
    :   '\u00D7'                //  ×  product of
    |   '*'
;

Zero
    :   '0'                     //  0  zero
;

Natural_numbers_origin_zero
    :   '\u2115'                //  ℕ  natural numbers including zero
;

Natural_numbers_origin_one
    :   '\u2115\u207A'          //  ℕ⁺  natural numbers excluding zero
;

ErrorCharacter
    :   .                       // any character
;                               //   failing to match any other token
