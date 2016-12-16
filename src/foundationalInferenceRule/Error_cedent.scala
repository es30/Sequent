/**
  * Created by EdSnow on 8/29/2016.
  */


package foundationalInferenceRule

import error.Error
import p_formula.P_Formula
import p_sequent.Cedent
import p_term.P_TermVariable
import p_axiomatization._

abstract class ErrorCedent(
    val c: Cedent,
    val f: P_Formula
) extends Error

class   Error_ante(c: Cedent, f: P_Formula) extends ErrorCedent(c, f)
class   Error_succ(c: Cedent, f: P_Formula) extends ErrorCedent(c, f)
class Error_L_ante(c: Cedent, f: P_Formula) extends ErrorCedent(c, f)
class Error_L_succ(c: Cedent, f: P_Formula) extends ErrorCedent(c, f)
class Error_R_ante(c: Cedent, f: P_Formula) extends ErrorCedent(c, f)
class Error_R_succ(c: Cedent, f: P_Formula) extends ErrorCedent(c, f)

abstract class zz_ErrorCedent(
    val fx : (Cedent, P_Formula) => ErrorCedent
) {

  def apply(x: Option[(Cedent,P_Formula)]) : Option[ErrorCedent] =
    x match {
      case Some((c, f)) => Some(fx(c, f))
      case None => None
    }

}

object   zz_Error_ante extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_ante(c, f))
object   zz_Error_succ extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_succ(c, f))
object zz_Error_L_ante extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_L_ante(c, f))
object zz_Error_L_succ extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_L_succ(c, f))
object zz_Error_R_ante extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_R_ante(c, f))
object zz_Error_R_succ extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_R_succ(c, f))


abstract class Error_occurs(
    val c: Cedent,
    val v: P_TermVariable
) extends Error

class Error_occurs_ante(c: Cedent, v: P_TermVariable) extends Error_occurs(c, v)
class Error_occurs_succ(c: Cedent, v: P_TermVariable) extends Error_occurs(c, v)

abstract class zz_Error_occurs(
    val fx : (Cedent, P_TermVariable) => Error_occurs
) {

  def apply(x: Option[(Cedent,P_TermVariable)]) : Option[Error_occurs] =
    x match {
      case Some((c, v)) => Some(fx(c, v))
      case None => None
    }
}

object zz_Error_occurs_ante extends zz_Error_occurs((c:Cedent,v:P_TermVariable)=>new Error_occurs_ante(c, v))
object zz_Error_occurs_succ extends zz_Error_occurs((c:Cedent,v:P_TermVariable)=>new Error_occurs_succ(c, v))


class Error_subst(
    val f: P_Formula,
    val f_subst: P_Formula
) extends Error

/*
  def zz_Error_subst(x: Option[(Formula,Formula)]) : OError =
    x match {
      case Some((f, f_subst)) => Some(new Error_subst(f, f_subst))
      case None => None
    }
*/


class Error_axiom(
    val c: Cedent,
    val f_axiom: P_Axiomatization
) extends Error

/*
  def zz_Error_axiom(x: Option[(Cedent,Formula)]) : OError =
    x match {
      case Some((c, f_axiom)) => Some(new Error_axiom(c, f_axiom))
      case None => None
    }
*/


class Error_subsm(
    val f: P_Axiomatization,
    val f_subsm: P_Formula
) extends Error

/*
  def zz_Error_subsm(x: Option[(Formula,Formula)]) : OError =
    x match {
      case Some((f, f_subsm)) => Some(new Error_subsm(f, f_subsm))
      case None => None
    }
*/
