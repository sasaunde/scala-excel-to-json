package api

import spray.routing.Directives
import scala.concurrent.ExecutionContext
import akka.actor.ActorRef
import core.{User, RegistrationActor}
import akka.util.Timeout
import RegistrationActor._
import spray.http._
import core.User
import core.RegistrationActor.Register
import scala.Some
import spray.routing.HttpService

class RegistrationService(registration: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  case class ImageUploaded(size: Int)

  import akka.pattern.ask
  import scala.concurrent.duration._  
  import akka.event.Logging._
  import spray.routing.directives.LogEntry
  import spray.routing._
  
  
  implicit val timeout = Timeout(2.seconds)

  implicit val userFormat = jsonFormat6(User)
  implicit val registerFormat = jsonFormat1(Register)
  implicit val registeredFormat = jsonObjectFormat[Registered.type]
  implicit val notRegisteredFormat = jsonObjectFormat[NotRegistered.type]
  implicit val imageUploadedFormat = jsonFormat1(ImageUploaded)

  implicit object EitherErrorSelector extends ErrorSelector[NotRegistered.type] {
    def apply(v: NotRegistered.type): StatusCode = StatusCodes.BadRequest
  }

   def createLogEntry(request: HttpRequest, text: String): Some[LogEntry] = {
    println("#### Request " + request + " => " + text);Some(LogEntry("#### Request " + request + " => " + text, DebugLevel))
  }

  def myLog(request: HttpRequest): Any => Option[LogEntry] = {
//    case x: HttpResponse => {
//      x.entity match {
//        case e: HttpBody => {
//          if (e.contentType.mediaType.binary) {
//            createLogEntry(request,   x.status + " " + e.contentType)
//          }
//          else if (e.contentType.mediaType.subType.contains("json")) {
//            createLogEntry(request,   x.toString())
//          }
//          else {
//            createLogEntry(request,   x.status + " " + e.contentType)
//          }
//        }
//        case _ => createLogEntry(request,   x.toString())
//      }
//    } // log response
//    case Rejected(rejections) => createLogEntry(request,   " Rejection " + rejections.toString())
    case x => createLogEntry(request,   x.toString())
  }
  
  
  
  val route =
    path("register") {
      post {
        handleWith { ru: Register => (registration ? ru).mapTo[Either[NotRegistered.type, Registered.type]] }
      }
    } ~
//    path("register" / "image") {
//      post {
//        handleWith { data: MultipartFormData =>
//          data.fields.get("files[]") match {
//            case Some(imageEntity) =>
//              val size = imageEntity.entity.buffer.length
//              println(s"Uploaded $size")
//              ImageUploaded(size)
//            case None =>
//              println("No files")
//              ImageUploaded(0)
//          }
//        }
//      }
//    } ~    
    path("register" / IntNumber) { id => get { logRequestResponse(myLog _) {
     
      complete {
      new Register(
          new User("SASAUNDE", "Sarah", "Saunders1", "Sarah.Saunders@ymail.com",  "UK-SAE-SALECHES", Uri("http://mysite.comm"))
          )}
      
    }}
    } ~
    path("register") { get { 
     		
    			complete {
    				new Register(
    						new User("SASAUNDE", "Sarah", "Saunders", "Sarah.Saunders@ymail.com",  "UK-SAE-SALECHES", Uri("http://mysite.comm"))
    						)}    		
    	}}
     

}
