package com.edu0988.phonebook.services;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

import com.edu0988.phonebook.model.User;

public interface UserAPI {

    //Для запуска в отдельном потоке
    ExecutorService es = Executors.newSingleThreadExecutor();

    default void exec(Runnable before, Runnable after) {
        es.execute(() -> {
/*
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            before.run();
                    new Handler(Looper.getMainLooper()).post(after);
                }
        );
    }
    ////

    //Работа с источником данных
    User get(String uuid);

    List<User> getAll();

    boolean add(User user);

    boolean update(User user);

    boolean delete(String uuid);

}
