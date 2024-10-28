package com.example.gmail

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout);

        val mails = mutableListOf<Mail>();
        repeat(50) {
            mails.add(Mail("Sender $it", "Title $it", "Content $it bla bla bla bla blab labasdas sdfgsdf sdfsdf", "12:0${0 + it} AM"));
        }

        val adapter = MailAdapter(mails);

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view);
        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = adapter
    }
}