package core

import akka.actor.{Props, Actor}
import spray.http.Uri
import java.util.UUID
import scalaj.http.Http
import scalaj.http.HttpOptions
import scalaj.http.Http._
import spray.util.SprayActorLogging
import spray.http.HttpHeader


object DirectorySearchActor {

  case class GetUser(user: User)
  case class SearchUser(uri: String, id : String)
  //case class Headers(headers: List[HttpHeader])
}

class DirectorySearchActor extends Actor with SprayActorLogging {
  import DirectorySearchActor._
  
  // Pattern matching functions on corporate directory entry
  def imageUrl(lastBit : String) : String = "http://corporatedirectory.capgemini.com" + lastBit.substring(lastBit.lastIndexOf("<img src=") + 10, lastBit.lastIndexOf(".gif"))
  
  def firstName(uri : String) : String = uri.substring(uri.indexOf(",") + 2, uri.length)
  
  def lastName(uri : String) : String  = uri.substring(0, uri.indexOf(", "))
  
  def loc(lastBit : String) : String = """(\w\w)-(\w+)-(\w+)""".r findFirstIn imageUrl(lastBit) get 
  
  def receive : Receive = {
    case GetUser(user) => {
      
      val result = Http("http://corporatedirectory.capgemini.com/MyDirectory/forms/user/entry/readUsersPhotoPicker.jsp?resource=user&view=WhitePage&application=MyDirectory&dn=CN%3DLDOUGHTY%2COU%3DUK-BSO-TOLTEC%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom").option(HttpOptions.connTimeout(10000)).option(HttpOptions.readTimeout(50000)).asString;
      println("Result 1is " + result)
      
      sender ! User("SASAUNDE", "Sarah", "Saunders", "Sarah.Saunders@foo.com", "UK-SAE-SALECHES", Uri("url"))
    }
    case SearchUser(uri, id) => {
      
      // Get data from corporate directory
      ///val result  = Http("http://localhost:8000/app/register.html").option(HttpOptions.connTimeout(10000)).option(HttpOptions.readTimeout(50000)).asString;
      
      
      val result = Http("http://corporatedirectory.capgemini.com/MyDirectory/forms/user/entry/readUsersPhotoPicker.jsp?resource=user&view=WhitePage&application=MyDirectory&dn=CN%3DLDOUGHTY%2COU%3DUK-BSO-TOLTEC%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom").option(HttpOptions.readTimeout(50000)).asString;
      
      if(result.body.indexOf(uri) == -1) {
        // Could use shorter first name (try two letters so Mike  matches Michael)
       
        if(result.body.indexOf(lastName(uri) + ", " + firstName(uri).substring(0, 2)) != -1) {
          
          // Get the section of the data containing the person's location details and picture URL (end sentence)
          val lastBit = result.body.substring(0, result.body.indexOf(lastName(uri) + ", " + firstName(uri).substring(0, 2)))
          
          println(lastBit)
          
          println(imageUrl(lastBit))
          
          sender ! User(id, firstName(uri), lastName(uri), firstName(uri).replace(' ', '.')+"."+lastName(uri) + "@capgemini.com", loc(lastBit), Uri(imageUrl(lastBit)))
        } else {
          // No such user
          sender ! User(id, firstName(uri), lastName(uri), "None", "UK-XUK-UNDEFINE", Uri("http://"))
        }
        
      } else {
            // Get the section of the data containing the person's location details and picture URL (end sentence)          
    	      val lastBit = result.body.substring(0, result.body.indexOf(uri))    			  
            
          println(lastBit)
          
          println(imageUrl(lastBit))
          
    			  sender ! User(id, firstName(uri), lastName(uri), firstName(uri).replace(' ', '.')+"."+lastName(uri) + "@capgemini.com", loc(lastBit), Uri(imageUrl(lastBit)))
      }
    }
//    case Headers(headers) => {
//      println("----------------------Got a list ")      
//      sender ! User(1, "S", "s", "goo", "UK-SAE-SALECHES", Uri("URI"))
//    }
  }
}