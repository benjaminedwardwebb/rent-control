package browser

import org.scalatestplus.play._
import org.scalatestplus.play.guice._

/**
 * To get the full round trip experience, you can use ScalaTest with Selenium.
 *
 * For browser testing, you need both a server (here "GuiceOneServerPerSuite") and a
 * browser driver (here "HtmlUnitFactory") to get running.
 *
 * The syntax comes from Scalatest's WebBrowser class, and for more details you can see:
 *
 * http://www.scalatest.org/user_guide/using_selenium
 */
class BrowserSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {

  "The browser should" must {
    "successfully process a form" in {
      val mapURL = controllers.routes.SearchController.map().absoluteURL(false, s"localhost:$port")

      go to mapURL

      // Enter in the form fields...
      textField("zipcode").value = "11206"

      // Press enter button...
      submit()

      // Wait for server to process...
      eventually {
        // Check to see that a marker is added in the Leaflet javascript
        pageSource contains "L.marker"
      }
    }
  }
}
