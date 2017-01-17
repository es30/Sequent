/**
  * Created by EdSnow on 12/3/2016.
  */


package object p_term {


  val constantMap = collection.mutable.Map[String, P_Constant]()
  def lookupConstant(s: String) = constantMap.getOrElseUpdate(s, new P_Constant(s))

  val  varNameMap = collection.mutable.Map[String, P_VarName ]()
  def lookupVarName (s: String) =  varNameMap.getOrElseUpdate(s, new P_VarName (s))

  val functionMap = collection.mutable.Map[String, P_Function]()
  def lookupFunction(s: String) = functionMap.getOrElseUpdate(s, new P_Function(s))
  def lookupFunction(s: String, f: P_Function) = functionMap.getOrElseUpdate(s, f)


  import scala.util.control.TailCalls._


  def equals_1(
      stack: List[(Iterator[P_Term], Iterator[P_Term])]
    ): TailRec[Boolean] = {
    val (subjs, objs) = stack.head
    if (subjs.hasNext)
      tailcall(subjs.next().equals(stack, objs.next()))
    else
      tailcall(equals_continue(stack.tail, true))
  }

  def equals_continue(
      stack: List[(Iterator[P_Term], Iterator[P_Term])],
      retval: Boolean
    ): TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else
      tailcall(equals_1(stack))

  def hashCode_1(
      stack: List[Iterator[P_Term]],
      hash: Int
    ): TailRec[Int] = {
    val subjs = stack.head
    if (subjs.hasNext)
      tailcall(subjs.next().hashCode(stack, hash))
    else
      tailcall(hashCode_continue(stack.tail, hash))
  }

  def hashCode_continue(
      stack: List[Iterator[P_Term]],
      retval: Int
    ): TailRec[Int] =
    if (stack.isEmpty)
      done(retval)
    else
      tailcall(hashCode_1(stack, retval))


}
