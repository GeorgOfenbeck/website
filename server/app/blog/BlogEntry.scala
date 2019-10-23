
package blog

sealed trait Tag{
  def clearName: String
  override def toString: String = clearName
}
case object WebDev extends Tag{
  val clearName = "Web Development"
}


case class Tags(tags: Vector[Tag])
object Tags{
  def apply(tags: Tag*): Tags = Tags(tags.toVector)
}

case class BlogEntry(timeStamp: java.time.LocalDateTime, tags: Tags, header: String, content: String)
