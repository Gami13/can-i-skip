package com.gami.can_i_skip

import kotlinx.serialization.Serializable

@Serializable
class SerializableColor(
    var red: Float,
    var green: Float,
    var blue: Float,
    var alpha: Float
) {
    override fun toString(): String {
        return "SerializableColor(red=$red, green=$green, blue=$blue, alpha=$alpha)"
    }
}