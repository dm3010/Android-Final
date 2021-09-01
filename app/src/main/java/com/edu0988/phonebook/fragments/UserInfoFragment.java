package com.edu0988.phonebook.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu0988.phonebook.databinding.FragmentUserInfoBinding;
import com.edu0988.phonebook.model.User;

public class UserInfoFragment extends Fragment {

    private static final String USER_PARAM = "user";
    private FragmentUserInfoBinding binding;

    private User mUser;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static UserInfoFragment newInstance(User user) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_PARAM, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(USER_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        binding.nameTv.setText(mUser.getName());
        binding.lastnameTv.setText(mUser.getLastname());
        binding.phoneTv.setText(mUser.getPhone());
        getActivity().setTitle("Информация пользователя");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public User getUser() {
        return mUser;
    }

}