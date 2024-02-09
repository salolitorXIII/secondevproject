package es.salvaaoliiver.secondevproject.main.chat

import java.util.*

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = Date().time
)
