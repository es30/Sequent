// Generated from C:/Users/EdSnow/IdeaProjects/Sequent/src/parseStuff\ParseStuff.g4 by ANTLR 4.5.3
package parseStuff;

//  from Term.g4
import term.*;
import p_term.*;
import formula.*;
import p_formula.*;
import p_axiomatization.*;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class provides an empty implementation of {@link ParseStuffListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class MyParseStuffListener implements ParseStuffListener {

	static scala.Function3<Formula, Formula, scala.Option<error.Error>, Formula> formulaBiconditional = formulaBuilder.package$.MODULE$.formulaBiconditional();
	static scala.Function3<Formula, Formula, scala.Option<error.Error>, Formula> formulaImplication = formulaBuilder.package$.MODULE$.formulaImplication();
	static scala.Function3<Formula, Formula, scala.Option<error.Error>, Formula> formulaDisjunction = formulaBuilder.package$.MODULE$.formulaDisjunction();
	static scala.Function3<Formula, Formula, scala.Option<error.Error>, Formula> formulaConjunction = formulaBuilder.package$.MODULE$.formulaConjunction();
	static scala.Function2<P_Proposition, scala.Option<error.Error>, Formula> formulaProposition = formulaBuilder.package$.MODULE$.formulaProposition();
	static scala.Function2<P_Axiomatization, scala.Option<error.Error>, SFormula> formulaAxiomatization = formulaBuilder.package$.MODULE$.formulaAxiomatization();
	static scala.Function3<P_Predicate, Term[], scala.Option<error.Error>, Formula> formulaApplication = formulaBuilder.package$.MODULE$.formulaApplication();
	static scala.Function2<Formula, scala.Option<error.Error>, Formula> formulaNegation = formulaBuilder.package$.MODULE$.formulaNegation();
	static scala.Function4<scala.Tuple2<scala.Enumeration.Value, java.lang.String>, P_VarName, Formula, scala.Option<error.Error>, Formula> formulaQuantification = formulaBuilder.package$.MODULE$.formulaQuantification();
	static scala.Function3<P_Function, Term[], scala.Option<error.Error>, Term> termApplication = formulaBuilder.package$.MODULE$.termApplication();
	static scala.Function2<P_VarName, scala.Option<error.Error>, Term> termVariable = formulaBuilder.package$.MODULE$.termVariable();

	public SFormula sf;
	public Formula f;
	public Term t;

	@Override
	public void enterSFormula_type_axiomatization(ParseStuffParser.SFormula_type_axiomatizationContext ctx) { }

	@Override
	public void exitSFormula_type_axiomatization(ParseStuffParser.SFormula_type_axiomatizationContext ctx)
	{
		sf = formulaAxiomatization.apply(ctx.a, p_formula.package$.MODULE$.noError());
	}

	@Override
	public void enterSFormula_type_formula(ParseStuffParser.SFormula_type_formulaContext ctx) {	}

	@Override
	public void exitSFormula_type_formula(ParseStuffParser.SFormula_type_formulaContext ctx)
	{
		sf = ctx.formula().f;
	}

	@Override
	public void enterAxiomatization(ParseStuffParser.AxiomatizationContext ctx) { }

	@Override
	public void exitAxiomatization(ParseStuffParser.AxiomatizationContext ctx) { }

	@Override public void enterFormula(ParseStuffParser.FormulaContext ctx) { }

	@Override public void exitFormula(ParseStuffParser.FormulaContext ctx)
	{
		f = ctx.implication().f;
		ParseStuffParser.FormulaContext fc = ctx.formula();
		if (fc != null && fc.f != null)
			f = formulaBiconditional.apply(f, fc.f, p_formula.package$.MODULE$.noError());
		ctx.f = f;
		// System.out.print(" Formula [" + f.toString() + "])");
	}

	@Override public void enterImplication(ParseStuffParser.ImplicationContext ctx) { }

	@Override public void exitImplication(ParseStuffParser.ImplicationContext ctx)
	{
		f = ctx.disjunction().f;
		ParseStuffParser.FormulaContext fc = ctx.formula();
		if (fc != null && fc.f != null)
			f = formulaImplication.apply(f, fc.f, p_formula.package$.MODULE$.noError());
		ctx.f = f;
		// System.out.print(" Implication [" + f.toString() + "])");
	}

	@Override public void enterDisjunction(ParseStuffParser.DisjunctionContext ctx) { }

	@Override public void exitDisjunction(ParseStuffParser.DisjunctionContext ctx)
	{
		List<ParseStuffParser.ConjunctionContext> list = ctx.conjunction();
		f = list.get(0).f;
		list.remove(0);
		while (! list.isEmpty())
		{
			f = formulaDisjunction.apply(f, list.get(0).f, p_formula.package$.MODULE$.noError());
			list.remove(0);
		}
		ctx.f = f;
		// System.out.print(" Disjunction [" + f.toString() + "])");
	}

	@Override public void enterConjunction(ParseStuffParser.ConjunctionContext ctx) { }

	@Override public void exitConjunction(ParseStuffParser.ConjunctionContext ctx)
	{
		List<ParseStuffParser.AtomContext> list = ctx.atom();
		f = list.get(0).f;
		list.remove(0);
		while (! list.isEmpty())
		{
			f = formulaConjunction.apply(f, list.get(0).f, p_formula.package$.MODULE$.noError());
			list.remove(0);
		}
		ctx.f = f;
		// System.out.print(" Conjunction [" + f.toString() + "])");
	}

	@Override public void enterAtom_type_formula(ParseStuffParser.Atom_type_formulaContext ctx)	{ }

	@Override public void exitAtom_type_formula(ParseStuffParser.Atom_type_formulaContext ctx)
	{
		f = ctx.formula().f;
		ctx.f = f;
	}

	@Override public void enterAtom_type_proposition(ParseStuffParser.Atom_type_propositionContext ctx)	{ }

	@Override public void exitAtom_type_proposition(ParseStuffParser.Atom_type_propositionContext ctx)
	{
		f = ctx.proposition().f;
		ctx.f = f;
	}

	@Override public void enterAtom_type_predicate(ParseStuffParser.Atom_type_predicateContext ctx)	{ }

	@Override public void exitAtom_type_predicate(ParseStuffParser.Atom_type_predicateContext ctx)
	{
        f = ctx.predicate_application().f;
        ctx.f = f;
	}

	@Override public void enterAtom_type_negation(ParseStuffParser.Atom_type_negationContext ctx) { }

	@Override public void exitAtom_type_negation(ParseStuffParser.Atom_type_negationContext ctx)
	{
		f = ctx.negation().f;
		ctx.f = f;
	}

	@Override public void enterAtom_type_relation(ParseStuffParser.Atom_type_relationContext ctx) { }

	@Override public void exitAtom_type_relation(ParseStuffParser.Atom_type_relationContext ctx)
	{
		f = ctx.relation().f;
		ctx.f = f;
	}

	@Override public void enterAtom_type_quantified(ParseStuffParser.Atom_type_quantifiedContext ctx) { }

	@Override public void exitAtom_type_quantified(ParseStuffParser.Atom_type_quantifiedContext ctx)
	{
		f = ctx.quantified_formula().f;
		ctx.f = f;
	}

	@Override public void enterProposition(ParseStuffParser.PropositionContext ctx) { }

	@Override public void exitProposition(ParseStuffParser.PropositionContext ctx)
	{
		f = formulaProposition.apply(ctx.p, p_formula.package$.MODULE$.noError());
		ctx.f = f;
		// System.out.print("(Proposition)");
	}

	@Override public void enterPredicate_application(ParseStuffParser.Predicate_applicationContext ctx) { }

	@Override public void exitPredicate_application(ParseStuffParser.Predicate_applicationContext ctx)
	{
        P_Predicate p = p_formula.package$.MODULE$.lookupPredicate(ctx.Identifier().getText());
        f = formulaApplication.apply(p, ctx.applicand().a, p_formula.package$.MODULE$.noError());
        ctx.f = f;
        // System.out.print(" Predicate_application)");
	}

	@Override public void enterNegation(ParseStuffParser.NegationContext ctx) { }

	@Override public void exitNegation(ParseStuffParser.NegationContext ctx)
	{
		f = ctx.atom().f;
		f = formulaNegation.apply(f, p_formula.package$.MODULE$.noError());
		ctx.f = f;
		// System.out.print(" Negation [" + f.toString() + "])");
	}

	@Override public void enterRelation(ParseStuffParser.RelationContext ctx) { }

	@Override public void exitRelation(ParseStuffParser.RelationContext ctx)
	{
		Term[] termArray = new Term[2];
		List<ParseStuffParser.TermContext> list = ctx.term();
		t = list.get(0).t;
		list.remove(0);
		while (! list.isEmpty())
		{
			termArray[0] = t;
			termArray[1] = list.get(0).t;
			f = formulaApplication.apply(ctx.p, termArray, p_formula.package$.MODULE$.noError());
			list.remove(0);
		}
		ctx.f = f;
		// System.out.print(" Term [" + f.toString() + "])");
	}

	@Override public void enterQuantified_formula_type_simple(ParseStuffParser.Quantified_formula_type_simpleContext ctx) { }

	@Override public void exitQuantified_formula_type_simple(ParseStuffParser.Quantified_formula_type_simpleContext ctx)
	{
		f = formulaQuantification.apply(ctx.quantified_variable().q, ctx.quantified_variable().variable().v, ctx.atom().f, p_formula.package$.MODULE$.noError());
		ctx.f = f;
	}

	@Override public void enterQuantified_formula_type_compound(ParseStuffParser.Quantified_formula_type_compoundContext ctx) { }

	@Override public void exitQuantified_formula_type_compound(ParseStuffParser.Quantified_formula_type_compoundContext ctx)
	{
		f = formulaQuantification.apply(ctx.quantified_variable().q, ctx.quantified_variable().variable().v, ctx.quantified_formula().f, p_formula.package$.MODULE$.noError());
		ctx.f = f;
	}

	@Override public void enterQuantified_variable(ParseStuffParser.Quantified_variableContext ctx) { }

	@Override public void exitQuantified_variable(ParseStuffParser.Quantified_variableContext ctx) { }

	@Override public void enterTerm(ParseStuffParser.TermContext ctx) { }

	@Override public void exitTerm(ParseStuffParser.TermContext ctx)
	{
        P_Function f = parseStuff.package$.MODULE$.functionSum();
        Term[] termArray = new P_Term[2];
		List<ParseStuffParser.ProductContext> list = ctx.product();
		t = list.get(0).t;
		list.remove(0);
		while (! list.isEmpty())
		{
			termArray[0] = t;
            termArray[1] = list.get(0).t;
            t = termApplication.apply(f, termArray, p_formula.package$.MODULE$.noError());
			list.remove(0);
		}
		ctx.t = t;
		// System.out.print(" Term [" + f.toString() + "])");
	}

	@Override public void enterProduct(ParseStuffParser.ProductContext ctx) { }

	@Override public void exitProduct(ParseStuffParser.ProductContext ctx)
	{
        P_Function f = parseStuff.package$.MODULE$.functionProduct();
        Term[] termArray = new P_Term[2];
		List<ParseStuffParser.ParticleContext> list = ctx.particle();
		t = list.get(0).t;
		list.remove(0);
		while (! list.isEmpty())
		{
            termArray[0] = t;
            termArray[1] = list.get(0).t;
            t = termApplication.apply(f, termArray, p_formula.package$.MODULE$.noError());
            list.remove(0);
		}
		ctx.t = t;
		// System.out.print(" Product [" + f.toString() + "])");
	}

	@Override public void enterParticle_type_term(ParseStuffParser.Particle_type_termContext ctx) { }

	@Override public void exitParticle_type_term(ParseStuffParser.Particle_type_termContext ctx)
	{
		t = ctx.term().t;
		ctx.t = t;
		// System.out.print("(Particle type term)");
	}

	@Override public void enterParticle_type_variable(ParseStuffParser.Particle_type_variableContext ctx) { }

	@Override public void exitParticle_type_variable(ParseStuffParser.Particle_type_variableContext ctx)
	{
		t = ctx.term_variable().t;
		ctx.t = t;
		// System.out.print("(Particle type variable)");
	}

	@Override public void enterParticle_type_application(ParseStuffParser.Particle_type_applicationContext ctx) { }

	@Override public void exitParticle_type_application(ParseStuffParser.Particle_type_applicationContext ctx)
	{
		t = ctx.function_application().t;
		ctx.t = t;
		// System.out.print("(Particle type variable)");
	}

	@Override public void enterParticle_type_successor(ParseStuffParser.Particle_type_successorContext ctx) { }

	@Override public void exitParticle_type_successor(ParseStuffParser.Particle_type_successorContext ctx)
	{
		t = ctx.successor_application().t;
		ctx.t = t;
		// System.out.print("(Particle type successor)");
	}

	@Override public void enterParticle_type_zero(ParseStuffParser.Particle_type_zeroContext ctx) { }

	@Override public void exitParticle_type_zero(ParseStuffParser.Particle_type_zeroContext ctx)
	{
        P_Constant zero = parseStuff.package$.MODULE$.constantZero();
		t = new P_TermConstant(zero, p_formula.package$.MODULE$.noError());
		ctx.t = t;
        // System.out.print(" Particle-type-zero)");
	}

	@Override public void enterTerm_variable(ParseStuffParser.Term_variableContext ctx) { }

	@Override public void exitTerm_variable(ParseStuffParser.Term_variableContext ctx)
	{
		ctx.t = termVariable.apply(ctx.variable().v, p_formula.package$.MODULE$.noError());
		t = ctx.t;
        // System.out.print(" Term-variable)");
	}

	@Override public void enterFunction_application(ParseStuffParser.Function_applicationContext ctx) { }

	@Override public void exitFunction_application(ParseStuffParser.Function_applicationContext ctx)
	{
		P_Function f = p_term.package$.MODULE$.lookupFunction(ctx.Identifier().getText());
		t = termApplication.apply(f, ctx.applicand().a, p_formula.package$.MODULE$.noError());
		ctx.t = t;
	}

	@Override public void enterSuccessor_application(ParseStuffParser.Successor_applicationContext ctx) { }

	@Override public void exitSuccessor_application(ParseStuffParser.Successor_applicationContext ctx)
	{
		P_Function f = parseStuff.package$.MODULE$.functionSuccessor();
		Term[] termArray = new P_Term[1];
		termArray[0] = ctx.term().t;
		t = termApplication.apply(f, termArray, p_formula.package$.MODULE$.noError());
		ctx.t = t;
		// System.out.print("(Successor-application)");
	}

	@Override public void enterVariable(ParseStuffParser.VariableContext ctx) { }

	@Override public void exitVariable(ParseStuffParser.VariableContext ctx)
	{
		ctx.v = p_term.package$.MODULE$.lookupVarName(ctx.Identifier().getText());
		// System.out.print("(Variable)");
	}

	@Override public void enterApplicand(ParseStuffParser.ApplicandContext ctx) { }

	@Override public void exitApplicand(ParseStuffParser.ApplicandContext ctx)
	{
		List<ParseStuffParser.TermContext> termContextList= ctx.term();
		int arity = termContextList.size();
		Term[] termArray = new Term[arity];
		for (int i = 0; i < arity; i++)
		{
			termArray[i] = termContextList.get(0).t;
			termContextList.remove(0);
		}
		ctx.a = termArray;
		// System.out.print(" Applicand [" + a.toString() + "])");
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) { }

	@Override public void exitEveryRule(ParserRuleContext ctx) { }

	@Override public void visitTerminal(TerminalNode node) { }

	@Override public void visitErrorNode(ErrorNode node) { }

}
