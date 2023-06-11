// stripMarginとraw文字列の使い方
{
  val s = """|aaaaaa
             |bbbbb""".stripMargin
  println(s)
  s.length
}

// symbolリテラル
{
  val a = 'aaaa
  a.name
  Symbol("p q r")
}

// stringの整形
{

  val a = "aaaa"
  val s = "ssss"
  s"${a}is $s"
}

// raw literal
{
  raw"asdf¥n¥n\n"
  // format
  f"${math.Pi}%.5f"
}

// 引数なしの関数は()があってもなくてもいい
{
  "AAA".toLowerCase()
  "AAA".toLowerCase
}

// &&は遅延評価, &は遅延しない
{
  false && true
  false & true
}

// リテラル識別子と単項演算子
// リテラル演算子は予約語をメソッド名にするためにつかう
// unary_から始まるメソッドは単項演算子になる。
{

  class Summer(val month: Int){
    require(month > 5 && month < 10)
    def `this` = month // literal identifier
    def unary_- = -month // あとで直す
  }

  val summer = new Summer(8)
  summer.`this` //予約後を使いたいときはliteral identifier
  -summer // -8
}

// コンストラクタにvalをつけるとclass fieldになり、public変数扱いになる
{
  class Summer(val month: Int)
  val summer = new Summer(8)
  summer.month
  class Summer2(month: Int)
  val summer2 = new Summer2(8)
  // println(summer2.month) --> Error
}

// Unit
{
  def greet = println("hello")
  () // Unit
  () == greet // true
  var b = 1
  (b=2)==() // true
}

// for式のbind
{
  for{
    i <- 1 to 3
    j <- 1 to 3
    a = i + j // bind
  } yield a
}

// iterator
{
  val it = Iterator("a", "b", "c")
  it.next()
  it.next()
  it.next()

}

// try
{
  class Summer(val month: Int) {
    require(month > 5 && month < 10)
  }

  try{
    new Summer(11)
  } catch{
    case ex: IllegalArgumentException => println("illegal month")
  } finally {
    println("final clause")
  }
}

// break
{
  import scala.util.control.Breaks._
  var i = 1
  breakable {
    while (i < 10) {
      println(i)
      i = i + 1
      break
      // Scalaにcontinueはない
    }
  }
}

// スコープ
{
  var j = 1
  // 括弧の中から外は見える
  for(i <- 1 to 2) yield { i + j }

  val jj = 1
  for(i <- 1 to 2) yield {
    val jj = 2 // 括弧の中で同じ名前の変数を作れる
    i + jj
  }

  def foo: Unit ={
    val a = 1
    def goo = {
      // 括弧の外は見える原則でlocal functionの外側の変数が見える
      println(a + 1)
    }
    goo
  }
  foo
}

// foreach
// Unitを返すので副作用があることにしか使えない
{
  val l = List(1,2,3,4)
  l.foreach(println)

  // place folder
  val f = (_: Int)+ (_: Int)
  f(1,2)
}

// 部分適用としてのplace holder
{
  def f (a: Int, b:Int, c:Int) = a + b + c
  val f1: (Int, Int, Int) => Int = f
  // f1 = f　とは書けない.
  val f2 = f _
  val fpartial = f(1, _: Int, _: Int)
  fpartial(1,2)
}

// 全ての引数を省略する場合のplace holder
{
  def increment(x : Int) = x + 1
  List(1,2,3) map(increment)

  def sum(b: Int, a:Int) = b + a
  List(1,2,3).foldLeft(0)(sum _) // 複数引数を全て省略
  List(1,2,3).foldLeft(0)(sum)   // 複数引数を全て省略する場合は_も不要
}

// closure
{
  var more = 3

  // この式単体ではopen termなので、moreを事前に宣言してmoreをbindする必要がある。
  // bindした関数はclosureと呼ばれる。(free variableの値を取り込むため)
  val a = (x: Int) => x + more
  a(2) // --> 5
  more = 4
  a(2) // --> 6 (副作用!)
}

// 連続パラメータ (可変長引数)
{
  def foo(a: Int*) = a.sum
  foo(1,2,3,4)
  val arr = List(1,2,3,4)
  foo(arr: _*)
}

// 名前付き引数
{
  def foo(a: Int, b: Int) = a
  foo(a = 2, b = 3)
  foo(b = 3, a = 2)
}

// デフォルト仮引数
{
  def foo(a: Int, b: Int = 10) = a + b
  foo(1, 2)
  foo(1)
}

