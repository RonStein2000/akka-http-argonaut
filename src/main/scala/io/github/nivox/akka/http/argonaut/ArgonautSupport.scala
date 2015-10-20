package io.github.nivox.akka.http.argonaut

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.stream.ActorMaterializer
import argonaut.Argonaut._
import argonaut._

import scalaz.{-\/, \/-}


object ArgonautSupport {

  implicit def argonautFromEntityUnmarshaller[T](decoder: DecodeJson[T])(implicit mat: ActorMaterializer): FromEntityUnmarshaller[T] =
    argonautJsonFromEntityUnmarshaller map {
      decoder.decodeJson(_).result match {
        case -\/( (err, _) ) => throw new IllegalArgumentException(err)
        case \/-(value) => value
      }
    }

  implicit def argonautJsonFromEntityUnmarshaller(implicit ec: ExecutionContext, mat: ActorMaterializer): FromEntityUnmarshaller[Json] =
    Unmarshaller.byteStringUnmarshaller.forContentTypes(MediaTypes.`application/json`) mapWithCharset { (data, charset) =>
      data.decodeString(charset.nioCharset.name())
    } map { dataString =>
      dataString.parseWith(identity, err => throw new IllegalArgumentException(err))
    }

  implicit def argonautToEntityMarshaller[T](encoder: EncodeJson[T])(implicit ec: ExecutionContext, mat: ActorMaterializer): ToEntityMarshaller[T] =
    argonautJsonToEntityMarshaller compose (encoder.encode(_))

  implicit def argonautJsonToEntityMarshaller(implicit ec: ExecutionContext, mat: ActorMaterializer): ToEntityMarshaller[Json] =
    Marshaller.stringMarshaller(MediaTypes.`application/json`) compose (_.nospaces)
}
