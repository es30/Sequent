package object parseStuff {


/**
  * Created by EdSnow on 8/20/2016.
  */


  import error._
  import term._
  import p_term._
  import p_term.P_Term.{lookupConstant, lookupVarName, lookupFunction}
  import formula._
  import p_formula._
  import p_formula.P_Formula.lookupPredicate


  object errorParse extends Error


  def v(x: String) = lookupVarName(x)

  private class PredicateInfix(s: String) extends P_Predicate(s) with DisplayString {
    val bumper = " " + s + " "
    def displayString(applicand: P_Applicand) =
      "(" + applicand.terms.map{_.termString}.mkString(bumper) + ")"
  }

  private class FunctionInfix(s: String) extends P_Function(s) with DisplayString {
    val bumper = " " + s + " "
    def displayString(applicand: P_Applicand) =
      "(" + applicand.terms.map{_.termString}.mkString(bumper) + ")"
  }

  private def addPredicateInfix(s: String): P_Predicate =
    lookupPredicate(s, new PredicateInfix(s))

  private def addFunctionInfix(s: String): P_Function =
    lookupFunction(s, new FunctionInfix(s))

  val predicateIsElementOf                = addPredicateInfix("∈")
  val predicateIsEqualTo                  = addPredicateInfix("=")
  val predicateIsNotEqualTo               = addPredicateInfix("≠")
  val predicateIsLessThan                 = addPredicateInfix("<")
  val predicateIsLessThanOrEqualTo        = addPredicateInfix("≤")
  val predicateIsGreaterThan              = addPredicateInfix(">")
  val predicateIsGreaterThanOrEqualTo     = addPredicateInfix("≥")

  val functionSuccessor                   = lookupFunction("S")
  val functionSum                         = addFunctionInfix("+")
  val functionProduct                     = addFunctionInfix("×")

  val constantZero                        = lookupConstant("0")
  val constantNaturalNumbersIncludingZero = lookupConstant("ℕ")
  val constantNaturalNumbersExcludingZero = lookupConstant("ℕ⁺")

  //  Order is important in what happens next. Initialization of the
  //  p_axiomatization package depends upon the parser (less axiomatization)
  //  already being initialized and available. If one axiomatization is
  //  dependent upon another, the dependee should be registered (and thus
  //  initialized) before the dependent.

  axiomatization_Eq.subsumer.register("Eq")
  axiomatization_PA.subsumer.register("PA")

  import org.antlr.v4.runtime._
  import org.antlr.v4.runtime.tree._
//  import java.io.IOException

//
  // class pf(input: ANTLRInputStream) {

    //import parseStuff.ErrorCollector

    //val ec = new ErrorCollector

/*
    object MyErrorListener extends BaseErrorListener {

      override def syntaxError(recognizer: Recognizer[_, _],
          offendingSymbol: AnyRef,
          line: Int, charPositionInLine: Int,
          msg: String, e: RecognitionException) =
        ec.collect(offendingSymbol, line, charPositionInLine, msg)
    }
*/
  def pt(s: String): Term =
  {
    val input  = new ANTLRInputStream(s)
    val lexer  = new ParseStuffLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new ParseStuffParser(tokens)
    val tree = parser.term
    //  println(tree.toStringTree(parser))
    val walker = new ParseTreeWalker
    val listener = new MyParseStuffListener
    walker.walk(listener, tree)  // walk parse tree
    val t = listener.t
    val out_term =
      if (t != null)
        t
      else
        new Term(Some(errorParse))
    out_term
  }

  def pf(s: String): Formula =
  {
    val input  = new ANTLRInputStream(s)
    val lexer  = new ParseStuffLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new ParseStuffParser(tokens)
    //  parser.setBuildParseTree(false)
    //  parser.setErrorCollector(ec)
    //  parser.removeErrorListeners()
    //  parser.addErrorListener(MyErrorListener)
    //  parser.unit
    val tree = parser.formula
    //  println(tree.toStringTree(parser))
    val walker = new ParseTreeWalker
    val listener = new MyParseStuffListener
    walker.walk(listener, tree)  // walk parse tree
    val f = listener.f
    val out_form = if (f != null)
      f
    else
      new Formula(Some(errorParse))
    out_form
  }

/*  def pf(s: String) =
  {
    try {
      val input = new ANTLRInputStream(s)
      //    new MyParser(input)
      val myParser = new MyParser(input)
      val myParseTree = myParser.parser.formula

      // create a standard ANTLR parse tree walker
      val walker = new ParseTreeWalker
      // create listener then feed to walker
      val listener = new MyFormulaTestListener
      walker.walk(listener, myParseTree)  // walk parse tree
      System.out.println

    }
    catch {
      case ioe: IOException => {
        System.out.println("IO error: " + ioe)
      }
    }
    System.out.println("Buh bye")
  }
*/

  def psf(s: String): SFormula =
  {
    val input  = new ANTLRInputStream(s)
    val lexer  = new ParseStuffLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new ParseStuffParser(tokens)
    val tree = parser.sformula
    //  println(tree.toStringTree(parser))
    val walker = new ParseTreeWalker
    val listener = new MyParseStuffListener
    walker.walk(listener, tree)  // walk parse tree
    val sf = listener.sf
    val out_form =
      if (sf != null)
        sf
      else
        new Formula(Some(errorParse))
    out_form
  }

}