// 末尾回帰
{
  // コンパイラは末尾回帰を見つけると、whileループと同じバイトコードを生成する。
  // なので回帰は積極的に使うべきだが、必ず回帰関数を末尾回帰に変換するべきである。
  // でなければstack overflowを起こしてしまう。
  def foo(acc: Int, a: Int): Int = {
    if(a == 0) acc
    else foo(acc + a, a - 1)
  }
  foo(0, 3)
}

// カリー化
{
  def foo(x: Int)(y: Int) = x + y
  val goo = foo(1) _ // place holderが必要
  goo(3)
}

// foo{ }
// 引数が一つの時は()ではなく{}を使える
{
  def foo(x: Int) = x + 1
  foo{1}
  // カリー化と組み合わせることで、制御構造に「見える」関数を作ることが出来る。
  def myIf[T](cond: Boolean)(exec: =>T) = if(cond) exec else println("Hello")

  myIf(1 == 1) {
    val a = 1
    val b = a * 2
    a + b
  } // --> 3
  myIf(1 == 2) {12} // --> Hello
  myIf(1==2) {throw new IllegalStateException()} // --> Hello
}

// ドットと()の省略
{
  object Foo {
    def goo(x: Int) = x + 1
  }
  // レシーバー（methodを呼び出されるオブジェクト）が指定されている場合. (ドット)や()は省略できる
  Foo goo 3
  // 次の式はレシーバーが指定されていないので()は省略できない
  def zoo(x: Int) = x * 2
  // zoo 3 --> Error
}

// class継承と合成の例
{
  abstract class Element {
    def contents: Array[String]
    def height: Int = contents.length
    def width: Int = if (height == 0) 0 else contents(0).length
    def above(that: Element) = new ArrayElement(this.contents ++ that.contents)
    def beside(that: Element): Element = new ArrayElement(
      for((line1, line2) <- this.contents zip that.contents) yield line1 + line2
    )
    override def toString = contents mkString "\n"
  }

  // defをvalでoverrideすることができる。
  // super.contentsはabstractなので下のoverrideは省略できる (overrideというより実装なので)。
  class ArrayElement(override val contents: Array[String]) extends Element

  // スーパークラスのコンストラクタを呼んで継承
  class LineElement1(s: String) extends ArrayElement(Array(s)) {
    override def width = s.length
    override def height = 1
  }

  // 継承するより合成した方がいい
  class LineElement(s: String) extends Element {
    val contents = Array(s)
    override def width = s.length
    override def height = 1
  }
  class UniformElement(
                      ch: Char,
                      override val width: Int,
                      override val height: Int
                      ) extends Element {
    private val line = ch.toString * width
    def contents = Array.fill(height)(line)
  }

  // factory methodを定義
  object Element {
    def elem(contents: Array[String]): Element = new ArrayElement(contents)
    def elem(chr: Char, width: Int, height: Int): Element = new UniformElement(chr, width, height)
    def elem(line: String): Element = new LineElement(line)
  }

  // factory methodを使うためにimportする。これでユーザーはElementの実装を気にする必要は無くなる。
  // elemだけ使えればユーザーには十分
  import Element.elem
  elem("abcdefg").toString
}

// fillの使い方
Array.fill(3)('a')

// equalsは内容を比較する。
List(1,2,3) equals List(1,2,3)  // --> true

// eqは参照等価性を比較する（メモリの同じ位置にあるか）
List(1,2,3) eq List(1,2,3)  // --> false

// neはeqの反対
List(1,2,3) ne List(1,2,3)  // --> false

// NullはAnyRefのサブクラスの底辺クラス
{
  val i: List[Int] = null

  // AnyValには入れられない
  // val i: Int = null --> Error
}

// 例外はNothingとして扱われる
{
  def error(message: String): Nothing = throw new RuntimeException(message)
  // NothingはIntのサブタイプなので次もOK
  def error1(message: String): Int =
    if(message == "abc") 3 else throw new RuntimeException(message)
  error1("abc")
}

// trait
{
  trait Philosopher {
    override def toString: String = "I consume memory, therefore I am."
  }

  class Frog extends Philosopher // 継承はextends

  val f = new Frog
  f
  val f1: Philosopher = new Frog // traitでは型も定義される
  f1
  // traitを単体でインスタンス化することはできない。通常
  // val f2 = new Philosopher  --> Error

  trait HasLegs
  class Animal
  // ミックスインはwith. withは必ずextendsと一緒に使う
  // class Dog with HasLegs with Philocopher --> Error
  class Dog extends Animal with Philosopher with HasLegs
  val d = new Dog
  d
}

