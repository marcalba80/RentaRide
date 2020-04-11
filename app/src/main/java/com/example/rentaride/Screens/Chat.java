package com.example.rentaride.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.rentaride.Logica.Message;
import com.example.rentaride.Logica.MessageAdapter;
import com.example.rentaride.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.rentaride.Utils.Utils.VIEW_TYPE_MESSAGE_RECEIVED;
import static com.example.rentaride.Utils.Utils.VIEW_TYPE_MESSAGE_SENT;

public class Chat extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;
    List<Message> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setTitle(R.string.chat_lucy);
        list.add(new Message(getString(R.string.buenas), getString(R.string.Lucy), new Date().getTime(), VIEW_TYPE_MESSAGE_RECEIVED));
        list.add(new Message(getString(R.string.estadisponible), getString(R.string.Lucy), new Date().getTime(), VIEW_TYPE_MESSAGE_RECEIVED));
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
            list.add(new Message(s, getString(R.string.jhon), new Date().getTime(), VIEW_TYPE_MESSAGE_SENT));
            mMessageAdapter.notifyDataSetChanged();
            e.setText("");
        }else{
            e.setError(getString(R.string.introduzca_mensaje));
        }
    }
}
