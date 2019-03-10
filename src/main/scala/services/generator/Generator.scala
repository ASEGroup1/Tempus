package services.generator

import java.time._
import java.util.UUID

import scala.util.Random

trait Generator[T] {
  def gen(size: Int): Seq[T] = for (_ <- 0 until size) yield gen

  def gen(): T

  def genStr = UUID.randomUUID().toString

  //These methods are 1 line however to avoid imports and maintain code concision
  def genInt = Random.nextInt

  def genInt(min:Int, max:Int) = Random.nextInt(max) + min

  def genODT = new OffsetDateTime()

  def genOT = new OffsetTime()
}