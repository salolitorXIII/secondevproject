package es.salvaaoliiver.secondevproject.main.bottombar.chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentChatBinding
import es.salvaaoliiver.secondevproject.login.AuthManager
import es.salvaaoliiver.secondevproject.main.MainActivity
import es.salvaaoliiver.secondevproject.main.bottombar.chat.`object`.Message
import es.salvaaoliiver.secondevproject.main.bottombar.home.HomeFragment
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.menuFragmentoContainer, HomeFragment())
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = "Chat"

        val (fontColor, bgColor) = obtenerColores()
        binding.layout.setBackgroundColor(bgColor)
        adapter = ChatAdapter(mutableListOf(), fontColor)

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
            timestamp = Date().time,
            nameUser = obtenerNombreUsuario()
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

    private fun obtenerNombreUsuario(): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPreferences.getString("chat_username", "") ?: ""
    }

    private fun obtenerColores(): Pair<Int, Int> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val fontColor = sharedPreferences.getInt("chat_font_color", Color.BLACK)
        val bgColor = sharedPreferences.getInt("chat_bg_color", Color.WHITE)
        return Pair(fontColor, bgColor)
    }


    companion object {
        private const val TAG = "ChatFragment"
    }
}
