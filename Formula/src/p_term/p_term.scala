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


trait DisplayString {
  def displayString(applicand: P_Applicand): String
}


class P_Function(val name: String) {

  override
  def toString = name

}


// ====================================================================


import scala.util.control.TailCalls._

import error._
import term._


abstract class P_Term (
    error: Option[Error] = None
) extends Term(error) {

  def equals(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      obj: Any
    ): TailRec[Boolean]

  final override
  def equals(obj: Any): Boolean =
    equals(Nil, obj).result

  def hashCode(
      stack: List[Iterator[P_Term]],
      hash: Int
    ): TailRec[Int]

  final override
  def hashCode(): Int =
    hashCode(Nil, 0).result

}


object P_Term {


  val constantMap = collection.mutable.Map[String, P_Constant]()
  def lookupConstant(s: String) = constantMap.getOrElseUpdate(s, new P_Constant(s))

  val  varNameMap = collection.mutable.Map[String, P_VarName ]()
  def lookupVarName (s: String) =  varNameMap.getOrElseUpdate(s, new P_VarName (s))

  val functionMap = collection.mutable.Map[String, P_Function]()
  def lookupFunction(s: String) = functionMap.getOrElseUpdate(s, new P_Function(s))
  def lookupFunction(s: String, f: P_Function) = functionMap.getOrElseUpdate(s, f)


  def equals_1(
      stack: List[(Iterator[P_Term], Iterator[P_Term])]
    ): TailRec[Boolean] = {
    val (subjs, objs) = stack.head
    if (subjs.hasNext)
      tailcall(subjs.next().equals(stack, objs.next()))
    else
      tailcall(equals_continue(stack.tail, true))
  }

  def equals_continue(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else
      tailcall(equals_1(stack))

  def hashCode_1(
      stack: List[Iterator[P_Term]],
      hash: Int
    ): TailRec[Int] = {
    val subjs = stack.head
    if (subjs.hasNext)
      tailcall(subjs.next().hashCode(stack, hash))
    else
      tailcall(hashCode_continue(stack.tail, hash))
  }

  def hashCode_continue(
      stack: List[Iterator[P_Term]],
      retval: Int
    ): TailRec[Int] =
    if (stack.isEmpty)
      done(retval)
    else
      tailcall(hashCode_1(stack, retval))

}


import P_Term._


case class P_TermConstant(c: P_Constant, override val error: Option[Error] = None) extends P_Term {

  override
  def termString() = c.toString

  final
  def equals(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case t: P_TermConstant =>
          t.c == c
        case _ =>
          false
      }
    tailcall(equals_continue(stack, retval))
  }

  final
  def hashCode(
      stack: List[Iterator[P_Term]],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(stack, 3 * hash + c.hashCode()))

}


case class P_TermVariable(v: P_VarName, override val error: Option[Error] = None) extends P_Term {

  override
  def termString() = v.toString

  final
  def equals(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case t: P_TermVariable =>
          t.v == v
        case _ =>
          false
      }
    tailcall(equals_continue(stack, retval))
  }

  final
  def hashCode(
      stack: List[Iterator[P_Term]],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(stack, 5 * hash + v.hashCode()))

}


case class P_TermApplication(f: P_Function, a: P_Applicand, override val error: Option[Error] = None) extends P_Term {

  override
  def termString() =
    f match {
      case function: DisplayString =>
        function.displayString(a)
      case _ =>
        f.toString + a.toString
    }

  final
  def equals(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case t: P_TermApplication if t.f == f && t.a.terms.length == a.terms.length =>
        tailcall(a.equals(stack, t.a.terms))
      case _ =>
        tailcall(equals_continue(stack, false))
    }

  final
  def hashCode(
     stack: List[Iterator[P_Term]],
     hash: Int
    ): TailRec[Int] =
    tailcall(a.hashCode(stack, 7 * hash + f.hashCode()))

}


// ====================================================================


case class P_Applicand(terms: Array[P_Term]) {

  final
  def equals(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      obj: Array[P_Term]
    ): TailRec[Boolean] = {
    val subjs = terms.iterator
    val objs = obj.iterator
    tailcall(equals_1((subjs, objs) :: stack))
  }

  final override
  def equals(obj: Any): Boolean =
    obj match {
      case a: P_Applicand =>
        equals(Nil, a.terms).result
      case _ =>
        false
    }

  final
  def hashCode(
      stack: List[Iterator[P_Term]],
      hash: Int
    ): TailRec[Int] = {
    val subjs = terms.iterator
    tailcall(hashCode_1(subjs :: stack, hash))
  }

  final override
  def hashCode(): Int =
    hashCode(Nil, 0).result

  override
  def toString =
    "(" + terms.map{_.termString}.mkString(", ") + ")"

}
