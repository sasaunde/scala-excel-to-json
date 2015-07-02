package api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import core.ProjectDataActor.ProjectData
import core.ProjectDataActor.TeamData
import core.ProjectUsers
import core.ProjectUser
import core.UserByGrade
import spray.http.HttpRequest
import spray.routing.directives.LogEntry
import akka.event.Logging.LogLevel
import akka.util.Timeout
import spray.json.DefaultJsonProtocol._
import spray.http.StatusCodes
import spray.routing.directives.PathDirectives.Segment
import core.ProjectUser
import spray.routing.HttpService

class ProjectDataService(projectData: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats { 

  import spray.http.HttpHeaders._
  import akka.pattern.ask
  
  import scala.concurrent.duration._  
  import akka.event.Logging._
  import spray.routing.directives.LogEntry
  import spray.routing.Directives._
  import spray.routing.Directive0

  
  implicit val projectDataFormat = jsonFormat4(ProjectData)
  implicit val projectUserFormat = jsonFormat7(ProjectUser)
  implicit val userByGradeFormat = jsonFormat2(UserByGrade)
  
  implicit val timeout = Timeout(2.seconds)
  
  
  def createLogEntry(request: HttpRequest, text: String): Some[LogEntry] = {
    println("#### Request " + request + " => " + text);Some(LogEntry("#### Request " + request + " => " + text, DebugLevel))
  }
  
  def myLog(request: HttpRequest): Any => Option[LogEntry] = {
    case x => createLogEntry(request,   x.toString())
  }
  
  val fileLocation = "D:\\Users\\sasaunde\\Documents\\WhereAreDsu\\DSUAPT19thJan.xlsx"
  
  val pData = new ProjectData(fileLocation)

  val route = (path("person") & get) {logRequestResponse(myLog _) {

     complete {
        (projectData ? pData).mapTo[List[ProjectUser]]
      }

  }
  } ~ path("person" / IntNumber) { id => get { logRequestResponse(myLog _) {

		 complete {
			  (projectData ? new ProjectData(fileLocation, id)).mapTo[List[ProjectUser]]
		  }

  }}
  } ~ path("people" / "graph" / "[A-Za-z0-9]+".r) {
      projectCode => get {
       
           complete {
            (projectData ? new ProjectData(fileLocation, -1, projectCode)).mapTo[UserByGrade]
            }
      }
  } ~ (path("people" / "[A-Za-z0-9]+".r) & get) { projectCode => logRequestResponse(myLog _) {
				 
					  complete {
						  (projectData ? new ProjectData(fileLocation, 0, projectCode)).mapTo[List[ProjectUser]]
					  }
				  
			  }
		  
	} ~ (path("people" / "[A-Za-z0-9]+".r) & (options)) { projectCode =>
    logRequestResponse(myLog _) {
        
          complete(StatusCodes.OK)
        
    }
  } ~ (path("people" / "[A-Za-z0-9]+".r) & (post)) { projectCode =>
    
    logRequestResponse(myLog _) {
     
        handleWith { 
          
          user : ProjectUser => (projectData ? new ProjectData(fileLocation, 0, user.projectCode, user.level)).mapTo[List[ProjectUser]]
         
        }
      
    }
  } ~ (path("teams" / "[A-Z]+".r) & (get)) { teamCode =>
  
    logRequestResponse(myLog _) {
      complete {
        (projectData ? new TeamData(fileLocation, teamCode)).mapTo[List[ProjectUser]]
      }
    }
  }
  
 
}