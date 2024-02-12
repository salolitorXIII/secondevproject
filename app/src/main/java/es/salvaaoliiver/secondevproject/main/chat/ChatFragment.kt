package es.salvaaoliiver.secondevproject.main.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import es.salvaaoliiver.secondevproject.databinding.FragmentChatBinding
import es.salvaaoliiver.secondevproject.login.AuthManager
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        adapter = ChatAdapter(mutableListOf())

        binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChat.adapter = adapter


        binding.btnSendMessage.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            sendMessage(messageText)
        }

        firestore.collection("chats")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e(TAG, "Error getting documents.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                querySnapshot?.let { snapshot ->
                    val messages = mutableListOf<Message>()
                    for (document in snapshot.documents) {
                        val message = document.toObject(Message::class.java)
                        message?.let {
                            messages.add(it)
                        }
                    }
                    adapter.updateMessages(messages)
                    binding.recyclerViewChat.scrollToPosition(messages.size - 1)
                }
            }
    }

    private fun sendMessage(messageText: String) {
        val currentUserID = AuthManager.getCorreo()
        val message = Message(
            senderId = currentUserID,
            text = messageText,
            timestamp = Date().time
        )
        firestore.collection("chats")
            .add(message)
            .addOnSuccessListener(OnSuccessListener {
                Log.d(TAG, "Message sent successfully")
                binding.editTextMessage.text.clear()
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.e(TAG, "Error sending message", e)
            })
    }


    companion object {
        private const val TAG = "ChatFragment"
    }
}
