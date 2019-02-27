package entities

import java.time
import java.time.OffsetDateTime

class AcademicYear {
	var academicYearId: Int = _
	var yearCode: String = _
	var startDate:  OffsetDateTime = _ // We also have the option to use LocalDate here which omits the time part and the timezone information
	var endDate: OffsetDateTime = _
}
