package repositories
import models.Child
import javax.inject.{ Inject, Singleton }
import org.slf4j.LoggerFactory
import play.modules.reactivemongo.ReactiveMongoApi

@Singleton
case class ChildRepository @Inject() (reactiveMongoApi: ReactiveMongoApi) extends AbstractRepository[Child] {
  import reactivemongo.api._
  import reactivemongo.play.json._

  val logger = LoggerFactory.getLogger(this.getClass)

  val collectionName = "childs"

}
