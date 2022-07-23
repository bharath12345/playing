package blog

import play.api.libs.json._

case object BlogGlobal {
  case class BlogIndexContent(date: String, content: String, title: String, path: String)

  case class BlogPostContent(title: String, content: String, date: String, subheading: Option[String], toc: Option[Boolean])

  implicit val blogIndexContentWrites = new Writes[BlogIndexContent] {
    def writes(bic: BlogIndexContent) = Json.obj(
      "date"  -> bic.date, "content" -> bic.content,
      "title" -> bic.title, "path" -> bic.path
    )
  }

  implicit val blogPostContentWrites = new Writes[BlogPostContent] {
    def writes(bpc: BlogPostContent) = Json.obj(
      "title" -> bpc.title, "content" -> bpc.content,
      "date" -> bpc.date, "subheading" -> bpc.subheading, "toc" -> bpc.toc
    )
  }
}
