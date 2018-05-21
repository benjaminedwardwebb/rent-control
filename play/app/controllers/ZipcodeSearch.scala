package controllers

import play.api.Logger


object ZipcodeSearch {
	case class Data(zipcode: String)
	/*
	import play.api.data.Forms._
	import play.api.data.Form
	*/
	import play.api.data._
	import play.api.data.Forms._
	import play.api.data.validation.Constraints._

	/**
	* A form processing DTO that maps to the form below.
	*
	* Using a class specifically for form binding reduces the chances
	* of a parameter tampering attack and makes code clearer.
	*/

	def validate(zipcode: String): Option[Data] = {
		Logger debug "validating..."
		val pattern = "\\d{5}".r
		zipcode match {
			case pattern(_*) => {
				Logger debug "matched!"
				Some(Data(zipcode))
			}
			case _ => {
				Logger debug "no match"
				None
			}
		}
	}

	/**
	* The form definition for the "create a widget" form.
	* It specifies the form fields and their types,
	* as well as how to convert from a Data to form data and vice versa.
	*/
	val form = Form(
		mapping(
			"zipcode" -> text
		)(Data.apply)(Data.unapply) verifying(
			"Invalid", 
			fields => fields match {
				case data => {
					Logger debug "about to validate..."
					validate(data.zipcode).isDefined
				}
			}
		)
	)
}
