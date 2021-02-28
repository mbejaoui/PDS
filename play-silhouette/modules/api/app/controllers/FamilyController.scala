package controllers
import utils.{ AuthController, MyEnv, WithService }
import services.FamilyService
import models.Family
import com.mohiva.play.silhouette.api.Silhouette
import javax.inject.{ Inject, Singleton }
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

@Singleton
case class FamilyController @Inject() (
    familyService: FamilyService,
    silhouette: Silhouette[MyEnv]
) extends InjectedController with AuthController with I18nSupport {

  import models.Role._
  import models.FamilyFormat._

  def newFamily = Action.async(parse.json) { request =>
    familyService.newFamily(request.body.as[Family]).map(_ => Created)
  }
  def getFamily(familyId: String) = SecuredAction(WithService(familyRead)).async { request =>
    familyService.getFamily(familyId).map {
      case Some(family) => Ok(Json.toJson(family))
      case _ => NotFound
    }
  }
  def getFamilies() = Action.async { request =>
    familyService.getAllFamilies.map { families =>
      Ok(Json.toJson(families))
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
        InternalServerError
    }
  }
  def updateFamily(familyId: String) = SecuredAction(WithService(familyWrite)).async(parse.json) { request =>
    familyService.updateFamily(familyId, request.body.as[Family]).map(_ => Created)
  }
  def deleteFamily(familyId: String) = SecuredAction(WithService(familyDelete)).async {
    familyService.deleteFamily(familyId).map { _ =>
      Ok("family deleted Successfully")
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
        InternalServerError
    }
  }

}
