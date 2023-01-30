package com.kokofarm.kokofarm_user_app.kkf_utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public abstract class BackTasker {

    ExecutorService executorService;

    public BackTasker(){
        executorService = Executors.newSingleThreadExecutor();
    }

    public Future execute(){
        Future<String> future = executorService.submit(() -> doInBackground());

        return future;
    }

    protected abstract String doInBackground();
}
