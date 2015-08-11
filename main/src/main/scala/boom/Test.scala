package boom

import java.util.UUID

import boom.model.{AHolder1, MyThing}

/**
 * @author steven
 *
 */
object Test extends App {
  def ru = UUID.randomUUID()

  def thing = MyThing(ru, ru, "name", None)
  def thingWithProps = thing.copy(properties = Map("a" -> "b"))

  println("\nThis seems to always work:\n")
  // when the map is there, it doesn't blow up
  println(upickle.default.read[AHolder1](upickle.default.write(AHolder1(thingWithProps))))

  println("\nThis seems to always fail:\n")

  // but here it usually does, until model has been incrementally compiled with changes once or twice
  println(upickle.default.read[AHolder1](upickle.default.write(AHolder1(thing))))
}
