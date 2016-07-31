package org.hrw.yfcc100m

import java.text.SimpleDateFormat

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import org.hrw.yfcc100m.es.Yfcc100mIndexer
import org.hrw.yfcc100m.stream.StreamUtils

object Yfcc100mReader {

  private lazy implicit val system = ActorSystem("mediaeval2016")
  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }
  val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  implicit val materializer = ActorMaterializer(materializerSettings)(system)

  implicit def string2Yfcc100m(data: String): Yfcc100m = {
    val splitData = data.split("\t")
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    Yfcc100m(
      lineNumber = splitData(0).toLong,
      id = splitData(1),
      hash = splitData(2),
      userNsid = splitData(3),
      nickName = splitData(4),
      dateTaken = Option(splitData(5)) match {
        case Some(x) if (!x.equals("null")) => sdf.parse(x).getTime
        case _ => 0L
      },
      dateUploaded = Option(splitData(6)) match {
        case Some(x) => x.toLong * 1000
        case None => 0L
      },
      captureDevice = splitData(7),
      title = splitData(8),
      description = splitData(9),
      userTags = splitData(10).split(","),
      machineTags = splitData(11).split(","),
      longitude = Option(splitData(12)).filter(_.nonEmpty) match {
        case Some(x) => x.toDouble
        case None => 0D
      },
      latitude = Option(splitData(13)).filter(_.nonEmpty) match {
        case Some(x) => x.toDouble
        case None => 0D
      },
      accuracy = Option(splitData(14)).filter(_.nonEmpty) match {
        case Some(x) => x.toInt
        case None => 0
      },
      url = splitData(15),
      downloadUrl = splitData(16),
      licenseName = splitData(17),
      licenseUrl = splitData(18),
      serverIdentifier = splitData(19),
      farmIdentifier = splitData(20),
      secretOriginal = splitData(21),
      secret = splitData(22),
      extension = splitData(23),
      marker = Option(splitData(24)).filter(_.nonEmpty) match {
        case Some(x) => x.toInt
        case None => 0
      })
  }


  def main(args: Array[String]) {
    val client = {
      //      val settings = Settings.settingsBuilder().put("client.transport.ignore_cluster_name", true).build()
      val settings = Settings.settingsBuilder().put("cluster.name", "es-cluster").build()
      ElasticClient.transport(settings, ElasticsearchClientUri("elasticsearch://192.168.2.14:9300"))
    }

    //    val numCPUs = Runtime.getRuntime().availableProcessors()
    //
    //        def flow(yfcc100mIndexer: Yfcc100mIndexer) = Flow[String].map { doc =>
    ////          println(doc)
    //          doc
    //        }.mapAsyncUnordered(numCPUs)(yfcc100mIndexer.indexData)

    def flow(yfcc100mIndexer: Yfcc100mIndexer) = Flow[String].map { doc =>
      //      println(doc)
      yfcc100mIndexer.insertDsl(doc)
    }

    val yfcc100mIndexer = new Yfcc100mIndexer(client)
    StreamUtils.bz2asSource("yfcc100m_dataset.bz2").via(flow(yfcc100mIndexer)).grouped(1000).mapAsync(8)(updates => yfcc100mIndexer.bulkIndex(updates.toList)).runWith(Sink.ignore)



    //    val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[akka.NotUsed] =>
    //      import GraphDSL.Implicits._
    //      val src = source("yfcc100m_dataset.bz2")
    //
    //      src ~> flow(yfcc100mIndexer) ~> Sink.ignore
    //      ClosedShape
    //    })
    //    graph.run()
  }


  def transform(data: String) = {

  }

}

case class Yfcc100m(lineNumber: Long, id: String, hash: String, userNsid: String, nickName: String,
                    dateTaken: Long, dateUploaded: Long, captureDevice: String, title: String,
                    description: String, userTags: Array[String], machineTags: Array[String], longitude: Double,
                    latitude: Double, accuracy: Int, url: String, downloadUrl: String, licenseName: String,
                    licenseUrl: String, serverIdentifier: String, farmIdentifier: String, secretOriginal: String,
                    secret: String, extension: String, marker: Int)
