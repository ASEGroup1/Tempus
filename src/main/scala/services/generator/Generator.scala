package services.generator

import java.util.UUID

import scala.util.Random

trait Generator[T] {
  def gen(size: Int): Seq[T] = for (_ <- 0 until size) yield gen

  def gen(): T

  def genStr = UUID.randomUUID().toString

  def genInt = Random.nextInt
}