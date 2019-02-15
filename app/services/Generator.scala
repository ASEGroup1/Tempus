package services

trait Generator[T] {
  def generate(size: Int): Array[T]
  def generate(): T
}