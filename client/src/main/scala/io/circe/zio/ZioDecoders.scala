package io.circe.zio

import io.circe.{ Decoder, SeqDecoder }
import zio.{ Chunk, ChunkBuilder }

import scala.collection.mutable

trait ZioDecoders {
  implicit def chunkDecoder[T: Decoder]: Decoder[Chunk[T]] =
    new SeqDecoder[T, Chunk](implicitly) {
      override protected def createBuilder(): mutable.Builder[T, Chunk[T]] = ChunkBuilder.make[T]
    }
}
