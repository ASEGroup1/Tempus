package entities.timing

import java.time.OffsetDateTime

import scala.collection.mutable.ListBuffer

class AcademicYear(
	var academicYearId: Int,
	var yearCode: String,
	var startDate: OffsetDateTime, // We also have the option to use LocalDate here which omits the time part and the timezone information
	var endDate: OffsetDateTime,
	var terms: ListBuffer[Term]
)
