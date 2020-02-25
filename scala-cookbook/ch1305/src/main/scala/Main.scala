import Messages.CreateChild
import akka.actor.{ActorSystem, PoisonPill, Props}

object Main extends App {
  val actorSystem = ActorSystem("ParentChildSystem")
  val parent = actorSystem.actorOf(Props[Parent], name ="Parent")

  parent ! CreateChild("Jhon")
  parent ! CreateChild("Amy")
  Thread.sleep(500)

  println("Sending John a PoisonPill ...")
  val john = actorSystem.actorSelection("/user/Parent/Jhon")
  john ! PoisonPill
  println("John was killed")

  Thread.sleep(5000)
  actorSystem.terminate()
}
