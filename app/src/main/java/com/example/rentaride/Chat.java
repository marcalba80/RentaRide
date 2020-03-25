package com.example.rentaride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;
    List<Message> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setTitle("Chat con Lucy");
        list.add(new Message("Buenas!", new User("Lucy", "https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg.jpg"), new Date().getTime(), 2));
        list.add(new Message("¿Está el vehiculo disponible?", new User("Lucy", "https://www.rd.com/wp-content/uploads/2017/09/01-shutterstock_476340928-Irina-Bg.jpg"), new Date().getTime(), 2));
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageAdapter(getApplicationContext(), list);
        mMessageRecycler.setHasFixedSize(true);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    public void sendMessage(View view) {
        EditText e = findViewById(R.id.edittext_chatbox);
        String s = e.getText().toString();
        if(!s.isEmpty()){
            list.add(new Message(s, new User("John", "https://widgetwhats.com/app/uploads/2019/11/free-profile-photo-whatsapp-4.png"), new Date().getTime(), 1));
            mMessageAdapter.notifyDataSetChanged();
            e.setText("");
        }else{
            e.setError("Debe introducir un mensaje!");
        }
    }
}
