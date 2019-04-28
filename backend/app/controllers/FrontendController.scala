package controllers

import javax.inject._
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._

/**
	* Frontend controller managing all static resource associate routes.
	* Taken From [[https://gist.github.com/yohangz/36a6841c792cb1aa0298251a3b6453ff here]] via [[https://blog.usejournal.com/react-with-play-framework-2-6-x-a6e15c0b7bd here]]
	*
	* @param assets Assets controller reference.
	* @param cc     Controller components reference.
	*/
@Singleton
class FrontendController @Inject()(assets: Assets, errorHandler: HttpErrorHandler, config: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

	def assetOrDefault(resource: String): Action[AnyContent] = if (resource.contains(".")) assets.at(resource) else index

	def index: Action[AnyContent] = assets.at("index.html")

}
