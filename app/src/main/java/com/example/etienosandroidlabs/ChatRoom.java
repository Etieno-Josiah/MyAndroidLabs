package com.example.etienosandroidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    MessageDatabase db;
    ChatMessageDAO mDAO;
    Executor thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread = Executors.newSingleThreadExecutor();
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class,
                "database-name").build();
        mDAO = db.cmDAO();

        if(messages == null){
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
    }

    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete this message: '" + messageText.getText() + "'")
                    .setTitle("Question:")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        ChatMessage removedMessage = messages.get(position);
                        thread.execute(() -> {
                            mDAO.deleteMessage(removedMessage);
                        });
                        messages.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG )
                            .setAction("UNDO", click -> {
                                messages.add(position, removedMessage);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                    })
                    .setNegativeButton("No", (dialog, cl) -> {})
                    .create()
                    .show();
            });
        }
    }
}