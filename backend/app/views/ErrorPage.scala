package views

object ErrorPage {

  def error(error: String, htmlBody: String): String ={
    """<html style="background-color:#134195">
      <head>
        <meta charset="utf-8">
          <title>""" + error + """</title>
        </head>
        <body >
          <div id = "container" style = "left: 10px; right: 10px; top:10px; bottom: 10px; position:absolute;text-align: center; background-color: #1a4ca8; color:white">
            <h2>""" + error + """</h2>
            <div style = "display: inline-block; width: 70%">""" + htmlBody + """</div>
          </div>
        </body>
      </html>"""
  }

  def badRequest(exception: Exception): String = {
    badRequest(exception.getLocalizedMessage)
  }

  def badRequest(string: String) = {
    error("Bad Request", string)
  }

  def notFound(file: String) = {
    error("File not found", file)
  }
}
