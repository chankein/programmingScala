// In file built-in-control-structures/Breakless1.scala
var i = 0
var foundIt = false

  while (i < args.length && !foundIt) {
    if (!args(i).startsWith("-")) {
      if (args(i).endsWith(".scala"))
        foundIt = true
    }
    i = i + 1
  }


// In file built-in-control-structures/Breakless2.scala

  def searchFrom(i: Int): Int =
    if (i >= args.length) -1
    else if (args(i).startsWith("-")) searchFrom(i + 1) 
    else if (args(i).endsWith(".scala")) i
    else searchFrom(i + 1)

  val i = searchFrom(0)


  import scala.util.control.Breaks._
  import java.io._

  val in = new BufferedReader(new InputStreamReader(System.in))

  breakable {
    while (true) {
      println("? ")
      if (in.readLine() == "") break
    }
  }

