var i = 0
var foundIt = false

while (i < args.length && !foundIt) {
  println(args(i))
    if (!args(i).startsWith("-")) {
      if (args(i).endsWith(".scala"))
        foundIt = true
        println(".foundIt")
    }
    i = i + 1
}

println("end")
