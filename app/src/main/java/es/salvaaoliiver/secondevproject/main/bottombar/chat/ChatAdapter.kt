package es.salvaaoliiver.secondevproject.main.bottombar.chat

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.login.AuthManager
import es.salvaaoliiver.secondevproject.main.bottombar.chat.`object`.Message
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatAdapter(private val messages: MutableList<Message>, private val fontColor: Int) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.textViewMessage)
        val senderTextView: TextView = itemView.findViewById(R.id.textViewSenderName)
        val timestampTextView: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if (message.senderId == AuthManager.getCorreo()){
            holder.senderTextView.setTextColor(Color.RED)
        }


        if (message.nameUser != ""){
            holder.senderTextView.text = message.nameUser
        } else{
            holder.senderTextView.text = message.senderId
        }

        holder.messageTextView.setTextColor(fontColor)
        holder.messageTextView.text = message.text
        holder.timestampTextView.text = formatDate(message.timestamp)
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return sdf.format(calendar.time)
    }



    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}
