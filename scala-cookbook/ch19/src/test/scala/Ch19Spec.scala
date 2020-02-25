import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch19Spec extends AnyFlatSpec with Matchers {
  class GrandParent
  class Parent extends GrandParent
  class Child extends Parent

  class InvariantClass[A]
  class CovariantClass[+A]
  class ContravariantClass[-A]

  "ch19.01" should "about variants" in {
    def invarMethod(x: InvariantClass[Parent]) {}
    def covarMethod(x: CovariantClass[Parent]) {}
    def contraMethod(x: ContravariantClass[Parent]) {}

    // invarMethod(new InvariantClass[Child]) //Error - won't compile
    invarMethod(new InvariantClass[Parent]) //Success
    // invarMethod(new InvariantClass[GrandParent]) //Error - won't compile

    covarMethod(new CovariantClass[Child]) //Success
    covarMethod(new CovariantClass[Parent]) //Success
    //contraMethod(new CovariantClass[GrandParent]) //Error - won't compile

    //contraMethod(new ContravariantClass[Child]) //Error - won't compile
    contraMethod(new ContravariantClass[Parent]) //Success
    contraMethod(new ContravariantClass[GrandParent]) //Success
  }

  "ch19.01" should "about bounds" in {
    // A <: B //A must be subclass of B
    // A >: B //A must be superclass of B
    // A <: Upper >: B //has upper and lower bonds at same time.
  }

  "ch19.01" should "type limitation" in {
    // A =:= B // A must be equal to B
    // A <:< B // A must be a subtype of B
    // A <%< B // A must be viewable as B
  }

  "ch19.03" should "use duck type" in {
    class Dog { def speak() { println("woof")} }
    class Klingon { def speak() { println("Qapla!")} }

    def callSpeak[A <: { def speak(): Unit}](obj: A): Unit = { //A is a type has speak(): Unit defined
      obj.speak()
    }

    callSpeak(new Dog)
    callSpeak(new Klingon)
  }
}
