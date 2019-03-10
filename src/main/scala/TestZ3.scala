import z3.scala._

object SecondsToTime {
  def main(args : Array[String]) = {
    val list =  scheduleEvents((1 to 100).map(e =>(("e"+e), 1)), roomCount =  100).get
    println("Valid: " + test(list))
    list.foreach(e => println(e.start, e.duration, e.day, e.room))
  }

  def scheduleEvents(elements : Seq[(String, Int)], roomCount: Int = 1, startTime: Int = 16, endTime: Int = 40): Option[Seq[SEvent]] = {
    println("Building model")
    val ctx = new Z3Context("MODEL" -> true)
    val is = ctx.mkIntSort()
    val (event, _, Seq(dur, room, day, start)) = ctx.mkTupleSort("Event", is, is, is, is)

    // define events
    val events = elements.map(e => (e._1, e._2, ctx.mkFreshConst(e._1, event)))

    val solver = ctx.mkSolver

    // specify durations and range constraints
    events.foreach(e => solver.assertCnstr(ctx.mkAnd(
      // specify duration
      ctx.mkEq(dur(e._3), ctx.mkInt(e._2, is)),
      // 0 <= day < 5
      ctx.mkGE(day(e._3), ctx.mkInt(0, is)),
      ctx.mkLT(day(e._3), ctx.mkInt(5, is)),
      // $startTime <= start <= ($endtime-duration)
      ctx.mkGE(start(e._3), ctx.mkInt(startTime, is)),
      ctx.mkLE(start(e._3), ctx.mkInt(endTime-e._2, is)),
      // 0 <= room <= $roomCount
      ctx.mkGE(room(e._3), ctx.mkInt(0, is)),
      ctx.mkLE(room(e._3), ctx.mkInt(roomCount, is)),
    )))

    val pairs = events.combinations(2)
    pairs.foreach(l => {
      val a = l(0)._3
      val b = l(1)._3 ;
      solver.assertCnstr(ctx.mkNot(ctx.mkAnd(
        ctx.mkEq(day(a), day(b)),
        ctx.mkEq(room(a), room(b)),
        ctx.mkLT(start(a), ctx.mkAdd(start(b), ctx.mkInt(l(1)._2, is))),
        ctx.mkLT(start(b), ctx.mkAdd(start(a), ctx.mkInt(l(0)._2, is)))
      )))})

    println("checking")

    val s = System.nanoTime()
    solver.check() match {
      case None => {println("Z3 failed. The reason is: " + solver.getReasonUnknown()); None}
      case Some(false) => {println("Unsat."); None}
      case Some(true) => {
        val model = solver.getModel()
        println("Done in: " +(System.nanoTime()-s)*1e-9)
        Some(model.getConstInterpretations.map(e =>
          new SEvent(model.evalAs[Int](dur(e._2)).get, model.evalAs[Int](day(e._2)).get, model.evalAs[Int](start(e._2)).get, model.evalAs[Int](room(e._2)).get)).toList)
      }
    }
  }

  def test(events: Seq[SEvent]): Boolean = {
    events.combinations(2).forall(e => {
      val a = e(0)
      val b = e(1)
      !(a.room == b.room &&
        a.day == b.day &&
        a.start < b.start + b.duration &&
        a.start + a.duration > b.start
        )
    })
  }

  class SEvent(val duration: Int, val day: Int, val start: Int, val room: Int)
}