package functional

import controllers.{SearchController, routes}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

/**
 * Functional specification that has a running Play application.
 *
 * This is good for testing filter functionality, such as CSRF token and template checks.
 *
 * See https://www.playframework.com/documentation/2.6.x/ScalaFunctionalTestingWithScalaTest for more details.
 */
class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting with ScalaFutures {
	// CSRF token helper adds "withCSRFToken" to FakeRequest:
	// https://www.playframework.com/documentation/2.6.x/ScalaCsrf#Testing-CSRF
	import CSRFTokenHelper._

	"SearchController" must {
		/*
		"process a POST request successfully" in {
			// Pull the controller from the already running Play application, 
			// using Injecting
			val controller = inject[SearchController]

			// Call using the FakeRequest and the correct body information 
			// and CSRF token
			val request = FakeRequest(routes.SearchController.search())
				.withFormUrlEncodedBody("zipcode" -> "11206")
				.withCSRFToken
			val futureResult: Future[Result] = controller.search()
				.apply(request)

			// And we can get the results out using Scalatest's "Futures" trait, 
			// which gives us whenReady
			whenReady(futureResult) { result =>
				result.header.headers(STATUS) must equal(
					routes.SearchController.search().url
				)
					//LOCATION = routes.SearchController.map().url
			}
		}
		*/

		"reject a POST request when given bad input" in {
			val controller = inject[SearchController]

			// Call the controller with negative price...
			val request = FakeRequest(routes.SearchController.search())
				.withFormUrlEncodedBody("name" -> "112061")
				.withCSRFToken
			val futureResult: Future[Result] = controller.search().apply(request)

			status(futureResult) must be(Status.BAD_REQUEST)
		}
	}
}