// traitとsuper
{
  abstract class IntQueue {
    def get: Int
    def put(x: Int)
  }

  import scala.collection.mutable.ArrayBuffer
  class BasicIntQueue extends IntQueue {
    private val buf = new ArrayBuffer[Int]()
    def get = buf.remove(0)
    def put(x: Int) = {buf += x}
  }

  val q = new BasicIntQueue
  q.put(0)
  q.put(1)
  q.put(2)
  q.get

  trait Doubling extends IntQueue {
    abstract override def put(x: Int) = {super.put(x * 2)}
  }

  trait Incrementing extends IntQueue {
    abstract override def put(x: Int) = {super.put(x + 1)}
  }

  trait Filtering extends IntQueue {
    abstract override def put(x: Int) = {
      if(x >= 0) super.put(x)
    }
  }

  // 呼び出し順序は MyQueue -> Filtering -> Incrementing -> Doubling -> BasicIntQueue
  class MyQueue extends BasicIntQueue with Doubling with Incrementing with Filtering

  val mq = new MyQueue
  mq.put(-1)
  mq.put(1)
  mq.put(2)
  mq.get // --> 4
}


// ensuring
{
  def double(x: Int) = {
    2 * x
  }.ensuring(_ > 10)

  double(10)
  // double(1) --> AssertionError
}

// copy
{
  case class C() { // case classはクラスパラメータが必ず必要なので()は略せない
    val a = 1
    val b = 2
    val c = 3
  }
  val x = C()
  val y = x.copy()
  y.a

  case class C1(a: Int, b:Int, c: Int) // こっちの方がよい
  val xx = C1(1,2,3)
  val yy = xx.copy(a = 11)
  yy.a
}

// 定数パターンと変数パターン
// 大文字から始まるパターンは定数パターンになり、小文字から始まる変数は変数パターンになる。
{
  val MyEpsilon = 0.001
  0.001 match {
    case MyEpsilon => "small"
    case _     => "big"
  } // --> small

  1 match {
    case myEpsilon => "small"
    case _         => "big"
  } // --> small

  // 小文字から始まる変数を定数パターンとしてマッチさせるときはliteral識別子を使う
  val one = 1
  2 match {
    case `one` => "ONE"
    case _     => "BIG"
  } // --> BIG

  1 match {
    case `one` => "ONE"
    case _     => "BIG"
  } // --> ONE
}

// 任意長とのマッチ
{
  List(1,2,3,4,5) match {
    case List(1, e, _*) => e
    case _  => -1
  }
}

// 型テストとしてのmatch
{
  def foo(x: Any): Any = x match {
    case y: Int => y * 2
    case y: Double => y * 3
  }
  foo(1) // --> 2
  foo(1.0) // --> 3.0
}

// 型キャストとしてのパターンマッチ
{
  def foo(x: Any) = {
    // x.length --> Error. xはAny
    x match {
      case y: String => y.length // OK. yはString
      case y: Int => 1
    }
  }
  foo("abcde") // --> 5
}

// 明示的な型テストと型キャスト
// このコードはScalaでは推奨されていない.
{
  def foo(x: Any) = {
    if(x.isInstanceOf[String]) {
      val y = x.asInstanceOf[String]
      y.length
    } else 0
  }
  foo("abcde")
}

// 型消去
// Scalaは消去モデルを使っているので実行時に型引数の情報は保持されない
{
  def foo(x: Any) = x match {
    case _ : Map[Int, Int] => "String map"
    case _ => "other object"
  }
  foo(Map("a" -> 2)) // --> "String map"
}

// 型消去の例外
// Arrayだけは型消去の例外で、要素型は配列値と共に保持されている
{
   def foo(x: Any) = x match {
     case _: Array[Int] => "Int Array"
     case _: Array[String] => "String Array"
     case _ => "other object"
   }
  foo(Array(1,2,3)) // --> Int Array
  foo(Array("a", "b", "c")) // --> String Array
}

// variable-bindin pattern (@)
{
  List(List(1,2,3), List(11,22,33)) match {
    case List(e @ List(1,_ ,_ ), _) => e(1) // 右辺でeが使えるようになる
    case _ => -1
  }
}

