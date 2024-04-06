import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammadahmad.i210790.MessageModel
import com.muhammadahmad.i210790.R
import com.muhammadahmad.i210790.UserData

class MessageAdapter(private val context: Context, private val list: List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    companion object {
        private const val MSG_TYPE_RIGHT = 1
        private const val MSG_TYPE_LEFT = 0    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.messageText)
        val image: ImageView = itemView.findViewById(R.id.senderImage)
//        val pic: ImageView=itemView.findViewById(R.id.senderimage23)
//        val l:LinearLayout=itemView.findViewById(R.id.layout)
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == MSG_TYPE_RIGHT) {
            R.layout.layout_reciever_message // Use sender layout for the current user's messages
        } else {
            R.layout.layout_sender_message // Use receiver layout for other users' messages
        }
        val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = list[position]

        holder.text.text = message.message

        val senderId = message.senderId ?: ""
        loadSenderImage(senderId, holder.image)
        holder.itemView.setOnClickListener(){

        }
        /*holder.itemView.setOnLongClickListener {
            if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
                // If the current user sent the message, delete it from Firebase
                deleteMessageFromFirebase(message)
            }
            true // Return true to consume the long click event
        }*/

        //for edit message
        /*holder.text.setOnLongClickListener {
            // Here you can implement the logic to edit the message
            Toast.makeText(context, "Message long-clicked: ${message.message}", Toast.LENGTH_SHORT).show()
            true // Return true to consume the long click event
        }*/
    }
//Delete messages code
    /*private fun deleteMessageFromFirebase(message: MessageModel) {
        val messageId = message.messageId ?: return
        FirebaseDatabase.getInstance().getReference("messages")
            .child(messageId)
            .removeValue()
            .addOnSuccessListener {
                // Message deleted successfully
                Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Error occurred while deleting the message
                Toast.makeText(context, "Failed to delete message: ${exception.message}", Toast.LENGTH_SHORT).show()
            }


    }*/

    private fun loadSenderImage(senderId: String, imageView: ImageView) {
        FirebaseDatabase.getInstance().getReference("users").child(senderId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(UserData::class.java)
                        userData?.let {
                            Glide.with(context)
                                .load(it.image)
                                .placeholder(R.drawable.pic2)
                                .into(imageView)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun addMessage(message: MessageModel) {
        val newList = ArrayList(list)
        newList.add(message)
        updateMessages(newList)
    }

    fun updateMessages(messages: List<MessageModel>) {
        (list as ArrayList).clear()
        (list as ArrayList).addAll(messages)
        notifyDataSetChanged()
    }
}