package com.example.etienosandroidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etienosandroidlabs.databinding.ActivityChatRoomBinding;
import com.example.etienosandroidlabs.databinding.ReceiveMessageBinding;
import com.example.etienosandroidlabs.databinding.SentMessageBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;

    ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter myAdapter;
    MessageDatabase db;
    ChatMessageDAO mDAO;
    Executor thread;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread = Executors.newSingleThreadExecutor();
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class,
                "database-name").build();
        mDAO = db.cmDAO();

        if(messages == null){
            messages = new ArrayList<>();
            chatModel.messages.setValue(messages = new ArrayList<>());


            thread.execute(() ->{
                messages.addAll(mDAO.getAllMessages());
                runOnUiThread(() -> {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerView.setLayoutManager(layoutManager);
                    binding.recyclerView.setAdapter(myAdapter);
                });
            });
        }

        binding.sendButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM hh-mm a");
            String currentDateandTime = sdf.format(new Date());
            String typed = binding.textInput.getText().toString();
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, true);

            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });

            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        binding.receiveButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM hh-mm a");
            String currentDateandTime = sdf.format(new Date());
            String typed = binding.textInput.getText().toString();
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, false);

            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });

            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });


        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding sBinding = null;
                ReceiveMessageBinding rBinding = null;

                if(viewType == 0) {
                    sBinding = SentMessageBinding.inflate(getLayoutInflater());
                } else {
                    rBinding = ReceiveMessageBinding.inflate(getLayoutInflater());
                }
                return new MyRowHolder(sBinding == null ? rBinding.getRoot(): sBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                return messages.get(position).sendOrReceive ? 0 : 1;
            }
        });

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation, chatFragment)
                .addToBackStack("")
                .commit();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ChatMessage selected = messages.get(position);
        chatModel.selectedMessage.postValue(selected);

        switch(item.getItemId()){
            case(R.id.delete):
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete this message: '" + selected.getMessage() + "'")
                        .setTitle("Question:")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage removedMessage = messages.get(position);
                            thread.execute(() -> {
                                mDAO.deleteMessage(removedMessage);
                            });
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            View view = findViewById(android.R.id.content);
                            Snackbar.make(view, "You deleted message #" + position, Snackbar.LENGTH_LONG )
                                    .setAction("UNDO", click -> {
                                        messages.add(position, removedMessage);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .create()
                        .show();
                break;
            case(R.id.about):
                Toast.makeText(getApplicationContext(), "Version 1.0, created by Etieno Josiah",
                        Toast.LENGTH_LONG).show();
        }
        return false;
    }

    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(clk ->{
                position = getAbsoluteAdapterPosition();

            });
        }
    }
}