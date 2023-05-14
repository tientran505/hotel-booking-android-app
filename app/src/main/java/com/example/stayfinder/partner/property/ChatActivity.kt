package com.example.stayfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {
    var mMessageRecycler: RecyclerView? = null
    var messageList: ArrayList<MessageAdapter> = arrayListOf()
    lateinit var currentUser: UserMessage
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        generateExampleMessages()
        context = this
//        mMessageRecycler = findViewById(R.id.recycler_gchat) as RecyclerView
//        val adapter = MessageListAdapter(this, messageList,currentUser)
//        mMessageRecycler!!.adapter = adapter
//        mMessageRecycler!!.layoutManager = LinearLayoutManager(this)
//        mMessageRecycler?.smoothScrollToPosition(adapter.itemCount - 1)


    }
    private fun generateExampleMessages() {

        val bundle = intent.extras
        val userAT = bundle!!.getSerializable("user") as UserMessage
//        val messagecheck1 = CheckinMessageAdapter(userAT,currentUser,Date(),cimessage("12:00 - 13:00","15-5-2023 | 16-5-2023"))
//        val message = NormalMessageAdapter(userAT,currentUser,Date(),"xin chào bạn")
//        val message2 = NormalMessageAdapter(currentUser,userAT,Date(),"chào bạn")
//        messageList = arrayListOf(messagecheck1,message,message2)
        val userfb: FirebaseUser? = FirebaseAuth.getInstance().currentUser!!
        val documents = Firebase.firestore.collection("users")
            .document(userfb!!.uid)
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                currentUser = UserMessage( document.get("displayName") as String,
                    document.getString("photoUrl"),
                    userfb!!.uid)
            }
            val db = FirebaseDatabase.getInstance()
            val chatRef = db.reference.child("message").child(currentUser.uid!!).orderByChild("userid").equalTo(userAT.uid)

            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (chatData in dataSnapshot.children) {
                        val messages = chatData.child("message").children
                        for (messageData in messages) {
                            val isRequest = messageData.child("request").getValue(Boolean::class.java) as Boolean
                            if(isRequest === true){
                                val message = messageData.getValue(CheckinMessage::class.java) as CheckinMessage
                                if(message.sender_id == currentUser.uid){
                                    messageList.add(CheckinMessageAdapter(currentUser,userAT,message.createdAt,message.message))
                                }
                                else  messageList.add(CheckinMessageAdapter(userAT,currentUser,message.createdAt,message.message))

                            }
                            else{
                                val message = messageData.getValue(NormalMessage::class.java) as NormalMessage
                                if(message.sender_id == currentUser.uid){
                                    messageList.add(NormalMessageAdapter(currentUser,userAT,message.createdAt,message.message))
                                }
                                else  messageList.add(NormalMessageAdapter(userAT,currentUser,message.createdAt,message.message))
                            }

//                            val message = messageData.getValue(Message::class.java)
//                            println(message)

                            // Do something with the message data here
                        }
                        mMessageRecycler = findViewById(R.id.recycler_gchat) as RecyclerView
                        val adapter = MessageListAdapter(context, messageList,currentUser)
                        mMessageRecycler!!.adapter = adapter
                        mMessageRecycler!!.layoutManager = LinearLayoutManager(context)
                        mMessageRecycler?.smoothScrollToPosition(adapter.itemCount - 1)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}

@IgnoreExtraProperties
open class Message(
    open val sender_id: String,
    open val receive_id: String,
    open val createdAt: Date = Date(),
    open val isRequest: Boolean,
    @DocumentId
    open val id: String? = null,
    )

@IgnoreExtraProperties
data class NormalMessage (
    override val sender_id: String="",
    override val receive_id: String="",
    override val createdAt: Date = Date(),
    val message: String?,
    @DocumentId
    override val id: String = "",
): Message(sender_id, receive_id,createdAt,false, id)
{
    constructor():this("","",Date(),"","")

}

@IgnoreExtraProperties
data class CheckinMessage (
    override val sender_id: String="",
    override val receive_id: String="",
    override val createdAt: Date = Date(),
    val message: cimessage?,
    @DocumentId
    override val id: String = "",
): Message(sender_id, receive_id,createdAt,true, id)
{
    constructor():this("","",Date(), cimessage("",""),"")
}
data class cimessage(
    val date: String = "",
    val time: String = ""
) {
    // add default constructor
    constructor() : this("", "")
}
@IgnoreExtraProperties
data class UserMessage (
    var displayName: String? = "",
    var photoUrl: String? = "",
    @DocumentId
    val uid: String? = ""
): java.io.Serializable {
}
open class MessageAdapter(
    open val sender: UserMessage?,
    open val receive: UserMessage?,
    open val createdAt: Date = Date(),
    open val isRequest: Boolean,
    @DocumentId
    open val id: String? = null,
)
@IgnoreExtraProperties
data class NormalMessageAdapter (
    override val sender: UserMessage?,
    override val receive: UserMessage?,
    override val createdAt: Date = Date(),
    val message: String?,
    @DocumentId
    override val id: String = "",
): MessageAdapter(sender, receive,createdAt,false, id)

@IgnoreExtraProperties
data class CheckinMessageAdapter (
    override val sender: UserMessage?,
    override val receive: UserMessage?,
    override val createdAt: Date = Date(),
    val message: cimessage?,
    @DocumentId
    override val id: String = "",
): MessageAdapter(sender, receive,createdAt,true, id)


