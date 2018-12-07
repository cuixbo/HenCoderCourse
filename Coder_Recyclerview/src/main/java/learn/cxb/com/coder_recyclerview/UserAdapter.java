package learn.cxb.com.coder_recyclerview;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<User> mUsers;

    public UserAdapter() {
        mUsers = new ArrayList<>(10);
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.name = "user" + i;
            user.age = i;
            user.addr = "addr" + i;
            mUsers.add(user);
        }
    }

    public void swapData(List<User> newList, boolean diff) {
        if (diff) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UserDifferCallback(mUsers, newList));//主线程，可能耗时
            diffResult.dispatchUpdatesTo(this);
        } else {
            mUsers = newList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("xbc", "onCreateViewHolder:" + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Log.e("xbc", "onBindViewHolder:" + position);
        holder.bindTo(mUsers.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle payload = (Bundle) payloads.get(0);
            for (String key : payload.keySet()) {
                switch (key) {
                    case "name":
                        holder.tvName.setText(payload.getString(key));
                        break;
                    case "age":
                        holder.tvAge.setText(String.valueOf(payload.getInt(key)));
                        break;
                    case "addr":
                        holder.tvAddr.setText(payload.getString(key));
                        break;
                }
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull UserViewHolder holder) {
        Log.e("xbc", "onViewAttachedToWindow:");
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvAddr;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvAddr = itemView.findViewById(R.id.tv_address);
        }

        public void bindTo(User user) {
            tvName.setText(user.name);
            tvAge.setText(String.valueOf(user.age));
            tvAddr.setText(user.addr);
        }
    }
}
