package com.gami.can_i_skip

import kotlinx.serialization.Serializable

@Serializable
class Preferences(
    var targetAttendance: Double,
    var safetyAfterWeeks: Int,
    var lastAdTimestamp: Long,
    var areNegativesEnabled : Boolean,
    var disabledSubjects: List<String>

) {
    override fun toString(): String {
        return "Preferences(targetAttendance=$targetAttendance, safetyAfterWeeks=$safetyAfterWeeks), adTimestap=$lastAdTimestamp, areNegativesEnabled=$areNegativesEnabled, disabledSubjects=$disabledSubjects)"
    }
}