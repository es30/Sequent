package object parseStuff {


/**
  * Created by EdSnow on 8/20/2016.
  */


  import error._
  import p_term._
  import p_formula._
  import formula._

  import p_axiomatization._


  object errorParse extends Error


  axiomatization_Eq.register("Eq")
  axiomatization_PA.register("PA")

  val predicateIsElementOf                = lookupPredicate("∈")
  val predicateIsEqualTo                  = lookupPredicate("=")
  val predicateIsNotEqualTo               = lookupPredicate("≠")
  val predicateIsLessThan                 = lookupPredicate("<")
  val predicateIsLessThanOrEqualTo        = lookupPredicate("≤")
  val predicateIsGreaterThan              = lookupPredicate(">")
  val predicateIsGreaterThanOrEqualTo     = lookupPredicate("≥")

  val functionSuccessor                   = lookupFunction("S")
  val functionSum                         = lookupFunction("+")
  val functionProduct                     = lookupFunction("×")

  val constantZero                        = lookupConstant("0")
  val constantNaturalNumbersIncludingZero = lookupConstant("ℕ")
  val constantNaturalNumbersExcludingZero = lookupConstant("ℕ⁺")

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
        new Formula(Some(errorParse));
    out_form
  }

}
