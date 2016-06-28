package org.hrw.yfcc100m.benchmark

import akka.stream.scaladsl.{Sink, Source}

object LoopBenchmark extends App with Benchmark {
  Source(1 to 1000000).map(_.toString).via(flow()).runWith(Sink.ignore)
}
