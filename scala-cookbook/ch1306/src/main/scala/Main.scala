import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.pattern.gracefulStop
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
object Main extends App {
  val actorSystem = ActorSystem("StopActorExamples")
  val simpleActor = actorSystem.actorOf(Props[SimpleActor], name="Simple")
  simpleActor ! "hello"
  actorSystem.stop(simpleActor)

  val pilledActor = actorSystem.actorOf(Props[PilledActor], name="Pilled")
  pilledActor ! "before poison pill"
  pilledActor ! PoisonPill
  pilledActor ! "after poison pill"
  pilledActor ! "still there?"

  val gracefulStopActor = actorSystem.actorOf(Props[PilledActor], name="GracefulStop")
  try {
    val stopped: Future[Boolean] = gracefulStop(gracefulStopActor, 2 seconds)
    Await.result(stopped, 3 seconds)
    println("gracefulStopActor was stopped")
  } catch {
    case e: Exception => e.printStackTrace
  } finally {
    actorSystem.terminate()
  }
}
