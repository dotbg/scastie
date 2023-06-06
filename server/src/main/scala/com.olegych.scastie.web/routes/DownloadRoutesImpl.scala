package com.olegych.scastie.web.routes

import com.olegych.scastie.balancer.DownloadSnippet


import akka.actor.ActorRef
import akka.pattern.ask


import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import scastie.endpoints.DownloadEndpoints
import scala.concurrent.Future
import java.io.File
import scala.concurrent.ExecutionContext

class DownloadRoutesImpl(dispatchActor: ActorRef)(implicit ec: ExecutionContext) {
  implicit val timeout = Timeout(5.seconds)

  val downloadSnippetImpl = DownloadEndpoints.downloadSnippetEndpoint.map { endpoint =>
    endpoint.serverLogicOption[Future](snippetId =>
      (dispatchActor ? DownloadSnippet(snippetId))
        .mapTo[Option[File]]
      )
  }

  val serverEndpoints = downloadSnippetImpl
}
