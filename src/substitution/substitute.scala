package substitution

import scala.util.control.TailCalls._

import error._
import p_term._
import p_formula._

/**
  * Created by EdSnow on 12/29/2016.
  */


case class Error_subst_variable(
  collisions: Seq[P_VarName]
) extends Error

case class Error_subst_applicand(
  errorList: List[(Error, Int)]
) extends Error


object substitute {


  implicit class P_TermExtension(val extendee: P_Term) {

    def substitution_1(
        t: P_Term,
        x: P_VarName,
        conflict: Seq[P_VarName]
      ): Either[Option[(P_Term, Option[Error])], P_TermApplication] = extendee match {

      case tc: P_TermConstant =>
        Left(None)       //  The constant is unaltered.

      case tv: P_TermVariable =>
        Left(
          if (tv.v == x)
            //  Variable x is replaced by term t.
            if (conflict.isEmpty)
              if (tv == t)
                //  Equal replaces equal and no alteration is required.
                None
              else
                Some((t, None))
            else
              Some((t, Some(Error_subst_variable(conflict))))
          else
            //  There is no substitution.
            None
        )

      case ta: P_TermApplication =>
        Right(ta)

    }

  }


  implicit class P_ApplicandExtension(val a: P_Applicand) {

    def toError(errorList: List[(Error, Int)]): Option[Error] = {
      if (errorList.isEmpty)
        None
      else
        Some(Error_subst_applicand(errorList.reverse))

    }

    def substitution(
        t: P_Term,
        x: P_VarName,
        conflict: Seq[P_VarName]
      ): Option[(P_Applicand, Option[Error])] = {
      val subst = a.substitution_1(Nil, None, 0, t, x, conflict).result
      subst match {
        case None =>
          None
        case Some((applicand, errorList)) =>
          Some((applicand, toError(errorList)))
      }
    }

    def substitution_1(
        stack: List[Option[(P_Applicand, List[(Error, Int)])] => TailRec[Option[(P_Applicand, List[(Error, Int)])]]],
        firstState: Option[(P_Applicand, List[(Error, Int)])],
        firstTermIndex: Int,
        t: P_Term,
        x: P_VarName,
        conflict: Seq[P_VarName]
      ): TailRec[Option[(P_Applicand, List[(Error, Int)])]] = {

        val terms = a.terms
        val length = terms.length

        def looper(
            state: Option[(P_Applicand, List[(Error, Int)])],
            termIndex: Int
          ): TailRec[Option[(P_Applicand, List[(Error, Int)])]] = {

          def update(subst: Option[(P_Term, Option[Error])]):
              Option[(P_Applicand, List[(Error, Int)])] =
            subst match {
              case None =>
                state match {
                  case None =>
                    None
                  case Some((applicand, errorList)) =>
                    applicand.a.terms(termIndex) = a.a.terms(termIndex)
                    Some((applicand, errorList))
                }
              case Some((term, error)) =>
                val patternTerms = a.a.terms
                val patternLength = patternTerms.length
                val (target, errorList): (P_Applicand, List[(Error, Int)]) =
                  state.getOrElse{
                    val newTerms = new Array[P_Term](patternLength)
                    patternTerms.copyToArray(newTerms, 0, termIndex)
                    (P_Applicand(newTerms), Nil)
                  }
                target.a.terms(termIndex) = term
                val newErrorList =
                  error match {
                    case None =>
                      errorList
                    case Some(e) =>
                      (e, termIndex + 1) :: errorList
                  }
                Some((target, newErrorList))
            }

          def continuation(ta: P_TermApplication) =
            (returnedApplicand: Option[(P_Applicand, List[(Error, Int)])]) => {
                val newTa =
                  returnedApplicand match {
                    case None =>
                      None
                    case Some((applicand, errorList)) =>
                      Some((ta.copy(a = applicand), toError(errorList)))
                  }
                val newState = update(newTa)
                tailcall(a.substitution_1(stack, newState, termIndex + 1, t, x, conflict))
              }: TailRec[Option[(P_Applicand, List[(Error, Int)])]]

          if (termIndex >= length)
            //  Processing of the current applicand is complete.
            if (stack.isEmpty)
              done(state)
            else {
              val continuation = stack.head
              tailcall(continuation(state))
            }
          else {
            val nextTerm = terms(termIndex)
            nextTerm.substitution_1(t, x, conflict) match {
              case Left(subst) =>
                val newState = update(subst)
                tailcall(looper(newState, termIndex + 1))
              case Right(ta) =>
                val newStack = continuation(ta) :: stack
                tailcall(ta.a.substitution_1(newStack, None, 0, t, x, conflict))
            }
          }
        }

        tailcall(looper(firstState, firstTermIndex))

    }

  }


