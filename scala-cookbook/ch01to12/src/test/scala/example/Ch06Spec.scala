package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Ch06Spec extends AnyFlatSpec with Matchers {
  "6.03" should "use getClass" in {
    val hello = <p>Hello, World</p>
    hello.child.foreach(e => println(e.getClass))
  }
}
