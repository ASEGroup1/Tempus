import org.mongodb.scala.{Completed, MongoClient, MongoCollection, Observer, Subscription}
import org.mongodb.scala.bson.collection.mutable.Document

class Dao {

  def test() = {
    val client =  MongoClient("mongodb://ec2-3-9-11-63.eu-west-2.compute.amazonaws.com")
    val db = client.getDatabase("master")
    val collection: MongoCollection[Document] = db.getCollection("test")

    val doc: Document = Document("_id" -> 0, "name" -> "MongoDB", "type" -> "database",
      "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))

    collection.insertOne(doc).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("Inserted")

      override def onError(e: Throwable): Unit = println("Failed: " + e.getMessage)

      override def onComplete(): Unit = println("Completed")

      override def onSubscribe(subscription: Subscription) = {
        subscription.request(Long.MaxValue)
        print("subscribed")
      }
    })
  }
}
