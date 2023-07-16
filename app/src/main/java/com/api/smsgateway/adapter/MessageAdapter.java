package com.api.smsgateway.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.api.smsgateway.R;
import com.api.smsgateway.model.RMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<RMessage> messages;

    public MessageAdapter(List<RMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RMessage message = messages.get(position);

        holder.addressTextView.setText(message.getAddress());
        holder.bodyTextView.setText(message.getBody());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(message.getDate()));
        holder.dateTextView.setText(formattedDate);
        if (message.getType() == 1){
            holder.profileImage.setImageResource(R.drawable.received);
        }else{
            holder.profileImage.setImageResource(R.drawable.sent);
        }

//        if (!message.getContactphoto().equals(null)){
//            holder.profileImage.setImageBitmap(message.getContactphoto());
//        }else{
        //}
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addressTextView;
        public TextView bodyTextView;
        public TextView dateTextView;
        public CircleImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }
}

