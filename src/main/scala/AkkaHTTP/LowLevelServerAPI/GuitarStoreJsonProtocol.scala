package AkkaHTTP.LowLevelServerAPI

import AkkaHTTP.LowLevelServerAPI.JSON.Guitar
import spray.json.{DefaultJsonProtocol, JsonWriter, RootJsonFormat, jsonWriter}

trait GuitarStoreJsonProtocol extends DefaultJsonProtocol {
  implicit val guitarFormat: RootJsonFormat[Guitar] = jsonFormat2(Guitar) // convert Guitar object to JSON
  implicit val guitarWriter: JsonWriter[Guitar] = jsonWriter(guitarFormat)
}