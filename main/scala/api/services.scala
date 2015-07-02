package api

import spray.http.StatusCodes._
import spray.routing._
import directives.{RouteDirectives}
import spray.util.{SprayActorLogging, LoggingContext}
import util.control.NonFatal
import spray.httpx.marshalling.{Marshaller, ToResponseMarshaller}
import spray.http.HttpHeaders.RawHeader
import akka.actor.{ActorLogging, Actor}
import spray.http.{HttpMethods, HttpMethod, HttpResponse, AllOrigins, StatusCode, HttpEntity}
import spray.http.HttpHeaders._
import spray.http.HttpMethods._


/**
 * Holds potential error response with the HTTP status and optional body
 *
 * @param responseStatus the status code
 * @param response the optional body
 */
case class ErrorResponseException(responseStatus: StatusCode, response: Option[HttpEntity]) extends Exception


/**
 * Allows you to construct Spray ``HttpService`` from a concatenation of routes; and wires in the error handler.
 * It also logs all internal server errors using ``SprayActorLogging``.
 *
 * @param route the (concatenated) route
 */
class RoutedHttpService(route: Route) extends HttpServiceActor with CORSSupport with SprayActorLogging {

  
  
  implicit def handler(implicit log: LoggingContext)  = ExceptionHandler {
    case e: IllegalArgumentException => requestUri { uri =>
      log.error("The server was asked a question that didn't make sense: " + e.getMessage, uri)
      complete(InternalServerError)
    }
    case f: NoSuchElementException => requestUri { uri =>
      log.error("The server is missing some information. Try again in a few moments.", uri)
        complete(NotFound)
    }
    case t: Throwable => requestUri { uri =>
      // note that toString here may expose information and cause a security leak, so don't do it.
      log.error(t.getMessage, uri)
      complete(t)
    }
  }



  def receive: Receive =
    runRoute(cors1(route))(handler, RejectionHandler.Default, context, RoutingSettings.default, LoggingContext.fromActorRefFactory)


}


trait CORSSupport {
  this: HttpService =>
  
  private val allowOriginHeader = `Access-Control-Allow-Origin`(AllOrigins)
  private val optionsCorsHeaders = List(
    `Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"),
    `Access-Control-Max-Age`(1728000))
    
    
  def cors1[T]: Directive0 = mapRequestContext { ctx => ctx.withHttpResponseHeadersMapped { 
    headers => allowOriginHeader :: optionsCorsHeaders ::: headers}  
  }
 
  
  // Copied the below.. not sure if it does the same as the above... not sure about the options thing
  // if works change cors1(route) to cors(route) above in runRoute()
  def cors[T]: Directive0 = mapRequestContext { ctx => ctx.withRouteResponseHandling({
    //It is an option requeset for a resource that responds to some other method
    case Rejected(x) if (ctx.request.method.equals(HttpMethods.OPTIONS) && !x.filter(_.isInstanceOf[MethodRejection]).isEmpty) => {
      val allowedMethods: List[HttpMethod] = x.filter(_.isInstanceOf[MethodRejection]).map(rejection=> {
        rejection.asInstanceOf[MethodRejection].supported
      })
      ctx.complete(HttpResponse().withHeaders(
        `Access-Control-Allow-Methods`(OPTIONS, allowedMethods :_*) ::  allowOriginHeader ::
         optionsCorsHeaders
      ))
    }
  }).withHttpResponseHeadersMapped { headers =>
    allowOriginHeader :: optionsCorsHeaders ::: headers
 
  }
  }
}
