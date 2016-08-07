package org.hrw.yfcc100m.benchmark

import akka.stream.ClosedShape
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink}
import org.hrw.yfcc100m.source.Yfcc100mSource

object Bz2ReadBenchmark extends App with Benchmark {
  //  StreamUtils.bz2asSource("yfcc100m_dataset.bz2").via(flow()).runWith(Sink.ignore)
  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[akka.NotUsed] =>
    import akka.stream.scaladsl.GraphDSL.Implicits._
    Yfcc100mSource.bz2AsSource("yfcc100m_dataset.bz2") ~> flow() ~> Sink.ignore
    ClosedShape
  })
  graph.run()
}
