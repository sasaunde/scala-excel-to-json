package api

import spray.routing.Directives
import spray.routing.HttpService
import scala.concurrent.ExecutionContext
import akka.actor.ActorRef
import core.{User, RegistrationActor}
import akka.util.Timeout
import RegistrationActor._
import spray.http._
import spray.httpx.SprayJsonSupport._

import spray.json.DefaultJsonProtocol._
import core.User
import scala.Some
import core.DirectorySearchActor.SearchUser


class DirectoryService(directory: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats 
  {

  
  import spray.http.HttpHeaders._
  import akka.pattern.ask
  
  import scala.concurrent.duration._  
  import akka.event.Logging._
  import spray.routing.directives.LogEntry
  import spray.routing.Directives._
  import spray.routing.Directive0
  
  implicit val timeout = Timeout(2.seconds)

  implicit val searchUserFormat = jsonFormat2(SearchUser)
  implicit val userFormat = jsonFormat6(User)
  
  def createLogEntry(request: HttpRequest, text: String): Some[LogEntry] = {
    println("#### Request " + request + " => " + text);Some(LogEntry("#### Request " + request + " => " + text, DebugLevel))
  }

  def myLog(request: HttpRequest): Any => Option[LogEntry] = {
    case x => createLogEntry(request,   x.toString())
  }
  
  
  val route = 
    
    (path("find") & options){
      
      logRequestResponse(myLog _) {
        
          complete(StatusCodes.OK)
       
      }
    } ~ (path("find"/ "[A-Z]+".r) & options){ id =>
      
      logRequestResponse(myLog _) {
        
          complete(StatusCodes.OK)
             }
    } ~ (path("find" / "[A-Z]+".r) & (post)) { id =>
  
	    logRequestResponse(myLog _) {
		  
			  handleWith { 
          
          uri : SearchUser => (directory ? new SearchUser(uri.uri,uri.id)).mapTo[User]
         
        }
		  
    }
  } ~ (path("find") & get) {
    logRequestResponse(myLog _) {
      
      complete("OK")
      
    }
  }

}