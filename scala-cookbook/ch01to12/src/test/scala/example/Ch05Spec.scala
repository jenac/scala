package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch05Spec extends AnyFlatSpec with Matchers {
  //Accessibility
  //private [this] // access by current instance
  //private //access by class
  //protected //access by class and sub classes
  //private[example] access by package example classes

  "5.02" should "call base class method" in {
    trait Human {
      def hello = "Human"
    }

    trait Mother extends Human {
      override def hello: String = "Mother"
    }

    trait Father extends Human {
      override def hello: String = "Father"
    }

    class Child extends Human with Mother with Father {
      def helloSuper = super.hello

      def helloMonther = super[Mother].hello

      def helloFather = super[Father].hello

      def helloHuman = super[Human].hello
    }

    val c = new Child
    c.helloSuper shouldBe "Father" //the last with
    c.helloMonther shouldBe "Mother"
    c.helloFather shouldBe "Father"
    c.helloHuman shouldBe "Human"

    class Child2 extends Mother {
      //the following won't compile
      //Child2 does not directly extens Human
      //compile error: Human does not name a parent class of class Child2

      //def helloHuman = super[Human].hello
    }
  }

  "5.09" should "chain/fluent syntax" in {
    //if class will be extended, return type should be this.type
    class Person {
      protected var fname = ""
      protected var lname = ""

      def setFirstName(firstName: String): this.type = {
        fname = firstName
        this
      }

      def setLastName(lastName: String): this.type = {
        lname = lastName
        this
      }
    }

    class Employee extends Person {
      protected var role = ""

      def setRole(roleName: String): this.type = {
        role = roleName
        this
      }

      override def toString: String = s"first: $fname, last: $lname, role: $role"
    }

    val e = new Employee

    e.setFirstName("Jen").setLastName("Ac").setRole("Owner").toString shouldBe "first: Jen, last: Ac, role: Owner"
  }
}
