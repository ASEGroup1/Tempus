package entities

import java.time.OffsetDateTime

import scala.collection.mutable.ListBuffer

class AcademicYear {
	var academicYearId: Int = _
	var yearCode: String = _
	var startDate:  OffsetDateTime = _ // We also have the option to use LocalDate here which omits the time part and the timezone information
	var endDate: OffsetDateTime = _
	var terms: ListBuffer[Term] = _
}
