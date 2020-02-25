import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("LifecycleDemo")
  val kenny = system.actorOf(Props[Kenny], name="Kenny")

  println("sending kenny a simple string message")
  kenny ! "hello"

  println("resarting kenny!")
  kenny ! ForceRestart
  Thread.sleep(2000)

  println("stopping kenny")
  system.stop(kenny)

  println("system terminate")
  system.terminate()
}
