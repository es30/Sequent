package p_axiomatization

import scala.util.control.TailCalls._
import scala.collection.mutable.ArrayBuffer

import p_term._
import p_term.P_Term.lookupVarName
import formula._
import p_formula._
import p_formula.P_Formula.lookupProposition

/**
  * Created by EdSnow on 1/5/2017.
  */

object deconstruct {

  val term_marker = lookupVarName("Z")

  implicit class P_TermDeconstructor(val template: P_Term) {

    def deconstruct(
        continuation: Option[() => TailRec[Boolean]],
        candidate: P_Term,
        extractees: ArrayBuffer[P_Term]
      ): TailRec[Boolean] =
      template match {

        case tc: P_TermConstant =>
          val retval = candidate == tc
          tailcall(deconstruct_continue(continuation, retval))

        case tv: P_TermVariable =>
          val retval =
            if (tv.v == term_marker) {
              extractees += candidate
              true
            }
            else
              candidate == tv
          tailcall(deconstruct_continue(continuation, retval))

        case ta: P_TermApplication =>
          candidate match {
            case t: P_TermApplication if t.f == ta.f =>
              tailcall(ta.a.deconstruct(continuation, t.a, extractees))
            case _ =>
              done(false)
          }

      }

  }

  implicit class P_ApplicandDeconstructor(val template: P_Applicand) {

    def deconstruct(
        candidate: P_Applicand,
        extractees: ArrayBuffer[P_Term]
      ): Boolean =
        tailcall(deconstruct(None, candidate, extractees)).result

    def deconstruct(
        continuation: Option[() => TailRec[Boolean]],
        candidate: P_Applicand,
        extractees: ArrayBuffer[P_Term]
      ): TailRec[Boolean] = {
      val  templateTerms =  template.terms
      val candidateTerms = candidate.terms
      val length = templateTerms.length
      if (candidateTerms.length == length) {

        def looper(
            termIndex: Int
          ): TailRec[Boolean] =
          if (termIndex >= length)
            tailcall(deconstruct_continue(continuation, true))
          else {
            def cont1: () => TailRec[Boolean] =
              () => tailcall(looper(termIndex + 1))
            tailcall(templateTerms(termIndex).
              deconstruct(Some(cont1), candidateTerms(termIndex), extractees))
          }

        tailcall(looper(0))

      }
      else
        done(false)
    }

  }

  val formula_marker = lookupProposition("Z")

  implicit class FormulaDeconstructor(val template: Formula) {

    def deconstructor(): P_Formula => Option[(ArrayBuffer[P_Formula], ArrayBuffer[P_Term])] =
      template match {
        case x: P_Formula =>
          (f: P_Formula) => x.deconstruct(f)
        case _ =>
          Formula => None
    }

  }

  implicit class P_FormulaDeconstructor(val template: P_Formula) {

    def deconstruct(
        candidate: P_Formula
      ): Option[(ArrayBuffer[P_Formula], ArrayBuffer[P_Term])] = {
      val extractees = (ArrayBuffer.empty[P_Formula], ArrayBuffer.empty[P_Term])
      if (deconstruct(None, candidate, extractees).result)
        Some(extractees)
      else
        None
    }

    def deconstruct(
        continuation: Option[() => TailRec[Boolean]],
        candidate: P_Formula,
        extractees: (ArrayBuffer[P_Formula], ArrayBuffer[P_Term])
      ): TailRec[Boolean] =
      template match {

        case ap: P_FormulaProposition =>
          val (formulas, _) = extractees
          val retval =
            if (ap.p == formula_marker) {
              formulas += candidate
              true
            }
            else candidate == ap
          tailcall(deconstruct_continue(continuation, retval))

        case aa: P_FormulaApplication =>
          val (_, terms) = extractees
          val retval =
            candidate match {
              case f: P_FormulaApplication if f.p == aa.p =>
                aa.a.deconstruct(f.a, terms)
              case _ =>
                false
            }
          tailcall(deconstruct_continue(continuation, retval))

        case an: P_FormulaNegation =>
          candidate match {
            case f: P_FormulaNegation =>
              tailcall(an.f1.deconstruct(continuation, f.f1, extractees))
            case _ =>
              done(false)
          }

        case ad: P_FormulaDyadic =>
          candidate match {
            case f: P_FormulaDyadic if f.kind == ad.kind =>
              def cont1: () => TailRec[Boolean] =
                () => tailcall(ad.f2.deconstruct(continuation, f.f2, extractees))
              tailcall(ad.f1.deconstruct(Some(cont1), f.f1, extractees))
            case _ =>
              done(false)
          }

        case aq: P_FormulaQuantification =>
          candidate match {
            case f: P_FormulaQuantification if f.kind == aq.kind && f.v == aq.v =>
              tailcall(aq.f1.deconstruct(continuation, f.f1, extractees))
            case _ =>
              done(false)
          }

      }

  }

  def deconstruct_continue(
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
