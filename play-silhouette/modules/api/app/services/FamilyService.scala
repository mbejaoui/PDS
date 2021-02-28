package services
import models.Family
import repositories.FamilyRepository
import javax.inject.{ Inject, Singleton }
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class FamilyService @Inject() (
    familyRepository: FamilyRepository

) {
  val logger = LoggerFactory.getLogger(getClass)
  import models.FamilyBSONFormat._

  def newFamily(family: Family) = {
    familyRepository.insert(family).map { _ =>
    }
  }

  def getAllFamilies = {
    logger.trace(s"get All families")
    familyRepository.findAll()
  }

  def getFamily(familyId: String) = {
    familyRepository.findById(familyId: String)
  }

  def deleteFamily(familyId: String) = {
    familyRepository.remove(familyId)
  }

  def updateFamily(familyId: String, family: Family) = {
    familyRepository.update(BSONObjectID.parse(familyId).get, family)
  }

}
