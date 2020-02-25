package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch08Spec extends AnyFlatSpec with Matchers {
  //initialized is concrete otherwise abstract
  "8.02" should "trait fields" in {
    trait PizzaTrait {
      var numToppings: Int // abstract
      var brand = "ABC" //concrete
      val size = 14 //concrete
      val maxToppings = 10 //concrete
    }

    class Pizza extends PizzaTrait {
      var numToppings = 8 //no override needed for var
      override val size = 16 //override is required for val
    }
  }

  "8.06" should "limit trait use on a base class" in {
    trait StarShipCore {
      this: Starship =>
      def name: String
    }

    class Starship
    class Enterprise extends Starship with StarShipCore {
      override def name: String = "ERP"
    }

    // the following fail. Since StarShipCore can only used for Starship's sub classes
    //    class EnterpriseFail extends StarShipCore {
    //
    //    }
  }

  "8.07" should "limit trait use with class having certain method" in {
    trait WarpCore {
      this: {def mustHave(password: String): Boolean} =>
      def usage: String = "Anyway"
    }

    class Warp extends WarpCore {
      def mustHave(someString: String): Boolean = true //must have this method to extend trait WarpCore
    }
  }

  "8.09" should "object with trait" in {
    trait Good {
      println("I am good")
    }

    class Bird {
      def name = "OK"
    }
    val bird = new Bird with Good
  }
}
