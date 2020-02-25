import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps


object Main extends App {
  val system = ActorSystem("AskSystem")
  val myActor = system.actorOf(Props[TestActor], name = "Fred")

  implicit  val timeout = Timeout(5 seconds)
  val future = myActor ? AskNameMessage //send and waiting for response
  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)

  val future2: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
  val result2 = Await.result(future2, timeout.duration)
  println(result2)

  system.terminate
}
