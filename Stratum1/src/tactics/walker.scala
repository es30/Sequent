package tactics

/**
  * Created by EdSnow on 1/8/2017.
  */

import scala.annotation.tailrec
import scala.util.control.TailCalls._

import p_term._


object walker {


  type SynVarIndex = BigInt


  private object visitor {

    def variable(
        v: P_VarName,
        vars: Set[SynVarIndex],
        firstGap: SynVarIndex
      ): (Set[SynVarIndex], SynVarIndex, Boolean, Boolean) = {
      val name = v.name
      val nameIndex =
        if (name.startsWith("x"))
          try {
            val candidateIndex = BigInt(name.substring(1))
            if (name.startsWith("x0") && name.length > 2)
              None    //  The string representation of a synthetic-
                      //    variable index will not include a
                      //    superfluous leading zero. This avoids
                      //    "xnnn..." and "x0nnn..." being mistaken
                      //    as naming the same variable.
            else
              Some(candidateIndex)
          } catch {
            case _: NumberFormatException => None
          }
        else None
      nameIndex match {
        case Some(index) if index >= firstGap =>
          val nextVars = vars + index

          @tailrec def nextGap(i: SynVarIndex): SynVarIndex =
            if (nextVars contains i)
              nextGap(i + 1)
            else i

          (nextVars, nextGap(index), false, false)
        case _ =>
          val varIsX = v == var_x
          val varIsY = v == var_y
          (vars, firstGap, varIsX, varIsY)
      }
    }

  }


  implicit class P_Term_walker(val term: P_Term) {

    def walk(
        vars: Set[SynVarIndex],
        firstGap: SynVarIndex
      ): (Set[SynVarIndex], SynVarIndex, Boolean, Boolean) =
      walk(None, vars, firstGap, false, false).result

    def walk(
        continuation: Option[((Set[SynVarIndex], SynVarIndex, Boolean, Boolean)) =>
          TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)]],
        vars: Set[SynVarIndex],
        firstGap: SynVarIndex,
        occursX: Boolean,
        occursY: Boolean
      ): TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)] =
      term match {

        case _: P_TermConstant =>
          tailcall(walk_continue(continuation, (vars, firstGap, occursX, occursY)))

        case tv: P_TermVariable =>
          val (newVars, newFirstGap, varIsX, varIsY) =
            visitor.variable(tv.v, vars, firstGap)
          tailcall(walk_continue(continuation,
            (newVars, newFirstGap, occursX || varIsX, occursY || varIsY)))

        case ta: P_TermApplication =>
          tailcall(ta.a.walk(continuation, vars, firstGap, occursX, occursY))

      }

  }

  implicit class P_Applicand_walker(val applicand: P_Applicand) {

    def walk(
        continuation: Option[((Set[SynVarIndex], SynVarIndex, Boolean, Boolean)) =>
          TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)]],
        vars: Set[SynVarIndex],
        firstGap: SynVarIndex,
        occursX: Boolean,
        occursY: Boolean
      ): TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)] = {
      val terms = applicand.terms
      val length = terms.length

      def looper(
          termIndex: Int,
          vars: Set[SynVarIndex],
          firstGap: SynVarIndex,
          occursX: Boolean,
          occursY: Boolean
        ): TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)] =
        if (termIndex >= length)
          tailcall(walk_continue(continuation, (vars, firstGap, occursX, occursY)))
        else {

          def cont1: ((Set[SynVarIndex], SynVarIndex, Boolean, Boolean)) =>
                  TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)] = (
                retval: (Set[SynVarIndex], SynVarIndex, Boolean, Boolean)
              ) => {
              val (vars, firstGap, occursX, occursY) = retval
              tailcall(looper(termIndex + 1, vars, firstGap, occursX, occursY))
            }

          tailcall(terms(termIndex).walk(Some(cont1), vars, firstGap, occursX, occursY))
        }

      tailcall(looper(0, vars, firstGap, occursX, occursY))

    }

  }


  private def walk_continue(
      continuation: Option[((Set[SynVarIndex], SynVarIndex, Boolean, Boolean)) =>
        TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)]],
      retval: (Set[SynVarIndex], SynVarIndex, Boolean, Boolean)
    ): TailRec[(Set[SynVarIndex], SynVarIndex, Boolean, Boolean)] =
    continuation match {
      case Some(cont) =>
        tailcall(cont(retval))
      case None =>
        done(retval)
    }


}
