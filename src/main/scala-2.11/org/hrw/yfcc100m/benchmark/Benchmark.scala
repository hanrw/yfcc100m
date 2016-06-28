package org.hrw.yfcc100m.benchmark

import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}

trait Benchmark {
  private lazy implicit val system = ActorSystem("Benchmark")
  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }

  def timed[A](block: => A) = {
    val t0 = System.currentTimeMillis
    val result = block
    println("took " + (System.currentTimeMillis - t0) + "ms")
    result
  }

  val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  implicit val materializer = ActorMaterializer(materializerSettings)(system)

  var count = 0L
  val startTime = System.currentTimeMillis()

  def statistics(data: String) = {
    count += 1L
    val cost = (System.currentTimeMillis() - startTime) / 1000
    if (cost > 0)
      println(s"process speed  ${count / cost}/seconds")
  }


  def flow() = Flow[String].map { doc =>
    statistics(doc)
  }
}
