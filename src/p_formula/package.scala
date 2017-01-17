/**
  * Created by EdSnow on 8/20/2016.
  */


package object p_formula {


  import scala.util.control.TailCalls._

  import error._
  import formula._

  implicit def P_SFormulaWrapper(pf: P_SFormula): SFormula =
    pf match {
      case f: P_Formula => f
      case f: P_SOnlyFormula => f
    }

  val noError: Option[Error] = None

  trait P_FormulaDyadic {
    val kind: DyadicKind.Kind
    val f1: P_Formula
    val f2: P_Formula
    val connective: String
    def replace(f1: P_Formula, f2: P_Formula): P_Formula

    lazy val bumper = " " + connective + " "

    def formulaString() = "(" + f1.formulaString + bumper + f2.formulaString + ")"

    final
    def equals(stack: List[(P_Formula, P_Formula)], obj: Any):
        TailRec[Boolean] =
      obj match {
        case f: P_FormulaDyadic if kind == f.kind =>
          tailcall(f1.equals((f2, f.f2) :: stack, f.f1))
        case _ =>
          tailcall(equals_continue(stack, false))
      }

    final
    def hashCode(stack: List[P_Formula], hash: Int): TailRec[Int] =
      tailcall(f1.hashCode(f2 :: stack, hash + kind.hashCode()))

  }

  def equals_continue(stack: List[(P_Formula, P_Formula)], retval: Boolean):
      TailRec[Boolean] =
    if (!retval || stack.isEmpty)
      done(retval)
    else {
      val (subj, obj) = stack.head
      tailcall(subj.equals(stack.tail, obj))
    }

  def hashCode_continue(stack: List[P_Formula], retval: Int):
      TailRec[Int] =
    if (stack.isEmpty)
      done(retval)
    else {
      val f = stack.head
      tailcall(f.hashCode(stack.tail, retval))
    }


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
  def lookupPredicate(s: String, p: P_Predicate) = predicateMap.getOrElseUpdate(s, p)


}
