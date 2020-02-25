import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.parallel.CollectionConverters._

class Ch1312Spec extends AnyFlatSpec with Matchers {
  "13.12" should "use collection as parallel" in {
    val vec = Vector.range(0, 10)
    vec.foreach(print)
    println("")
    (1 to 10).foreach { i =>
      vec.par.foreach(print)
      println("")
    }

  }

}
