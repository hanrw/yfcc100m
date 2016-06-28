package org.hrw.yfcc100m

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink}
import org.hrw.yfcc100m.stream.StreamUtils

object MediaEval2016PlacingReader {

  private lazy implicit val system = ActorSystem("mediaeval2016")
  private implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {
    StreamUtils.bz2asSource("yfcc100m_places.bz2").via(flow()).runWith(Sink.ignore)
  }

  def flow() = Flow[String].map { doc =>
    println(doc)
  }

}

