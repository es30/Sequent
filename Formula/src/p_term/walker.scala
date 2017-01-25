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
      walk(Nil, visitor, vars).result

    def walk(
        stack: List[Iterator[P_Term]],
        visitor: Visitor,
        vars: Set[P_VarName]
      ): TailRec[Boolean] =
      term match {

        case tc: P_TermConstant =>
          tailcall(walk_continue(stack, visitor, vars, false))

        case tv: P_TermVariable =>
          tailcall(walk_continue(stack, visitor, vars, visitor.variable(vars, tv.v)))

        case ta: P_TermApplication =>
          tailcall(ta.a.walk(stack, visitor, vars))

      }

  }

  implicit class P_Applicand_walker(val applicand: P_Applicand) {

    def walk(
        stack: List[Iterator[P_Term]],
        visitor: Visitor,
        vars: Set[P_VarName]
      ): TailRec[Boolean] = {
      val terms = applicand.terms.iterator
      tailcall(walk_1(terms :: stack, visitor, vars))
    }

  }

  def walk_1(
      stack: List[Iterator[P_Term]],
      visitor: Visitor,
      vars: Set[P_VarName]
    ): TailRec[Boolean] = {
    val terms = stack.head
    if (terms.hasNext)
      tailcall(terms.next().walk(stack, visitor, vars))
    else
      tailcall(walk_continue(stack.tail, visitor, vars, false))
  }

  def walk_continue(
      stack: List[Iterator[P_Term]],
      visitor: Visitor,
      vars: Set[P_VarName],
      retval: Boolean
    ): TailRec[Boolean] =
    if (retval || stack.isEmpty)
      done(retval)
    else
      tailcall(walk_1(stack, visitor, vars))

}
