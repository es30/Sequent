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
      walk(Nil, visitor).result

    final def walk(
        stack: List[P_Formula],
        visitor: Visitor
      ): TailRec[Boolean] =
      formula match {

        case ap: P_FormulaProposition =>
          tailcall(walk_continue(stack, visitor, false))

        case aa: P_FormulaApplication =>
          val found = aa.a.walk(Nil, visitor, Set()).result
          tailcall(walk_continue(stack, visitor, found))

        case an: P_FormulaNegation =>
          tailcall(an.f1.walk(stack, visitor))

        case ad: P_FormulaDyadic =>
          tailcall(ad.f1.walk(ad.f2 :: stack, visitor))

        case aq: P_FormulaQuantification =>
          if (visitor.binding(aq.v))
            //  The visitor has chosen not to walk the formula
            //  within the scope of the perhaps previously free
            //  but now bound variable.
            tailcall(walk_continue(stack, visitor, false))
          else
            tailcall(aq.f1.walk(stack, visitor))

      }

  }

  def walk_continue(
      stack: List[P_Formula],
      visitor: Visitor,
      retval: Boolean
    ): TailRec[Boolean] =
    if (retval || stack.isEmpty)
      done(retval)
    else {
      val f2 = stack.head
      tailcall(f2.walk(stack.tail, visitor))
    }

}
