package com.example.gmail

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout);

        val mails = mutableListOf<Mail>();
        repeat(50) {
            val randomCircle = Random.nextInt(1, 4)

            var avatarId: Int = R.drawable.circle1;
            when (randomCircle) {
                1 -> avatarId = R.drawable.circle1
                2 -> avatarId = R.drawable.circle2
                3 -> avatarId = R.drawable.circle3
            }

            mails.add(Mail(
                "Sender $it",
                "Title $it",
                "Content $it bla bla bla bla blab labasdas sdfgsdf sdfsdf",
                "12:0${0 + it} AM",
                avatarId
            ));
        }

        val adapter = MailAdapter(mails);

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view);
        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = adapter
    }
}