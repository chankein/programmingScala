import org.stairwaybook.layout.Element
import Element.elem


object Misc {

  sealed abstract class Expr
  case class Var(name: String) extends Expr
  case class Number(num: Double) extends Expr
  case class UnOp(operator: String, arg: Expr) extends Expr
  case class BinOp(operator: String, 
      left: Expr, right: Expr) extends Expr

  def checkbinary(expr: Expr): Unit = {
    expr match {
      case BinOp(op, left, right) =>
        println(s"$expr is a binary operation")
      case _ =>
    }
  }

  def checkbinary1(expr: Expr): Unit = {
    expr match {
      case BinOp(op, left, right) =>
        println(s"$expr is a binary operation")
      case _ => // handle the default case
    }
  }

  def checkbinary2(expr: Expr): Unit = {
    expr match {
      case BinOp(_, _, _) => println(s"$expr is a binary operation")
      case _ => println("It's something else")
    }
  }

  def describe(x: Any) = x match {
    case 5 => "five"
    case true => "truth"
    case "hello" => "hi!"
    case Nil => "the empty list"
    case _ => "something else"
  }

  def f() = 10

  def deepmatch(expr: Expr) = {
    expr match {
      case BinOp("+", e, Number(0)) => println("a deep match")
      case _ =>
    }
  }

  def startsWithZero(expr: Any) = {
    expr match {
      case 0 => "zero"
      case somethingElse => "not zero: " + somethingElse
    }
  }

  def startsWithZero1(expr: List[Int]) = 
    expr match {
      case List(0, _, _) => println("found it")
      case _ =>
    }

  def startsWithZero2(expr: List[Int]) =
    expr match {
      case List(0, _*) => println("found it")
      case _ =>
    }

  object OtherDescribe {
    def describe(e: Expr): String = (e: @unchecked) match {
      case Number(_) => "a number"
      case Var(_)    => "a variable"
    }
  }

  def isInstance(expr: Any) = {
    if (
      expr.isInstanceOf[String]
    ) {
    val s = 
      expr.asInstanceOf[String]
      s.length
    } else 0
  }

  def matchUnOp(expr: Expr) = {
    expr match {
      case UnOp("abs", e @ UnOp("abs", _)) => e
      case _ =>
    }
  }

  def main(args: Array[String]): Unit = {
    val varExpr = new Var("var")
    val zeroExpr = new Number(0)
    val binopExpr = new BinOp("=", varExpr, varExpr)
    val unopExpr = new UnOp("abs", new Number(3))

    checkbinary(varExpr)
    checkbinary(binopExpr)

    checkbinary1(varExpr)
    checkbinary1(binopExpr)

    checkbinary2(varExpr)
    checkbinary2(binopExpr)

    println("describe(new Number(4)) [" + describe(new Number(4)) + "]")
    println("describe(varExpr) [" + describe(varExpr) + "]")
    println("OtherDescribe.describe(new Number(4)) [" +
             OtherDescribe.describe(new Number(4)) + "]")
    println("OtherDescribe.describe(varExpr) [" +
             OtherDescribe.describe(varExpr) + "]")
    println("f [" + f + "]")
    println("startsWithZero(0) [" + startsWithZero(0) + "]")
    println("startsWithZero(\"0\") [" + startsWithZero("0") + "]")
    deepmatch(zeroExpr)
    deepmatch(new BinOp("+", varExpr, zeroExpr))
    startsWithZero1(List(0, 1, 2))
    startsWithZero1(List(0, 1))
    startsWithZero1(List(1, 0))
    startsWithZero2(List(0, 1, 2))
    startsWithZero2(List(0, 1))
    startsWithZero2(List(1, 0))
    println("isInstance(\"foo\") [" + isInstance("foo") + "]")
    println("matchUnOp(unopExpr) [" + matchUnOp(unopExpr) + "]")
    println("matchUnOp(new UnOp(\"abs\", unopExpr)) [" + matchUnOp(new UnOp("abs", unopExpr)) + "]")
  }
}