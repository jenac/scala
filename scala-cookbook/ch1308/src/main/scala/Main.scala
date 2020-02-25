import akka.actor.{ActorSystem, PoisonPill, Props}

object Main extends App {
  val system = ActorSystem("WatchingChild")
  val parent = system.actorOf(Props[Parent], name = "Parent")
  val kenny = system.actorSelection("/user/Parent/Kenny")

  kenny ! "ready?"
  parent ! "ready?"

  kenny ! Explode
  kenny ! "still ready?"

  kenny ! PoisonPill

  Thread.sleep(5000)
  system.terminate()

}
