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
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean]

  final override
  def equals(obj: Any): Boolean =
    equals(None, obj).result

  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int]

  final override
  def hashCode(): Int =
    hashCode(None, 0).result

}


object P_Term {


  val constantMap = collection.mutable.Map[String, P_Constant]()
  def lookupConstant(s: String) = constantMap.getOrElseUpdate(s, new P_Constant(s))

  val  varNameMap = collection.mutable.Map[String, P_VarName ]()
  def lookupVarName (s: String) =  varNameMap.getOrElseUpdate(s, new P_VarName (s))

  val functionMap = collection.mutable.Map[String, P_Function]()
  def lookupFunction(s: String) = functionMap.getOrElseUpdate(s, new P_Function(s))
  def lookupFunction(s: String, f: P_Function) = functionMap.getOrElseUpdate(s, f)


  def equals_continue(
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

  def hashCode_continue(
      continuation: Option[Int => TailRec[Int]],
      retval: Int
    ): TailRec[Int] =
    continuation match {
      case Some(cont) =>
        tailcall(cont(retval))
      case None =>
        done(retval)
    }

}


import P_Term._


case class P_TermConstant(c: P_Constant, override val error: Option[Error] = None) extends P_Term {

  override
  def termString() = c.toString

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case t: P_TermConstant =>
          t.c == c
        case _ =>
          false
      }
    tailcall(equals_continue(continuation, retval))
  }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(continuation, 3 * hash + c.hashCode()))

}


case class P_TermVariable(v: P_VarName, override val error: Option[Error] = None) extends P_Term {

  override
  def termString() = v.toString

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case t: P_TermVariable =>
          t.v == v
        case _ =>
          false
      }
    tailcall(equals_continue(continuation, retval))
  }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(continuation, 5 * hash + v.hashCode()))

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
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case t: P_TermApplication if t.f == f =>
        tailcall(a.equals(continuation, t.a.terms))
      case _ =>
        tailcall(equals_continue(continuation, false))
    }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(a.hashCode(continuation, 7 * hash + f.hashCode()))

}


// ====================================================================


//object P_Applicand {type Terms = immutable.ArraySeq[P_Term]}
                                //  aspirational, via IndexedSeq
object P_Applicand {type Terms = Array[P_Term]}
import P_Applicand.Terms
case class P_Applicand(terms: Terms) {

  final override
  def equals(obj: Any): Boolean =
    obj match {
      case a: P_Applicand =>
        equals(None, a.terms).result
      case _ =>
        false
    }

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Terms
    ): TailRec[Boolean] = {
    val length = terms.length
    if (obj.length == length) {

      def looper(
          termIndex: Int
        ): TailRec[Boolean] =
        if (termIndex >= length)
          tailcall(equals_continue(continuation, true))
        else {
          def cont1: () => TailRec[Boolean] =
            () => tailcall(looper(termIndex + 1))
          tailcall(terms(termIndex).equals(Some(cont1), obj(termIndex)))
        }

      tailcall(looper(0))

    }
    else
      done(false)
  }

  final override
  def hashCode(): Int =
    hashCode(None, 0).result

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] = {
    val length = terms.length

    def looper(
        termIndex: Int,
        hash: Int
      ): TailRec[Int] =
      if (termIndex >= length)
        tailcall(hashCode_continue(continuation, hash))
      else {
        def cont1: Int => TailRec[Int] =
          (hash: Int) => tailcall(looper(termIndex + 1, hash))
        tailcall(terms(termIndex).hashCode(Some(cont1), hash))
      }

    tailcall(looper(0, hash))

  }

  override
  def toString =
    "(" + terms.map{_.termString}.mkString(", ") + ")"

}
