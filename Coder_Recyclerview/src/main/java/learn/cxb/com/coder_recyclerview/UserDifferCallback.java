package learn.cxb.com.coder_recyclerview;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class UserDifferCallback extends DiffUtil.Callback {

    private List<User> oldList, newList;

    public UserDifferCallback(List<User> oldList, List<User> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        User oldUser = oldList.get(oldItemPosition);
        User newUser = oldList.get(newItemPosition);
        return TextUtils.equals(oldUser.name, newUser.name)
                && oldUser.age == newUser.age;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        User oldUser = oldList.get(oldItemPosition);
        User newUser = oldList.get(newItemPosition);
        return TextUtils.equals(oldUser.name, newUser.name)
                && oldUser.age == newUser.age
                && TextUtils.equals(oldUser.addr, newUser.addr);
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        User oldUser = oldList.get(oldItemPosition);
        User newUser = oldList.get(newItemPosition);
        Bundle payload = new Bundle();
        if (!TextUtils.equals(oldUser.name, newUser.name)) {
            payload.putString("name", newUser.name);
        }
        if (oldUser.age != newUser.age) {
            payload.putInt("age", newUser.age);
        }
        if (!TextUtils.equals(oldUser.addr, newUser.addr)) {
            payload.putString("addr", newUser.addr);
        }
        if (payload.size() == 0) {
            return null;
        }
        return payload;//有效负荷，净负荷
    }
}
