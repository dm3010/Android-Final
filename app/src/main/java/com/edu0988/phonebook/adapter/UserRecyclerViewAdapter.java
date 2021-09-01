package com.edu0988.phonebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.edu0988.phonebook.MainActivity;
import com.edu0988.phonebook.databinding.FragmentUsersBinding;
import com.edu0988.phonebook.model.User;
import com.edu0988.phonebook.services.UserAPI;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues = new ArrayList<>();

    private int position = -1;
    private UserAPI mUserAPI;
    private final Context mContext;
    private final View.OnClickListener mOnClickListener;

    public User getSelectedPosition() {
        return mValues.get(position);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public UserRecyclerViewAdapter(Context context, UserAPI userAPI, View.OnClickListener onClickListener) {
        mContext = context;
        mUserAPI = userAPI;
        mOnClickListener = onClickListener;
        refreshList();
    }

    public void refreshList() {
        MainActivity activity = (MainActivity) mContext;
        activity.startRefreshing();

        notifyItemRangeRemoved(0, mValues.size());
        mValues.clear();
        List<User> newValues = new ArrayList<>();
        mUserAPI.exec(
                () -> newValues.addAll(mUserAPI.getAll()),
                () -> {
                    mValues.addAll(newValues);
                    notifyItemRangeInserted(0, mValues.size());
                    activity.stopRefreshing("Получено " + mValues.size() + " записей");
                }
        );
    }

    public void addOrUpdate(User user) {
        MainActivity activity = (MainActivity) mContext;
        activity.startRefreshing();
        final boolean[] result = new boolean[1];
        mUserAPI.exec(
                () -> result[0] = mUserAPI.update(user),
                () -> {
                    if (result[0]) {
                        if (!mValues.contains(user)) mValues.add(user);
                        notifyDataSetChanged();
                        activity.stopRefreshing(user + " записан");
                    } else
                        activity.stopRefreshing("Произошла ошибка");
                }
        );
    }

    public void delete(User user) {
        MainActivity activity = (MainActivity) mContext;
        activity.startRefreshing();
        mUserAPI.exec(
                () -> mUserAPI.delete(user.getUuid()),
                () -> {
                    notifyItemRemoved(mValues.indexOf(user));
                    mValues.remove(user);
                    activity.stopRefreshing(user + " удален");
                }
        );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentUsersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItemOptionsView.setOnClickListener(v -> {
            setPosition(position);
            holder.itemView.showContextMenu(v.getX(), v.getY());
        });
        holder.itemView.setOnClickListener(v -> {
            setPosition(position);
            mOnClickListener.onClick(holder.itemView);
        });
        holder.itemView.setOnLongClickListener(v -> true);

        holder.itemView.setTag(mValues.get(position));
        holder.mItemTextView.setText(mValues.get(position).getName() + "\n" + mValues.get(position).getLastname());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setUserAPI(UserAPI mUserAPI) {
        this.mUserAPI = mUserAPI;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mItemTextView;
        public final TextView mItemOptionsView;

        public ViewHolder(FragmentUsersBinding binding) {
            super(binding.getRoot());
            mItemTextView = binding.itemTextView;
            mItemOptionsView = binding.itemOptionsTextView;
        }
    }
}