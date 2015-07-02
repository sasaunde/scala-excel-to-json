package core

import java.util.Date
import org.apache.poi.ss.usermodel.Row
import java.util.Formatter.DateTime
import spray.http.DateTime


object DataUtils {
  
   
  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  //val Pattern = "1.(\d)$nE(\d)$d".r
  
  def deformat(doubleStr : String) : String = doubleStr match {
    case r"1.[0-9]*E(\d+)$d" => java.lang.Double.parseDouble(doubleStr).toInt.toString
    //case _ => doubleStr
    //case Pattern(x) => println(">>>>>>>>>>>>>>>>>>>Match2");java.lang.Double.parseDouble(x).toInt.toString
    case _ => doubleStr
  }
  
 def userId(str : String) : String = str match {
    //case r"([A-Za-z]*, [A-Za-z ]*)$s\((\d+)$d\)" => d.toString() now we only have character id
   case r"([-A-Za-z]*)$f \(([A-Za-z ]*)$n\) ([A-Za-z ]*)$s\(([A-Z]+)$d\)" => d.toString()
    case _ => ""
  }                                               //> userId: (str: String)String
  
  def userName(str : String) : String = str match {
    case r"([-A-Za-z]*)$f \(([A-Za-z ]*)$n\) ([A-Za-z ]*)$s\(([A-Z]+)$d\)" => n + " " + f
    case _ => ""
  }    
  
  //val NamePattern="(\([A-Za-z]*, [A-Za-z]*\))$s (\d+)$d".r
  
}

case class ProjectUser(name : String, id : String, projectCode: String, level : String, baseLocation : String, team : String, clientName: String) {
 
  def this(row: Row) = {
    
    
    this(DataUtils.userName(row.getCell(0).getStringCellValue), 
         DataUtils.userId(row.getCell(0).getStringCellValue), 
         DataUtils.deformat(row.getCell(4).toString),  
         row.getCell(1).getStringCellValue, 
         row.getCell(2).getStringCellValue.substring(0, row.getCell(2).getStringCellValue.indexOf('(')).trim(),
         row.getCell(3).getStringCellValue,
         row.getCell(5).getStringCellValue)
    
  }
  
  override def hashCode() : Int = 41 * id.hashCode
  
  override def equals (other : Any) : Boolean = other match {    
    case x : ProjectUser => (this.id equals x.id) && (this.projectCode equals x.projectCode)
    case _ => false
  }
}

case class UserByGrade(series : List[String], data: List[Point]) {
   def this(allUsers : List[ProjectUser]) = {     
     this(allUsers.map(_.level).distinct, PointUtils.buildUserPoint(allUsers))
   }
}

case class UserByBaseLocation(series : List[String], data : List[Point]) {
  def this (allUsers : List[ProjectUser]) = {
    this(allUsers.map(_.baseLocation).distinct, PointUtils.buildBaseLocationPoint(allUsers))
  }
}

case class ProjectUsers(users : List[ProjectUser])
