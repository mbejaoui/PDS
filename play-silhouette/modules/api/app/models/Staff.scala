package models

import org.joda.time.DateTime
import play.api.libs.json.{ Format, Json }
import reactivemongo.bson.BSONObjectID

// A nursery has n customers (families), and every customer can has n children on the nursery

case class Staff(
                  _id: Option[BSONObjectID] = None,
                  firstName: String,
                  lastName: String,
                  gender: String,
                  dateOfBirth: DateTime,
                  function: String,
                  /* pere: String,
                   mere: String,
                   creationDate: Option[DateTime] = Some(DateTime.now()),
                   deletionDate: Option[DateTime] = None*/
                )

object StaffFormat {
  import json.DateTimeJsonFormat._
  import json.ObjectIdJsonFormat._
  implicit val staffFormat = Json.using[Json.WithDefaultValues].format[Staff]
  def format()(implicit objectIdFormat: Format[BSONObjectID]) = Json.using[Json.WithDefaultValues].format[Staff]
}

object StaffBSONFormat {
  import json.DateTimeJsonFormat._
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat
  implicit val staffFormat = Json.using[Json.WithDefaultValues].format[Staff]
}
