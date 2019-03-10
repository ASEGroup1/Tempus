package entities.timing

import java.time.OffsetDateTime

import entities.Generator

import scala.collection.mutable.ListBuffer

object AcademicYear extends Generator[AcademicYear] {
	override def gen() = new AcademicYear(genInt, genStr, genODT, genODT, null)
}

class AcademicYear(
	var academicYearId: Int,
	var yearCode: String,
	var startDate: OffsetDateTime, // We also have the option to use LocalDate here which omits the time part and the timezone information
	var endDate: OffsetDateTime,
	var terms: ListBuffer[Term]
)
