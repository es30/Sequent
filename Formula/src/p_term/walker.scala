package p_term

/**
  * Created by EdSnow on 1/8/2017.
  */

import scala.util.control.TailCalls._


trait Visitor {
  def variable(vars: Set[P_VarName], v: P_VarName): Boolean
}


object walker {

  implicit class P_Term_walker(val term: P_Term) {

    def walk(
        visitor: Visitor,
        vars: Set[P_VarName]
      ): Boolean =
      walk(None, visitor, vars).result

    def walk(
        continuation: Option[() => TailRec[Boolean]],
        visitor: Visitor,
        vars: Set[P_VarName]
      ): TailRec[Boolean] =
      term match {

        case _: P_TermConstant =>
          tailcall(walk_continue(continuation, false))

        case tv: P_TermVariable =>
          tailcall(walk_continue(continuation, visitor.variable(vars, tv.v)))

        case ta: P_TermApplication =>
          tailcall(ta.a.walk(continuation, visitor, vars))

      }

  }

  implicit class P_Applicand_walker(val applicand: P_Applicand) {

    def walk(
        continuation: Option[() => TailRec[Boolean]],
        visitor: Visitor,
        vars: Set[P_VarName]
      ): TailRec[Boolean] = {
      val terms = applicand.terms
      val length = terms.length

      def looper(
          termIndex: Int
        ): TailRec[Boolean] =
        if (termIndex >= length)
          tailcall(walk_continue(continuation, false))
        else {
          def cont1: () => TailRec[Boolean] =
            () => tailcall(looper(termIndex + 1))

          tailcall(terms(termIndex).walk(Some(cont1), visitor, vars))
        }

      tailcall(looper(0))

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
