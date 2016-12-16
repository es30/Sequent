/**
  * Created by EdSnow on 8/20/2016.
  */


package object p_formula {


  import error._
  import p_term._


  val noError: Option[Error] = None

  val failedSubstitutedVariable = new P_VarName("Derp!")


  trait POrAName

  val pOrANameMap =  collection.mutable.Map[String, POrAName]()

  //  This is only called from Java code (The ANTLR-generated parser in particular),
  //  hence the use of null as a return value.
  def lookupProposition(s: String): P_Proposition = {
    val entry = pOrANameMap.getOrElseUpdate(s, new P_Proposition(s))
    entry match {
      case p: P_Proposition => p
      case _ => null
    }
  }

  val predicateMap =  collection.mutable.Map[String, P_Predicate]()
  def lookupPredicate(s: String) = predicateMap.getOrElseUpdate(s, new P_Predicate(s))


}
