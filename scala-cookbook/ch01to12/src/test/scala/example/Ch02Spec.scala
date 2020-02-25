package example

import java.util.Locale
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.Random

class Ch02Spec extends AnyFlatSpec with Matchers {
  it should "2.01" in {
    "100".toInt shouldBe 100
    "100".toDouble shouldBe 100.0
    "100".toFloat shouldBe 100.0
    "1".toLong shouldBe 1
    "1".toShort shouldBe 1
    "1".toByte shouldBe 1

    Integer.parseInt("1", 2) shouldBe 1
    Integer.parseInt("10", 2) shouldBe 2
    Integer.parseInt("100", 2) shouldBe 4
    Integer.parseInt("1", 8) shouldBe 1
    Integer.parseInt("10", 8) shouldBe 8
    Integer.parseInt("11", 8) shouldBe 9

    import example.utils.ValueUtils._
    "1".toInt(2) shouldBe 1
    "10".toInt(2) shouldBe 2
    "100".toInt(2) shouldBe 4
    "100".toInt(8) shouldBe 64
    "100".toInt(16) shouldBe 256
  }

  it should "2.06" in {
    val b = BigInt(1234567890)
    (b * b).toString shouldBe "1524157875019052100"
    (b + b).toString shouldBe "2469135780"
    b.isValidByte shouldBe false
    b.isValidChar shouldBe false
    val c = if (b.isValidShort) b.toShort else None
    c shouldBe None
    val d = if (b.isValidInt) b.toInt else None
    d shouldBe 1234567890 //not Some(1234567890) ?
  }

  it should "2.07" in {
    val r = Random
    r.nextInt()
    r.nextDouble

    r.nextInt(100) < 100 shouldBe true
    r.nextInt(100) > 0 shouldBe true
  }

  it should "2.08" in {
    (1 to 10).size shouldBe 10
    (1 to 10 by 2).size shouldBe 5
    (1 to 10 by 3).size shouldBe 4
    for (i <- 1 to 5) println(i)
    println("----------------------------------------")
    for (i <- 1 until 5) println(i) //no 5 printed
    (1 to 5).toArray.foreach(println)
    println("----------------------------------------")
    (1 to 10).toList.foreach(println)
  }

  it should "2.09" in {
    val pi = scala.math.Pi
    f"$pi%1.5f" shouldBe "3.14159"
    f"$pi%1.2f" shouldBe "3.14"
    f"$pi%06.2f" shouldBe "003.14"
    "%06.2f".format(pi) shouldBe "003.14"

    val formatter = java.text.NumberFormat.getInstance
    formatter.format(10000) shouldBe "10,000"
    formatter.format(1000000) shouldBe "1,000,000"

    val localeDe = new Locale("de", "DE")
    val formatterDe = java.text.NumberFormat.getInstance(localeDe)
    formatterDe.format(1000000) shouldBe "1.000.000"

    val formaterC = java.text.NumberFormat.getCurrencyInstance
    formaterC.format(1000000.99) shouldBe "$1,000,000.99"
  }
}
