package com.gami.can_i_skip

import kotlinx.serialization.Serializable

@Serializable
class Preferences(
    var targetAttendance: Double,
    var safetyAfterWeeks: Int,

) {
    override fun toString(): String {
        return "Preferences(targetAttendance=$targetAttendance, safetyAfterWeeks=$safetyAfterWeeks)"
    }
}