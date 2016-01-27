package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.listeners.OnActivityListener;
import com.udacity.gradle.builditbigger.listeners.OnFragmentListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnFragmentListener {

    private boolean wait;
    private InterstitialAd mInterstitialAd;
    private OnActivityListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.adUnitId));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                if (listener != null) {
                    listener.onCallJoke();
                }
            }
        });
        requestNewInterstitial();
        return root;
    }

    public void setListener(OnActivityListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCallFragment() {
        wait = false;
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            wait = true;
        }
    }

    @Override
    public void onForceFinish() {
        if (wait) {
            if (listener != null) {
                listener.onCallJoke();
            }
            wait = false;
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

}
