package blog

case object BlogGlobal {
  case class BlogIndexContent(date: String, content: String, title: String, path: String)
  case class BlogPostContent(title: String, content: String, date: String, subheading: Option[String], toc: Option[Boolean])
}
