package controllers

import javax.inject.Inject

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._

//import models.Widget
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.mvc._

case class Zipcode(zipcode: String)
/**
 * The classic WidgetController using MessagesAbstractController.
 *
 * Instead of MessagesAbstractController, you can use the I18nSupport trait,
 * which provides implicits that create a Messages instance from a request
 * using implicit conversion.
 *
 * See https://www.playframework.com/documentation/2.6.x/ScalaForms#passing-messagesprovider-to-form-helpers
 * for details.
 */
class SearchController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
	def validate(zipcode: String): Option[Zipcode] = {
		Logger debug "validating..."
		val pattern = "\\d{5}".r
		zipcode match {
			case pattern(_*) => {
				Logger debug "matched!"
				Some(Zipcode(zipcode))
			}
			case _ => {
				Logger debug "no match"
				None
			}
		}
	}
	val form = Form(
		mapping(
			"zipcode" -> text
		)(Zipcode.apply)(Zipcode.unapply) verifying(
			"Invalid",
			fields => fields match {
				case data => validate(data.zipcode).isDefined
			}
		)
	)

  // db connection
  val xa = Transactor.fromDriverManager[IO](
	  "org.postgresql.Driver",
	  "jdbc:postgresql:rentcontrol",
	  "rentcontrol",
	  "rentcontrol"
  )

  // The URL to the widget.  You can call this directly from the template, but it
  // can be more convenient to leave the template completely stateless i.e. all
  // of the "WidgetController" references are inside the .scala file.
  private val postUrl = routes.SearchController.search()

  def index = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index(form, postUrl))
  }

  def listSearch = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.zipcode(form, postUrl))
  }

  // This will be the action that handles our form post
  def search = Action { implicit request: MessagesRequest[AnyContent] =>
	Logger.debug("search")

    val errorFunction = { formWithErrors: Form[Zipcode] =>
		Logger debug "error"
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.zipcode(formWithErrors, postUrl))
    }

    val successFunction = { zipcode: Zipcode =>
		// This is the good case, where the form was successfully parsed as a Data object.
		Logger debug "success"

		// query postgres for listings w/ zipcode
		def whereZipcode(zipcode: String) = sql"""
			select address 
			from rentcontrol.listing 
			where zipcode = $zipcode
		""".query[String]
		
		val address = whereZipcode(zipcode.zipcode).to[List].transact(xa).unsafeRunSync
		//val address = List(zipcode.zipcode)
		Logger.debug(address.mkString(", "))
	  	Ok(views.html.zipcode(form, postUrl, address))
		//Redirect(routes.SearchController.listSearch(form, postUrl, address))
		/*
		val widget = Widget(name = data.name, price = data.price)
		widgets.append(widget)

		*/
    }

    val formValidationResult = form.bindFromRequest
	formValidationResult.fold(
		errorFunction, 
		successFunction
	)
  }
}
