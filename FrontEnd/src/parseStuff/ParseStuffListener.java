// Generated from C:/Users/EdSnow/IdeaProjects/Sequent/FrontEnd/src/parseStuff\ParseStuff.g4 by ANTLR 4.6
package parseStuff;

//  from Term.g4
    import org.antlr.v4.runtime.Parser;
    import term.*;
    import p_term.*;
    import formula.*;
    import p_formula.*;
	import p_axiomatization.*;
    import parseStuff.formulaBuilder.*;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ParseStuffParser}.
 */
public interface ParseStuffListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code SFormula_type_axiomatization}
	 * labeled alternative in {@link ParseStuffParser#sformula}.
	 * @param ctx the parse tree
	 */
	void enterSFormula_type_axiomatization(ParseStuffParser.SFormula_type_axiomatizationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SFormula_type_axiomatization}
	 * labeled alternative in {@link ParseStuffParser#sformula}.
	 * @param ctx the parse tree
	 */
	void exitSFormula_type_axiomatization(ParseStuffParser.SFormula_type_axiomatizationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SFormula_type_formula}
	 * labeled alternative in {@link ParseStuffParser#sformula}.
	 * @param ctx the parse tree
	 */
	void enterSFormula_type_formula(ParseStuffParser.SFormula_type_formulaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SFormula_type_formula}
	 * labeled alternative in {@link ParseStuffParser#sformula}.
	 * @param ctx the parse tree
	 */
	void exitSFormula_type_formula(ParseStuffParser.SFormula_type_formulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#axiomatization}.
	 * @param ctx the parse tree
	 */
	void enterAxiomatization(ParseStuffParser.AxiomatizationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#axiomatization}.
	 * @param ctx the parse tree
	 */
	void exitAxiomatization(ParseStuffParser.AxiomatizationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(ParseStuffParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(ParseStuffParser.FormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#implication}.
	 * @param ctx the parse tree
	 */
	void enterImplication(ParseStuffParser.ImplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#implication}.
	 * @param ctx the parse tree
	 */
	void exitImplication(ParseStuffParser.ImplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(ParseStuffParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(ParseStuffParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(ParseStuffParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(ParseStuffParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom_type_formula}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom_type_formula(ParseStuffParser.Atom_type_formulaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom_type_formula}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom_type_formula(ParseStuffParser.Atom_type_formulaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom_type_proposition}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom_type_proposition(ParseStuffParser.Atom_type_propositionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom_type_proposition}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom_type_proposition(ParseStuffParser.Atom_type_propositionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom_type_predicate}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom_type_predicate(ParseStuffParser.Atom_type_predicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom_type_predicate}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom_type_predicate(ParseStuffParser.Atom_type_predicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom_type_negation}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom_type_negation(ParseStuffParser.Atom_type_negationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom_type_negation}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom_type_negation(ParseStuffParser.Atom_type_negationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom_type_relation}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom_type_relation(ParseStuffParser.Atom_type_relationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom_type_relation}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom_type_relation(ParseStuffParser.Atom_type_relationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom_type_quantified}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom_type_quantified(ParseStuffParser.Atom_type_quantifiedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom_type_quantified}
	 * labeled alternative in {@link ParseStuffParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom_type_quantified(ParseStuffParser.Atom_type_quantifiedContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#proposition}.
	 * @param ctx the parse tree
	 */
	void enterProposition(ParseStuffParser.PropositionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#proposition}.
	 * @param ctx the parse tree
	 */
	void exitProposition(ParseStuffParser.PropositionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#predicate_application}.
	 * @param ctx the parse tree
	 */
	void enterPredicate_application(ParseStuffParser.Predicate_applicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#predicate_application}.
	 * @param ctx the parse tree
	 */
	void exitPredicate_application(ParseStuffParser.Predicate_applicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(ParseStuffParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(ParseStuffParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(ParseStuffParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(ParseStuffParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Quantified_formula_type_simple}
	 * labeled alternative in {@link ParseStuffParser#quantified_formula}.
	 * @param ctx the parse tree
	 */
	void enterQuantified_formula_type_simple(ParseStuffParser.Quantified_formula_type_simpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Quantified_formula_type_simple}
	 * labeled alternative in {@link ParseStuffParser#quantified_formula}.
	 * @param ctx the parse tree
	 */
	void exitQuantified_formula_type_simple(ParseStuffParser.Quantified_formula_type_simpleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Quantified_formula_type_compound}
	 * labeled alternative in {@link ParseStuffParser#quantified_formula}.
	 * @param ctx the parse tree
	 */
	void enterQuantified_formula_type_compound(ParseStuffParser.Quantified_formula_type_compoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Quantified_formula_type_compound}
	 * labeled alternative in {@link ParseStuffParser#quantified_formula}.
	 * @param ctx the parse tree
	 */
	void exitQuantified_formula_type_compound(ParseStuffParser.Quantified_formula_type_compoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#quantified_variable}.
	 * @param ctx the parse tree
	 */
	void enterQuantified_variable(ParseStuffParser.Quantified_variableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#quantified_variable}.
	 * @param ctx the parse tree
	 */
	void exitQuantified_variable(ParseStuffParser.Quantified_variableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(ParseStuffParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(ParseStuffParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#product}.
	 * @param ctx the parse tree
	 */
	void enterProduct(ParseStuffParser.ProductContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#product}.
	 * @param ctx the parse tree
	 */
	void exitProduct(ParseStuffParser.ProductContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Particle_type_term}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void enterParticle_type_term(ParseStuffParser.Particle_type_termContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Particle_type_term}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void exitParticle_type_term(ParseStuffParser.Particle_type_termContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Particle_type_variable}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void enterParticle_type_variable(ParseStuffParser.Particle_type_variableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Particle_type_variable}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void exitParticle_type_variable(ParseStuffParser.Particle_type_variableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Particle_type_application}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void enterParticle_type_application(ParseStuffParser.Particle_type_applicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Particle_type_application}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void exitParticle_type_application(ParseStuffParser.Particle_type_applicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Particle_type_successor}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void enterParticle_type_successor(ParseStuffParser.Particle_type_successorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Particle_type_successor}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void exitParticle_type_successor(ParseStuffParser.Particle_type_successorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Particle_type_zero}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void enterParticle_type_zero(ParseStuffParser.Particle_type_zeroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Particle_type_zero}
	 * labeled alternative in {@link ParseStuffParser#particle}.
	 * @param ctx the parse tree
	 */
	void exitParticle_type_zero(ParseStuffParser.Particle_type_zeroContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#term_variable}.
	 * @param ctx the parse tree
	 */
	void enterTerm_variable(ParseStuffParser.Term_variableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#term_variable}.
	 * @param ctx the parse tree
	 */
	void exitTerm_variable(ParseStuffParser.Term_variableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#function_application}.
	 * @param ctx the parse tree
	 */
	void enterFunction_application(ParseStuffParser.Function_applicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#function_application}.
	 * @param ctx the parse tree
	 */
	void exitFunction_application(ParseStuffParser.Function_applicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#successor_application}.
	 * @param ctx the parse tree
	 */
	void enterSuccessor_application(ParseStuffParser.Successor_applicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#successor_application}.
	 * @param ctx the parse tree
	 */
	void exitSuccessor_application(ParseStuffParser.Successor_applicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(ParseStuffParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(ParseStuffParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParseStuffParser#applicand}.
	 * @param ctx the parse tree
	 */
	void enterApplicand(ParseStuffParser.ApplicandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParseStuffParser#applicand}.
	 * @param ctx the parse tree
	 */
	void exitApplicand(ParseStuffParser.ApplicandContext ctx);
}