package services
import models.Child
import repositories.ChildRepository
import javax.inject.{ Inject, Singleton }
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ChildService @Inject() (
    childRepository: ChildRepository

) {
  val logger = LoggerFactory.getLogger(getClass)
  import models.ChildBSONFormat._

  def newChild(child: Child) = {
    logger.trace(s"insert new child: $child")
    childRepository.insert(child).map { _ =>

    }
  }

  def getAllChildren = {
    logger.trace(s"get All children")
    childRepository.findAll()
  }

  def getChild(childId: String) = {
    logger.trace(s"get child with id $childId")
    childRepository.findById(childId: String)
  }

  def deleteChild(childId: String) = {
    logger.trace(s"delete child with id $childId")
    childRepository.remove(childId)
  }

  def updateChild(childId: String, child: Child) = {
    logger.trace(s"update child: $child")
    childRepository.update(BSONObjectID.parse(childId).get, child)
  }

}
