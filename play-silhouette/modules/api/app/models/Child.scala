package models

import org.joda.time.DateTime
import play.api.libs.json.{ Format, Json }
import reactivemongo.bson.BSONObjectID

// A nursery has n customers (families), and every customer can has n children on the nursery

case class Child(
  _id: Option[BSONObjectID] = None,
  firstName: String,
  lastName: String,
  gender: String,
  dateOfBirth: DateTime,
  families: Option[String] = None,
 /* pere: String,
  mere: String,
  creationDate: Option[DateTime] = Some(DateTime.now()),
  deletionDate: Option[DateTime] = None*/
)

object ChildFormat {
  import json.DateTimeJsonFormat._
  import json.ObjectIdJsonFormat._
  implicit val childFormat = Json.using[Json.WithDefaultValues].format[Child]
  def format()(implicit objectIdFormat: Format[BSONObjectID]) = Json.using[Json.WithDefaultValues].format[Child]
}

object ChildBSONFormat {
  import json.DateTimeJsonFormat._
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat
  implicit val childFormat = Json.using[Json.WithDefaultValues].format[Child]
}
