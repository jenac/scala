package macwire.di
import com.softwaremill.tagging._

object ClassDef {

  class PointSwitcher()

  class TrainCarCoupler()

  class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

  class CraneController()

  class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  class TrainDispatch()

  class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain() {
      println("prepareAndDispatchNextTrain is called")
    }
  }

  //factory
  class A()

  class C (a: A, specialValue: Int) {
    def doWork(): Unit = {
      println(s"I have $a and $specialValue")
    }
  }

  object C {
    def create(a: A) = new C(a, 42)
  }

  //qualifiers
  trait Berry {
    def color
  }

  class BlackBerry extends Berry {
    override def color: Unit = println("BLACK")
  }

  class BlueBerry extends Berry {
    override def color: Unit = println("BLUE")
  }

  trait Black
  trait Blue

  class Basket(val blueBerry: Berry @@ Blue, val blackBerry: Berry @@ Black) {
    def show = {
      blueBerry.color
      blackBerry.color
    }
  }
}