// パターン変数はパターンの中で一度しか登場させられない
{
  List(1,1) match {
    // case List(x, x) => x --> Error.
    // pattern guardを使う
    case List(x, y) if x == y => x
    case _ => -1
  }
}

// sealed class
{
  sealed abstract class Base
  case class Child1() extends Base
  case class Child2() extends Base
  case class Child3() extends Base

  // sealedにするとBaseのサブクラスは同じファイル内に配置しなければいけないので、
  // コンパイラは全てのサブクラスを把握できる.
  // 従ってmatchでケース漏れしていることをコンパイラは教えてくれる.
  def foo(x: Base) = x match {
    case _: Child1 => 1
    // case漏れ。"match may not be exhaustive"
    case _: Child3 => 3
  }
  foo(Child3())

  // @uncheckedアノーテーション
  // コンパイラにcase漏れの警告を出させないためのannotation
  def goo(x: Base) = (x: @ unchecked) match {
    case _: Child1 => 1
    // case漏れだけど警告は出ない
    case _: Child3 => 3
  }
}

// 変数定義におけるパターンマッチ
{
  val List(a, b, _*) = List(1,2,3,4,5)
  a
}

// 関数リテラルにおけるパターンマッチ
{
  def foo: Double => Double  = {
    case y if y > 0 => 1.0
    case y if y < 0 => 0.0
    case y if y == 0 => 0.5
  }
  foo(-1)
}

// ケースによる部分関数の作成
// 関数リテラルによるパターンマッチは部分関数を作成する
{
  def foo: PartialFunction[Double, Double] = {
    case x if x > 0 => 1.0
    case x if x <0  => 0.0
  }
  // foo(0) --> Error
}

// for-comprehensionにおけるパターンマッチ
{
  val l = "apple"::"orange"::"pinepple"::Nil
  val loption = None::l.map(Option(_)):::(None::Nil)
  // マッチしなかったものは捨てられる
  for(Some(x) <- loption) println(x) // --> apple, orange, pineapple
}


// Listは共変
{
  val a: List[Int] = List(1,2,3)
  val b: List[Any] = a
  b(1) == a(1) // --> true
}

// Listの入れ子
{
  List(List(1,2,3), List(4,5,6)) ==
    (1::2::3::Nil) :: (4::5::6::Nil) :: Nil
  // --> true
}

// splitAt
{
  val l = List(1,2,3,4,5,6,7,8,9)
  l splitAt 5  equals (l take 5, l drop 5 )
  // --> true
}

// indices
{
  val l = List(1,2,3,4,5,6,7,8,9)
  l.indices == 0.to(8) // --> true
}

// zipWithIndex
{
  List("a", "b", "c") zipWithIndex
  // = List((a,0), (b,1), (c,2))
}

// mkString
{
  val s = List("a", "b", "c")
  s.mkString == "abc" // --> true
  s.mkString(",") == "a,b,c" // --> true
  s.mkString("[", ",", "]") == "[a,b,c]" // --> true
}

//StringBuilder
{
  val buff = new StringBuilder
  val s = List("a", "b", "c")
  s addString(buff) toString
}

// copyToArray
{
  val xs = Array(1,2,3,4,5)
  val ys = Array(11,22)
  ys copyToArray(xs, 2)
  xs // Array(1,2,11,22,5)
}

// foreach
// mapと違ってUnitなので副作用を目的とする
List(1,2,3) foreach(print)

// partition
{
  val xs = 1 to 10
  xs partition(_ % 2 == 0)
  // = (Vector(2, 4, 6, 8, 10),Vector(1, 3, 5, 7, 9))
}

// takeWhile, dropWhile
{
  val xs = 1 to 10
  xs takeWhile(_ < 5) // 1 to 4
  xs dropWhile(_ < 5) // 5 to 10
}

// span
// spanはtakeWhileとdropWhileを同時に行うことと同じ
{
  val xs = List(1,2,3,-3,-2,-1,4,5,6)
  xs span(_ > 0)
  // = (List(1, 2, 3),List(-3, -2, -1, 4, 5, 6))
}

// /:, :\
// foldLeftとfoldRight
{
  val z = 1
  (z /: List(2,3,4)) ((a,b) => a + b)
  // op(op(op(z, a), b), c)

  (List(1,2,3) :\ List[Int]())(_ :: _)
  // op(a, op(b, op(c, z)))
}

// List.range
// 一方1 until nはArrayを作る
{
  List.range(1,10) // List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  List.range(1,10,3) // List(1,4,7)
}

