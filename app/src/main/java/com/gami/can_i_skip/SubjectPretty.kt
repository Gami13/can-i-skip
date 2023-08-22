package com.gami.can_i_skip

import io.github.wulkanowy.sdk.pojo.AttendanceSummary

class SubjectPretty(
    var id: Int,
    var name: String,
    var totalClasses: Int,
    var presence: Int,
    var absence: Int,
    var absenceExcused: Int,
    var absenceForSchoolReasons: Int,
    var lateness: Int,
    var latenessExcused: Int,
    var exemption: Int,
    var perMonth: List<AttendanceSummary>
) {
    override fun toString(): String {
        return "SubjectPretty(id=$id, name='$name', totalClasses=$totalClasses, presence=$presence, absence=$absence, absenceExcused=$absenceExcused, absenceForSchoolReasons=$absenceForSchoolReasons, lateness=$lateness, latenessExcused=$latenessExcused, exemption=$exemption, perMonth=$perMonth)"
    }

}