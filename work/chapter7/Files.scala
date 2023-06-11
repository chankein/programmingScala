val filesHere = (new java.io.File(".")).listFiles

for (file <- filesHere)
    println(file)


// In file built-in-control-structures/Files.scala

  // Not common in Scala...
  for (i <- 0 to filesHere.length - 1)
    println(filesHere(i))


// In file built-in-control-structures/Files.scala

  val filesHere = (new java.io.File(".")).listFiles

  for (file <- filesHere if file.getName.endsWith(".scala")) // with if condition
    println(file)


// In file built-in-control-structures/Files.scala

  for (file <- filesHere)
    if (file.getName.endsWith(".scala")) //simplefy
      println(file)


// In file built-in-control-structures/Files.scala

  for (
    file <- filesHere
    if file.isFile
    if file.getName.endsWith(".scala")
  ) println(file)


// In file built-in-control-structures/Files.scala

def fileLines(file: java.io.File) = 
    scala.io.Source.fromFile(file).getLines().toArray

def grep(pattern: String) =
    for (
      file <- filesHere
        if file.getName.endsWith(".scala");
            line <- fileLines(file)
            if line.trim.matches(pattern) 
    ) println(s"file: {line.trim}")

grep(".*gcd.*")


// In file built-in-control-structures/Files.scala

def grep(pattern: String) =
    for {
      file <- filesHere
      if file.getName.endsWith(".scala")
      line <- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(pattern)  
    } println(s"file: trimmed")

grep(".*gcd.*")


// In file built-in-control-structures/Files.scala

def scalaFiles =
    for {
      file <- filesHere
      if file.getName.endsWith(".scala")
    } yield file


for clauses yield body


for (file <- filesHere if file.getName.endsWith(".scala")) {
    yield file  // Syntax error!
  }


// In file built-in-control-structures/Files.scala

val forLineLengths =
    for {
      file <- filesHere
      if file.getName.endsWith(".scala")
      line <- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(".*for.*")  
    } yield trimmed.length
