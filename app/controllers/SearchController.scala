package controllers

import javax.inject.Inject

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._

import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.mvc._

import models.search._
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
	// db connection
	val xa = Transactor.fromDriverManager[IO](
		"org.postgresql.Driver",
		"jdbc:postgresql:rentcontrol",
		"rentcontrol",
		"rentcontrol"
	)

	val searchForm = Form(
		mapping(
			"zipcode" -> text
		)(Entry.apply)(Entry.unapply) verifying(
			"Invalid input",
			fields => fields match {
				case entry => entry.validate(Entry(entry.zipcode)).isDefined
			}
		)
	)

	private val postUrl = routes.SearchController.search()

	// This will be the action that handles our form post
	def search = Action { implicit request: MessagesRequest[AnyContent] =>
		Logger debug "searching ..."

		val invalid = { formWithErrors: Form[Entry] =>
			Logger debug "invalid input, highlighting errors ..."
			BadRequest(views.html.Map(formWithErrors, postUrl))
		}

		val valid = { entry: Entry =>
			Logger debug "valid input, searching ..."

			val result: List[Listing] = Search(entry)
				.sql
				.to[List]
				.transact(xa)
				.unsafeRunSync

			Logger debug result.mkString(", ")
			Ok(views.html.Map(searchForm, postUrl, result))
			//Redirect(routes.SearchController.listSearch(form, postUrl, address))
		}

		searchForm.bindFromRequest.fold(
			invalid, 
			valid
		)
	}

	def map = Action { implicit request: MessagesRequest[AnyContent] =>
		Ok(views.html.Map(searchForm, postUrl))
	}

	def about = Action { implicit request: MessagesRequest[AnyContent] =>
		Ok(views.html.About(searchForm, postUrl))
	}

	def index = Action { implicit request: MessagesRequest[AnyContent] =>
		Ok(views.html.index(searchForm, postUrl))
	}
}
