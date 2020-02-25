package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch04Spec extends AnyFlatSpec with Matchers {
  "4.03" should "additional constructors" in {
    class Pizza(var crustSize: Int, var crustType: String) {
      def this(crustSize: Int) {
        this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
      }

      def this(crustType: String) {
        this(Pizza.DEFAULT_CRUST_SIZE, crustType)
      }

      def this() {
        this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
      }
    }

    object Pizza {
      val DEFAULT_CRUST_TYPE = "Thin"
      val DEFAULT_CRUST_SIZE = 12
    }

    val p1  = new Pizza()
    val p2  = new Pizza(Pizza.DEFAULT_CRUST_TYPE)
    val p3  = new Pizza(Pizza.DEFAULT_CRUST_SIZE)
    val p4  = new Pizza(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)

    p1.crustSize == p2.crustSize && p1.crustType == p2.crustType shouldBe true
  }

  "4.03" should "use apply to simulate additional constructor for case class" in {
    case class Person(name: String, age: Int)
    object  Person {

      def apply(age: Int): Person = new Person("Someone", age)
      def apply(name: String): Person = new Person(name, 1)
    }

    Person("Someone", 12) == Person(12) shouldBe true
    Person("baby", 1) == Person("baby") shouldBe true
  }

  "4.04" should "private primary constructor for singleton" in {
    class SomeHelper private {
      def hello = "I am Helper"
    }
    object SomeHelper {
      def getInstance = new SomeHelper()
    }
    SomeHelper.getInstance.hello shouldBe "I am Helper"

    //with arg
    class SomeService private(serviceName: String) {
      def hello = s"I am $serviceName service"
    }
    object SomeService {
      def getInstance(name: String) = new SomeService(name)
    }
    SomeService.getInstance("whois").hello shouldBe "I am whois service"
  }

  "4.07" should "private[this] looks more private" in {
    class Stock {
      private var price: Double = _
      def setPrice(p: Double) { price = p }
      def isHigher(that: Stock) = this.price > that.price //works
    }

    class MorePrivateStock {
      private[this] var price: Double = _
      def setPrice(p: Double) { price = p }
      //def isHigher(that: MorePrivateStock) = this.price > that.price
      // fails cause only instance itself can access private[this] member
      def moreThan100() = this.price > 100.0 //this works!
    }
  }

  "4.08" should "lazy is executed when access" in {
    class Foo {
      val text = {
        println("text")
        "text"
      }
      lazy val lazyText = {
        println("Lazy Text")
        "lazy"
      }
    }

    val foo = new Foo()
    println("------------------------------")
    foo.lazyText
  }

  "4.10" should "sub class constructor"  in {
    class Person(var name: String, var address: String) {
      override def toString: String = s"$name @ $address"
    }
    class Employee(name: String, address: String, var age: Int) //no var for name, address
      extends Person(name, address) {
      override def toString: String = super.toString + s", $age years old"
    }

    val manager = new Employee("Jenac", "5850 Opus pkwy", 18)
    manager.toString shouldBe "Jenac @ 5850 Opus pkwy, 18 years old"
  }

  "4.11" should "call parent class constructor" in {
    class Animal(var name: String, var age: Int) {
      def this(name: String) {
        this(name, 0)
        println("Animal secondary constructor called")
      }
      println("Animal primary constructor called")
      override def toString: String = s"$name is $age years old"
    }

    //call parent default constructor
    class Dog(name: String) extends Animal(name, 0) {

    }

    //call parent secondary constructor
    class Cat(name: String) extends Animal(name) {

    }

    val dog = new Dog("haha")
    val cat = new Cat("cccc")
  }
}
