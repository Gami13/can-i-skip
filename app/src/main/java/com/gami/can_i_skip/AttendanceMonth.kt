package com.gami.can_i_skip

import kotlinx.serialization.Serializable
import java.time.Month

@Serializable
data class AttendanceMonth(
    val month: Month,
    val presence: Int,
    val absence: Int,
    val absenceExcused: Int,
    val absenceForSchoolReasons: Int,
    val lateness: Int,
    val latenessExcused: Int,
    val exemption: Int,
)

