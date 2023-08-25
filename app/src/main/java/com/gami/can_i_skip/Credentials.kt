package com.gami.can_i_skip

import kotlinx.serialization.Serializable
import java.time.Month

@Serializable
data class Credentials(
val email : String,
val password : String,
    val scrapperBaseUrl : String,
    val loginType : String,
    val symbol:String,
    val schoolSymbol:String,
    val studentId:Int,
    val diaryId:Int,

)

