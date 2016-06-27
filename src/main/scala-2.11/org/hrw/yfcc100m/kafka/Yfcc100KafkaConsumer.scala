package org.hrw.yfcc100m.kafka

import akka.actor.ActorSystem
import akka.kafka.ConsumerSettings
import akka.kafka.scaladsl.Consumer
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.Future


class Yfcc100KafkaConsumer(system: ActorSystem){

  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }

  val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  implicit val materializer = ActorMaterializer(materializerSettings)(system)

  import scala.concurrent.ExecutionContext.Implicits.global
  def countDDD(data: String) = {
    count += 1L
    val cost = (System.currentTimeMillis() + 1 - startTime) / 1000
    if (cost > 0)
      println(s"process speed  ${count / cost}/seconds")
    Future("")
  }

  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer, Set("test"))
    .withBootstrapServers("localhost:9092")
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  val startTime = System.currentTimeMillis()
  var count: Long = 1L

  Consumer.atMostOnceSource(consumerSettings.withClientId("client1"))
    .mapAsync(1) { record =>
      countDDD(record.value)
    }.runWith(Sink.ignore)
}
