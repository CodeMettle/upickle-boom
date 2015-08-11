package boom.model

import java.util.UUID
import upickle._
import default.{Reader, Writer}

import scala.reflect.ClassTag

/**
 * @author steven
 *
 */
trait ATrait {
  def uuid: UUID
}

case class MyThing(uuid1: UUID, uuid2: UUID, name: String, boolOpt: Option[Boolean],
                   properties: Map[String, String] = Map.empty, uuid: UUID = UUID.randomUUID()) extends ATrait

object PickleRegistry {
  private var msgMap = Map.empty[String, (Reader[_], Writer[_])]

  def add[T : ClassTag : Reader : Writer] = {
    def ct = implicitly[ClassTag[T]]
    def r = implicitly[Reader[T]]
    def w = implicitly[Writer[T]]

    msgMap += (ct.runtimeClass.getName -> (r -> w))
  }

  def pickleJs(obj: Any): Js.Value = {
    val className = obj.getClass.getName

    val (_, writer) = msgMap.getOrElse(className, sys.error(s"$obj is not registered"))

    Js.Arr(Js.Str(className), writer.asInstanceOf[Writer[Any]].write(obj))
  }

  def pickle(obj: Any): String = {
    json.write(pickleJs(obj))
  }

  def unpickleJs(js: Js.Value): Any = js match {
    case arr: Js.Arr ⇒
      if (arr.value.size != 2)
        throw Invalid.Data(arr, "Expected 2 elements")

      val className = default.readJs[String](arr.value(0))
      val jsVal = arr.value(1)

      val (reader, _) = msgMap.getOrElse(className, throw Invalid.Data(arr, s"$className is not registered"))

      reader.read(jsVal)

    case jsval ⇒ throw Invalid.Data(jsval, "Expected an Array of 2 elements or a failure")
  }

  def unpickle(json: String): Any = {
    unpickleJs(upickle.json.read(json))
  }

  add[MyThing]
}

trait Holder {
  def thing: ATrait
}

object Holder {
  def createWriter[T <: Holder]: Writer[T] = Writer {
    case msg ⇒ PickleRegistry.pickleJs(msg.thing)
  }

  def createReader[T <: Holder](create: (ATrait) ⇒ T): Reader[T] = Reader {
    case json ⇒ create(PickleRegistry.unpickleJs(json).asInstanceOf[ATrait])
  }

  implicit val w: Writer[Holder] = Writer[Holder] {
    case h1: AHolder1 => Js.Arr(Js.Str("1"), upickle.default.writeJs(h1))
    case h2: AHolder2 => Js.Arr(Js.Str("2"), upickle.default.writeJs(h2))
  }

  implicit val r: Reader[Holder] = Reader[Holder] {
    case arr: Js.Arr if arr.value.size == 2 ⇒ arr.value.head match {
      case Js.Str("1") ⇒ upickle.default.readJs[AHolder1](arr.value.last)
      case Js.Str("2") ⇒ upickle.default.readJs[AHolder2](arr.value.last)
      case o ⇒ throw Invalid.Data(o, "invalid holder type")
    }

    case x ⇒ throw Invalid.Data(x, "Array(2)")
  }
}

case class AHolder1(thing: ATrait) extends Holder

object AHolder1 {
  implicit val w: Writer[AHolder1] = Holder.createWriter[AHolder1]
  implicit val r: Reader[AHolder1] = Holder.createReader[AHolder1](AHolder1(_))
}

case class AHolder2(thing: ATrait) extends Holder

object AHolder2 {
  implicit val w: Writer[AHolder2] = Holder.createWriter[AHolder2]
  implicit val r: Reader[AHolder2] = Holder.createReader[AHolder2](AHolder2(_))
}
