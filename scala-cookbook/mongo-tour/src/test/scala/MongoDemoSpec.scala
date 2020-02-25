import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.{Accumulators, Filters}
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observable, Observer}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MongoDemoSpec extends AnyFlatSpec with Matchers with BeforeAndAfterEach {

  // To directly connect to the default server localhost on port 27017
  // val mongoClient: MongoClient = MongoClient()
  val mongoClient = MongoClient("mongodb://localhost")

  // or provide custom MongoClientSettings
  // val clusterSettings: ClusterSettings = ClusterSettings.builder().hosts(List(new ServerAddress("localhost")).asJava).build()
  // val settings: MongoClientSettings = MongoClientSettings.builder().clusterSettings(clusterSettings).build()
  // val mongoClient: MongoClient = MongoClient(settings)

  val database = mongoClient.getDatabase("mydb")
  var maybeCollection: Option[MongoCollection[Document]] = None
  override def beforeEach(): Unit = {
    maybeCollection = Some(database.getCollection("test"))
  }

  override def afterEach() = {
    if (maybeCollection.isDefined) {
      maybeCollection.get.drop().subscribe(new Observer[Completed] {
        override def onNext(result: Completed): Unit = println("collection dropped")

        override def onError(e: Throwable): Unit = ()

        override def onComplete(): Unit = ()
      })
    }
  }
  "Mongo" should "insert one document" in {
    val connection = maybeCollection.get
    val doc: Document = Document("_id" -> 0, "name" -> "MongoDB", "type" -> "database",
      "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))

    val observable: Observable[Completed] = connection.insertOne(doc)
    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("1 document inserted")

      override def onError(e: Throwable): Unit = println("1 document insertion failed")

      override def onComplete(): Unit =  println("1 document insertion completed")
    })
    Thread.sleep(1000)
  }

  "Mongo" should "insert multiple documents" in {
    val collection = maybeCollection.get
    val documents = (1 to 100) map { i => Document ("i" -> i)}
    val insertObservable = collection.insertMany(documents)
    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("Inserted")

      override def onError(e: Throwable): Unit = println("Failed")

      override def onComplete(): Unit =  println("Completed")
    })
    Thread.sleep(1000)

    //insert and count
    val insertAndCount = for {
      insertResult <- insertObservable
      countResult <- collection.estimatedDocumentCount()
    } yield countResult

    insertAndCount.subscribe(new Observer[Long] {
      override def onNext(result: Long): Unit = println(s"Document count: $result")

      override def onError(e: Throwable): Unit = println("Failed")

      override def onComplete(): Unit =  println("Completed")
    })
    Thread.sleep(1000)
  }

  "Mongo" should "query single document" in {
    val collection = maybeCollection.get
    val documents = (1 to 100) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val find6thObservable = collection.find().skip(5).first()
    val find71Observable = collection.find(Filters.equal("i", 71)).first()
    val finalObservable = for {
      _ <- insertObservable
      find6thResult <- find6thObservable
      find71Result <- find71Observable
    } yield (find6thResult, find71Result)

    finalObservable.subscribe(new Observer[(Document, Document)] {
      override def onNext(result: (Document, Document)): Unit = result match {
        case (d6: Document, d71: Document) =>
          println(d6.toJson())
          println(d71.toJson())
      }
      override def onError(e: Throwable): Unit = ()
      override def onComplete(): Unit = ()
    })
    Thread.sleep(1000)


  }

  "Mongo" should "query multiple documents" in {
    val collection = maybeCollection.get
    val documents = (1 to 100) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val find30to50Observable = collection.find(
      Filters.and(Filters.gt("i", 30), Filters.lte("i", 50)))
    val finalObservable = for {
      _ <- insertObservable
      find30to50Result <- find30to50Observable
    } yield find30to50Result
    finalObservable.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result.toJson)
      override def onError(e: Throwable): Unit = ()
      override def onComplete(): Unit = ()
    })

    Thread.sleep(1000)
  }

  "Mongo" should "sorting" in {
    import org.mongodb.scala.model.Sorts
    val collection = maybeCollection.get
    val documents = (1 to 100) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val sortAndFind = collection.find(Filters.exists("i"))
      .sort(Sorts.descending("i")).first()
    val finalObservable = for {
      _ <- insertObservable
      sortAndFileResult <- sortAndFind
    } yield sortAndFileResult
    finalObservable.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result.toJson)
      override def onError(e: Throwable): Unit = ()
      override def onComplete(): Unit = ()
    })

    Thread.sleep(1000)
  }

  "Mongo" should "projecting fields" in {
    import org.mongodb.scala.model.Projections
    val collection = maybeCollection.get
    val documents = (1 to 100) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val findAndProject = collection.find()
      .projection(Projections.excludeId()).first()
    val finalObservable = for {
      _ <- insertObservable
      findAndProjectResult <- findAndProject
    } yield findAndProjectResult
    finalObservable.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result.toJson)
      override def onError(e: Throwable): Unit = ()
      override def onComplete(): Unit = ()
    })

    Thread.sleep(1000)
  }

  "Mongo" should "aggregations" in {
    import org.mongodb.scala.model.Aggregates
    val collection = maybeCollection.get
    val documents = (1 to 100) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val aggregateAndProject = collection.aggregate(
      Seq(Aggregates.filter(Filters.gt("i", 0)),
        Aggregates.project(Document("""{ITimes10: {$multiply: ["$i", 10]}}"""))))
    val totalAggregate = collection.aggregate(
      List(Aggregates.group(null,
        Accumulators.sum("total", "$i"))))
    val finalObservable = for {
      _ <- insertObservable
      aggregateAndProjectResult <- aggregateAndProject
      totalAggregateResult <- totalAggregate
    } yield (aggregateAndProjectResult, totalAggregateResult)
    finalObservable.subscribe(new Observer[(Document, Document)] {
      override def onNext(result: (Document, Document)): Unit = result match {
        case (a: Document, b: Document) =>
          println(a.toJson)
          println(b.toJson)
      }
      override def onError(e: Throwable): Unit = ()
      override def onComplete(): Unit = ()
    })

    Thread.sleep(1000)
  }

  "Mongo" should "update" in {
    import org.mongodb.scala.model.Updates
    val collection = maybeCollection.get
    val documents = (1 to 10) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val updateOne = collection.updateOne(Filters.equal("i", 10), Updates.set("square", 110))
    val updateMany = collection.updateMany(Filters.lt("i", 5), Updates.set("square", -1))
    val finalObservable = for {
      _ <- insertObservable
      updateOneResult <- updateOne
      updateManyResult <- updateMany
    } yield (updateOneResult, updateManyResult)
    finalObservable.subscribe(new Observer[(UpdateResult, UpdateResult)] {
      override def onNext(result: (UpdateResult, UpdateResult)): Unit = println("updated")

      override def onError(e: Throwable): Unit = ()

      override def onComplete(): Unit = ()
    })

    Thread.sleep(1000)
    val afterUpdate = collection.find()
    afterUpdate.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result.toJson)

      override def onError(e: Throwable): Unit = ()

      override def onComplete(): Unit = ()
    })
    Thread.sleep(1000)
  }

  "Mongo" should "delete" in {
    import org.mongodb.scala.model.Updates
    val collection = maybeCollection.get
    val documents = (1 to 10) map { i => Document ("i" -> i, "square" -> i*i) }
    val insertObservable = collection.insertMany(documents)
    val deleteOne = collection.deleteOne(Filters.equal("i", 10))
    val deleteMany = collection.deleteMany(Filters.lt("i", 5))
    val finalObservable = for {
      _ <- insertObservable
      deleteOneResult <- deleteOne
      deleteManyResult <- deleteMany
    } yield (deleteOneResult, deleteManyResult)
    finalObservable.subscribe(new Observer[(DeleteResult, DeleteResult)] {
      override def onNext(result: (DeleteResult, DeleteResult)): Unit = println("deleted")

      override def onError(e: Throwable): Unit = ()

      override def onComplete(): Unit = ()
    })

    Thread.sleep(1000)
    val afterUpdate = collection.find()
    afterUpdate.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result.toJson)

      override def onError(e: Throwable): Unit = ()

      override def onComplete(): Unit = ()
    })
    Thread.sleep(1000)
  }
}
