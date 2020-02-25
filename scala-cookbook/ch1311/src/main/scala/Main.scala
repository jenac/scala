import Messages.{ActNormalMessage, BadGuysMakeMeAngry, TryToFindSolution}
import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("BecomeSystem")
  val davidBanner = system.actorOf(Props[DavidBanner], name = "DavidBanner")
  davidBanner ! ActNormalMessage
  davidBanner ! TryToFindSolution
  davidBanner ! BadGuysMakeMeAngry
  Thread.sleep(1000)
  davidBanner ! ActNormalMessage
  system.terminate
}
