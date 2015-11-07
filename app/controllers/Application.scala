package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.mapping.{Failure, Success}
import play.api.libs.json._
import play.api.mvc._
import com.eclipsesource.schema._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import scala.util.Try

object Application extends Controller {

  val validationRequestForms = Form(
    mapping(
      "schema" -> text,
      "instance" -> text
    )(ValidationRequest.apply)(ValidationRequest.unapply)
  )

  def index = Action { Ok(views.html.index(validationRequestForms)) }

  def validate = Action { implicit request =>
    validationRequestForms.bindFromRequest.fold(
      // errors occurred
      formWithErrors => BadRequest(views.html.index(formWithErrors)),
      // valid form
      validationRequest =>
        Try {
          (Json.parse(validationRequest.schema), Json.parse(validationRequest.instance))
        }.map { case (schema, instance) =>
          schema.validate[SchemaType] match {
            case JsSuccess(validSchema, _) =>
              SchemaValidator.validate(validSchema)(instance) match {
                case Success(validInstance) => Ok(views.html.index(validationRequestForms.fill(validationRequest)))
                case Failure(errors) => okWithErrors(validationRequest, errors.toJson)
              }
            case JsError(invalidSchema) => okWithErrors(validationRequest, Json.arr("Invalid JSON schema"))
          }
        }.recover { case throwable => okWithErrors(validationRequest, Json.arr(throwable.getMessage)) }
         .get
    )
  }

  private def okWithErrors(validationRequest: ValidationRequest, errors: JsArray): Result =
    Ok(views.html.index(fillFormAndDisplayErrors(validationRequest, errors)))

  private def fillFormAndDisplayErrors(data: ValidationRequest, errors: JsArray): Form[ValidationRequest] = {
    val form = validationRequestForms.fill(
      ValidationRequest(data.schema, data.instance)
    )
    form.withGlobalError(Json.prettyPrint(errors))
  }
}