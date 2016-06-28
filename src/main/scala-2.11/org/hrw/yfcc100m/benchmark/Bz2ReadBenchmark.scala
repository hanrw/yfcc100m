package org.hrw.yfcc100m.benchmark

import akka.stream.scaladsl.Sink
import org.hrw.yfcc100m.stream.StreamUtils

object Bz2ReadBenchmark extends App with Benchmark {
  StreamUtils.bz2asSource("yfcc100m_dataset.bz2").via(flow()).runWith(Sink.ignore)
}
