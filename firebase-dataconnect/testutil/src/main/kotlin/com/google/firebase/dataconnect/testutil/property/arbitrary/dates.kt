/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.dataconnect.testutil.property.arbitrary


import com.google.firebase.dataconnect.ConnectorConfig
import com.google.firebase.dataconnect.DataConnectSettings
import com.google.firebase.dataconnect.FirebaseDataConnect.CallerSdkType
import com.google.firebase.dataconnect.testutil.dateFromYearMonthDayUTC
import com.google.firebase.dataconnect.testutil.expectedAnyScalarRoundTripValue
import com.google.firebase.dataconnect.testutil.property.arbitrary.EdgeCases.Dates.MAX_YEAR
import com.google.firebase.dataconnect.testutil.property.arbitrary.EdgeCases.Dates.MIN_YEAR
import com.google.firebase.util.nextAlphanumericString
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.arabic
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.ascii
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.cyrillic
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.egyptianHieroglyphs
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterIsInstance
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.merge
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.of
import io.kotest.property.arbitrary.string
import java.util.Date
import kotlin.random.nextInt

private fun maxDayForMonth(month: Int): Int {
  return when (month) {
    1 -> 31
    2 -> 28
    3 -> 31
    4 -> 30
    5 -> 31
    6 -> 30
    7 -> 31
    8 -> 31
    9 -> 30
    10 -> 31
    11 -> 30
    12 -> 31
    else ->
      throw IllegalArgumentException("invalid month: $month (must be between 1 and 12, inclusive)")
  }
}

data class DateAndString(val date: Date, val string: String)

fun Arb.Companion.dateAndString(): Arb<DateAndString> =
  arbitrary(edgecases = EdgeCases.dateAndStrings) { rs ->
    val year = rs.random.nextInt(MIN_YEAR..MAX_YEAR)
    val month = rs.random.nextInt(1..12)
    val day = rs.random.nextInt(1..maxDayForMonth(month))

    val date = dateFromYearMonthDayUTC(year, month, day)

    val yearStr = "$year"
    val monthStr = "$month".padStart(2, '0')
    val dayStr = "$day".padStart(2, '0')
    val string = "$yearStr-$monthStr-$dayStr"

    DateAndString(date, string)
  }

fun Arb.Companion.dateAndStringOffDayBoundary(): Arb<DateAndString> =
  arbitrary(edgecases = EdgeCases.dateAndStringOffDayBoundary) {
    // Skip dates with the maximum year, as adding non-zero milliseconds will result in the year
    // 10,000, which is invalid.
    val dateAndStrings = Arb.dateAndString().filterNot { it.string.contains("9999") }
    // Don't add more than 86_400_000L, the number of milliseconds per day, to the date.
    val millisOffsets = Arb.long(0L until 86_400_000L)

    val dateAndString = dateAndStrings.bind()
    val millisOffset = millisOffsets.bind()
    val dateOffDayBoundary = Date(dateAndString.date.time + millisOffset)

    DateAndString(dateOffDayBoundary, dateAndString.string)
  }
