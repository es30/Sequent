package p_formula

/**
  * Created by EdSnow on 1/7/2017.
  */

import scala.util.control.TailCalls._

import p_term._
import p_term.walker.P_Applicand_walker


trait Visitor extends p_term.Visitor {
  def binding(v: P_VarName): Boolean
}


object walker {

  implicit class P_Formula_walker(val formula: P_Formula) {

    def walk(visitor: Visitor): Boolean =
      walk(None, visitor).result

    final def walk(
        continuation: Option[() => TailRec[Boolean]],
        visitor: Visitor
      ): TailRec[Boolean] =
      formula match {

        case ap: P_FormulaProposition =>
          tailcall(walk_continue(continuation, false))

        case aa: P_FormulaApplication =>
          val found = aa.a.walk(None, visitor, Set()).result
          tailcall(walk_continue(continuation, found))

        case an: P_FormulaNegation =>
          tailcall(an.f1.walk(continuation, visitor))

        case ad: P_FormulaDyadic =>
          def cont1: () => TailRec[Boolean] =
            () => tailcall(ad.f2.walk(continuation, visitor))
          tailcall(ad.f1.walk(Some(cont1), visitor))
        case aq: P_FormulaQuantification =>
          if (visitor.binding(aq.v))
            //  The visitor has chosen not to walk the formula
            //  within the scope of the perhaps previously free
            //  but now bound variable.
            tailcall(walk_continue(continuation, false))
          else
            tailcall(aq.f1.walk(continuation, visitor))

      }

  }

  private def walk_continue(
      continuation: Option[() => TailRec[Boolean]],
      retval: Boolean
    ): TailRec[Boolean] =
    if (retval)
      done(true)
    else
      continuation match {
        case Some(cont) =>
          tailcall(cont())
        case None =>
          done(false)
      }

}
