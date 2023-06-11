object Ex6 {
  import java.util.regex
  
  class AStarB {
    // Accesses java.util.regex.Pattern
    val pat = regex.Pattern.compile("a*b")
  }

  def main(args: Array[String]): Unit = {
    println("(new AStarB).pat [" + (new AStarB).pat + "]")
  }
}