package com.udacity.gradle.builditbigger;

import android.test.AndroidTestCase;
import android.util.Log;

import com.udacity.gradle.builditbigger.listeners.JokeListener;
import com.udacity.gradle.builditbigger.tasks.JokesAsyncTask;

import java.util.concurrent.CountDownLatch;

/**
 * @author Carlos Piñan
 */
public class JokeAsyncTaskTest extends AndroidTestCase implements JokeListener {

    private static final String LOG_TAG = "JokeAsyncTaskTest";

    private CountDownLatch countDownLatch;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        countDownLatch = new CountDownLatch(1);
    }

    public void testJokeAsyncTask() {
        JokesAsyncTask jokesAsyncTask = new JokesAsyncTask(this);
        assertNotNull(jokesAsyncTask);
        jokesAsyncTask.execute();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(String result) {
        assertNotNull(result);
        Log.i(LOG_TAG, result);
        countDownLatch.countDown();
    }
}
