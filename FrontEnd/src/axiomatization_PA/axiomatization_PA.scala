package axiomatization_PA

import scala.util.control.TailCalls.{TailRec, done, tailcall}

import p_term.P_Term.{lookupConstant, lookupVarName}
import p_term.{P_VarName, P_Term, P_TermConstant}
import p_term.walker.P_Applicand_walker
import formula.Formula
import p_formula._
import p_axiomatization.{AxiomTemplate, AxiomTemplateList, P_Axiomatization, Visitor}
import p_axiomatization.prefixWalker.P_Formula_prefixWalker
import p_axiomatization.deconstruct.FormulaDeconstructor
import p_axiomatization.checkSubstitution.P_FormulaChecker

/**
  * Created by EdSnow on 1/5/2017.
  */


private object closureChecker {

  private class TermChecker extends p_term.Visitor {
    def variable(vars: Set[P_VarName], v: P_VarName): Boolean =
      !(vars contains v)
  }

  implicit class P_Formula_isClosed(val formula: P_Formula) {

    def isClosed(vars: Set[P_VarName]): Boolean =
      isClosed(None, new TermChecker, vars).result

    private def isClosed(
        continuation: Option[() => TailRec[Boolean]],
        visitor: TermChecker,
        vars: Set[P_VarName]
      ): TailRec[Boolean] =
      formula match {

        case _: P_FormulaProposition =>
          tailcall(isClosed_continue(continuation, true))

        case aa: P_FormulaApplication =>
          val found = aa.a.walk(None, visitor, vars).result
          tailcall(isClosed_continue(continuation, !found))

        case an: P_FormulaNegation =>
          tailcall(an.f1.isClosed(continuation, visitor, vars))

        case ad: P_FormulaDyadic =>
          def cont1: () => TailRec[Boolean] =
            () => tailcall(ad.f2.isClosed(continuation, visitor, vars))
          tailcall(ad.f1.isClosed(Some(cont1), visitor, vars))

        case aq: P_FormulaQuantification =>
          tailcall(aq.f1.isClosed(continuation, visitor, vars + aq.v))

      }

  }

  private def isClosed_continue(
      continuation: Option[() => TailRec[Boolean]],
      retval: Boolean
    ): TailRec[Boolean] =
    if (retval)
      continuation match {
        case Some(cont) =>
          tailcall(cont())
        case None =>
          done(true)
      }
    else
      done(false)

}


object subsumer extends P_Axiomatization("PA") {

  import parseStuff._
  import closureChecker.P_Formula_isClosed

  private val template_1 = pf("∀x.(0≠S(x))")
  private val template_2 = pf("∀x∀y.(S(x)=S(y) → x=y)")

  private val template_3 = pf("∀x.(x+0 = x)")
  private val template_4 = pf("∀x∀y.(x+S(y) = S(x+y))")

  private val template_5 = pf("∀x.(x×0 = 0)")
  private val template_6 = pf("∀x∀y.(x×S(y) = x×y+x)")

  private val template_7 = pf("Z ∧ ∀x.(Z → Z) → ∀x.Z")
  //  schema:  ϕ(0) ∧ ∀x(ϕ(x) → ϕ(S(x))) → ∀xϕ(x)

  private class PrefixVisitor extends Visitor[Set[P_VarName]] {
    def collect(v: P_VarName, vars: Set[P_VarName]): Set[P_VarName] =
      vars + v
  }

  private val inductionSchemaMatcher = {
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
