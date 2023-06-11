
// In file built-in-control-structures/Exceptions.scala

throw new IllegalArgumentException


// In file built-in-control-structures/Exceptions.scala

val half =
    if (n % 2 == 0)
      n / 2
    else
      throw new RuntimeException("n must be even")


// In file built-in-control-structures/Exceptions.scala

import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException

  try {
    val f = new FileReader("input.txt")
    // Use and close file
  } catch {
    case ex: FileNotFoundException => // Handle missing file
    case ex: IOException => // Handle other I/O error
  }


// In file built-in-control-structures/Exceptions.scala

import java.io.FileReader

val file = new FileReader("input.txt")
try {
// Use the file
} finally {
    file.close()  // Be sure to close the file
}


// In file built-in-control-structures/Exceptions.scala

import java.net.URL
import java.net.MalformedURLException

def urlFor(path: String) =
    try {
      new URL(path)
    } catch {
      case e: MalformedURLException =>
        new URL("http://www.scala-lang.org")
    }


// In file built-in-control-structures/Exceptions.scala

def f(): Int = try return 1 finally return 2


// In file built-in-control-structures/Exceptions.scala

def g(): Int = try 1 finally 2