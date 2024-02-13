package es.salvaaoliiver.secondevproject.main.bottombar.chat.`object`

import java.util.*

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = Date().time,
    val nameUser: String = ""
)
