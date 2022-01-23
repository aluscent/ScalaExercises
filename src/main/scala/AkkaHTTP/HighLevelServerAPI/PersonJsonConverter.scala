package AkkaHTTP.HighLevelServerAPI

import spray.json.RootJsonFormat
import AkkaHTTP.HighLevelServerAPI.Exercise.Person
import spray.json.DefaultJsonProtocol.{IntJsonFormat, StringJsonFormat, jsonFormat2}

trait PersonJsonConverter {
  implicit def personFormat: RootJsonFormat[Person] = jsonFormat2(Person)
}
