package controllers
import utils.{ AuthController, MyEnv, WithService }
import services.StaffService
import models.Staff
import com.mohiva.play.silhouette.api.Silhouette
import javax.inject.{ Inject, Singleton }
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

@Singleton
case class StaffController @Inject() (
    staffService: StaffService,
    silhouette: Silhouette[MyEnv]
) extends InjectedController with AuthController with I18nSupport {

  import models.Role._
  import models.StaffFormat._

  def newStaff = Action.async(parse.json) { request =>
    staffService.newStaff(request.body.as[Staff]).map(_ => Created)
  }
  def getStaff(staffId: String) = SecuredAction(WithService(staffRead)).async { request =>
    staffService.getStaff(staffId).map {
      case Some(staff) => Ok(Json.toJson(staff))
      case _ => NotFound
    }
  }
  def getStaffs() = Action.async { request =>
    staffService.getAllStaffs.map { staffs =>
      Ok(Json.toJson(staffs))
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
        InternalServerError
    }
  }
  def updateStaff(staffId: String) = SecuredAction(WithService(staffWrite)).async(parse.json) { request =>
    staffService.updateStaff(staffId, request.body.as[Staff]).map(_ => Created)
  }
  def deleteStaff(staffId: String) = SecuredAction(WithService(staffDelete)).async {
    staffService.deleteStaff(staffId).map { _ =>
      Ok("staff deleted Successfully")
    }.recover {
      case NonFatal(e) =>
        e.printStackTrace()
        InternalServerError
    }
  }

}
