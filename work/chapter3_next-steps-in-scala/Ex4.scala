import scala.io.Source

val lines = Source.fromFile(args(0)).getLines().toList
println(args(0))
def widthOfLength(s: String) = s.length.toString.length

var maxWidth = 0
for (line <- lines)
    maxWidth = maxWidth.max(widthOfLength(line))
    println(maxWidth)
//