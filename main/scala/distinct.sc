object distinct {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  import core.ProjectUser
  
  def fill(count: Int = 0) : List[ProjectUser] = if(count == 10) Nil else new ProjectUser("Name"+count, "1"+ count % 2, "pCode", "level", new java.util.Date()) :: fill (count + 1)
                                                  //> fill: (count: Int)List[core.ProjectUser]
  
  
  val bigList = fill()                            //> bigList  : List[core.ProjectUser] = List(ProjectUser(Name0,10,pCode,level,Mo
                                                  //| n Jan 12 13:39:29 GMT 2015), ProjectUser(Name1,11,pCode,level,Mon Jan 12 13:
                                                  //| 39:29 GMT 2015), ProjectUser(Name2,10,pCode,level,Mon Jan 12 13:39:29 GMT 20
                                                  //| 15), ProjectUser(Name3,11,pCode,level,Mon Jan 12 13:39:29 GMT 2015), Project
                                                  //| User(Name4,10,pCode,level,Mon Jan 12 13:39:29 GMT 2015), ProjectUser(Name5,1
                                                  //| 1,pCode,level,Mon Jan 12 13:39:29 GMT 2015), ProjectUser(Name6,10,pCode,leve
                                                  //| l,Mon Jan 12 13:39:29 GMT 2015), ProjectUser(Name7,11,pCode,level,Mon Jan 12
                                                  //|  13:39:29 GMT 2015), ProjectUser(Name8,10,pCode,level,Mon Jan 12 13:39:29 GM
                                                  //| T 2015), ProjectUser(Name9,11,pCode,level,Mon Jan 12 13:39:29 GMT 2015))
  
  val uniqueList = fill().distinct.length         //> uniqueList  : Int = 2
 
  
}