package untangle


/**
  * Created by EdSnow on 8/19/2016.
  */


/*

abstract class TermX
{

  def glom: Seq[TermVar]

  def crawl(in: Seq[TermVar]): Seq[TermVar]

  def examine(): (TermVar, Option[TermX])

}

class TermSum(val left: TermX, val right: TermX) extends TermX
{

  def glom = left.glom ++ right.glom

  def crawl(in: Seq[TermVar]) = left.crawl(right.crawl(in))

  def examine() =
  {
    //  Case 1a: (min + a') + b = min + (a' + b)
    //      by associativity
    //  Case 1b: min + b = min + b, when a' is empty
    //  Case 2: a + (min + b') = min + (a + b')
    //      by:
    //          a + (min + b') = a + (min + b')
    //                         = (a + min) + b'   [assoc.]
    //                         = (min + a) + b'   [comm.]
    //                         = min + (a + b')   [assoc.]
    //      or:
    //                         = a + (min + b')
    //                         = (min + b') + a   [comm.]
    //                         = min + (b' + a)   [assoc.]
    //                         = min + (a + b')   [comm.]
    //  Case 2b: min + a = min + a, when b' is empty
    val leftExamined = left.examine()
    val rightExamined = right.examine()
    if (leftExamined._1 <= rightExamined._1)
    {
      val min = leftExamined._1
      val newLeftExamined =
        rightExamined match {
        Some(x) => (min, new TermSum(leftExamined, rightExamined)
        None => (min, rightExamined))
        }
    }
  }
  new TermExamined

  override
  def toString = "( " + left.toString + " + " + right.toString + " )"

}

class TermVar(val v: String) extends TermX
{

  def glom = Seq(this)

  def crawl(in: Seq[TermVar]) = this +: in

  def examine = (this, None)

  override
  def toString = v

}


class TermExamined(var min: TermVar, var t: TermX)

*/


import p_term._


class Untangler[T] {

  type Pairing = (Option[P_Term], Option[P_Term])

  def addToString(a: P_Applicand) =
    "( " + a.terms(0).toString + " + " + a.terms(1).toString + " )"

//  val functionAdd = new Function("+")(addToString)
  val functionAdd = new P_Function("+")

  def newSum(x: P_Term, y: P_Term) =
  {
    val a = new P_Applicand(Array[P_Term](x, y))
    new P_TermApplication(functionAdd, a)
  }

  def swozzle(zzz: P_TermApplication): Pairing =
  {

    def comb(t: P_Term): Pairing =
      t match {
        case t: P_TermApplication => swozzle(t)
        case t: P_TermVariable => (Some(t), None)
        case _ => (None, None)
      }

    def wunk(x: Option[P_Term], y: Option[P_Term]): Option[P_Term] =
      x match {
        case Some(tx) =>
          y match {
            case Some(ty) => Some(newSum(tx, ty))
            case _ => x
          }
        case _ => y
      }

    val (x_left, x_right) = comb(zzz.a.terms(0))
    val (y_left, y_right) = comb(zzz.a.terms(1))
    val xx = wunk(x_left,  y_left )
    val yy = wunk(x_right, y_right)
    (xx, yy)

  }

  def rectify(zzz: P_Term) =
    zzz match {
      case zzz: P_TermApplication =>
        val z = swozzle(zzz)
        z match {
          case (Some(x), Some(y)) => newSum(x, y)
          case _ => (None, None)
        }
      case zzz: P_TermVariable => zzz
      case _ => (None, None)
  }

  def goforit =
  {
    val var_a = new P_VarName("a")
    val var_b = new P_VarName("b")
    val var_c = new P_VarName("c")
    val var_d = new P_VarName("d")
    val var_e = new P_VarName("e")
    val var_f = new P_VarName("f")
    val v_a = new P_TermVariable(var_a)
    val v_b = new P_TermVariable(var_b)
    val v_c = new P_TermVariable(var_c)
    val v_d = new P_TermVariable(var_d)
    val v_e = new P_TermVariable(var_e)
    val v_f = new P_TermVariable(var_f)
    val sum = newSum(newSum(v_a, v_b), newSum(v_c, v_d))
    println(sum.toString)
  }

}
