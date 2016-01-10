package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.mapping.{Failure, Success}
import play.api.libs.json._
import play.api.mvc._
import com.eclipsesource.schema._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import scala.util.{Try, Success => TrySuccess, Failure => TryFailure}

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
    println("LOL")
    validationRequestForms.bindFromRequest.fold(
      // errors occurred
      formWithErrors =>{ println(formWithErrors); BadRequest(views.html.index(formWithErrors)) },
      // valid form
      validationRequest => {
        println(s"schema is ${validationRequest.schema}")
        println(s"instance is ${validationRequest.instance}")
        Try {
          (Json.parse(validationRequest.schema), Json.parse(validationRequest.instance))
        } match {
          case TrySuccess((schema, instance)) =>
            schema.validate[SchemaType] match {
              case JsSuccess(validSchema, _) =>
                SchemaValidator.validate(validSchema)(instance) match {
                  case Success(validInstance) => Ok(views.html.index(validationRequestForms.fill(validationRequest)))
                  case Failure(errors) =>
                    okWithErrors(validationRequest, errors.toJson)
                }
              case JsError(invalidSchema) => okWithErrors(validationRequest, Json.arr("Invalid JSON schema"))
            }
          case TryFailure(throwable) => okWithErrors(validationRequest, Json.arr(throwable.getMessage))
        }
      }
    )
  }



  private def okWithErrors(validationRequest: ValidationRequest, errors: JsValue): Result =
    Ok(views.html.index(fillFormAndDisplayErrors(validationRequest, errors)))

  private def fillFormAndDisplayErrors(data: ValidationRequest, errors: JsValue): Form[ValidationRequest] = {
    val form = validationRequestForms.fill(
      ValidationRequest(data.schema, data.instance)
    )
    form.withGlobalError(Json.prettyPrint(errors))
  }
}