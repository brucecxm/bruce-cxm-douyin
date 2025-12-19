package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.ChatUser;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder> {
    private Context context;
    private List<ChatUser> chatUserList;
    private OnItemClickListener listener;

    // 构造函数
    public ChatUserAdapter(Context context, List<ChatUser> chatUserList) {
        this.context = context;
        this.chatUserList = chatUserList;
    }

    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(ChatUser chatUser);
    }

    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
        return new ChatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder holder, int position) {
        ChatUser chatUser = chatUserList.get(position);
        holder.userName.setText(chatUser.getUserName());
        holder.lastMessage.setText(chatUser.getLastMessage());
        holder.lastMessageTime.setText(chatUser.getLastMessageTime());
        
        // 设置未读消息数量
        if (chatUser.getUnreadCount() > 0) {
            holder.unreadCount.setVisibility(View.VISIBLE);
            holder.unreadCount.setText(String.valueOf(chatUser.getUnreadCount()));
        } else {
            holder.unreadCount.setVisibility(View.GONE);
        }
        
        // TODO: 设置头像，目前使用默认头像
        // holder.avatar.setImageResource(R.drawable.ic_user_placeholder);
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    // 清空列表
    public void clear() {
        chatUserList.clear();
        notifyDataSetChanged();
    }

    // 添加用户列表
    public void addUsers(List<ChatUser> users) {
        chatUserList.addAll(users);
        notifyDataSetChanged();
    }

    // 添加单个用户
    public void addUser(ChatUser user) {
        chatUserList.add(user);
        notifyItemInserted(chatUserList.size() - 1);
    }

    // 内部ViewHolder类
    class ChatUserViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avatar;
        TextView userName;
        TextView lastMessage;
        TextView lastMessageTime;
        TextView unreadCount;

        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageViewAvatar);
            userName = itemView.findViewById(R.id.textViewUserName);
            lastMessage = itemView.findViewById(R.id.textViewLastMessage);
            lastMessageTime = itemView.findViewById(R.id.textViewLastMessageTime);
            unreadCount = itemView.findViewById(R.id.textViewUnreadCount);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(chatUserList.get(position));
                        }
                    }
                }
            });
        }
    }
}