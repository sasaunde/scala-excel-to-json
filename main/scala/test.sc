object test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  val Pattern = "1.0".r                           //> Pattern  : scala.util.matching.Regex = 1.0
  
  def deformat(doubleStr : String) : String = doubleStr match {
    case r"1.[0-9]*E(\d+)$d" => java.lang.Double.parseDouble(doubleStr).toInt.toString
    case _ => doubleStr
    //case Pattern(x) => println(">>>>>>>>>>>>>>>>>>>Match");java.lang.Double.parseDouble(x).toInt.toString
    //case _ => doubleStr
  }                                               //> deformat: (doubleStr: String)String
  
  def userId(str : String) : String = str match {
    case r"([A-Za-z]*, [A-Za-z ]*)$s\((\d+)$d\)" => d.toString()
    case _ => ""
  }                                               //> userId: (str: String)String
  
  def userName(str : String) : String = str match {
    case r"([A-Za-z]*)$f, ([A-Za-z ]*)$s\((\d+)$d\)" => s + f+"!"
    case _ => ""
  }                                               //> userName: (str: String)String
  
  //val NamePattern="(\([A-Za-z]*, [A-Za-z]*\))$s (\d+)$d".r
  
  def result = "<img src=\"/MyDirectory/servlet/binaryProcessing?action=download&amp;contextID=1110664564&amp;application=MyDirectory&amp;view=WhitePage&amp;resource=user&amp;realAttribute=thumbnailPhoto&amp;contentType=image%2Fjpeg&amp;multiValuedPos=0&amp;dn=CN%3DJASARKAR%2COU%3DUK-WKG-WOKING%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom&amp;default=%2FMyDirectory%2Ft.gif\" height=\"128\" alt=\"Photo\"></span></td></tr><tr valign=\"top\"><td nowrap=\"nowrap\" width=\"100%\" class=\"FormReadable\" style=\"text-align:center;\"><span class=\"FormReadable\" style=\"text-align:center;\"><a href=\"/MyDirectory/forms/user/entry/read.jsp?resource=user&amp;view=WhitePage&amp;application=MyDirectory&amp;dn=CN%3DJASARKAR%2COU%3DUK-WKG-WOKING%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom\">Sarkar, Jayanti</a></span></td></tr></table></td><td width=\"20.0%\" align=\"center\" valign=\"top\"><table cellspacing=\"1\" cellpadding=\"0\" border=\"0\" width=\"100%\" class=\"FormContainer\"><tr valign=\"top\"><td nowrap=\"nowrap\" width=\"100%\" style=\"padding:0px;vertical-align:middle;text-align:center;\"><span style=\"padding:0px;vertical-align:middle;text-align:center;\"><img src=\"/MyDirectory/servlet/binaryProcessing?action=download&amp;contextID=1110664564&amp;application=MyDirectory&amp;view=WhitePage&amp;resource=user&amp;realAttribute=thumbnailPhoto&amp;contentType=image%2Fjpeg&amp;multiValuedPos=0&amp;dn=CN%3DSASAUNDE%2COU%3DUK-SAE-SALECHES%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom&amp;default=%2FMyDirectory%2Ft.gif\" height=\"128\" alt=\"Photo\"></span></td></tr><tr valign=\"top\"><td nowrap=\"nowrap\" width=\"100%\" class=\"FormReadable\" style=\"text-align:center;\"><span class=\"FormReadable\" style=\"text-align:center;\"><a href=\"/MyDirectory/forms/user/entry/read.jsp?resource=user&amp;view=WhitePage&amp;application=MyDirectory&amp;dn=CN%3DSASAUNDE%2COU%3DUK-SAE-SALECHES%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom\">Saunders, Sarah"
                                                  //> result: => String
  
  
