package com.google.firebase.dataconnect.testutil.property.arbitrary



  object Dates {
    const val MIN_YEAR = 1583
    const val MAX_YEAR = 9999

    val MIN: Date
      get() = dateFromYearMonthDayUTC(MIN_YEAR, 1, 1)
    val MIN_DATE_AND_STRING: DateAndString
      get() = DateAndString(MIN, "$MIN_YEAR-01-01")

    val MAX: Date
      get() = dateFromYearMonthDayUTC(MAX_YEAR, 12, 31)
    val MAX_DATE_AND_STRING: DateAndString
      get() = DateAndString(MAX, "$MAX_YEAR-12-31")

    val ZERO: Date
      get() = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply { timeInMillis = 0 }.time
    val ZERO_DATE_AND_STRING: DateAndString
      get() = DateAndString(ZERO, "1970-01-01")
  }

  val dates: List<Date> = listOf(Dates.MIN, Dates.MAX, Dates.ZERO)

  val dateAndStrings: List<DateAndString> =
    listOf(
      Dates.MIN_DATE_AND_STRING,
      Dates.MAX_DATE_AND_STRING,
      Dates.ZERO_DATE_AND_STRING,
    )

  val dateAndStringOffDayBoundary: List<DateAndString> =
    listOf(
      DateAndString(
        Date(Dates.MIN_DATE_AND_STRING.date.time + 1),
        Dates.MIN_DATE_AND_STRING.string
      ),
      DateAndString(
        Date(Dates.MAX_DATE_AND_STRING.date.time + 1),
        Dates.MAX_DATE_AND_STRING.string
      ),
      DateAndString(
        Date(Dates.ZERO_DATE_AND_STRING.date.time + 1),
        Dates.ZERO_DATE_AND_STRING.string
      ),
      DateAndString(Date(Dates.ZERO_DATE_AND_STRING.date.time - 1), "1969-12-31"),
    )

  object Timestamps {
    data class TestCase(
      val timestamp: Timestamp,
      val fullString: String,
      val fdcRoundTripString: String
    )

    val MIN: TestCase
      get() =
        TestCase(
          Timestamp(-12_212_553_600, 0),
          fullString = "1583-01-01T00:00:00.000000000Z",
          fdcRoundTripString = "1583-01-01T00:00:00.000000Z",
        )

    val MAX: TestCase
      get() =
        TestCase(
          Timestamp(253_402_300_799, 999_999_999),
          fullString = "9999-12-31T23:59:59.999999999Z",
          fdcRoundTripString = "9999-12-31T23:59:59.999999Z",
        )

    val ZERO: TestCase
      get() =
        TestCase(
          Timestamp(0, 0),
          fullString = "1970-01-01T00:00:00.000000000Z",
          fdcRoundTripString = "1970-01-01T00:00:00.000000Z",
        )
  }

  val timestamps: List<Timestamps.TestCase> =
    listOf(Timestamps.MIN, Timestamps.MAX, Timestamps.ZERO)
