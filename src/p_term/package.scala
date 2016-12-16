/**
  * Created by EdSnow on 12/3/2016.
  */


package object p_term {

  val constantMap =  collection.mutable.Map[String, P_Constant]()
  def lookupConstant(s: String) = constantMap.getOrElseUpdate(s, new P_Constant(s))

  val varNameMap =  collection.mutable.Map[String, P_VarName]()
  def lookupVarName(s: String) = varNameMap.getOrElseUpdate(s, new P_VarName(s))

  val functionMap =  collection.mutable.Map[String, P_Function]()
  def lookupFunction(s: String) = functionMap.getOrElseUpdate(s, new P_Function(s))

}