val uri = "Saunders, Sarah"                       //> uri  : String = Saunders, Sarah
val firstName = uri.substring(uri.indexOf(", ") + 2, uri.length)
                                                  //> firstName  : String = Sarah
      val lastName = uri.substring(0, uri.indexOf(", "))
                                                  //> lastName  : String = Saunders



          val lastBit = result.substring(0, result.indexOf(lastName + ", " + firstName.substring(0, 3)))
                                                  //> lastBit  : String = <img src="/MyDirectory/servlet/binaryProcessing?action=
                                                  //| download&amp;contextID=1110664564&amp;application=MyDirectory&amp;view=Whit
                                                  //| ePage&amp;resource=user&amp;realAttribute=thumbnailPhoto&amp;contentType=im
                                                  //| age%2Fjpeg&amp;multiValuedPos=0&amp;dn=CN%3DJASARKAR%2COU%3DUK-WKG-WOKING%2
                                                  //| COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom&amp;defau
                                                  //| lt=%2FMyDirectory%2Ft.gif" height="128" alt="Photo"></span></td></tr><tr va
                                                  //| lign="top"><td nowrap="nowrap" width="100%" class="FormReadable" style="tex
                                                  //| t-align:center;"><span class="FormReadable" style="text-align:center;"><a h
                                                  //| ref="/MyDirectory/forms/user/entry/read.jsp?resource=user&amp;view=WhitePag
                                                  //| e&amp;application=MyDirectory&amp;dn=CN%3DJASARKAR%2COU%3DUK-WKG-WOKING%2CO
                                                  //| U%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgemini%2CDC%3Dcom">Sarkar, Ja
                                                  //| yanti</a></span></td></tr></table></td><td width="20.0%" align="center" val
                                                  //| ign="top"><table cellsp
                                                  //| Output exceeds cutoff limit.
          
          
          val imageUrl = "http://corporatedirectory.capgemini.com" + lastBit.substring(lastBit.lastIndexOf("<img src=") + 10, lastBit.lastIndexOf(".gif") + 4)
                                                  //> imageUrl  : String = http://corporatedirectory.capgemini.com/MyDirectory/se
                                                  //| rvlet/binaryProcessing?action=download&amp;contextID=1110664564&amp;applica
                                                  //| tion=MyDirectory&amp;view=WhitePage&amp;resource=user&amp;realAttribute=thu
                                                  //| mbnailPhoto&amp;contentType=image%2Fjpeg&amp;multiValuedPos=0&amp;dn=CN%3DS
                                                  //| ASAUNDE%2COU%3DUK-SAE-SALECHES%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%
                                                  //| 3Dcapgemini%2CDC%3Dcom&amp;default=%2FMyDirectory%2Ft.gif
      
          println("Image URL of " + imageUrl)     //> Image URL of http://corporatedirectory.capgemini.com/MyDirectory/servlet/bi
                                                  //| naryProcessing?action=download&amp;contextID=1110664564&amp;application=MyD
                                                  //| irectory&amp;view=WhitePage&amp;resource=user&amp;realAttribute=thumbnailPh
                                                  //| oto&amp;contentType=image%2Fjpeg&amp;multiValuedPos=0&amp;dn=CN%3DSASAUNDE%
                                                  //| 2COU%3DUK-SAE-SALECHES%2COU%3DUK%2COU%3DEmployees%2CDC%3Dcorp%2CDC%3Dcapgem
                                                  //| ini%2CDC%3Dcom&amp;default=%2FMyDirectory%2Ft.gif
             
          
          val Location = """(\w\w)-(\w+)-(\w+)""".r
                                                  //> Location  : scala.util.matching.Regex = (\w\w)-(\w+)-(\w+)
       
       val matched =  Location.findFirstIn(imageUrl)
                                                  //> matched  : Option[String] = Some(UK-SAE-SALECHES)
       
       val alsoMatched = Location.findFirstIn("wej%Dewfe%3GB-NEWC-NEWCASTLE%2Dwoejfowe")
                                                  //> alsoMatched  : Option[String] = Some(GB-NEWC-NEWCASTLE)
       

       
def location(str: String) : String = str match {
	case r"UK-([A-Z]*-[A-Z]*)$s" => s
    case _ => ""
}                                                 //> location: (str: String)String


	val loca = location(imageUrl)             //> loca  : String = ""

	val one : String = userId("Abbas, Abba Steven (1324)")
                                                  //> one  : String = 1324
                                                  
val name : String = userName("Abbas, Abba James (453453)")
                                                  //> name  : String = Abba James Abbas!

val projectCode : String = deformat("1.00203203E8")
                                                  //> projectCode  : String = 100203203
}