// Generated from C:/Users/EdSnow/IdeaProjects/Sequent/FrontEnd/src/parseStuff\ParseStuff.g4 by ANTLR 4.7
package parseStuff;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParseStuffLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		Conjunction_connective=18, Disjunction_connective=19, Negation_connective=20, 
		Conditional_connective=21, Biconditional_connective=22, Qualification_sign=23, 
		Identifier=24, Whitespace=25, Union_operator=26, Intersection_operator=27, 
		Successor_operator=28, Addition_operator=29, Multiplication_operator=30, 
		Empty_set=31, Zero=32, Natural_numbers_origin_zero=33, Natural_numbers_origin_one=34, 
		ErrorCharacter=35;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"Conjunction_connective", "Disjunction_connective", "Negation_connective", 
		"Conditional_connective", "Biconditional_connective", "Qualification_sign", 
		"Identifier", "Whitespace", "Union_operator", "Intersection_operator", 
		"Successor_operator", "Addition_operator", "Multiplication_operator", 
		"Empty_set", "Zero", "Natural_numbers_origin_zero", "Natural_numbers_origin_one", 
		"ErrorCharacter"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'\u2208'", "'\u2286'", "'='", "'\u2260'", "'!='", 
		"'<'", "'\u2264'", "'<='", "'>'", "'\u2265'", "'>='", "'.'", "'\u2200'", 
		"'\u2203'", "','", null, null, null, null, null, "'|'", null, null, "'\u222A'", 
		"'\u2229'", "'/S'", "'+'", null, "'\u2205'", "'0'", "'\u2115'", "'\u2115\u207A'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "Conjunction_connective", "Disjunction_connective", 
		"Negation_connective", "Conditional_connective", "Biconditional_connective", 
		"Qualification_sign", "Identifier", "Whitespace", "Union_operator", "Intersection_operator", 
		"Successor_operator", "Addition_operator", "Multiplication_operator", 
		"Empty_set", "Zero", "Natural_numbers_origin_zero", "Natural_numbers_origin_one", 
		"ErrorCharacter"
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


	public ParseStuffLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ParseStuff.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2%\u00ab\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\5\23r\n\23"+
		"\3\24\3\24\3\24\5\24w\n\24\3\25\3\25\3\26\3\26\3\26\5\26~\n\26\3\27\3"+
		"\27\3\27\3\27\5\27\u0084\n\27\3\30\3\30\3\31\3\31\7\31\u008a\n\31\f\31"+
		"\16\31\u008d\13\31\3\32\6\32\u0090\n\32\r\32\16\32\u0091\3\32\3\32\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\""+
		"\3#\3#\3#\3$\3$\2\2%\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C#E$G%\3\2\t\4\2\u0080\u0080\u00ae\u00ae\4"+
		"\2\u2194\u2194\u21d4\u21d4\4\2\u2196\u2196\u21d6\u21d6\4\2C\\c|\6\2\62"+
		";C\\aac|\6\2\13\f\17\17\"\"\uff01\uff01\4\2,,\u00d9\u00d9\2\u00b0\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2"+
		"\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2"+
		"\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2"+
		"\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\3"+
		"I\3\2\2\2\5K\3\2\2\2\7M\3\2\2\2\tO\3\2\2\2\13Q\3\2\2\2\rS\3\2\2\2\17U"+
		"\3\2\2\2\21X\3\2\2\2\23Z\3\2\2\2\25\\\3\2\2\2\27_\3\2\2\2\31a\3\2\2\2"+
		"\33c\3\2\2\2\35f\3\2\2\2\37h\3\2\2\2!j\3\2\2\2#l\3\2\2\2%q\3\2\2\2\'v"+
		"\3\2\2\2)x\3\2\2\2+}\3\2\2\2-\u0083\3\2\2\2/\u0085\3\2\2\2\61\u0087\3"+
		"\2\2\2\63\u008f\3\2\2\2\65\u0095\3\2\2\2\67\u0097\3\2\2\29\u0099\3\2\2"+
		"\2;\u009c\3\2\2\2=\u009e\3\2\2\2?\u00a0\3\2\2\2A\u00a2\3\2\2\2C\u00a4"+
		"\3\2\2\2E\u00a6\3\2\2\2G\u00a9\3\2\2\2IJ\7*\2\2J\4\3\2\2\2KL\7+\2\2L\6"+
		"\3\2\2\2MN\7\u220a\2\2N\b\3\2\2\2OP\7\u2288\2\2P\n\3\2\2\2QR\7?\2\2R\f"+
		"\3\2\2\2ST\7\u2262\2\2T\16\3\2\2\2UV\7#\2\2VW\7?\2\2W\20\3\2\2\2XY\7>"+
		"\2\2Y\22\3\2\2\2Z[\7\u2266\2\2[\24\3\2\2\2\\]\7>\2\2]^\7?\2\2^\26\3\2"+
		"\2\2_`\7@\2\2`\30\3\2\2\2ab\7\u2267\2\2b\32\3\2\2\2cd\7@\2\2de\7?\2\2"+
		"e\34\3\2\2\2fg\7\60\2\2g\36\3\2\2\2hi\7\u2202\2\2i \3\2\2\2jk\7\u2205"+
		"\2\2k\"\3\2\2\2lm\7.\2\2m$\3\2\2\2nr\7\u2229\2\2op\7\61\2\2pr\7^\2\2q"+
		"n\3\2\2\2qo\3\2\2\2r&\3\2\2\2sw\7\u222a\2\2tu\7^\2\2uw\7\61\2\2vs\3\2"+
		"\2\2vt\3\2\2\2w(\3\2\2\2xy\t\2\2\2y*\3\2\2\2z~\t\3\2\2{|\7/\2\2|~\7@\2"+
		"\2}z\3\2\2\2}{\3\2\2\2~,\3\2\2\2\177\u0084\t\4\2\2\u0080\u0081\7>\2\2"+
		"\u0081\u0082\7/\2\2\u0082\u0084\7@\2\2\u0083\177\3\2\2\2\u0083\u0080\3"+
		"\2\2\2\u0084.\3\2\2\2\u0085\u0086\7~\2\2\u0086\60\3\2\2\2\u0087\u008b"+
		"\t\5\2\2\u0088\u008a\t\6\2\2\u0089\u0088\3\2\2\2\u008a\u008d\3\2\2\2\u008b"+
		"\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\62\3\2\2\2\u008d\u008b\3\2\2"+
		"\2\u008e\u0090\t\7\2\2\u008f\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u008f"+
		"\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094\b\32\2\2"+
		"\u0094\64\3\2\2\2\u0095\u0096\7\u222c\2\2\u0096\66\3\2\2\2\u0097\u0098"+
		"\7\u222b\2\2\u00988\3\2\2\2\u0099\u009a\7\61\2\2\u009a\u009b\7U\2\2\u009b"+
		":\3\2\2\2\u009c\u009d\7-\2\2\u009d<\3\2\2\2\u009e\u009f\t\b\2\2\u009f"+
		">\3\2\2\2\u00a0\u00a1\7\u2207\2\2\u00a1@\3\2\2\2\u00a2\u00a3\7\62\2\2"+
		"\u00a3B\3\2\2\2\u00a4\u00a5\7\u2117\2\2\u00a5D\3\2\2\2\u00a6\u00a7\7\u2117"+
		"\2\2\u00a7\u00a8\7\u207c\2\2\u00a8F\3\2\2\2\u00a9\u00aa\13\2\2\2\u00aa"+
		"H\3\2\2\2\t\2qv}\u0083\u008b\u0091\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}