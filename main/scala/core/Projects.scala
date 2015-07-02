package core

import org.apache.poi.ss.usermodel.Row


object PointUtils {
  
  def buildPoint(projects : List[ProjectInfo]) : List[Point] = (for{ 
    proj <- projects groupBy(x => x.projectCode)
    } yield new Point(proj._1, List(proj._2.length), proj._2.head.name+" ("+proj._1+")")).toList
    
  def buildTeamPoint(teams : List[TeamInfo]) : List[Point] = (for{
    team <- teams groupBy(x => x.team)
  } yield new Point(team._1, List(team._2.length), team._1)).toList
  
  def buildUserPoint(users : List[ProjectUser]) : List[Point] = (for{
    user <- users groupBy(x => x.level)
    } yield new Point(user._1, List(user._2.length), user._1)).toList
    
  def buildBaseLocationPoint(users : List[ProjectUser]) : List[Point] = (for {
    user <- users groupBy(x => x.baseLocation)
  } yield new Point(user._1, List(user._2.length), user._1)).toList
  
}

case class Point(x : String, y : List[Int], tooltip : String = "") {
  
  
  //override def toString() = "{ x : " + x + ", y : [" + y.mkString(",") + "]" + ", tooltip: " + tooltip + "}"
  
}

case class Projects(series : List[String], data: List[Point]) {
   def this(allProjects : List[ProjectInfo]) = {
     this(allProjects.map(_.projectCode).distinct, PointUtils.buildPoint(allProjects))
   } 
}


case class TeamInfo(team : String) {
  def this(row: Row) = {
    this(row.getCell(3).getStringCellValue)
  }
}

case class ProjectInfo(projectCode : String, name: String) {
  def this(row: Row) = {
    
    
    this(DataUtils.deformat(row.getCell(4).toString), row.getCell(5).getStringCellValue)
    
  }
} 