package p_axiomatization

import scala.util.control.TailCalls._

import p_term._
import p_formula._

/**
  * Created by EdSnow on 1/7/2017.
  */

object checkSubstitutionP_Term {

  implicit class P_TermChecker(val template: P_Term) {

    def checkSubstitution(
        candidate: P_Term,
        subst: (P_Term, P_VarName)
      ): Boolean =
      checkSubstitution(Nil, candidate, subst).result

    def checkSubstitution(
        stack: List[(Iterator[P_Term], Iterator[P_Term])],
        candidate: P_Term,
        subst: (P_Term, P_VarName)
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
          tailcall(checkSubstitution_continue(stack, subst, retval))

        case tv: P_TermVariable =>
          val (substitution, substitutee) = subst
          val retval =
            if (tv.v == substitutee)
              candidate == substitution
            else
              candidate == tv
          tailcall(checkSubstitution_continue(stack, subst, retval))

        case ta: P_TermApplication =>
          candidate match {
            case t: P_TermApplication if t.f == ta.f =>
              tailcall(ta.a.checkSubstitution_1(stack, t.a, subst))
            case _ =>
              tailcall(checkSubstitution_continue(stack, subst, false))
          }

      }

  }

  implicit class P_ApplicandChecker(val template: P_Applicand) {

    def checkSubstitution_applicand(
        candidate: P_Applicand,
        subst: (P_Term, P_VarName)
      ): Boolean =
      tailcall(checkSubstitution_1(Nil, candidate, subst)).result

    def checkSubstitution_1(
        stack: List[(Iterator[P_Term], Iterator[P_Term])],
        candidate: P_Applicand,
        subst: (P_Term, P_VarName)
      ): TailRec[Boolean] = {
      val  templateTerms =  template.terms
      val candidateTerms = candidate.terms
      if (candidateTerms.length == templateTerms.length) {
        val  templates =  templateTerms.iterator
        val candidates = candidateTerms.iterator
        tailcall(checkSubstitution_2((templates, candidates) :: stack, subst))
      }
      else
        tailcall(checkSubstitution_continue(stack, subst, false))
    }

  }

  def checkSubstitution_2(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      subst: (P_Term, P_VarName)
    ): TailRec[Boolean] = {
    val (templates, candidates) = stack.head
    if (templates.hasNext)
      tailcall(templates.next().checkSubstitution(stack, candidates.next(), subst))
    else
      tailcall(checkSubstitution_continue(stack.tail, subst, true))
  }

  def checkSubstitution_continue(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      subst: (P_Term, P_VarName),
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else
      tailcall(checkSubstitution_2(stack, subst))

}

object checkSubstitutionP_Formula {

  import checkSubstitutionP_Term.P_ApplicandChecker

  implicit class P_FormulaChecker(val template: P_Formula) {

    def checkSubstitution(
        candidate: P_Formula,
        subst: (P_Term, P_VarName)
      ): Boolean =
      checkSubstitution_1(Nil, candidate, subst).result

    def checkSubstitution_1(
        stack: List[(P_Formula, P_Formula)],
        candidate: P_Formula,
        subst: (P_Term, P_VarName)
      ): TailRec[Boolean] =
      template match {

        case ap: P_FormulaProposition =>
          val retval = candidate == ap
          tailcall(checkSubstitution_continue(stack, subst, retval))

        case aa: P_FormulaApplication =>
          val retval =
            candidate match {
              case f: P_FormulaApplication if f.p == aa.p =>
                aa.a.checkSubstitution_applicand(f.a, subst)
              case _ =>
                false
            }
          tailcall(checkSubstitution_continue(stack, subst, retval))

        case an: P_FormulaNegation =>
          candidate match {
            case f: P_FormulaNegation =>
              tailcall(an.f1.checkSubstitution_1(stack, f.f1, subst))
            case _ =>
              tailcall(checkSubstitution_continue(stack, subst, false))
          }

        case ad: P_FormulaDyadic =>
          candidate match {
            case f: P_FormulaDyadic if f.kind == ad.kind =>
              val newStack = (ad.f2, f.f2) :: stack
              tailcall(ad.f1.checkSubstitution_1(newStack, f.f1, subst))
            case _ =>
              tailcall(checkSubstitution_continue(stack, subst, false))
          }

        case aq: P_FormulaQuantification =>
          candidate match {
            case f: P_FormulaQuantification if f.kind == aq.kind && f.v == aq.v =>
              tailcall(aq.f1.checkSubstitution_1(stack, f.f1, subst))
            case _ =>
              tailcall(checkSubstitution_continue(stack, subst, false))
          }

      }

  }

  def checkSubstitution_continue(
      stack: List[(P_Formula, P_Formula)],
      subst: (P_Term, P_VarName),
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else {
      val (template, candidate) = stack.head
      tailcall(template.checkSubstitution_1(stack.tail, candidate, subst))
    }

}
