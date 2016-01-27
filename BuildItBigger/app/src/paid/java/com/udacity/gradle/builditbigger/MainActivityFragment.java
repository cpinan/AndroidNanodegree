package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.listeners.OnActivityListener;
import com.udacity.gradle.builditbigger.listeners.OnFragmentListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnFragmentListener {

    private OnActivityListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        return root;
    }

    @Override
    public void onCallFragment() {
        // Unused
    }

    @Override
    public void onForceFinish() {
        if (listener != null) {
            listener.onCallJoke();
        }
    }

    public void setListener(OnActivityListener listener) {
        this.listener = listener;
    }

}
