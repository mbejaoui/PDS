package repositories
import models.Family
import javax.inject.{ Inject, Singleton }
import org.slf4j.LoggerFactory
import play.modules.reactivemongo.ReactiveMongoApi

@Singleton
case class FamilyRepository @Inject() (reactiveMongoApi: ReactiveMongoApi) extends AbstractRepository[Family] {
  import reactivemongo.api._
  import reactivemongo.play.json._

  val logger = LoggerFactory.getLogger(this.getClass)

  val collectionName = "families"

}
