package two_consumer_group

import utils.Utils
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Consumer2 {
  val readTopic = "test"
  val zookeeper = "localhost:2181"

  def main(args: Array[String]) {
    val consumer2 = Utils.createConsumerFor(readTopic, "2", zookeeper).createMessageStreams(Map(readTopic -> 1)).get(readTopic).get.head.iterator()

    Future {
      while (true)
        println("consumer2", consumer2  .next())
    }

  }
}
