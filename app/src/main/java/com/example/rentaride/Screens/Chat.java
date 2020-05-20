package com.example.rentaride.Screens;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.rentaride.Logica.Message;
import com.example.rentaride.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton botonEnviar;
    private FloatingActionButton botonNoEnviar;
    private EditText mensaje;
    private ProgressBar progressBar;
    private TextView progressText;
    private TextView noMensaje;
    private ImageView emojiTriste;
    private String otheruid, ownuid;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Message, ViewHolderChat> firebaseRecyclerAdapter;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chat");
        actionBar.setElevation(0);
        otheruid = getIntent().getStringExtra(getString(R.string.otheruid));
        ownuid = getIntent().getStringExtra(getString(R.string.ownuid));
        recyclerView = findViewById(R.id.chat_list);
        botonEnviar = findViewById(R.id.button_send_message);
        botonNoEnviar = findViewById(R.id.button_send_not_able);
        mensaje = findViewById(R.id.message_chat);
        progressBar = findViewById(R.id.chat_list_progress);
        progressText = findViewById(R.id.progress_text);
        noMensaje = findViewById(R.id.no_messages);
        emojiTriste = findViewById(R.id.sad_icon);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10000);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(ownuid + "_" + otheruid);

        inicializarTextWatcher();
        inicializarAdapter();
        inicializarBoton();
        inicializarEventListener();
        inicializarHandler();
    }

    private void inicializarHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (linearLayoutManager.getItemCount()==0){
                    showNoMessages();
                    disableProgress();
                }
            }
        }, 4000);
    }

    private void inicializarEventListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateChatList();
                disableNoMessages();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                showNoMessages();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Chat.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.addChildEventListener(childEventListener);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    private void inicializarBoton() {
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = mensaje.getText().toString();

                databaseReference.push().setValue(new Message()
                        .setMessage(message)
                        .setSenderName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                        .setDate(new SimpleDateFormat("HH:mm - dd/MM/yyyy").format(Calendar.getInstance().getTime()))
                        .setSenderUid(FirebaseAuth.getInstance().getCurrentUser().getUid()));

                mensaje.setText("");
            }
        });
    }

    private void inicializarAdapter() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, ViewHolderChat>(Message.class, R.layout.chat_item_list, ViewHolderChat.class, databaseReference) {
            @Override
            protected void populateViewHolder(ViewHolderChat viewHolder, Message model, int position) {

                disableProgress();

                if (model.getSenderUid().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
                    viewHolder.msgOtroLayout.setVisibility(View.INVISIBLE);
                    viewHolder.msgPropio.setText(model.getMessage());
                    viewHolder.fechaPropia.setText(model.getDate());
                }else {
                    viewHolder.layoutChat.setVisibility(View.INVISIBLE);
                    viewHolder.msgOtro.setText(model.getMessage());
                    viewHolder.nombreOtro.setText(model.getSenderName());
                    viewHolder.fechaOtro.setText(model.getDate());
                }
            }
        };
    }

    private void inicializarTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    botonEnviar.setVisibility(View.VISIBLE);
                    botonNoEnviar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    botonEnviar.setVisibility(View.INVISIBLE);
                    botonNoEnviar.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.RubberBand).duration(400).playOn(botonNoEnviar);
                }
            }
        };

        mensaje.addTextChangedListener(textWatcher);
    }

    private void showNoMessages() {
        if (linearLayoutManager.getItemCount() == 0) {
            emojiTriste.setVisibility(View.VISIBLE);
            noMensaje.setVisibility(View.VISIBLE);
        }
    }

    private void disableNoMessages() {
        if (emojiTriste.getVisibility() == View.VISIBLE) {
            emojiTriste.setVisibility(View.GONE);
            noMensaje.setVisibility(View.GONE);
        }
    }

    private void updateChatList(){
        if (firebaseRecyclerAdapter.getItemCount()==0){
            return;
        }

        recyclerView.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount() -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
        databaseReference.removeEventListener(childEventListener);
    }

    private void disableProgress(){
        if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
        }
    }

    public static class ViewHolderChat extends RecyclerView.ViewHolder {

        public final RelativeLayout layoutChat;
        public final TextView msgPropio;
        public final TextView fechaPropia;
        public final TextView nombreOtro;
        public final TextView msgOtro;
        public final RelativeLayout msgOtroLayout;
        public final TextView fechaOtro;

        public ViewHolderChat(View itemView) {
            super(itemView);
            layoutChat = itemView.findViewById(R.id.msgPropio);
            msgPropio = itemView.findViewById(R.id.txtpropio);
            fechaPropia = itemView.findViewById(R.id.fecha);
            nombreOtro = itemView.findViewById(R.id.nombreotro);
            msgOtro = itemView.findViewById(R.id.txtotro);
            msgOtroLayout = itemView.findViewById(R.id.msgOtro);
            fechaOtro = itemView.findViewById(R.id.fechaotro);
        }
    }
}

