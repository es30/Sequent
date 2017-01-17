/**
  * Created by EdSnow on 8/29/2016.
  */


package foundationalInferenceRule

import error.Error
import p_term._
import p_formula.P_Formula
import p_axiomatization._
import p_sequent.Cedent

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
      case None         => None
    }

}

object   zz_Error_ante extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_ante(c, f))
object   zz_Error_succ extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_succ(c, f))
object zz_Error_L_ante extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_L_ante(c, f))
object zz_Error_L_succ extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_L_succ(c, f))
object zz_Error_R_ante extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_R_ante(c, f))
object zz_Error_R_succ extends zz_ErrorCedent((c:Cedent,f:P_Formula)=>new Error_R_succ(c, f))


abstract class Error_occurs(
    val s: Set[P_Formula],
    val v: P_VarName
) extends Error

class Error_occurs_ante(s: Set[P_Formula], v: P_VarName) extends Error_occurs(s, v)
class Error_occurs_succ(s: Set[P_Formula], v: P_VarName) extends Error_occurs(s, v)

abstract class zz_Error_occurs(
    val fx : (Set[P_Formula], P_VarName) => Error_occurs
) {

  def apply(x: Option[(Set[P_Formula], P_VarName)]) : Option[Error_occurs] =
    x match {
      case Some((s, v)) => Some(fx(s, v))
      case None         => None
    }

}

object zz_Error_occurs_ante extends zz_Error_occurs((s: Set[P_Formula], v: P_VarName) => new Error_occurs_ante(s, v))
object zz_Error_occurs_succ extends zz_Error_occurs((s: Set[P_Formula], v: P_VarName) => new Error_occurs_succ(s, v))


class Error_axiom(
    val c: Cedent,
    val f_axiom: P_SFormulaAxiomatization
) extends Error

/*
  def zz_Error_axiom(x: Option[(Cedent,Formula)]) : OError =
    x match {
      case Some((c, f_axiom)) => Some(new Error_axiom(c, f_axiom))
      case None => None
    }
*/


class Error_subsm(
    val f: P_SFormulaAxiomatization,
    val f_subsm: P_Formula
) extends Error

/*
  def zz_Error_subsm(x: Option[(Formula,Formula)]) : OError =
    x match {
      case Some((f, f_subsm)) => Some(new Error_subsm(f, f_subsm))
      case None => None
    }
*/
