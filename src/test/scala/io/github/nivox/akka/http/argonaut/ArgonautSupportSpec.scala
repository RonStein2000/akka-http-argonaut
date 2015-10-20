package io.github.nivox.akka.http.argonaut

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.RequestEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import argonaut.Argonaut._
import argonaut._
import io.github.nivox.akka.http.argonaut.ArgonautSupport._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._
import scalaz.Scalaz._

class JsonWrapperSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  implicit val system = ActorSystem(this.getClass.getSimpleName)
  implicit val mat = ActorMaterializer()
  implicit val ec = scala.concurrent.ExecutionContext.global

  override protected def afterAll(): Unit = system.shutdown()

  val jsonValue = Json(
    "stringField" := "string",
    "integerField" := 1,
    "floatField" := 2.0,
    "booleanField" := true,
    "arrayField" := List(1,2,3),
    "objectField" := Json(
      "subField1" := 1,
      "subField2" := 2
    )
  )

  val stringValue =
    """{
      |  "stringField": "string",
      |  "integerField": 1,
      |  "floatField": 2.0,
      |  "booleanField": true,
      |  "arrayField": [1,2,3],
      |  "objectField":  {
      |    "subField1": 1,
      |    "subField2": 2
      |  }
      |}
    """.stripMargin.replaceAll("\\s+", "")

  "The test json" should "be unmashalled using Argonaut" in {
    stringValue.parse shouldBe jsonValue.right
  }

  it should "be marshalled using argonaut" in {
    jsonValue.nospaces.parse shouldBe stringValue.parse
  }

  it should "be marshalled/unmarshalled using akka-http FromEntityUnmarshaller" in {
    val entity = Await.result(Marshal(jsonValue).to[RequestEntity], 1.second)
    val unmarshalledValue = Await.result(Unmarshal(entity).to[Json], 1.second)

    unmarshalledValue shouldBe jsonValue
  }
}

case class InnerModel(subField1: Int, subField2: Int)
case class Model(stringFiled: String, integerField: Int, floatField: Double, booleanFiled: Boolean, arrayField: List[Int], objectField: InnerModel)

class GenericWrapperSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  implicit val system = ActorSystem(this.getClass.getSimpleName)
  implicit val mat = ActorMaterializer()
  implicit val ec = scala.concurrent.ExecutionContext.global

  override protected def afterAll(): Unit = system.shutdown()

  implicit val innerModelCodec: CodecJson[InnerModel] = casecodec2(InnerModel.apply, InnerModel.unapply)("subField1", "subField2")
  implicit val modelCodec: CodecJson[Model] = casecodec6(Model.apply, Model.unapply)("stringField", "integerField", "floatField", "booleanField", "arrayField", "objectField")

  val modelValue = Model("string", 1, 2.0, true, List(1,2,3), InnerModel(1, 2))

  val stringValue =
    """{
      |  "stringField": "string",
      |  "integerField": 1,
      |  "floatField": 2.0,
      |  "booleanField": true,
      |  "arrayField": [1,2,3],
      |  "objectField":  {
      |    "subField1": 1,
      |    "subField2": 2
      |  }
      |}
    """.stripMargin.replaceAll("\\s+", "")

  "The test model" should "be unmashalled using Argonaut" in {
    stringValue.parse.flatMap(_.as[Model].result) shouldBe modelValue.right
  }

  it should "be marshalled using argonaut" in {
    modelValue.asJson.right shouldBe stringValue.parse
  }

  it should "be marshalled/unmarshalled using akka-http FromEntityUnmarshaller" in {
    val entity = Await.result(Marshal(modelValue).to[RequestEntity], 1.second)
    val unmarshalledValue = Await.result(Unmarshal(entity).to[Model], 1.second)

    unmarshalledValue shouldBe modelValue
  }
}
