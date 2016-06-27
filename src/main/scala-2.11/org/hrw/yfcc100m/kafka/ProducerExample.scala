package org.hrw.yfcc100m.kafka

import akka.actor.ActorSystem
import akka.kafka.{ProducerMessage, ProducerSettings}
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

trait ProducerExample {
  val system = ActorSystem("example")
  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }
  val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  implicit val materializer = ActorMaterializer(materializerSettings)(system)

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

}

object PlainSinkExample extends App with ProducerExample {
  Source(1 to 10000)
    .map(_.toString)
    .map(elem => new ProducerRecord[Array[Byte], String]("test", elem))
    .to(Producer.plainSink(producerSettings)).run()(materializer)
}

object ProducerFlowExample extends App with ProducerExample {
  Source(1 to 10000)
    .map(elem => ProducerMessage.Message(new ProducerRecord[Array[Byte], String]("test", elem.toString), elem))
    .via(Producer.flow(producerSettings))
    .map { result =>
      val record = result.message.record
      println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value} (${result.message.passThrough}")
      result
    }.runWith(Sink.ignore)
}
