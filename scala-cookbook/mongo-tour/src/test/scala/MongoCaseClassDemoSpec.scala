import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.{Filters, Sorts, Updates}
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observer}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MongoCaseClassDemoSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll{

  val printPersonOnly = new Observer[Person] {
    override def onNext(result: Person): Unit = println(result)

    override def onError(e: Throwable): Unit = ()

    override def onComplete(): Unit = ()
  }
  override def beforeAll() = println("started")
  override def afterAll() = println("ended.")

  "Mongo" should "1" in {
    import org.mongodb.scala.bson.codecs.Macros._
    import org.bson.codecs.configuration.CodecRegistries.{fromRegistries, fromProviders}
    import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
    //register case class
    val codecRegistry = fromRegistries(fromProviders(classOf[Person]), DEFAULT_CODEC_REGISTRY )
    val mongoClient: MongoClient = MongoClient()
    //register case class
    val database: MongoDatabase = mongoClient.getDatabase("mydb").withCodecRegistry(codecRegistry)
    val collection: MongoCollection[Person] = database.getCollection("person")

    //insert a person
    val person = Person("Ada", "Lovelace")
    val insertOne = collection.insertOne(person)
    val afterInsertOne = for {
      _ <- insertOne
      result <- collection.find()
    } yield result

    afterInsertOne.subscribe(printPersonOnly)

    Thread.sleep(1000)
    println("----------------------------------------")

    //insert many person
    val people: Seq[Person] = Seq(
      Person("Charles", "Babbage"),
      Person("George", "Boole"),
      Person("Gertrude", "Blanch"),
      Person("Grace", "Hopper"),
      Person("Ida", "Rhodes"),
      Person("Jean", "Bartik"),
      Person("John", "Backus"),
      Person("Lucy", "Sanders"),
      Person("Tim", "Berners Lee"),
      Person("Zaphod", "Beeblebrox")
    )
    val insertMany = collection.insertMany(people)
    val afterInsertMany = for {
      _ <- insertMany
      result <- collection.find()
    } yield result

    afterInsertMany
    afterInsertMany.subscribe(printPersonOnly)
    Thread.sleep(1000)
    println("----------------------------------------")

    //find first
    collection.find().first().subscribe(printPersonOnly)
    Thread.sleep(1000)
    println("----------------------------------------")

    //find single with query, if not found, onNext will not called, no OnError.
    collection.find(Filters.equal("firstName", "Ida")).first().subscribe(new Observer[Person] {
      override def onNext(result: Person): Unit = {
        println(result)
      }

      override def onError(e: Throwable): Unit = {
        println(e.getMessage)
      }

      override def onComplete(): Unit = {
        println("completed")
      }
    })

    Thread.sleep(1000)
    println("----------------------------------------")
    //find set of with query
    collection.find(Filters.regex("firstName", "^G"))
      .sort(Sorts.ascending("lastName")).subscribe(printPersonOnly)
    Thread.sleep(1000)
    println("----------------------------------------")

    //updating
    val update = collection.updateOne(Filters.equal("lastName", "Berners Lee"), Updates.set("lastName", "Berners-Lee"))
    val afterUpdate = for {
      _ <- update
      result <- collection.find()
    } yield result
    afterUpdate.subscribe(printPersonOnly)
    Thread.sleep(1000)
    println("----------------------------------------")

    //delete
    val delete = collection.deleteMany(Filters.regex("firstName", "^[C-Z]"))
    val afterDelete = for {
      _ <- delete
      result <- collection.find()
    } yield result
    afterDelete.subscribe(printPersonOnly)
    Thread.sleep(1000)
    println("----------------------------------------")




    //
    collection.drop().subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("collection dropped" )

      override def onError(e: Throwable): Unit =()

      override def onComplete(): Unit = ()
    })
    Thread.sleep(1000)
  }

  "Mongo" should "2" in {
    println(2)
  }



}
