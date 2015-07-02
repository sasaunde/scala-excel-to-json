package api


import akka.actor._

import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.routing._

import spray.routing.HttpService
import spray.http.StatusCodes._


class DataService(data : ActorRef) extends Directives {

  import spray.http.HttpHeaders._
  
  
  val route = pathPrefix("person") {
    
    pathEnd { complete("OK")} ~ path(IntNumber){id => complete("ID was " + id) }
   
  }
  
  
}