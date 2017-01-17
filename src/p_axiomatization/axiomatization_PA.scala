package p_axiomatization

import p_term._
import p_term.walker.P_Applicand_walker
import formula.Formula
import p_formula._
import parseStuff._
import deconstructP_Formula.FormulaDeconstructor
import checkSubstitutionP_Formula._
import p_axiomatization.prefixWalker.P_Formula_prefixWalker
import closureChecker.P_Formula_isClosed

import scala.util.control.TailCalls.{TailRec, done, tailcall}

/**
  * Created by EdSnow on 1/5/2017.
  */


object closureChecker {

  class TermChecker extends p_term.Visitor {
    def variable(vars: Set[P_VarName], v: P_VarName): Boolean =
      !(vars contains v)
  }

  implicit class P_Formula_isClosed(val formula: P_Formula) {

    def isClosed(vars: Set[P_VarName]): Boolean =
      !isNotClosed(Nil, new TermChecker, vars).result

    def isNotClosed(
        stack: List[P_Formula],
        visitor: TermChecker,
        vars: Set[P_VarName]
      ): TailRec[Boolean] =
      formula match {

        case ap: P_FormulaProposition =>
          tailcall(isNotClosed_continue(stack, visitor, vars, false))

        case aa: P_FormulaApplication =>
          val found = aa.a.walk(Nil, visitor, vars).result
          tailcall(isNotClosed_continue(stack, visitor, vars, found))

        case an: P_FormulaNegation =>
          tailcall(an.f1.isNotClosed(stack, visitor, vars))

        case ad: P_FormulaDyadic =>
          tailcall(ad.f1.isNotClosed(ad.f2 :: stack, visitor, vars))

        case aq: P_FormulaQuantification =>
          tailcall(aq.f1.isNotClosed(stack, visitor, vars + aq.v))

      }

  }

  def isNotClosed_continue(
      stack: List[P_Formula],
      visitor: TermChecker,
      vars: Set[P_VarName],
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else {
      val f2 = stack.head
      tailcall(f2.isNotClosed(stack.tail, visitor, vars))
    }

}


object axiomatization_PA extends P_Axiomatization("PA") {

  val template_1 = pf("∀x.(0≠S(x))")
  val template_2 = pf("∀x∀y.(S(x)=S(y) → x=y)")

  val template_3 = pf("∀x.(x+0 = x)")
  val template_4 = pf("∀x∀y.(x+S(y) = S(x+y))")

  val template_5 = pf("∀x.(x×0 = 0)")
  val template_6 = pf("∀x∀y.(x×S(y) = x×y+x)")

  val template_7 = pf("Z ∧ ∀x.(Z → Z) → ∀x.Z")
  //  schema:  ϕ(0) ∧ ∀x(ϕ(x) → ϕ(S(x))) → ∀xϕ(x)

  class PrefixVisitor extends Visitor[Set[P_VarName]] {
    def collect(v: P_VarName, vars: Set[P_VarName]): Set[P_VarName] =
      vars + v
  }

  val inductionSchemaMatcher = {
    val deconstructor = template_7.deconstructor
    val var_x   = lookupVarName("x")
    val const_0 = P_TermConstant(lookupConstant("0"))
    val succ_x  = pt("S(x)").asInstanceOf[P_Term]
    AxiomTemplate(
      (f: Formula) =>
        f match {
          case candidate: P_Formula =>
            val (prefixVars, stripped_formula) =
              candidate.walkPrefix[Set[P_VarName]](new PrefixVisitor, Set())
            deconstructor(stripped_formula) match {
              case Some((formulas, _)) =>
                formulas(0).isClosed(prefixVars) &&
                formulas(1) == formulas(3) &&
                formulas(1).checkSubstitution(formulas(0), (const_0, var_x)) &&
                formulas(1).checkSubstitution(formulas(2), (succ_x,  var_x))
              case None =>
                false
            }
          case _ =>
            false
        }
    )
  }

  def subsumes(f: Formula) =
    AxiomTemplateList(Seq(

      AxiomTemplate(template_1),
      AxiomTemplate(template_2),

      AxiomTemplate(template_3),
      AxiomTemplate(template_4),

      AxiomTemplate(template_5),
      AxiomTemplate(template_6),

      inductionSchemaMatcher

    )).matchAxiom(f)

}
