import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}
object Main extends App {
  implicit val baseTime = System.currentTimeMillis
  implicit val ex = ExecutionContext.global

  val f = Future{
    Thread.sleep(500)
    1+1
  }

  //this will block
  val result = Await.result(f, 1 second)
  println(result)

  //callback non-block
  println("starting calculation ...")
  val f2 = Future {
    Thread.sleep(Random.nextInt(1000))
    42
  }
  println("before onComplelte")
  f.onComplete {
    case Success(value) => println(s"Got the callback. meaning = $value")
    case Failure(e) => e.printStackTrace
  }
  ('A' to 'Z').foreach { l =>
    println(s"$l ...")
    Thread.sleep(100)
  }

  Thread.sleep(3000)

  println("--------Future[T]--------")
  def longRunningComputation(i: Int): Future[Int] = Future {
    Thread.sleep(100)
    i+1
  }

  longRunningComputation(11).onComplete {
    case Success(result) => println(s"result = $result")
    case Failure(e) => e.printStackTrace
  }
  Thread.sleep(1000)

  println("--------for multiple futures--------")
  println("starting futures")
  val result1 = Cloud.runAlgorithm(11)
  val result2 = Cloud.runAlgorithm(12)
  val result3= Cloud.runAlgorithm(13)
  println("before for-comprehension")
  val res = for {
    r1 <- result1
    r2 <- result2
    r3 <- result3
  } yield (r1 + r2 + r3)

  println("before success")
  res.onComplete {
    case Success(value) => println(s"total = $value")
    case Failure(exception) => exception.printStackTrace
  }
  println("before sleep and end")
  Thread.sleep(3000)
}
