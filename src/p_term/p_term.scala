/**
  * Created by EdSnow on 8/15/2016.
  */


package p_term


class P_Constant(val name: String) {

  override
  def toString = name

}


class P_VarName(val name: String) {

  override
  def toString = name

}


//class P_Function(val name: String)(implicit applicationToString: Applicand[Any] => String) {
class P_Function(val name: String) {

  //  implicit
  //  def applicationToString(a: Applicand): String = tag + a.toString
  //  def toString = "(" + a.terms(0).toString + " + " + a.terms(1).toString + ")"
  def applicationToString(a: P_Applicand) =
    "(" + a.terms(0).toString + " + " + a.terms(1).toString + ")"

  override
  def toString = name

}


// ====================================================================


import error._
import term._


abstract class P_Term (
    error: Option[Error] = None
) extends Term(error)


case class P_TermConstant(val c: P_Constant, override val error: Option[Error] = None) extends P_Term {

  final override
  def equals(obj: Any) =
    obj match {
      case t: P_TermConstant => c == t.c
      case _ => false
    }

  final override
  def hashCode(): Int = c.hashCode()

  override
  def toString = c.toString

}


case class P_TermVariable(val v: P_VarName, override val error: Option[Error] = None) extends P_Term {

  final override
  def equals(obj: Any) =
    obj match {
      case t: P_TermVariable => v == t.v
      case _ => false
    }

  final override
  def hashCode(): Int = v.hashCode()

  override
  def toString = v.toString

}


case class P_TermApplication(val f: P_Function, val a: P_Applicand, override val error: Option[Error] = None) extends P_Term {

  final override
  def equals(obj: Any) =
    obj match {
      case t: P_TermApplication => f == t.f && a == t.a
      case _ => false
    }

  final override
  def hashCode(): Int = f.hashCode() + a.hashCode()

  override
  //  def toString = f.applicationToString(a)
  def toString = f.toString + a.toString

}


// ====================================================================


case class P_Applicand(val terms: Array[P_Term]) {

  final override
  def equals(obj: Any) =
    obj match {
      case a: P_Applicand =>
        if (terms.length == a.terms.length) {
          val if1 = terms.iterator
          val if2 = a.terms.iterator
          var eq = true
          while (if1.hasNext) {
            eq = eq & (if1.next() == if2.next())
          }
          eq
        }
        else
          false
      case _ => false
    }

  final override
  def hashCode(): Int = terms.foldLeft(0)((b, t) => b + t.hashCode())

  override
  def toString =
    "(" + terms.foldLeft(("", true))((b, t) => (b._1 + (if (b._2) "" else ", ") + t.toString, false))._1 + ")"

}
