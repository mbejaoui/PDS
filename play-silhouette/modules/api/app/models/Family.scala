package models

import org.joda.time.DateTime
import play.api.libs.json.{ Format, Json }
import reactivemongo.bson.BSONObjectID



case class Family(
                  _id: Option[BSONObjectID] = None,
                  fatherFirstName: String,
                  motherFirstName: String,
                  fatherPhoneNumber: String,
                  motherPhoneNumber: String,
                  fatherEmail: String,
                  motherEmail: String,
                  address: String,


                  /* pere: String,
                   mere: String,
                   creationDate: Option[DateTime] = Some(DateTime.now()),
                   deletionDate: Option[DateTime] = None*/
                )

object FamilyFormat {
  import json.DateTimeJsonFormat._
  import json.ObjectIdJsonFormat._
  implicit val familyFormat = Json.using[Json.WithDefaultValues].format[Family]
  def format()(implicit objectIdFormat: Format[BSONObjectID]) = Json.using[Json.WithDefaultValues].format[Family]
}

object FamilyBSONFormat {
  import json.DateTimeJsonFormat._
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat
  implicit val familyFormat = Json.using[Json.WithDefaultValues].format[Family]
}
