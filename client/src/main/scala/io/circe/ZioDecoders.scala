package io.circe

import zio.Chunk

import scala.collection.mutable

object ZioDecoders {

  implicit def chunkDecoder[T: Decoder]: Decoder[Chunk[T]] =
    new SeqDecoder[T, Chunk](implicitly) {
      override protected def createBuilder(): mutable.Builder[T, Chunk[T]] = Chunk.newBuilder[T]
    }

}
