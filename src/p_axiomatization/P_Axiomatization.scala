package p_axiomatization

/**
  * Created by EdSnow on 12/3/2016.
  */


import p_formula._


abstract class P_Axiomatization(name: String)
      extends P_SFormula with POrAName {

  def register(s: String) = pOrANameMap.update(s, this)

  def checkSubsumption(f: P_Formula): Option[(P_Axiomatization, P_Formula)] =
    if (subsumes(f))
      None
    else
      Some((this, f))

  def subsumes(f: P_Formula): Boolean

  def formulaString: String = name

  override
  def toString = "     : " + formulaString

  final override
  def equals(obj: Any) =
    obj match {
      case a: P_Axiomatization => this == a
      case _ => false
    }

  final override
  def hashCode(): Int = name.hashCode()

}
