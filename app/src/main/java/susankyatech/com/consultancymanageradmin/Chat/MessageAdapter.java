package susankyatech.com.consultancymanageradmin.Chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messagesList;

    public MessageAdapter(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_message_layout, viewGroup, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        final Message messages = messagesList.get(i);

        if (messages.student.status.equals("sender")){
            messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_message_layout);
            messageViewHolder.senderMessageText.setText(messages.message);
            messageViewHolder.senderLayout.setVisibility(View.VISIBLE);
            messageViewHolder.receiverLayout.setVisibility(View.GONE);
        }else {
            messageViewHolder.senderLayout.setVisibility(View.GONE);
            messageViewHolder.receiverLayout.setVisibility(View.VISIBLE);
            messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.reciever_message_layout);
            messageViewHolder.receiverMessageText.setText(messages.message);
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sender_layout)
        View senderLayout;
        @BindView(R.id.receiver_layout)
        View receiverLayout;
        @BindView(R.id.sender_message_text)
        TextView senderMessageText;
        @BindView(R.id.receiver_message_text)
        TextView receiverMessageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}