val longestLine = lines.reduceLeft(
    (a, b) => if (a.length > b.length) a else b 
) 

val maxWidth = widthOfLength(longestLine)

for (line <- lines) {
    val numSpaces = maxWidth - widthOfLength(line)
    val padding = " " * numSpaces
    println(padding + line.length + " | " + line)
}
