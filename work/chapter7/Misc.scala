// 7.1 If expressions
var filename = "default.txt"
  if (!args.isEmpty)
    filename = args(0)

val filename2 =
    if (!args.isEmpty) args(0)
    else "default.txt"

println(if (!args.isEmpty) args(0) else "default.txt")


// 7.2 While loops
def gcdLoop(x: Long, y: Long): Long = {
    var a = x
    var b = y
    while (a != 0) {
      val temp = a
      a = b % a
      b = temp
    }
    b
}
  var line = ""
  do {
    line = readLine()
    println("Read: " + line)
  } while (line != "")


def greet() = { println("hi") }

val whatAmI = greet()


var line = ""
  while ((line = readLine()) != "") // This doesn't work!
    println("Read: " + line)

def gcd(x: Long, y: Long): Long =
    if (y == 0) x else gcd(y, x % y) 