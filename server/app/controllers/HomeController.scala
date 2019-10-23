package controllers


import akka.stream.scaladsl.Sink
import akka.util.Timeout
import play.api.Configuration
import play.api.mvc._
import javax.inject._
import org.joda.time.LocalTime
import play.api.libs.EventSource
import play.api.libs.json.{JsError, Reads}
import play.api.libs.json.Json
import util._
import play.api.libs.ws._


import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               config: Configuration,
                               ws: WSClient)
  extends AbstractController(cc) {


  def index = Action{
    ///val check = Vector(1,2,3,4)
    //Ok(views.html.index("blub"))
    import blog._
    val first = BlogEntry(java.time.LocalDateTime.now(),Tags(WebDev),"Setting up my first programming blog","stuff")
    val second = BlogEntry(java.time.LocalDateTime.now() ,Tags(WebDev),"Setting up my first programming blog Part 2","more stuff")
    val site = Blog(first,second)


    Ok(views.html.scalatag(blog.TestPage.page.render))
  }
}
