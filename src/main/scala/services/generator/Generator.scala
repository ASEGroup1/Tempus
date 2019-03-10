package services.generator

import java.util.UUID

import scala.util.Random

trait Generator[T] {
  def generate(size: Int): Seq[T] = for (_ <- 0 until size) yield generate

  def generate(): T

  def generate(types: Class[Any]*) = {
    types.map(_ => {
      case Int => Random.nextInt
      case Double => Random.nextDouble
      case String => UUID.randomUUID
      case  _ => None
    })
  }
}