import org.mongodb.scala.bson.ObjectId

case class Person(_id: ObjectId, firstName: String, lastName: String)
object Person {
  def apply(firstName: String, lastName: String): Person =
    new Person(new ObjectId(), firstName, lastName)
}