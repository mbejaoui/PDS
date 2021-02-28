package services
import models.Staff
import repositories.StaffRepository
import javax.inject.{ Inject, Singleton }
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class StaffService @Inject() (
    staffRepository: StaffRepository

) {
  val logger = LoggerFactory.getLogger(getClass)
  import models.StaffBSONFormat._

  def newStaff(staff: Staff) = {
    staffRepository.insert(staff).map { _ =>
    }
  }

  def getAllStaffs = {
    staffRepository.findAll()
  }

  def getStaff(staffId: String) = {
    staffRepository.findById(staffId: String)
  }

  def deleteStaff(staffId: String) = {
    staffRepository.remove(staffId)
  }

  def updateStaff(staffId: String, staff: Staff) = {
    staffRepository.update(BSONObjectID.parse(staffId).get, staff)
  }

}
