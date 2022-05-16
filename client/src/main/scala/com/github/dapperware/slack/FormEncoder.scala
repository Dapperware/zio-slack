package com.github.dapperware.slack

trait FormEncoder[-A] { self =>
  def encode(a: A): List[(String, String)]
  def contramap[B](f: B => A): FormEncoder[B] = (b: B) => self.encode(f(b))

  def both[B](that: FormEncoder[B]): FormEncoder[(A, B)]         = (a: (A, B)) => self.encode(a._1) ++ that.encode(a._2)
  def either[B](that: FormEncoder[B]): FormEncoder[Either[A, B]] = (a: Either[A, B]) => a.fold(self.encode, that.encode)
}

object FormEncoder {
  def fromParams: FormEncoder[List[(String, SlackParamMagnet)]] = new FormEncoder[List[(String, SlackParamMagnet)]] {
    override def encode(a: List[(String, SlackParamMagnet)]): List[(String, String)] =
      a.flatMap(p => p._2.produce.map(p._1 -> _))
  }
}
