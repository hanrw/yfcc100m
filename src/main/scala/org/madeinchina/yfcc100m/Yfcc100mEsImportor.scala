package org.madeinchina.yfcc100m

import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import com.typesafe.config.ConfigFactory
import org.madeinchina.yfcc100m.es.Yfcc100mIndexer
import org.madeinchina.yfcc100m.flow.Yfcc100mFlow
import org.madeinchina.yfcc100m.source.Yfcc100mSource

import scala.util.Properties

object Yfcc100mEsImportor extends App {
  private lazy implicit val system = ActorSystem("yfcc100m")
  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }

  val config = ConfigFactory.load()

  val materializerSettings =
    ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  implicit val materializer = ActorMaterializer(materializerSettings)(system)
  val client = HttpClient(ElasticsearchClientUri(envOrElseConfig("yfcc100m.es-host"), envOrElseConfig("yfcc100m.es-port").toInt))
  val yfcc100mIndexer = new Yfcc100mIndexer(client)

  Yfcc100mSource
    .bz2AsSource(
      envOrElseConfig("yfcc100m.dataset"))
    .via(Yfcc100mFlow.flow(Yfcc100mIndexer.stringToUpdateDefinition(_)))
    .grouped(1000)
    .mapAsync(8)(updates => yfcc100mIndexer.bulkIndex(updates.toList))
    .runWith(Sink.ignore)


  def envOrElseConfig(name: String): String = {
    Properties.envOrElse(
      name.toUpperCase.replaceAll("""\.""", "_"),
      config.getString(name)
    )
  }
}
