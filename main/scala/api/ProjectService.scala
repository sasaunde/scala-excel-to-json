package api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import spray.routing.HttpService
import spray.http.HttpRequest
import spray.routing.directives.LogEntry
import akka.event.Logging._
import spray.json.DefaultJsonProtocol._

class ProjectService(project : ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import core._
  import core.ProjectActor._
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._ 
  
  implicit val projectInfoDataFormat = jsonFormat2(ProjectInfo)
  implicit val projectsDataFormat = jsonFormat2(Projects.apply)
  
  implicit val timeout = Timeout(2.seconds)
  
  def createLogEntry(request: HttpRequest, text: String): Some[LogEntry] = {
    println("#### Request " + request + " => " + text);Some(LogEntry("#### Request " + request + " => " + text, DebugLevel))
  }
  
  def myLog(request: HttpRequest): Any => Option[LogEntry] = {
    case x => createLogEntry(request,   x.toString())
  }
  
  val fileLocation = "D:\\Users\\sasaunde\\Documents\\WhereAreDsu\\DSUAPT19thJan.xlsx"
  
  val route = (path("pie") & get) {
    logRequestResponse(myLog _) {
      
        complete {
          (project ? new Project(fileLocation)).mapTo[Projects]
        }
    }
  } ~ (path("teams") & get) {
    logRequestResponse(myLog _) {
      complete {
        (project ? new Team(fileLocation)).mapTo[Projects]
      }
    }
  }
}