class MessageListAdapter(private val mContext: Context, private val mMessageList: List<MessageAdapter>, private val mCurrentUser: UserMessage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = mMessageList[position]
        val curID = mCurrentUser.uid
        return if(message.sender?.uid.equals(curID) && message is CheckinMessageAdapter){
            VIEW_TYPE_CHECKIN_MESSAGE_SENT
        }
        else if(!message.sender?.uid.equals(curID) && message is CheckinMessageAdapter){
            VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED
        }
        else if (message.sender?.uid.equals(curID)) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view: View
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_me, parent, false)
            return SentMessageHolder(view)
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_other, parent, false)
            return ReceivedMessageHolder(view)
        }
        else if (viewType == VIEW_TYPE_CHECKIN_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_booking_me, parent, false)
            return SentCheckinRequestMessageHolder(view)
        }
        else if (viewType == VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_booking_other, parent, false)
            return ReceiveCheckinRequestMessageHolder(view)
        }
        return object : RecyclerView.ViewHolder(View(parent.context)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = mMessageList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message as NormalMessageAdapter)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message as NormalMessageAdapter)
            VIEW_TYPE_CHECKIN_MESSAGE_SENT -> (holder as SentCheckinRequestMessageHolder).bind(message as CheckinMessageAdapter)
            VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED -> (holder as ReceiveCheckinRequestMessageHolder).bind(message as CheckinMessageAdapter)

        }
    }

    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var dateText: TextView
        init {
            messageText = itemView.findViewById(R.id.text_gchat_message_me)
            dateText = itemView.findViewById(R.id.text_gchat_date_me)
        }

        fun bind(message: NormalMessageAdapter) {
            messageText.setText(message.message)
            dateText.setText(
                SimpleDateFormat(
                    "MMMM dd YYYY",
                    Locale.getDefault()
                ).format(message.createdAt)
                        + " at " +
                        SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                        ).format(message.createdAt)
            )
        }
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var dateText: TextView
        var nameText: TextView
        var profileImage: ImageView

        init {
            messageText = itemView.findViewById(R.id.text_gchat_message_other)
            dateText = itemView.findViewById(R.id.text_gchat_date_other)
            nameText = itemView.findViewById(R.id.text_gchat_user_other)
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other) as ImageView
        }

        fun bind(message: NormalMessageAdapter) {
                messageText.setText(message.message)
                dateText.setText(
                    SimpleDateFormat("MMMM dd YYYY",
                        Locale.getDefault()).format(message.createdAt)+ " at "+
                            SimpleDateFormat("HH:mm",
                                Locale.getDefault()).format(message.createdAt))
                nameText.setText(message.sender?.displayName)
                    //change image for message.sender.photoUrl / image_gchat_profile_other
            Glide.with(this.itemView)
                .load(URL(message.sender?.photoUrl))
                .apply(RequestOptions().centerCrop())
                .into(profileImage)

        }
    }
    inner class SentCheckinRequestMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        var messageText: TextView
        var dateText: TextView
        //        var nameText: TextView
        var timemessText: TextView
        var datemessText: TextView
        init {
//            messageText = itemView.findViewById(R.id.text_gchat_message_other)
            dateText = itemView.findViewById(R.id.text_gchat_date_me)
//            nameText = itemView.findViewById(R.id.text_gchat_user_other)
            timemessText = itemView.findViewById(R.id.timeTv)
            datemessText = itemView.findViewById(R.id.dateTv)
        }

        fun bind(message: CheckinMessageAdapter) {
//            messageText.setText(message.message)
            dateText.setText(
                SimpleDateFormat("MMMM dd YYYY",
                    Locale.getDefault()).format(message.createdAt)+ " at "+
                        SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(message.createdAt))
//            nameText.setText(message.sender.nickname)
            timemessText.setText(message.message?.time)
            datemessText.setText(message.message?.date)
            //change image for message.sender.photoUrl / image_gchat_profile_other
        }
    }
    inner class ReceiveCheckinRequestMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateText: TextView
        var timemessText: TextView
        var datemessText: TextView
        var nameText: TextView
        var profileImage: ImageView

        init {
            dateText = itemView.findViewById(R.id.text_gchat_date_other)
            timemessText = itemView.findViewById(R.id.timeTv)
            datemessText = itemView.findViewById(R.id.dateTv)
            nameText = itemView.findViewById(R.id.text_gchat_user_other)
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other) as ImageView

        }

        fun bind(message: CheckinMessageAdapter) {
//            messageText.setText(message.message)
            nameText.setText(message.sender?.displayName)

            dateText.setText(
                SimpleDateFormat("MMMM dd YYYY",
                    Locale.getDefault()).format(message.createdAt)+ " at "+
                        SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(message.createdAt))
            timemessText.setText(message.message?.time)
            datemessText.setText(message.message?.date)
            //change image for message.sender.photoUrl / image_gchat_profile_other
            Glide.with(this.itemView)
                .load(URL(message.sender?.photoUrl))
                .apply(RequestOptions().centerCrop())
                .into(profileImage)
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
        private const val VIEW_TYPE_CHECKIN_MESSAGE_SENT = 3
        private const val VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED = 4
    }
}