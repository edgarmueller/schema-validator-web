package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import com.eclipsesource.schema._
import play.api.Play.current
import play.api.data.mapping.{Failure, Success}
import play.api.i18n.Messages.Implicits._
import scala.util.{Try, Failure => TryFailure, Success => TrySuccess}

object Application extends Controller {

  val validationRequestForms = Form(
    mapping(
      "schema" -> text,
      "instance" -> text
    )(ValidationRequest.apply)(ValidationRequest.unapply)
  )

  def index = Action {
    Ok(views.html.index(validationRequestForms.fill(ValidationRequest("{}", "{}"))))
  }

  def validate = Action { implicit request =>
    validationRequestForms.bindFromRequest.fold(
      // errors occurred
      formWithErrors =>{ println(formWithErrors); BadRequest(views.html.index(formWithErrors)) },
      // valid form
      validationRequest => {
        Try {
          (Json.parse(validationRequest.schema), Json.parse(validationRequest.instance))
        } match {
          case TrySuccess((schema, instance)) =>
            schema.validate[SchemaType] match {
              case JsSuccess(validSchema, _) =>
                SchemaValidator.validate(validSchema)(instance) match {
                  case Success(validInstance) =>
                    Ok(validInstance)
                  case Failure(errors) =>
                    Ok(errors.toJson)
                }
              case JsError(invalidSchema) =>  Ok(Json.arr("Invalid JSON schema"))
            }
          case TryFailure(throwable) => Ok(Json.arr(throwable.getMessage))
        }
      }
    )
  }
}