package com.example.etienosandroidlabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.etienosandroidlabs.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    ChatMessage selected;

    public MessageDetailsFragment(){
        super();
    }
    public MessageDetailsFragment(ChatMessage m){
        super();
        selected = m;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageHere.setText(selected.message);
        binding.timeHere.setText(selected.timeSent);
        binding.databaseText.setText("Id = " + selected.id);

        return binding.getRoot();
    }

}
