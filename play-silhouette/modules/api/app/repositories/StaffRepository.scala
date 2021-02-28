package repositories
import models.Staff
import javax.inject.{ Inject, Singleton }
import org.slf4j.LoggerFactory
import play.modules.reactivemongo.ReactiveMongoApi

@Singleton
case class StaffRepository @Inject() (reactiveMongoApi: ReactiveMongoApi) extends AbstractRepository[Staff] {
  import reactivemongo.api._
  import reactivemongo.play.json._

  val logger = LoggerFactory.getLogger(this.getClass)

  val collectionName = "staffs"

}
