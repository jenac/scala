package macwire.di

import macwire.di.ClassDef._
import com.softwaremill.macwire.{wire, wireWith}
import com.softwaremill.tagging._
object TheApp extends App {
  lazy val pointSwitcher = wire[PointSwitcher]
  lazy val trainCarCoupler = wire[TrainCarCoupler]
  lazy val trainShunter = wire[TrainShunter]

  lazy val craneController = wire[CraneController]
  lazy val trainLoader = wire[TrainLoader]
  lazy val trainDispatch = wire[TrainDispatch]

  lazy val trainStation = wire[TrainStation]

  trainStation.prepareAndDispatchNextTrain()

  //wire C using factory
  lazy val a = wire[A]
  lazy val c = wireWith(C.create( _))
  c.doWork()

  //qualifiers
  lazy val blueBerry = wire[BlueBerry].taggedWith[Blue]
  lazy val blackBerry = wire[BlackBerry].taggedWith[Black]

  lazy val basket = wire[Basket]
  basket.show

}
