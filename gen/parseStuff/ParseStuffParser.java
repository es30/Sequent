// Generated from C:/Users/EdSnow/IdeaProjects/Sequent/src/parseStuff\ParseStuff.g4 by ANTLR 4.6
package parseStuff;

//  from Term.g4
    import org.antlr.v4.runtime.Parser;
    import term.*;
    import p_term.*;
    import formula.*;
    import p_formula.*;
	import p_axiomatization.*;
    import formulaBuilder.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParseStuffParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.6", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, Conjunction_connective=17, 
		Disjunction_connective=18, Negation_connective=19, Conditional_connective=20, 
		Biconditional_connective=21, Qualification_sign=22, Identifier=23, Whitespace=24, 
		Successor_operator=25, Addition_operator=26, Multiplication_operator=27, 
		Zero=28, Natural_numbers_origin_zero=29, Natural_numbers_origin_one=30, 
		ErrorCharacter=31;
	public static final int
		RULE_sformula = 0, RULE_axiomatization = 1, RULE_formula = 2, RULE_implication = 3, 
		RULE_disjunction = 4, RULE_conjunction = 5, RULE_atom = 6, RULE_proposition = 7, 
		RULE_predicate_application = 8, RULE_negation = 9, RULE_relation = 10, 
		RULE_quantified_formula = 11, RULE_quantified_variable = 12, RULE_term = 13, 
		RULE_product = 14, RULE_particle = 15, RULE_term_variable = 16, RULE_function_application = 17, 
		RULE_successor_application = 18, RULE_variable = 19, RULE_applicand = 20;
	public static final String[] ruleNames = {
		"sformula", "axiomatization", "formula", "implication", "disjunction", 
		"conjunction", "atom", "proposition", "predicate_application", "negation", 
		"relation", "quantified_formula", "quantified_variable", "term", "product", 
		"particle", "term_variable", "function_application", "successor_application", 
		"variable", "applicand"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'\\u2208'", "'='", "'\\u2260'", "'!='", "'<'", "'\\u2264'", 
		"'<='", "'>'", "'\\u2265'", "'>='", "'.'", "'\\u2200'", "'\\u2203'", "','", 
		null, null, null, null, null, "'|'", null, null, "'/S'", "'+'", null, 
		"'0'", "'\\u2115'", "'\\u2115\\u207A'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, "Conjunction_connective", "Disjunction_connective", 
		"Negation_connective", "Conditional_connective", "Biconditional_connective", 
		"Qualification_sign", "Identifier", "Whitespace", "Successor_operator", 
		"Addition_operator", "Multiplication_operator", "Zero", "Natural_numbers_origin_zero", 
		"Natural_numbers_origin_one", "ErrorCharacter"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ParseStuff.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ParseStuffParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SformulaContext extends ParserRuleContext {
		public P_SFormula f;
		public Token t;
		public P_Axiomatization a;
		public SformulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sformula; }
	 
		public SformulaContext() { }
		public void copyFrom(SformulaContext ctx) {
			super.copyFrom(ctx);
			this.f = ctx.f;
			this.t = ctx.t;
			this.a = ctx.a;
		}
	}
	public static class SFormula_type_formulaContext extends SformulaContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public SFormula_type_formulaContext(SformulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterSFormula_type_formula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitSFormula_type_formula(this);
		}
	}
	public static class SFormula_type_axiomatizationContext extends SformulaContext {
		public AxiomatizationContext axiomatization() {
			return getRuleContext(AxiomatizationContext.class,0);
		}
		public SFormula_type_axiomatizationContext(SformulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterSFormula_type_axiomatization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitSFormula_type_axiomatization(this);
		}
	}

	public final SformulaContext sformula() throws RecognitionException {
		SformulaContext _localctx = new SformulaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_sformula);
		try {
			setState(45);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new SFormula_type_axiomatizationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(42);
				if (!((_localctx.t=_input.LT(1)).getType()==Identifier && (_localctx.a=P_Axiomatization$.MODULE$.lookupAxiomatization(_localctx.t.getText()))!=null)) throw new FailedPredicateException(this, "($t=_input.LT(1)).getType()==Identifier && ($a=P_Axiomatization$.MODULE$.lookupAxiomatization($t.getText()))!=null");
				setState(43);
				axiomatization();
				}
				break;
			case 2:
				_localctx = new SFormula_type_formulaContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(44);
				formula();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AxiomatizationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ParseStuffParser.Identifier, 0); }
		public AxiomatizationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_axiomatization; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAxiomatization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAxiomatization(this);
		}
	}

	public final AxiomatizationContext axiomatization() throws RecognitionException {
		AxiomatizationContext _localctx = new AxiomatizationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_axiomatization);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormulaContext extends ParserRuleContext {
		public Formula f;
		public ImplicationContext implication() {
			return getRuleContext(ImplicationContext.class,0);
		}
		public TerminalNode Biconditional_connective() { return getToken(ParseStuffParser.Biconditional_connective, 0); }
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitFormula(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_formula);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			implication();
			setState(52);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(50);
				match(Biconditional_connective);
				setState(51);
				formula();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImplicationContext extends ParserRuleContext {
		public Formula f;
		public DisjunctionContext disjunction() {
			return getRuleContext(DisjunctionContext.class,0);
		}
		public TerminalNode Conditional_connective() { return getToken(ParseStuffParser.Conditional_connective, 0); }
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public ImplicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_implication; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterImplication(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitImplication(this);
		}
	}

	public final ImplicationContext implication() throws RecognitionException {
		ImplicationContext _localctx = new ImplicationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_implication);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			disjunction();
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Conditional_connective) {
				{
				setState(55);
				match(Conditional_connective);
				setState(56);
				formula();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DisjunctionContext extends ParserRuleContext {
		public Formula f;
		public List<ConjunctionContext> conjunction() {
			return getRuleContexts(ConjunctionContext.class);
		}
		public ConjunctionContext conjunction(int i) {
			return getRuleContext(ConjunctionContext.class,i);
		}
		public List<TerminalNode> Disjunction_connective() { return getTokens(ParseStuffParser.Disjunction_connective); }
		public TerminalNode Disjunction_connective(int i) {
			return getToken(ParseStuffParser.Disjunction_connective, i);
		}
		public DisjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterDisjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitDisjunction(this);
		}
	}

	public final DisjunctionContext disjunction() throws RecognitionException {
		DisjunctionContext _localctx = new DisjunctionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_disjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			conjunction();
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Disjunction_connective) {
				{
				{
				setState(60);
				match(Disjunction_connective);
				setState(61);
				conjunction();
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConjunctionContext extends ParserRuleContext {
		public Formula f;
		public List<AtomContext> atom() {
			return getRuleContexts(AtomContext.class);
		}
		public AtomContext atom(int i) {
			return getRuleContext(AtomContext.class,i);
		}
		public List<TerminalNode> Conjunction_connective() { return getTokens(ParseStuffParser.Conjunction_connective); }
		public TerminalNode Conjunction_connective(int i) {
			return getToken(ParseStuffParser.Conjunction_connective, i);
		}
		public ConjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterConjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitConjunction(this);
		}
	}

	public final ConjunctionContext conjunction() throws RecognitionException {
		ConjunctionContext _localctx = new ConjunctionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_conjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			atom();
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Conjunction_connective) {
				{
				{
				setState(68);
				match(Conjunction_connective);
				setState(69);
				atom();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public Formula f;
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
	 
		public AtomContext() { }
		public void copyFrom(AtomContext ctx) {
			super.copyFrom(ctx);
			this.f = ctx.f;
		}
	}
	public static class Atom_type_relationContext extends AtomContext {
		public RelationContext relation() {
			return getRuleContext(RelationContext.class,0);
		}
		public Atom_type_relationContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAtom_type_relation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAtom_type_relation(this);
		}
	}
	public static class Atom_type_predicateContext extends AtomContext {
		public Predicate_applicationContext predicate_application() {
			return getRuleContext(Predicate_applicationContext.class,0);
		}
		public Atom_type_predicateContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAtom_type_predicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAtom_type_predicate(this);
		}
	}
	public static class Atom_type_propositionContext extends AtomContext {
		public PropositionContext proposition() {
			return getRuleContext(PropositionContext.class,0);
		}
		public Atom_type_propositionContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAtom_type_proposition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAtom_type_proposition(this);
		}
	}
	public static class Atom_type_negationContext extends AtomContext {
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		public Atom_type_negationContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAtom_type_negation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAtom_type_negation(this);
		}
	}
	public static class Atom_type_quantifiedContext extends AtomContext {
		public Quantified_formulaContext quantified_formula() {
			return getRuleContext(Quantified_formulaContext.class,0);
		}
		public Atom_type_quantifiedContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAtom_type_quantified(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAtom_type_quantified(this);
		}
	}
	public static class Atom_type_formulaContext extends AtomContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public Atom_type_formulaContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterAtom_type_formula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitAtom_type_formula(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_atom);
		try {
			setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new Atom_type_formulaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				match(T__0);
				setState(76);
				formula();
				setState(77);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new Atom_type_propositionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(79);
				proposition();
				}
				break;
			case 3:
				_localctx = new Atom_type_predicateContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(80);
				predicate_application();
				}
				break;
			case 4:
				_localctx = new Atom_type_negationContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(81);
				negation();
				}
				break;
			case 5:
				_localctx = new Atom_type_relationContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(82);
				relation();
				}
				break;
			case 6:
				_localctx = new Atom_type_quantifiedContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(83);
				quantified_formula();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropositionContext extends ParserRuleContext {
		public Formula f;
		public P_Proposition p;
		public Token Identifier;
		public TerminalNode Identifier() { return getToken(ParseStuffParser.Identifier, 0); }
		public PropositionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_proposition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterProposition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitProposition(this);
		}
	}

	public final PropositionContext proposition() throws RecognitionException {
		PropositionContext _localctx = new PropositionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_proposition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			((PropositionContext)_localctx).Identifier = match(Identifier);
			setState(87);
			if (!((_localctx.p=p_formula.package$.MODULE$.lookupProposition(((PropositionContext)_localctx).Identifier.getText()))!=null)) throw new FailedPredicateException(this, "($p=p_formula.package$.MODULE$.lookupProposition($Identifier.getText()))!=null", "axiomatization not permitted");
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Predicate_applicationContext extends ParserRuleContext {
		public Formula f;
		public TerminalNode Identifier() { return getToken(ParseStuffParser.Identifier, 0); }
		public ApplicandContext applicand() {
			return getRuleContext(ApplicandContext.class,0);
		}
		public Predicate_applicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate_application; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterPredicate_application(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitPredicate_application(this);
		}
	}

	public final Predicate_applicationContext predicate_application() throws RecognitionException {
		Predicate_applicationContext _localctx = new Predicate_applicationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_predicate_application);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(Identifier);
			setState(90);
			applicand();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NegationContext extends ParserRuleContext {
		public Formula f;
		public TerminalNode Negation_connective() { return getToken(ParseStuffParser.Negation_connective, 0); }
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public NegationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitNegation(this);
		}
	}

	public final NegationContext negation() throws RecognitionException {
		NegationContext _localctx = new NegationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_negation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(Negation_connective);
			setState(93);
			atom();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationContext extends ParserRuleContext {
		public Formula f;
		public p_formula.P_Predicate p;
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitRelation(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_relation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			term();
			setState(113);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(96);
				match(T__2);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsElementOf();
				}
				break;
			case T__3:
				{
				setState(98);
				match(T__3);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsEqualTo();
				}
				break;
			case T__4:
				{
				setState(100);
				match(T__4);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsNotEqualTo();
				}
				break;
			case T__5:
				{
				setState(102);
				match(T__5);
				}
				break;
			case T__6:
				{
				setState(103);
				match(T__6);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsLessThan();
				}
				break;
			case T__7:
				{
				setState(105);
				match(T__7);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsLessThanOrEqualTo();
				}
				break;
			case T__8:
				{
				setState(107);
				match(T__8);
				}
				break;
			case T__9:
				{
				setState(108);
				match(T__9);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsGreaterThan();
				}
				break;
			case T__10:
				{
				setState(110);
				match(T__10);
				((RelationContext)_localctx).p = parseStuff.package$.MODULE$.predicateIsGreaterThanOrEqualTo();
				}
				break;
			case T__11:
				{
				setState(112);
				match(T__11);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(115);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Quantified_formulaContext extends ParserRuleContext {
		public Formula f;
		public Quantified_formulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantified_formula; }
	 
		public Quantified_formulaContext() { }
		public void copyFrom(Quantified_formulaContext ctx) {
			super.copyFrom(ctx);
			this.f = ctx.f;
		}
	}
	public static class Quantified_formula_type_simpleContext extends Quantified_formulaContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public Quantified_variableContext quantified_variable() {
			return getRuleContext(Quantified_variableContext.class,0);
		}
		public Quantified_formula_type_simpleContext(Quantified_formulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterQuantified_formula_type_simple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitQuantified_formula_type_simple(this);
		}
	}
	public static class Quantified_formula_type_compoundContext extends Quantified_formulaContext {
		public Quantified_variableContext quantified_variable() {
			return getRuleContext(Quantified_variableContext.class,0);
		}
		public Quantified_formulaContext quantified_formula() {
			return getRuleContext(Quantified_formulaContext.class,0);
		}
		public Quantified_formula_type_compoundContext(Quantified_formulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterQuantified_formula_type_compound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitQuantified_formula_type_compound(this);
		}
	}

	public final Quantified_formulaContext quantified_formula() throws RecognitionException {
		Quantified_formulaContext _localctx = new Quantified_formulaContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_quantified_formula);
		try {
			setState(131);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				_localctx = new Quantified_formula_type_simpleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__13:
				case T__14:
					{
					setState(117);
					quantified_variable();
					setState(118);
					match(T__12);
					}
					break;
				case T__0:
					{
					setState(120);
					match(T__0);
					setState(121);
					quantified_variable();
					setState(122);
					match(T__1);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(126);
				atom();
				}
				break;
			case 2:
				_localctx = new Quantified_formula_type_compoundContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(128);
				quantified_variable();
				setState(129);
				quantified_formula();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Quantified_variableContext extends ParserRuleContext {
		public scala.Tuple2<scala.Enumeration.Value,java.lang.String> q;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public Quantified_variableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantified_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterQuantified_variable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitQuantified_variable(this);
		}
	}

	public final Quantified_variableContext quantified_variable() throws RecognitionException {
		Quantified_variableContext _localctx = new Quantified_variableContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_quantified_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__13:
				{
				setState(133);
				match(T__13);
				((Quantified_variableContext)_localctx).q = p_formula.QuantificationKind.Universal();
				}
				break;
			case T__14:
				{
				setState(135);
				match(T__14);
				((Quantified_variableContext)_localctx).q = p_formula.QuantificationKind.Existential();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(139);
			variable();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public Term t;
		public List<ProductContext> product() {
			return getRuleContexts(ProductContext.class);
		}
		public ProductContext product(int i) {
			return getRuleContext(ProductContext.class,i);
		}
		public List<TerminalNode> Addition_operator() { return getTokens(ParseStuffParser.Addition_operator); }
		public TerminalNode Addition_operator(int i) {
			return getToken(ParseStuffParser.Addition_operator, i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_term);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			product();
			setState(146);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(142);
					match(Addition_operator);
					setState(143);
					product();
					}
					} 
				}
				setState(148);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProductContext extends ParserRuleContext {
		public Term t;
		public List<ParticleContext> particle() {
			return getRuleContexts(ParticleContext.class);
		}
		public ParticleContext particle(int i) {
			return getRuleContext(ParticleContext.class,i);
		}
		public List<TerminalNode> Multiplication_operator() { return getTokens(ParseStuffParser.Multiplication_operator); }
		public TerminalNode Multiplication_operator(int i) {
			return getToken(ParseStuffParser.Multiplication_operator, i);
		}
		public ProductContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_product; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterProduct(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitProduct(this);
		}
	}

	public final ProductContext product() throws RecognitionException {
		ProductContext _localctx = new ProductContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_product);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			particle();
			setState(154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(150);
					match(Multiplication_operator);
					setState(151);
					particle();
					}
					} 
				}
				setState(156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParticleContext extends ParserRuleContext {
		public Term t;
		public ParticleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_particle; }
	 
		public ParticleContext() { }
		public void copyFrom(ParticleContext ctx) {
			super.copyFrom(ctx);
			this.t = ctx.t;
		}
	}
	public static class Particle_type_termContext extends ParticleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public Particle_type_termContext(ParticleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterParticle_type_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitParticle_type_term(this);
		}
	}
	public static class Particle_type_variableContext extends ParticleContext {
		public Term_variableContext term_variable() {
			return getRuleContext(Term_variableContext.class,0);
		}
		public Particle_type_variableContext(ParticleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterParticle_type_variable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitParticle_type_variable(this);
		}
	}
	public static class Particle_type_applicationContext extends ParticleContext {
		public Function_applicationContext function_application() {
			return getRuleContext(Function_applicationContext.class,0);
		}
		public Particle_type_applicationContext(ParticleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterParticle_type_application(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitParticle_type_application(this);
		}
	}
	public static class Particle_type_zeroContext extends ParticleContext {
		public TerminalNode Zero() { return getToken(ParseStuffParser.Zero, 0); }
		public Particle_type_zeroContext(ParticleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterParticle_type_zero(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitParticle_type_zero(this);
		}
	}
	public static class Particle_type_successorContext extends ParticleContext {
		public Successor_applicationContext successor_application() {
			return getRuleContext(Successor_applicationContext.class,0);
		}
		public Particle_type_successorContext(ParticleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterParticle_type_successor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitParticle_type_successor(this);
		}
	}

	public final ParticleContext particle() throws RecognitionException {
		ParticleContext _localctx = new ParticleContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_particle);
		try {
			setState(165);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				_localctx = new Particle_type_termContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(157);
				match(T__0);
				setState(158);
				term();
				setState(159);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new Particle_type_variableContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
				term_variable();
				}
				break;
			case 3:
				_localctx = new Particle_type_applicationContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(162);
				function_application();
				}
				break;
			case 4:
				_localctx = new Particle_type_successorContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(163);
				successor_application();
				}
				break;
			case 5:
				_localctx = new Particle_type_zeroContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(164);
				match(Zero);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Term_variableContext extends ParserRuleContext {
		public Term t;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public Term_variableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterTerm_variable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitTerm_variable(this);
		}
	}

	public final Term_variableContext term_variable() throws RecognitionException {
		Term_variableContext _localctx = new Term_variableContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_term_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			variable();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_applicationContext extends ParserRuleContext {
		public Term t;
		public TerminalNode Identifier() { return getToken(ParseStuffParser.Identifier, 0); }
		public ApplicandContext applicand() {
			return getRuleContext(ApplicandContext.class,0);
		}
		public Function_applicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_application; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterFunction_application(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitFunction_application(this);
		}
	}

	public final Function_applicationContext function_application() throws RecognitionException {
		Function_applicationContext _localctx = new Function_applicationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_function_application);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(Identifier);
			setState(170);
			applicand();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Successor_applicationContext extends ParserRuleContext {
		public Term t;
		public TerminalNode Successor_operator() { return getToken(ParseStuffParser.Successor_operator, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public Successor_applicationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_successor_application; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterSuccessor_application(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitSuccessor_application(this);
		}
	}

	public final Successor_applicationContext successor_application() throws RecognitionException {
		Successor_applicationContext _localctx = new Successor_applicationContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_successor_application);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(Successor_operator);
			setState(173);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public P_VarName v;
		public TerminalNode Identifier() { return getToken(ParseStuffParser.Identifier, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ApplicandContext extends ParserRuleContext {
		public Term[] a;
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public ApplicandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_applicand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).enterApplicand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ParseStuffListener ) ((ParseStuffListener)listener).exitApplicand(this);
		}
	}

	public final ApplicandContext applicand() throws RecognitionException {
		ApplicandContext _localctx = new ApplicandContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_applicand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(T__0);
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << Identifier) | (1L << Successor_operator) | (1L << Zero))) != 0)) {
				{
				setState(178);
				term();
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__15) {
					{
					{
					setState(179);
					match(T__15);
					setState(180);
					term();
					}
					}
					setState(185);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(188);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return sformula_sempred((SformulaContext)_localctx, predIndex);
		case 7:
			return proposition_sempred((PropositionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean sformula_sempred(SformulaContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return (_localctx.t=_input.LT(1)).getType()==Identifier && (_localctx.a=P_Axiomatization$.MODULE$.lookupAxiomatization(_localctx.t.getText()))!=null;
		}
		return true;
	}
	private boolean proposition_sempred(PropositionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return (_localctx.p=p_formula.package$.MODULE$.lookupProposition(((PropositionContext)_localctx).Identifier.getText()))!=null;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3!\u00c1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\5\2\60\n\2\3\3\3"+
		"\3\3\4\3\4\3\4\5\4\67\n\4\3\5\3\5\3\5\5\5<\n\5\3\6\3\6\3\6\7\6A\n\6\f"+
		"\6\16\6D\13\6\3\7\3\7\3\7\7\7I\n\7\f\7\16\7L\13\7\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\5\bW\n\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f"+
		"t\n\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\177\n\r\3\r\3\r\3\r\3\r"+
		"\3\r\5\r\u0086\n\r\3\16\3\16\3\16\3\16\5\16\u008c\n\16\3\16\3\16\3\17"+
		"\3\17\3\17\7\17\u0093\n\17\f\17\16\17\u0096\13\17\3\20\3\20\3\20\7\20"+
		"\u009b\n\20\f\20\16\20\u009e\13\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\5\21\u00a8\n\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\7\26\u00b8\n\26\f\26\16\26\u00bb\13\26\5\26\u00bd"+
		"\n\26\3\26\3\26\3\26\2\2\27\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \""+
		"$&(*\2\2\u00c9\2/\3\2\2\2\4\61\3\2\2\2\6\63\3\2\2\2\b8\3\2\2\2\n=\3\2"+
		"\2\2\fE\3\2\2\2\16V\3\2\2\2\20X\3\2\2\2\22[\3\2\2\2\24^\3\2\2\2\26a\3"+
		"\2\2\2\30\u0085\3\2\2\2\32\u008b\3\2\2\2\34\u008f\3\2\2\2\36\u0097\3\2"+
		"\2\2 \u00a7\3\2\2\2\"\u00a9\3\2\2\2$\u00ab\3\2\2\2&\u00ae\3\2\2\2(\u00b1"+
		"\3\2\2\2*\u00b3\3\2\2\2,-\6\2\2\3-\60\5\4\3\2.\60\5\6\4\2/,\3\2\2\2/."+
		"\3\2\2\2\60\3\3\2\2\2\61\62\7\31\2\2\62\5\3\2\2\2\63\66\5\b\5\2\64\65"+
		"\7\27\2\2\65\67\5\6\4\2\66\64\3\2\2\2\66\67\3\2\2\2\67\7\3\2\2\28;\5\n"+
		"\6\29:\7\26\2\2:<\5\6\4\2;9\3\2\2\2;<\3\2\2\2<\t\3\2\2\2=B\5\f\7\2>?\7"+
		"\24\2\2?A\5\f\7\2@>\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\13\3\2\2\2"+
		"DB\3\2\2\2EJ\5\16\b\2FG\7\23\2\2GI\5\16\b\2HF\3\2\2\2IL\3\2\2\2JH\3\2"+
		"\2\2JK\3\2\2\2K\r\3\2\2\2LJ\3\2\2\2MN\7\3\2\2NO\5\6\4\2OP\7\4\2\2PW\3"+
		"\2\2\2QW\5\20\t\2RW\5\22\n\2SW\5\24\13\2TW\5\26\f\2UW\5\30\r\2VM\3\2\2"+
		"\2VQ\3\2\2\2VR\3\2\2\2VS\3\2\2\2VT\3\2\2\2VU\3\2\2\2W\17\3\2\2\2XY\7\31"+
		"\2\2YZ\6\t\3\3Z\21\3\2\2\2[\\\7\31\2\2\\]\5*\26\2]\23\3\2\2\2^_\7\25\2"+
		"\2_`\5\16\b\2`\25\3\2\2\2as\5\34\17\2bc\7\5\2\2ct\b\f\1\2de\7\6\2\2et"+
		"\b\f\1\2fg\7\7\2\2gt\b\f\1\2ht\7\b\2\2ij\7\t\2\2jt\b\f\1\2kl\7\n\2\2l"+
		"t\b\f\1\2mt\7\13\2\2no\7\f\2\2ot\b\f\1\2pq\7\r\2\2qt\b\f\1\2rt\7\16\2"+
		"\2sb\3\2\2\2sd\3\2\2\2sf\3\2\2\2sh\3\2\2\2si\3\2\2\2sk\3\2\2\2sm\3\2\2"+
		"\2sn\3\2\2\2sp\3\2\2\2sr\3\2\2\2tu\3\2\2\2uv\5\34\17\2v\27\3\2\2\2wx\5"+
		"\32\16\2xy\7\17\2\2y\177\3\2\2\2z{\7\3\2\2{|\5\32\16\2|}\7\4\2\2}\177"+
		"\3\2\2\2~w\3\2\2\2~z\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081\5\16\b\2\u0081"+
		"\u0086\3\2\2\2\u0082\u0083\5\32\16\2\u0083\u0084\5\30\r\2\u0084\u0086"+
		"\3\2\2\2\u0085~\3\2\2\2\u0085\u0082\3\2\2\2\u0086\31\3\2\2\2\u0087\u0088"+
		"\7\20\2\2\u0088\u008c\b\16\1\2\u0089\u008a\7\21\2\2\u008a\u008c\b\16\1"+
		"\2\u008b\u0087\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e"+
		"\5(\25\2\u008e\33\3\2\2\2\u008f\u0094\5\36\20\2\u0090\u0091\7\34\2\2\u0091"+
		"\u0093\5\36\20\2\u0092\u0090\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0092\3"+
		"\2\2\2\u0094\u0095\3\2\2\2\u0095\35\3\2\2\2\u0096\u0094\3\2\2\2\u0097"+
		"\u009c\5 \21\2\u0098\u0099\7\35\2\2\u0099\u009b\5 \21\2\u009a\u0098\3"+
		"\2\2\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d"+
		"\37\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a0\7\3\2\2\u00a0\u00a1\5\34\17"+
		"\2\u00a1\u00a2\7\4\2\2\u00a2\u00a8\3\2\2\2\u00a3\u00a8\5\"\22\2\u00a4"+
		"\u00a8\5$\23\2\u00a5\u00a8\5&\24\2\u00a6\u00a8\7\36\2\2\u00a7\u009f\3"+
		"\2\2\2\u00a7\u00a3\3\2\2\2\u00a7\u00a4\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7"+
		"\u00a6\3\2\2\2\u00a8!\3\2\2\2\u00a9\u00aa\5(\25\2\u00aa#\3\2\2\2\u00ab"+
		"\u00ac\7\31\2\2\u00ac\u00ad\5*\26\2\u00ad%\3\2\2\2\u00ae\u00af\7\33\2"+
		"\2\u00af\u00b0\5\34\17\2\u00b0\'\3\2\2\2\u00b1\u00b2\7\31\2\2\u00b2)\3"+
		"\2\2\2\u00b3\u00bc\7\3\2\2\u00b4\u00b9\5\34\17\2\u00b5\u00b6\7\22\2\2"+
		"\u00b6\u00b8\5\34\17\2\u00b7\u00b5\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00b7"+
		"\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc"+
		"\u00b4\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00bf\7\4"+
		"\2\2\u00bf+\3\2\2\2\21/\66;BJVs~\u0085\u008b\u0094\u009c\u00a7\u00b9\u00bc";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}