// fill
{
  List.fill(4)('a') // = List(a, a, a, a)
  List.fill(2,3)('a') // 2 * 3の行列
}

// concat
{
  List.concat(List(1,2), List(3,4), List(5,6))
}

// zipped
{
  (List("a", "b"), List(11,22)) zipped
  // Tuple2Zipped[String,List[String],Int,List[Int]]
  // (List("a", "b"), List(11,22)).zipped.map(_ + _)
  // なぜかエラー
}

{
  List.tabulate(5)(n => n * n) //List(0,1,4,9,16)
  List.tabulate(10,10)(_*_) //九九
}

// ListBuffer
// headもlastも定数時間でアクセスできる
{
  import scala.collection.mutable.ListBuffer
  val buf = new ListBuffer[Int]
  buf += 1
  buf += 2
  3 +=: buf
  buf.toList // List(3,1,2)
}

// ArrayBuffer
{
  import scala.collection.mutable.ArrayBuffer
  val buf = new ArrayBuffer[Int]
  buf += 1
  buf += 2
  3 +=: buf
  buf.toArray
}

// TreeSet, TreeMap
// 赤黒木を使って実装されており、順序の情報を保持する
{
  import scala.collection.immutable.{TreeSet, TreeMap}
  val ts = TreeSet(9,3,2,4,1,5,6)
  ts // TreeSet(1, 2, 3, 4, 5, 6, 9)
  val tm = TreeMap(3 -> 33, 1->11, 4->44)
  tm
}

// valのcollectionとvarのcollectionの違い
// val x = immutable.Set()は+=を持たない
// var x = mutable.Set()は+=を持つ
{
  val x = scala.collection.immutable.Set[Int]()
  // x += 1   --> Error
  var y = scala.collection.mutable.Set[Int]()
  y += 1
  y // Set(1)
}

// 同時定義
{
  val a, b = 1
  b // 1
}

// varを持っていることは純粋関数型でないことは意味しない例
{
  case class sortedList(val l: Int*) {
    var cache: List[Int] = l.toList.sorted
    def values = cache
  }
  val s1 = sortedList(5,4,1,2)
  s1.values
}

// defaultを意味するアンダーバー
{
  class Time {
    var hour: Int = _ // defaultの0が入る
  }
  val t = new Time
  t.hour // 0
}

// setterとgetterを手動で定義する
{
  class MyTime {
    private[this] var h: Int = _ // defaultで初期化。Intの場合は0

    def hour: Int = h // getter
    def hour_= (n: Int): Unit = { //setter
      require(n >= 0 && n <= 23)
      h = n
    }
  }
  val mt = new MyTime
  mt.hour = 22
  mt.hour
}

// 情報隠蔽
// コンストラクタの隠蔽
{
  // クラスパラメータの前にprivateを書くとプライマリコンストラクタを呼べ出せなくなる
  class C private (val x: Int){
    def this(p: Int, q: Int) = this(p + q)
  }
  // val z = new C(5) --> Error
  val z = new C(5,5) // 補助コンストラクタは公開
  z.x // 10
}

{
  trait C {
    def head: Int
  }

  object C {
    private class CImpl extends C {
      def head: Int = 10
    }
  }
}

// 関数のサブ型、スーパー型
{
  class C[-T <: Int, +S >: Int]{ // -Tにしないとfooが反変にならないのでコンパイルエラー
    def foo(x: T) = x + 1
    def goo(x: T): S = x + 2
  }

  (new C) foo(3)
  (new C) goo(3)
}

// Ordered
{
  case class C(x: Int) extends Ordered[C] {
    override def compare(that: C) = this.x - that.x
  }
  val x = C(2)
  val y = C(3)
  x < y // true
}

// 無名クラス
{
  trait MyTrait {
    type T
    val x: T
  }

  // これはリファインメント型だと思った方が分かりやすいかも
  new MyTrait {
    type T = String
    val x = "John"
  }
  /*
  これは次のコードと同じ
  class C() extends MyTrait {
    type T = String
    val x = "John"
  }
  new C
  */
}

// valの初期化順序
// ClassやTraitはスーパークラスから初期化される
{
  abstract class Base {
    val x: Int
  }

  class Sub extends Base {
    val y: Int = x * x
    val x = 1
  }

  (new Sub).y // 0
}

