import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
object Element {

  private class ArrayElement(
    val contents: Array[String]
  ) extends Element

  private class LineElement(s: String) extends Element {
    val contents = Array(s)
    override def width = s.length
    override def height = 1
  }

  private class UniformElement(
    ch: Char,
    override val width: Int,
    override val height: Int
  ) extends Element {
    private val line = ch.toString * width
    def contents = Array.fill(height)(line)
  }

  def elem(contents:  Array[String]): Element =
    new ArrayElement(contents)

  def elem(chr: Char, width: Int, height: Int): Element =
    new UniformElement(chr, width, height)

  def elem(line: String): Element =
    new LineElement(line)
}
import Element.elem

class ElementSpec extends AnyFlatSpec with Matchers {

    "A UniformElement" should
        "have a width equal to the passed value" in {
        val ele = elem('x', 2, 3)
        ele.width should be (2)
    }

    it should "have a height equal to the passed value" in {
        val ele = elem('x', 2, 3)
        ele.height should be (3)
    }

    it should "throw an IAE if passed a negative width" in {
        an [IllegalArgumentException] should be thrownBy {
        elem('x', -2, 3)
        }
    }
}
