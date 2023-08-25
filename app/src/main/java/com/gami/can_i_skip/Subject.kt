package com.gami.can_i_skip

import kotlinx.serialization.Serializable

@Serializable
class Subject(
    val name: String,
    val teacher: String,

) {
    override fun toString(): String {
        return "Subject(name='$name', teacher='$teacher')"
    }
}