  implicit class P_FormulaExtension(val formula: P_Formula) {

    /*
      What follows is the (non-tail-)recursive implementation of substitution
      that serves as the model for the tail-recursive implementation.

    def substitution(t: P_Term, x: P_VarName, vars: Set[P_VarName], conflict: Seq[P_VarName]): Option[(P_Formula, Option[Error])] = extendee match {

      case ap: P_FormulaProposition =>
        None

      case aa: P_FormulaApplication =>
        val subst = aa.a.substitution(t, x, conflict)
        subst match {
          case None =>
            None
          case Some(applicand) =>
            val (a, error) = applicand
            Some((aa.copy(a = a), error))
        }

      case an: P_FormulaNegation =>
        val subst = an.f1.substitution(t, x, vars, conflict)
        subst match {
          case None =>
            None
          case Some(sf1) =>
            val (f1, error) = sf1
            Some((an.copy(f1 = f1), error))
        }

      case ad: P_FormulaDyadic =>
        val subst_f1 = ad.f1.substitution(t, x, vars, conflict)
        val subst_f2 = ad.f2.substitution(t, x, vars, conflict)
        if (subst_f1.isEmpty && subst_f2.isEmpty)
          None
        else {
          val (f1, error1) = subst_f1.getOrElse((ad.f1, None))
          val (f2, error2) = subst_f2.getOrElse((ad.f2, None))
          val error = collect(Seq(error1, error2))
          Some(ad.replace(f1, f2), error)
        }

      case aq: P_FormulaQuantification =>
        if (aq.v == x)
          None    //  x is bound and no longer subject to substitution.
        else {
          val newConflict =
            if (vars contains aq.v)
              aq.v +: conflict
            else
              conflict
          val subst = aq.f1.substitution(t, x, vars, newConflict)
          subst match {
            case None =>
              None
            case Some(sf1) =>
              val (f1, error) = sf1
              Some((aq.copy(f1 = f1), error))
          }
        }

    }
    */

    def substitution(
        t: P_Term,
        x: P_VarName,
        vars: Set[P_VarName]
      ): Option[(P_Formula, Option[Error])] =
      substitution_1(Nil, t, x, vars, Nil).result

    def substitution_1(
        stack: List[Option[(P_Formula, Option[Error])] => TailRec[Option[(P_Formula, Option[Error])]]],
        t: P_Term,
        x: P_VarName,
        vars: Set[P_VarName],
        conflict: Seq[P_VarName]
      ): TailRec[Option[(P_Formula, Option[Error])]] =
      formula match {

        case ap: P_FormulaProposition =>
          tailcall(substitution_continue(stack, None))

        case aa: P_FormulaApplication =>
          val subst = aa.a.substitution(t, x, conflict)
          val retval =
            subst match {
              case None =>
                None
              case Some(applicand) =>
                val (a, error) = applicand
                Some((aa.copy(a = a), error))
            }
          tailcall(substitution_continue(stack, retval))

        case an: P_FormulaNegation =>

          def cont1: Option[(P_Formula, Option[Error])] => TailRec[Option[(P_Formula, Option[Error])]] =
            (subst_f1: Option[(P_Formula, Option[Error])]) => {
              val retval =
                subst_f1 match {
                  case None =>
                    None
                  case Some(sf1) =>
                    val (f1, error) = sf1
                    Some((an.copy(f1 = f1), error))
                }
              tailcall(substitution_continue(stack, retval))
            }

          tailcall(an.f1.substitution_1(cont1 :: stack, t, x, vars, conflict))

        case ad: P_FormulaDyadic =>

          def cont1: Option[(P_Formula, Option[Error])] => TailRec[Option[(P_Formula, Option[Error])]] =
            (subst_f1: Option[(P_Formula, Option[Error])]) =>
              tailcall(ad.f2.substitution_1(cont2(subst_f1) :: stack, t, x, vars, conflict))

          def cont2(subst_f1: Option[(P_Formula, Option[Error])]) =
            (subst_f2: Option[(P_Formula, Option[Error])]) => {
              val retval =
                if (subst_f1.isEmpty && subst_f2.isEmpty)
                  None
                else {
                  val (f1, error1) = subst_f1.getOrElse((ad.f1, None))
                  val (f2, error2) = subst_f2.getOrElse((ad.f2, None))
                  val error = collect(Seq(error1, error2))
                  Some(ad.replace(f1, f2), error)
                }
              tailcall(substitution_continue(stack, retval))
            }

          tailcall(ad.f1.substitution_1(cont1 :: stack, t, x, vars, conflict))

        case aq: P_FormulaQuantification =>

          def cont1: Option[(P_Formula, Option[Error])] => TailRec[Option[(P_Formula, Option[Error])]] =
            (subst_f1: Option[(P_Formula, Option[Error])]) => {
              val retval =
                subst_f1 match {
                  case None =>
                    None
                  case Some(sf1) =>
                    val (f1, error) = sf1
                    Some((aq.copy(f1 = f1), error))
                }
              tailcall(substitution_continue(stack, retval))
            }

          if (aq.v == x)
            //  x is bound and no longer subject to substitution.
            tailcall(substitution_continue(stack, None))
          else {
            val newConflict =
              if (vars contains aq.v)
                aq.v +: conflict
              else
                conflict
            tailcall(aq.f1.substitution_1(cont1 :: stack, t, x, vars, newConflict))
          }

      }

    def substitution_continue(
        stack: List[Option[(P_Formula, Option[Error])] => TailRec[Option[(P_Formula, Option[Error])]]],
        retval: Option[(P_Formula, Option[Error])]
      ): TailRec[Option[(P_Formula, Option[Error])]] =
      if (stack.isEmpty)
        done(retval)
      else {
        val continuation = stack.head
        tailcall(continuation(retval))
      }

    import variables._

    def substitute(t: P_Term, x: P_VarName): (P_Formula, Option[Error]) = {
      val subst = formula.substitution(t, x, t.variables())
      subst match {
        case None =>
          (formula, None)
        case Some(sub) =>
          sub
      }
    }

    def substitute(y: P_VarName, x: P_VarName): (P_Formula, Option[Error]) =
      formula.substitute(P_TermVariable(y), x)

  }

}
