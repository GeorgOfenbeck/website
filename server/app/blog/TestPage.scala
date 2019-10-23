package blog

object TestPage {
  import scalatags.Text.all._

  val title = "blub"
  val page = scalatags.Text.tags.html(
    head(
      scalatags.Text.tags2.title(title)
    ),
    body(
      raw(scalajs.html
        .scripts("jsclient", name => s"/assets/$name", name => getClass.getResource(s"/public/$name") != null)
        .body),
      script("Placeholder.parse()")

    )
  )


}
