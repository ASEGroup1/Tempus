package services.generator

trait Generator[T] {
  def generate(size: Int): IndexedSeq[T] = {
    for(_ <- 0 until size) yield generate
  }
  def generate(): T
}
