package core

import akka.actor.Actor

import spray.util.SprayActorLogging
import java.io.FileInputStream
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import scala.collection.JavaConversions._

import org.apache.poi.ss.usermodel.Row

import org.apache.poi.ss.usermodel.Cell

object ProjectActor {
  
  case class Project(fileName : String)
  case class Team(fileName : String)
  def name = "project"
}

class ProjectActor extends Actor with SprayActorLogging {
  import ProjectActor._
  
  
  def buildTeams(fileName: String) : Projects = {
    val f : FileInputStream = new FileInputStream(fileName)
    
    try {
      println("Creating workbook >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    val wb = new XSSFWorkbook(f)
    
    val sheet = wb.getSheetAt(0)
    
    val it = sheet.iterator()
    
    // avoid header row
    it.next()
    
    val allProjects = buildTeam(it)
    
    new Projects(allProjects.map(_.team).distinct, PointUtils.buildTeamPoint(allProjects))
    
    
    } catch {
      case e : Exception => { println("Caught exception " + e.getMessage); throw e }
      
    
    } finally {
      f.close()
    }
  }
  
  def buildTeam(it : Iterator[Row]) : List[TeamInfo] = {
    if(!it.hasNext()) return List()    
    else {
      val row = it.next()      
      
      // Only want rows with category External
      row.getCell(6).toString().trim() match {
        case "" => return List()
        case "Firm Project" => new TeamInfo(row) :: buildTeam(it)
        case _ => buildTeam(it)
      }
    }
  }
  
  def buildProjects(fileName: String) : Projects = {
    
    val f : FileInputStream = new FileInputStream(fileName)
    
    try {
      println("Creating workbook >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    val wb = new XSSFWorkbook(f)
    
    val sheet = wb.getSheetAt(0)
    
    val it = sheet.iterator()
    
    // avoid header row
    it.next()
    
    val allProjects = buildProject(it)
    
    new Projects(allProjects)
    
    
    } catch {
      case e : Exception => { println("Caught exception " + e.getMessage); throw e }
      
    
    } finally {
      f.close()
    }
  }
  
  def buildProject(it : Iterator[Row]) : List[ProjectInfo] = {
    if(!it.hasNext()) return List()    
    else {
      val row = it.next()      
      
      // Only want rows with category External
      row.getCell(6).toString().trim() match {
        case "" => return List()
        case "Firm Project" => new ProjectInfo(row) :: buildProject(it)
        case _ => buildProject(it)
      }
//      
//      // Spreadsheets might have more rows,but they might be blank - this is a base case
//      if(row.getCell(0).getCellType.equals(Cell.CELL_TYPE_BLANK)) return List()
//           
//      
//      new ProjectInfo(row) :: buildProject(it)
    }
  }
  
  def receive : Receive = {
    case Project(f) => sender ! buildProjects(f)
    case Team(f) => sender ! buildTeams(f)
  }
} 