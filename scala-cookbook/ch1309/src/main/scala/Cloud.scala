import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object Cloud {
  def runAlgorithm(i: Int)(implicit ec: ExecutionContext): Future[Int] = Future {
    val delay = Random.nextInt(1000)
    Thread.sleep(delay)
    val result = i + 10
    println(s"returning result from cloud: $result, takes $delay ms")
    result
  }

}
