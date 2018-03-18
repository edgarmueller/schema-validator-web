package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import com.eclipsesource.schema._
import play.api.i18n._

import scala.io.Source

class Application  @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  val validationRequestForms = Form(
    mapping(
      "schema" -> text,
      "instance" -> text
    )(ValidationRequest.apply)(ValidationRequest.unapply)
  )

  def index = Action { implicit request =>
    Ok(views.html.index(validationRequestForms.fill(ValidationRequest("{}", "{}"))))
  }

  def validate = Action { implicit request =>
    val validator = SchemaValidator()
    validationRequestForms.bindFromRequest.fold(
      // errors occurred
      formWithErrors =>{ BadRequest(views.html.index(formWithErrors)) },
      // valid form
      validationRequest => {
        validator.validate(Source.fromString(validationRequest.schema), Json.parse(validationRequest.instance)) match {
          case JsSuccess(validInstance, _) => Ok(validInstance)
          case JsError(errors) => Ok(errors.toJson)
        }
      }
    )
  }
}
