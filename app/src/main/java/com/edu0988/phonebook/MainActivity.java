package com.edu0988.phonebook;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks;

import com.edu0988.phonebook.adapter.UserRecyclerViewAdapter;
import com.edu0988.phonebook.fragments.UserEditFragment;
import com.edu0988.phonebook.fragments.UserInfoFragment;
import com.edu0988.phonebook.fragments.UserListFragment;
import com.edu0988.phonebook.databinding.ActivityMainBinding;
import com.edu0988.phonebook.model.User;
import com.edu0988.phonebook.services.UserAPIHttp;
import com.edu0988.phonebook.services.UserAPISqlLite;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment currentFragment;
    private UserRecyclerViewAdapter adapter;
    private static int currentAPI = R.id.local_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, new UserListFragment(), "USER_LIST_FRAGMENT")
                    .commit();
        }

        adapter = new UserRecyclerViewAdapter(this,
                currentAPI == R.id.local_api ?
                        new UserAPISqlLite(this) :
                        new UserAPIHttp(),
                itemViewOnClickListener);

        // События фрагментов (onResumed)
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true);
        // SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(() -> getAdapter().refreshList());
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks);
        super.onDestroy();
    }

    // Меню выбора подключения (локальная БД, удаленный сервер)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.api_option_menu, menu);
        menu.findItem(currentAPI).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() != currentAPI) {
            switch (item.getItemId()) {
                case R.id.local_api:
                    getAdapter().setUserAPI(new UserAPISqlLite(this));
                    currentAPI = R.id.local_api;
                    break;
                case R.id.http_api:
                    getAdapter().setUserAPI(new UserAPIHttp());
                    currentAPI = R.id.http_api;
                    break;
            }
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            getAdapter().refreshList();
            item.setChecked(true);
        }
        return true;
    }
    //


    private FragmentLifecycleCallbacks fragmentLifecycleCallbacks = new FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);

            currentFragment = getSupportFragmentManager().getFragments().get(0);

            switch (f.getTag()) {
                case "USER_VIEW_FRAGMENT":
                    binding.fab.setImageResource(R.drawable.ic_action_edit);
                    binding.fab.setOnClickListener(fabEditAction);
                    break;
                case "USER_LIST_FRAGMENT":
                    binding.fab.setImageResource(R.drawable.ic_action_add);
                    binding.fab.setOnClickListener(fabAddAction);
                    break;
                case "USER_EDIT_FRAGMENT":
                    binding.fab.setImageResource(R.drawable.ic_action_save);
                    binding.fab.setOnClickListener(fabSaveAction);
                    break;
            }
        }
    };

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, fragment, tag)
                .commit();
        currentFragment = fragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.item_option_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        getAdapter().delete(getAdapter().getSelectedPosition());
        return super.onContextItemSelected(item);
    }

    // Выбор элемента
    private View.OnClickListener itemViewOnClickListener = v -> {
        replaceFragment(UserInfoFragment.newInstance(getAdapter().getSelectedPosition()), "USER_VIEW_FRAGMENT");
    };

    // Методы обработки FAB (Save, Add, Edit)
    private View.OnClickListener fabSaveAction = v -> {
        User user = ((UserEditFragment) currentFragment).getUser();
        getAdapter().addOrUpdate(user);
        onBackPressed();
    };

    private View.OnClickListener fabAddAction = v ->
            replaceFragment(UserEditFragment.newInstance(new User()), "USER_EDIT_FRAGMENT");


    private View.OnClickListener fabEditAction = v ->
            replaceFragment(UserEditFragment.newInstance(((UserInfoFragment) currentFragment)
                    .getUser()), "USER_EDIT_FRAGMENT");

    public void startRefreshing() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    public void stopRefreshing(String text) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public UserRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}


