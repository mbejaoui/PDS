package controllers
import utils.{ AuthController, MyEnv, WithService }
import services.ChildService
import models.Child
import com.mohiva.play.silhouette.api.Silhouette
import javax.inject.{ Inject, Singleton }
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

@Singleton
case class ChildController @Inject() (
    childService: ChildService,
    silhouette: Silhouette[MyEnv]
) extends InjectedController with AuthController with I18nSupport {
  import models.Role._
  import models.ChildFormat._

  def newChild = Action.async(parse.json) { request =>
    childService.newChild(request.body.as[Child]).map(_ => Created)
  }
  def getChild(childId: String) = SecuredAction(WithService(childRead)).async { request =>
    childService.getChild(childId).map {
      case Some(child) => Ok(Json.toJson(child))
      case _ => NotFound
    }
  }
  def getChildren() = Action.async { request =>
    childService.getAllChildren.map { children =>
      Ok(Json.toJson(children))
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
        InternalServerError
    }
  }
  def updateChild(childId: String) = SecuredAction(WithService(childWrite)).async(parse.json) { request =>
    childService.updateChild(childId, request.body.as[Child]).map(_ => Created)
  }
  def deleteChild(childId: String) = SecuredAction(WithService(childDelete)).async {
    childService.deleteChild(childId).map { _ =>
      Ok("Child deleted Successfully")
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
        InternalServerError
    }
  }

}
