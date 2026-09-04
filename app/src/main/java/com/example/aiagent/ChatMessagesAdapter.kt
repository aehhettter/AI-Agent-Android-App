package com.example.aiagent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatMessagesAdapter : ListAdapter<ChatMessage, ChatMessagesAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false) as FrameLayout
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(private val itemView: FrameLayout) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: ChatMessage) {
            messageText.text = message.text

            // Styling basierend auf User/AI
            if (message.isUser) {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_blue_light)
                )
                messageText.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.white)
                )
            } else {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_green_light)
                )
                messageText.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.black)
                )
            }
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
            oldItem == newItem
    }
}
