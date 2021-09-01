package com.edu0988.phonebook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.edu0988.phonebook.databinding.FragmentUserEditBinding;
import com.edu0988.phonebook.model.User;

public class UserEditFragment extends Fragment {

    private static final String USER_PARAM = "user";
    private FragmentUserEditBinding binding;

    private User mUser;

    public UserEditFragment() {
        // Required empty public constructor
    }

    public static UserEditFragment newInstance(User user) {
        UserEditFragment fragment = new UserEditFragment();
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
        binding = FragmentUserEditBinding.inflate(inflater, container, false);
        if (mUser != null) {
            binding.nameEt.setText(mUser.getName());
            binding.lastnameEt.setText(mUser.getLastname());
            binding.phoneEt.setText(mUser.getPhone());
            getActivity().setTitle("Редактирование пользователя");
        } else getActivity().setTitle("Новый пользователь");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public User getUser() {
        mUser.setName(binding.nameEt.getText().toString());
        mUser.setLastname(binding.lastnameEt.getText().toString());
        mUser.setPhone(binding.phoneEt.getText().toString());
        return mUser;
    }

}