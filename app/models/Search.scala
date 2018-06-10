package models.search

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._

/**
 * Presentation object used for displaying data in a template.
 *
 * Note that it's a good practice to keep the presentation DTO,
 * which are used for reads, distinct from the form processing DTO,
 * which are used for writes.
 */
case class Entry(zipcode: String) {
	def validateZipcode(entry: Entry): Option[Entry] = {
		val pattern = "\\d{5}|\\d{5}-\\d{4}".r
		entry.zipcode match {
			case pattern(_*) => Some(entry)
			case _ => None
		}
	}
	def validate(entry: Entry): Option[Entry] = validateZipcode(entry)
}

case class Search(entry: Entry) {
	val sql = sql"""
		select 
			address
		,	zipcode
		,	latitude
		,	longitude
		from rentcontrol.listing
		where zipcode = ${entry.zipcode}
	""".query[Listing]
}

case class Listing(
	address: String, 
	zipcode: String, 
	latitude: Double,
	longitude: Double
)
