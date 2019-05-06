package services

import play.api.libs.json.{JsNumber, JsString}

object JsonUtils {
  def extractInt(value: Any):Int = if(value != null) value.asInstanceOf[JsNumber].value.toIntExact else -1
  def extractString(value: Any):String = if(value != null) value.asInstanceOf[JsString].value.mkString else ""
}
