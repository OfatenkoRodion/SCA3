package module

import akka.actor.ActorSystem


trait ActorModule {
  val system: ActorSystem
}


trait ActorModuleImpl extends ActorModule {
  this: Configuration =>
  val system = ActorSystem("akkinvideo", config)
}