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

object deconstructP_Term {

  val marker = lookupVarName("Z")

  implicit class P_TermDeconstructor(val template: P_Term) {

    def deconstruct(
        stack: List[(Iterator[P_Term], Iterator[P_Term])],
        candidate: P_Term,
        extractees: ArrayBuffer[P_Term]
      ): TailRec[Boolean] =
      template match {

        case tc: P_TermConstant =>
          val retval =
            candidate match {
              case t: P_TermConstant =>
                t.c == tc.c
              case _ =>
                false
            }
          tailcall(deconstruct_continue(stack, extractees, retval))

        case tv: P_TermVariable =>
          val retval =
            if (tv.v == marker) {
              extractees += candidate
              true
            }
            else
              candidate == tv
          tailcall(deconstruct_continue(stack, extractees, retval))

        case ta: P_TermApplication =>
          candidate match {
            case t: P_TermApplication if t.f == ta.f =>
              tailcall(ta.a.deconstruct_1(stack, t.a, extractees))
            case _ =>
              tailcall(deconstruct_continue(stack, extractees, false))
          }

      }

  }

  implicit class P_ApplicandDeconstructor(val template: P_Applicand) {

    def deconstruct_applicand(
        candidate: P_Applicand,
        extractees: ArrayBuffer[P_Term]
      ): Boolean =
        tailcall(deconstruct_1(Nil, candidate, extractees)).result

    def deconstruct_1(
        stack: List[(Iterator[P_Term], Iterator[P_Term])],
        candidate: P_Applicand,
        extractees: ArrayBuffer[P_Term]
      ): TailRec[Boolean] = {
      val  templateTerms =  template.terms
      val candidateTerms = candidate.terms
      if (candidateTerms.length == templateTerms.length) {
        val  templates =  templateTerms.iterator
        val candidates = candidateTerms.iterator
        tailcall(deconstruct_2((templates, candidates) :: stack, extractees))
      }
      else
        tailcall(deconstruct_continue(stack, extractees, false))
    }

  }

  def deconstruct_2(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      extractees: ArrayBuffer[P_Term]
    ): TailRec[Boolean] = {
    val (templates, candidates) = stack.head
    if (templates.hasNext)
      tailcall(templates.next().deconstruct(stack, candidates.next(), extractees))
    else
      tailcall(deconstruct_continue(stack.tail, extractees, true))
  }

  def deconstruct_continue(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      extractees: ArrayBuffer[P_Term],
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else
      tailcall(deconstruct_2(stack, extractees))

}

object deconstructP_Formula {

  import deconstructP_Term.P_ApplicandDeconstructor

  val marker = lookupProposition("Z")

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
      if (deconstruct_1(Nil, candidate, extractees).result)
        Some(extractees)
      else
        None
    }

    def deconstruct_1(
        stack: List[(P_Formula, P_Formula)],
        candidate: P_Formula,
        extractees: (ArrayBuffer[P_Formula], ArrayBuffer[P_Term])
      ): TailRec[Boolean] =
      template match {

        case ap: P_FormulaProposition =>
          val (formulas, _) = extractees
          val retval =
            if (ap.p == marker) {
              formulas += candidate
              true
            }
            else candidate == ap
          tailcall(deconstruct_continue(stack, extractees, retval))

        case aa: P_FormulaApplication =>
          val (_, terms) = extractees
          val retval =
            candidate match {
              case f: P_FormulaApplication if f.p == aa.p =>
                aa.a.deconstruct_applicand(f.a, terms)
              case _ =>
                false
            }
          tailcall(deconstruct_continue(stack, extractees, retval))

        case an: P_FormulaNegation =>
          candidate match {
            case f: P_FormulaNegation =>
              tailcall(an.f1.deconstruct_1(stack, f.f1, extractees))
            case _ =>
              tailcall(deconstruct_continue(stack, extractees, false))
          }

        case ad: P_FormulaDyadic =>
          candidate match {
            case f: P_FormulaDyadic if f.kind == ad.kind =>
              val newStack = (ad.f2, f.f2) :: stack
              tailcall(ad.f1.deconstruct_1(newStack, f.f1, extractees))
            case _ =>
              tailcall(deconstruct_continue(stack, extractees, false))
          }

        case aq: P_FormulaQuantification =>
          candidate match {
            case f: P_FormulaQuantification if f.kind == aq.kind && f.v == aq.v =>
              tailcall(aq.f1.deconstruct_1(stack, f.f1, extractees))
            case _ =>
              tailcall(deconstruct_continue(stack, extractees, false))
          }

      }

  }

  def deconstruct_continue(
      stack: List[(P_Formula, P_Formula)],
      extractees: (ArrayBuffer[P_Formula], ArrayBuffer[P_Term]),
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else {
      val (template, candidate) = stack.head
      tailcall(template.deconstruct_1(stack.tail, candidate, extractees))
    }

}
