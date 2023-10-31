package com.example.etienosandroidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.etienosandroidlabs.databinding.ActivityChatRoomBinding;
import com.example.etienosandroidlabs.databinding.ReceiveMessageBinding;
import com.example.etienosandroidlabs.databinding.SentMessageBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;

    ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
//        messages = chatModel.messages.getValue();
//
//        if(messages == null){
//            chatModel.messages.postValue(messages = new ArrayList<String>());
//        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.sendButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            String typed = binding.textInput.getText().toString();
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, true);


            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        binding.receiveButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            String typed = binding.textInput.getText().toString();
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, false);

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
                return messages.get(position).isSentButton ? 0 : 1;
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
        }
    }
}