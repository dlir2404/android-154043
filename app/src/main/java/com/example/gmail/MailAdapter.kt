package com.example.gmail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MailAdapter(val mails: List<Mail>) : RecyclerView.Adapter<MailAdapter.MailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MailAdapter.MailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false);
        return MailViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: MailAdapter.MailViewHolder, position: Int) {
        val mail = mails[position]
        holder.avatarCharacter.text = mail.sender[0].toString();
        holder.avatarBackground.setImageResource(mail.avatarId);
        holder.sender.text = mail.sender;
        holder.title.text = mail.title;
        holder.content.text = mail.content;
        holder.sendAt.text = mail.sendAt;
    }

    override fun getItemCount(): Int = mails.size;

    class MailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarCharacter: TextView;
        val avatarBackground: ImageView;
        val sender: TextView;
        val title: TextView;
        val content: TextView;
        val sendAt: TextView;

        init {
            avatarCharacter = itemView.findViewById<TextView>(R.id.avatar_character);
            avatarBackground = itemView.findViewById<ImageView>(R.id.avatar_wrapper);
            sender = itemView.findViewById<TextView>(R.id.sender);
            title = itemView.findViewById<TextView>(R.id.title);
            content = itemView.findViewById<TextView>(R.id.content);
            sendAt = itemView.findViewById<TextView>(R.id.sendAt);
        }
    }
}