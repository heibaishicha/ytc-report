package common.utils

import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JsonUtil {

  private val mapper: ObjectMapper = new ObjectMapper
  mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true)
  mapper.registerModule(DefaultScalaModule)

  def getObjectMapper: ObjectMapper = {
    mapper
  }

}
