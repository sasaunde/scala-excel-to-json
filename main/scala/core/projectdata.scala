package core

import akka.actor.Actor
import scala.io.Source
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.util.Iterator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Cell
import java.util.UUID
import java.util.Date
import java.util.Calendar
import akka.actor.ActorRef
import spray.util.SprayActorLogging
import scala.collection.JavaConversions._
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Workbook

object ProjectDataActor {
  
  case class ProjectData(fileName: String, pos: Int = 0, code : String = "", level : String = "")
  case class TeamData(fileName: String, teamCode: String)
  
  def name = "projectData"
}



class ProjectDataActor extends Actor with SprayActorLogging{

  import ProjectDataActor._
  
 
  def buildUser(it : Iterator[Row], wb : XSSFWorkbook) : List[ProjectUser] = {
    if(!it.hasNext()) return List()    
    else {
      val row = it.next()      
      // Spreadsheets might have more rows,but they might be blank - this is a base case
      if(row.getCell(0).getCellType.equals(Cell.CELL_TYPE_BLANK)) return List()
      
      new ProjectUser(row) :: buildUser(it, wb)
    }
  }
  
  def buildUsers(file: String) : List[ProjectUser] = {
    
    val f : FileInputStream = new FileInputStream(file)
    
    try {
      
    val wb = new XSSFWorkbook(f)
    
    val sheet = wb.getSheetAt(0)
    
    val it = sheet.iterator()
    
    // avoid header row
    it.next()
    
    buildUser(it, wb)
    
    } catch {
      case e : Exception => { println("Caught exception " + e.getMessage); throw e }
      
    
    } finally {
      f.close()
    }
  }
  
  def buildUserList(f: String, projectCode : String) = buildUsers(f).distinct filter (a => a.projectCode.startsWith(projectCode))
    
  def buildTeamUsers(f : String, teamCode : String) = buildUsers(f).distinct filter (a => a.team == teamCode)
  
  def buildOneUser(f: String, userId : Int) = buildUsers(f).distinct filter (a => a.id == userId.toString)
   
  def buildGradeUsers(f: String, projectCode : String, grade : String) = {println("buildGradeUsers " + projectCode + ", " + grade);buildUsers(f).distinct filter (a => a.projectCode.startsWith(projectCode) && a.level == grade)}
  
  def buildGraphUsers(f : String, projectCode : String) = {println("buildGraphUsers " + projectCode);new UserByGrade(buildUsers(f).distinct.filter(a => a.projectCode.startsWith(projectCode)))}  
  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }
  def receive : Receive = {
    // Open project data file and access
    case ProjectData(f, 0, "", "") => sender ! buildUsers(f).distinct
    case ProjectData(f, x, "", "") => sender ! buildOneUser(f, x)
    case ProjectData(f, -1, y, "") => sender ! buildGraphUsers(f, y)
    case ProjectData(f, x, y, "") => sender ! buildUserList(f, y)
    case ProjectData(f, x, y, z) => {println(">>>>>>>>>E "+y+z);sender ! buildGradeUsers(f, y, z)} 
    case TeamData(f, x) => sender ! buildTeamUsers(f, x)
  }
}