package org.hrw.yfcc100m.flow

import akka.stream.scaladsl.Flow
import com.sksamuel.elastic4s.UpdateDefinition

object Yfcc100mFlow {
  def flow(stringToUpdateDefinition: String => UpdateDefinition) = Flow[String].map { doc =>
    stringToUpdateDefinition(doc)
  }
}
