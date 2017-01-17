package p_axiomatization

import scala.annotation.tailrec

import p_term.P_VarName
import p_formula._

/**
  * Created by EdSnow on 1/11/2017.
  */


trait Visitor[T] {
  def collect(v: P_VarName, vars: T): T
}


object prefixWalker {

  implicit class P_Formula_prefixWalker(val formula: P_Formula) {

    @tailrec final def walkPrefix[T](visitor: Visitor[T], vars: T): (T, P_Formula) =
      formula match {
        case aq: P_FormulaQuantification if aq.kind == QuantificationKind.Universal =>
          aq.f1.walkPrefix(visitor, visitor.collect(aq.v, vars))
        case _ =>
          (vars, formula)
      }
  }

}
