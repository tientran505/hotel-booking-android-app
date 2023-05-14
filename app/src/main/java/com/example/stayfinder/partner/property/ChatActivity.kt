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
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {
    var mMessageRecycler: RecyclerView? = null
    var messageList: List<Message> = listOf()
    var currentUser: UserMessage = UserMessage("Cien", "","1")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        generateExampleMessages()

        mMessageRecycler = findViewById(R.id.recycler_gchat) as RecyclerView
        val adapter = MessageListAdapter(this, messageList, currentUser)
        mMessageRecycler!!.adapter = adapter
        mMessageRecycler!!.layoutManager = LinearLayoutManager(this)
        mMessageRecycler?.smoothScrollToPosition(adapter.itemCount - 1)
    }
    private fun generateExampleMessages() {
        val userAT = UserMessage("AT", "", "2")
        val messagecheck1 = CheckinMessage("12:00 - 13:00","12-2-2023","14-2-2023",userAT,Date())
        val messagecheck2 = CheckinMessage("12:00 - 13:00","12-2-2023","14-2-2023",currentUser,Date())
        val message1 = NormalMessage("Hello AT!", currentUser, Date())
        val message2 = NormalMessage("Hi Cien!", userAT, Date())
        val message3 = NormalMessage("Di choi khom", currentUser, Date())
        val message4 = NormalMessage("Di", userAT, Date())
        val message5 = NormalMessage("Tui qua don ban", userAT, Date())
        val message6 = NormalMessage("Uki", currentUser, Date())
        val message7 = NormalMessage("uoauoauoauoauoauoaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", userAT, Date())

        messageList = listOf(messagecheck1,messagecheck2,message1, message2, message3, message4, message5, message6, message7)
    }
}

@IgnoreExtraProperties
open class Message (
    open var sender: UserMessage = UserMessage(),
    open var createdAt: Date = Date(),
    @DocumentId
    open val id: String = ""
)

@IgnoreExtraProperties
data class NormalMessage (
    var message: String = "",
    override var sender: UserMessage = UserMessage(),
    override var createdAt: Date = Date(),
    @DocumentId
    override val id: String = "",
): Message(sender, createdAt, id)

data class CheckinMessage(
    var timeCheckin: String = "",
    var dateStart: String = "",
    var dateEnd:String ="",
    override var sender: UserMessage = UserMessage(),
    override var createdAt: Date = Date(),
    @DocumentId
    override val id: String = "",
): Message(sender, createdAt, id)

@IgnoreExtraProperties
data class UserMessage (
    var displayName: String? = "",
    var photoUrl: String? = "",
    @DocumentId
    val uid: String? = ""
): java.io.Serializable {
}



class MessageListAdapter(private val mContext: Context, private val mMessageList: List<Message>, private val mCurrentUser: UserMessage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = mMessageList[position]
        val curID = mCurrentUser.uid
        return if(message.sender.uid.equals(curID) && message is CheckinMessage){
            VIEW_TYPE_CHECKIN_MESSAGE_SENT
        }
        else if(!message.sender.uid.equals(curID) && message is CheckinMessage){
            VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED
        }
        else if (message.sender.uid.equals(curID)) {
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
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
            VIEW_TYPE_CHECKIN_MESSAGE_SENT -> (holder as SentCheckinRequestMessageHolder).bind(message as CheckinMessage)
            VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED -> (holder as ReceiveCheckinRequestMessageHolder).bind(message as CheckinMessage)

        }
    }

    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var dateText: TextView
        init {
            messageText = itemView.findViewById(R.id.text_gchat_message_me)
            dateText = itemView.findViewById(R.id.text_gchat_date_me)
        }

        fun bind(message: Message) {
            when (message) {
                is NormalMessage -> {
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

        fun bind(message: Message) {
            when (message) {
                is NormalMessage -> {
                    messageText.setText(message.message)
                    dateText.setText(
                        SimpleDateFormat("MMMM dd YYYY",
                            Locale.getDefault()).format(message.createdAt)+ " at "+
                                SimpleDateFormat("HH:mm",
                                    Locale.getDefault()).format(message.createdAt))
                    nameText.setText(message.sender.displayName)

                    //change image for message.sender.photoUrl / image_gchat_profile_other
                }
            }

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

        fun bind(message: CheckinMessage) {
//            messageText.setText(message.message)
            dateText.setText(
                SimpleDateFormat("MMMM dd YYYY",
                    Locale.getDefault()).format(message.createdAt)+ " at "+
                        SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(message.createdAt))
//            nameText.setText(message.sender.nickname)
            timemessText.setText(message.timeCheckin)
            datemessText.setText(message.dateStart +" to "+message.dateEnd)
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

        fun bind(message: CheckinMessage) {
//            messageText.setText(message.message)
            nameText.setText(message.sender.displayName)

            dateText.setText(
                SimpleDateFormat("MMMM dd YYYY",
                    Locale.getDefault()).format(message.createdAt)+ " at "+
                        SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(message.createdAt))
            timemessText.setText(message.timeCheckin)
            datemessText.setText(message.dateStart +" to "+message.dateEnd)
            //change image for message.sender.photoUrl / image_gchat_profile_other
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
        private const val VIEW_TYPE_CHECKIN_MESSAGE_SENT = 3
        private const val VIEW_TYPE_CHECKIN_MESSAGE_RECEIVED = 4
    }
}