// 事前初期化済みフィールド
{
  trait MyTrait {
    val x: Int
    val y = x * x
  }

  // { }をwith <trait>の前に置いて事前初期化できる
  // extendsじゃなくてwithなのはMyTraitの初期化が後であることを暗示している
  val z = new {val x = 10} with MyTrait
  z.y // 100


  // classやobjectに対する事前初期化
  class C extends {
    val x = 2
  } with MyTrait

  val w = new C
  w.y

  object O extends {
    val x = 5
  } with MyTrait

  O.y
}

// クラスのパス依存性
// C1.C2と指定する場合とC1#C2と指定する場合がある。
// new (new C1).C2 と new (new C1).C2 は等しくない.
// (new C1).C2 <: C1 # C2 が成立する。
{
  class Animal
  class Food
  class DogFood extends Food
  class Dog extends Animal {
    type SuitableFood = DogFood
    class Bone
    class BigBone
  }

  val d1 = new Dog
  val d2 = new Dog
  (new d1.SuitableFood).isInstanceOf[d2.SuitableFood] // true. 両方ともDogFoodの別名だから
  (new d1.Bone).isInstanceOf[d2.Bone] // false. d1.Boneとd2.Boneは全くの別物
  (new d1.Bone).isInstanceOf[Dog#Bone] // true

  def makeBoneBigger(dog: Dog, bone: Dog#Bone): Dog#BigBone = new dog.BigBone
  makeBoneBigger(d1, new d1.Bone)
}


// リファインメント型
{
  abstract class Animal {
    val food: String
    val sleepingHour: Int
  }

  val a = new Animal{val food = "Grass"; val sleepingHour = 10}
  a.sleepingHour // 10
}

// Enumeration
{
  object Color extends Enumeration {
    val Red, Green, Blue = Value
  }

  object Name extends Enumeration {
    val Hocha = Value("Yoshihito Hotta")
    val Pei = Value("Liew Seng Pei")
    val Ryo = Value("Ryo Matsuda")
  }
  import Color._
  import Name._
  object Person {
    val favColor: Color.Value = Red
    val name: Name.Value = Hocha
  }

  Person.favColor
  Person.name
  Name.values foreach(println)
}

// implicit
// 型エラーの修正
// x.op(y)が型エラーを起こすとき、レシーバーxか引数yの型を直す

{
  class C {
    val x = 3
  }

  class D {
    val x = 2
    def foo(cc: D): Int = cc.x * this.x
  }

  // 名前はPreambleにするのが習慣
  object Preamble {
    implicit def c2d(cc: C) = new D {override val x = cc.x}
  }

  import Preamble._ // implicit conversionは同じスコープにないといけない
  val cc = new C
  val dd = new D

  // レシーバーのimplicit conversionの例
  cc foo dd // = c2d(cc) foo dd

  // 引数のimplicit conversionの例
  dd foo cc // = dd foo c2d(cc)
}

// コンパニオンオブジェクトにimplicitを入れても良い
// さっきの例なら、object Cかobject Dに入れてもOK
{
  class C {
    val x = 3
  }

  class D {
    val x = 2
    def foo(cc: D): Int = cc.x * this.x
  }

  object C {
    implicit def c2d(cc: C) = new D {override val x = cc.x}
  }

  /* これでもよい
  object D {
    implicit def c2d(cc: C) = new D {override val x = cc.x}
  }
  */
  val cc = new C
  val dd = new D

  // レシーバーのimplicit conversionの例
  cc foo dd // = c2d(cc) foo dd
}

// implicit class
{
  // クラスの引数からクラス自体への変換するコードを作成する
  // レシーバーを変換するのに使う
  implicit class Multiple(a: Int) {
    def x (b: Int) = a  *  b
  }
  4 x 3 // = Multiple(4) x 3 = 12

  class C {
    val x = 5
  }
}

// implicit parameter
// 変数定義と関数定義の両方でimplicitを付ける。
// 変数名は一致していなくてもよい。
{
  object ImplPara {
    implicit val a: Int = 3
    implicit val s: String = "Hocha"
  }

  object Greeter {
    // 一個implicitを付けるだけで全ての変数がimplicitになる。
    def greet(implicit x: Int, y: String) = s"Hello, ${y}. " * x
  }

  import ImplPara._
  Greeter.greet // Hello, Hocha. Hello, Hocha. Hello, Hocha.
}

// implicitly[T]
/*
implicitly[T]で暗黙の変数を呼び出せる。
 */
{
  // 引数としてimplicitを陽に書くバージョン
  class MyMax[T](val x: T) {
    def myMax(y: T)(implicit order: Ordering[T]) =
      if(order.gt(x, y)) x else y
  }

  // これはImplicitly[T]を使って次の様にも書ける.
  class MyMax2[T](val x: T)(implicit order: Ordering[T]) {
    def myMax2(y:T) = if(implicitly[Ordering[T]].gt(x, y)) x else y
  }

  val x = new MyMax(5)
  x myMax(6)
  val y = new MyMax2("abc")
  y myMax2("cde")
}

// context bound
// MyMax2でorderが使われていなかった。
// context boundを使えばimplicit引数を省略できる.
// boundは型コンストラクタでなければいけない。
{
  class MyMax3[T: Ordering](val x: T) {
// = class MyMax3[T](val x: T)(implicit order: Ordering[T])
    def myMax3(y: T) = if(implicitly[Ordering[T]].gt(x, y)) x else y
  }
  val x = new MyMax3(5)
  x myMax3(6)
}

// withFilter
// withFilterはfilterと同じだが、中間状態を生成せず、実際の操作が行われるまで何もしない (lazy)
// 最後に具体的な操作 (例えばmap(x => x))をしてSeqに戻してやる必要がある。
{
  val l = 1 to 10
  val x = l.withFilter( _ % 2 == 0).withFilter( _ % 3 == 0).map(x => x)
  x
}

// view
{
  val l = 1 to 10
  l map(_ * 2) map(_ + 1) // 中間リストが生成されて無駄
  // viewでビューを作成、forceを実行すると計算が開始する
  l.view.map(_ * 2).map(_ + 1).force
}

// wildcard type
// ワイルドカード型は任意の型とパターンマッチする。
{
  def foo(x: List[_]): Int = x.length
  foo(List("a", "b"))
  foo(List(1,2,3))

  List(1,2,3) match {
    case x: List[_] => x.length
    case _ => -1
  }
}

// extractor
// extractorとはunapplyが定義されているobjectのこと。
// applyが定義されている必要はないが、通常一緒に定義する
{
  object Email {
    def apply(user: String, domain: String): String = user + "@" + domain
    def unapply(address: String): Option[(String, String)] = {
      var parts = address split "@"
      if (parts.length == 2) Some(parts(0), parts(1)) else None
    }
  }

  // x match { case Extractor(z) => ...}は次の様に動作する.
  // Extractor.unapply(x)
  // --> Some(y)となれば zをyにバインドする。
  val domain = "hoge@gmail.com" match {
    //     Email.unapply("hoge@gmail.com")
    // --> Some("hoge", "gmail.com")
    case Email(user, domain) => domain
    case _ => ""
  }
  domain // gmail.com
}

// Some(tuple)
{
  Some("abc", 1) == Some(("abc", 1)) // true
}

// 可変長引数のextractor
{
  object Domain {
    def unapplySeq(whole: String): Option[Seq[String]] = {
      Some(whole.split("\\.").reverse)
    }
  }

  val x = "abc.co.jp" match {
    case Domain(x, _*) => x
  }
  x
}

import scala.concurrent.Promise
// 正規表現
{
  import scala.util.matching.Regex

  val Decimal = new Regex("""(-)?(\d+)(\.\d*)?""")
  // または単純に.rを付けるだけでも同じ効果が得られる
  val Decimal2 = """(-)?(\d+)(\.\d*)?""".r

  Decimal2 findFirstIn "The value of the eq is -2.1111." // Some(-2.1111)

  val str = "-1 -2 -3 -4 111 111"
  // findAllInはイテレータを返す
  Decimal2.findAllIn(str).toList // List(-1, -2, -3, -4, 111, 111)

  // 正規表現をextractorとして使う
  val Decimal(sign, intPart, decimalPart) = "-1.234"
  println(s"$sign, $intPart, $decimalPart") // -, 1, .234
}

// annotation
{
  @deprecated
  object OldObj {
    def foo = 1
  }

  OldObj.foo
}

// XML
{
  val price = 25
  val year = 1999
  val s =
      <bookstore>
          <book>
            <title>Harry Potter</title>
            <price>{if(year==1999) price * 2 else price}$</price>
            <year>{year}</year>
          </book>
      </bookstore>
  // カレントノードはbookstore
  // XPathの/は\に、//は\\に対応する
  s \ "book"
  s \ "book" \ "title"
  s \\ "price"
}

// XMLのパターンマッチ
{
  def proc(s: scala.xml.Node): String = {
    s match {
      case <a>{content}</a> => s"a: $content"
      case <a>{content}</a> => s"a: $content"
      case _ => "Something else"
    }
  }
  proc(<a><b>brabrabra</b></a>) // <b>brabra</b>
  // 上の式だと単一のノードにしかマッチしない
  proc(<a>
          ahahaha
          <b>
            brabra
          </b>
      </a>
    ) // Something else

  // 任意個のノードにマッチさせる
  def proc2(s: scala.xml.Node) = {
    s match {
      case <a>{contents @ _*}</a> =>
        for(content <- contents) println("content: " + content)
      case _ => "Something else"
    }
  }
}

// 自分型 (self type)
// thisの別名を定義する
{
  class C {
    self =>
    val c = 1
    override def toString = self.c.toString
  }

  new C // 1
}

// self typeの型を指定すると、指定した型のサブ型であることをコンパイラは想定する。
// つまり指定した型を継承したのと同じになる。
{
  trait B {
    val b =11
  }
  trait C {
    self: B =>
    val c = 1
    override def toString = self.b.toString
  }

  new C with B // 11
}

// 自分型を使った依存性の注入 (Dependency Injection; DI)
// 自分型が使われている場合は、ほぼ確実にDIを目的としている。
// DIとは抽象トレイトを指定し、後から実装を追加すること。
{
  trait Greet {
    def greet: String
  }

  trait Robot {
    self: Greet =>
    def boot(): Unit = println(greet)
  }

  // これは駄目。greetがabstractのままだからnewできない。
  // val r = new Robot with Greet

  trait JapaneseGreet extends Greet {
    override def greet = "こんにちは、みなさん"
  }

  val r = new Robot with JapaneseGreet
  r.boot()
}

// singleton type
// .typeでsingleton objectの型を表す。
{
  object Foo {
    val x = 2
    def goo(y: Foo.type) = y.x
  }
  Foo.goo(Foo)
}

// オブジェクトの等価性
// ==はequalsと全く同じ
// Javaと同様にequals と hashCodeをオーバーライドしなければいけない。
{
  class Point(val x: Int, val y: Int) {
    override def equals(obj: scala.Any): Boolean = obj match {
      case p: Point => this.x == p.x && this.y == p.y
      case _ => false
    }

    override def hashCode: Int = (x,y).##
  }

  val p1 = new Point(1, 2)
  val p2 = new Point(1, 2)
  p1 == p2 // true. equalsをoverrideしているから。

  val s = collection.mutable.HashSet(p1)
  s contains p2 // true. hashCodeをoverrideしているから。
}

// Future
// FutureはpollingのためにisCompletedとvalueをメソッドとして持っている。
// Future.valueはOption[Try[_]]を返す。
// つまりFutureはFuture[Option[Try[_]]]の三重の入れ子になっている。
{
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  val fut = Future { Thread.sleep(3000); 21}
  fut.isCompleted //false
  fut.value // None
  Thread.sleep(3000)
  println(fut.isCompleted) // true
  println(fut.value) // Some(Success(21))

  val fut2 = Future {21/0}
  Thread.sleep(1000)
  fut2.value // Some(Failure(java.lang.ArithmeticException: / by zero))
}

// Futureの合成
{
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  val fut = Future{Thread.sleep(1000); 1}
  val fut2 = fut.map(_*2)
  Thread.sleep(2000)
  println(fut2.value) // Some(Success(2))

  val fut3 = for(x <- fut; y <- fut2) yield x + y
  Thread.sleep(2000)
  println(fut3.value) // Some(Success(3))
}

// Futureを直接作る。
{
  import scala.concurrent.Future
  import scala.util.{Success, Failure}

  // (1) Futureのコンパニオンオブジェクトのメソッド、successful, failed, fromTryを使う
  val fut1 = Future.successful(1 + 2)
  val fut2 = Future.failed(new Exception("FAILED!"))
  val fut3 = Future.fromTry(Success(4))

  println(fut1) // Future(Success(3))
  println(fut2) // Future(Failure(java.lang.Exception: FAILED!))
  println(fut3) // Future(Success(4))

  // (2) Promiseを使う。（こっちが一般的な方法）
  val pro = Promise[Int]
  val fut4 = pro.future
  pro.success(44) // Promiseを変化させる副作用
  println(fut4.value) // Some(Success(44))
}