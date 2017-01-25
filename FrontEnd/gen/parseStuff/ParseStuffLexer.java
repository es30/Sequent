// Generated from C:/Users/EdSnow/IdeaProjects/Sequent/FrontEnd/src/parseStuff\ParseStuff.g4 by ANTLR 4.6
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
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "Conjunction_connective", 
		"Disjunction_connective", "Negation_connective", "Conditional_connective", 
		"Biconditional_connective", "Qualification_sign", "Identifier", "Whitespace", 
		"Successor_operator", "Addition_operator", "Multiplication_operator", 
		"Zero", "Natural_numbers_origin_zero", "Natural_numbers_origin_one", "ErrorCharacter"
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2!\u009b\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \3\2"+
		"\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21"+
		"\3\21\3\22\3\22\3\22\5\22h\n\22\3\23\3\23\3\23\5\23m\n\23\3\24\3\24\3"+
		"\25\3\25\3\25\5\25t\n\25\3\26\3\26\3\26\3\26\5\26z\n\26\3\27\3\27\3\30"+
		"\3\30\7\30\u0080\n\30\f\30\16\30\u0083\13\30\3\31\6\31\u0086\n\31\r\31"+
		"\16\31\u0087\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3"+
		"\36\3\36\3\37\3\37\3\37\3 \3 \2\2!\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!\3\2\7\4\2\u0080\u0080\u00ae\u00ae"+
		"\4\2C\\c|\6\2\62;C\\aac|\6\2\13\f\17\17\"\"\uff01\uff01\4\2,,\u00d9\u00d9"+
		"\u00a0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2"+
		"/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2"+
		"\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\3A\3\2\2\2\5C\3\2\2\2\7E\3\2\2\2\t"+
		"G\3\2\2\2\13I\3\2\2\2\rK\3\2\2\2\17N\3\2\2\2\21P\3\2\2\2\23R\3\2\2\2\25"+
		"U\3\2\2\2\27W\3\2\2\2\31Y\3\2\2\2\33\\\3\2\2\2\35^\3\2\2\2\37`\3\2\2\2"+
		"!b\3\2\2\2#g\3\2\2\2%l\3\2\2\2\'n\3\2\2\2)s\3\2\2\2+y\3\2\2\2-{\3\2\2"+
		"\2/}\3\2\2\2\61\u0085\3\2\2\2\63\u008b\3\2\2\2\65\u008e\3\2\2\2\67\u0090"+
		"\3\2\2\29\u0092\3\2\2\2;\u0094\3\2\2\2=\u0096\3\2\2\2?\u0099\3\2\2\2A"+
		"B\7*\2\2B\4\3\2\2\2CD\7+\2\2D\6\3\2\2\2EF\7\u220a\2\2F\b\3\2\2\2GH\7?"+
		"\2\2H\n\3\2\2\2IJ\7\u2262\2\2J\f\3\2\2\2KL\7#\2\2LM\7?\2\2M\16\3\2\2\2"+
		"NO\7>\2\2O\20\3\2\2\2PQ\7\u2266\2\2Q\22\3\2\2\2RS\7>\2\2ST\7?\2\2T\24"+
		"\3\2\2\2UV\7@\2\2V\26\3\2\2\2WX\7\u2267\2\2X\30\3\2\2\2YZ\7@\2\2Z[\7?"+
		"\2\2[\32\3\2\2\2\\]\7\60\2\2]\34\3\2\2\2^_\7\u2202\2\2_\36\3\2\2\2`a\7"+
		"\u2205\2\2a \3\2\2\2bc\7.\2\2c\"\3\2\2\2dh\7\u2229\2\2ef\7\61\2\2fh\7"+
		"^\2\2gd\3\2\2\2ge\3\2\2\2h$\3\2\2\2im\7\u222a\2\2jk\7^\2\2km\7\61\2\2"+
		"li\3\2\2\2lj\3\2\2\2m&\3\2\2\2no\t\2\2\2o(\3\2\2\2pt\7\u2194\2\2qr\7/"+
		"\2\2rt\7@\2\2sp\3\2\2\2sq\3\2\2\2t*\3\2\2\2uz\7\u2196\2\2vw\7>\2\2wx\7"+
		"/\2\2xz\7@\2\2yu\3\2\2\2yv\3\2\2\2z,\3\2\2\2{|\7~\2\2|.\3\2\2\2}\u0081"+
		"\t\3\2\2~\u0080\t\4\2\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2"+
		"\2\2\u0081\u0082\3\2\2\2\u0082\60\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0086"+
		"\t\5\2\2\u0085\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\b\31\2\2\u008a\62\3\2\2"+
		"\2\u008b\u008c\7\61\2\2\u008c\u008d\7U\2\2\u008d\64\3\2\2\2\u008e\u008f"+
		"\7-\2\2\u008f\66\3\2\2\2\u0090\u0091\t\6\2\2\u00918\3\2\2\2\u0092\u0093"+
		"\7\62\2\2\u0093:\3\2\2\2\u0094\u0095\7\u2117\2\2\u0095<\3\2\2\2\u0096"+
		"\u0097\7\u2117\2\2\u0097\u0098\7\u207c\2\2\u0098>\3\2\2\2\u0099\u009a"+
		"\13\2\2\2\u009a@\3\2\2\2\t\2glsy\u0081\u0087\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}