package com.multimedia.eformatic.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.multimedia.eformatic.R;

/**
 * Created by Sandra on 23/08/15.
 */
public class QcmTabFragment extends Fragment {

    private TextView mMessage;
    private WebView mWebView;

    private String mQcmUrl;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQcmUrl = getArguments().getString("qcm", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_training_qcm, container, false);

        mWebView = (WebView) v.findViewById(R.id.qcm_webview);
        mMessage = (TextView) v.findViewById(R.id.qcm_msg);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateDisplay();
    }

    public void updateDisplay() {
        mWebView.setVisibility(mQcmUrl != null && !mQcmUrl.isEmpty() ? View.VISIBLE : View.GONE);
        mMessage.setVisibility(mQcmUrl != null && !mQcmUrl.isEmpty() ? View.GONE : View.VISIBLE);

        if (mQcmUrl != null && !mQcmUrl.isEmpty()) {
            mWebView.loadUrl(mQcmUrl);
        }

    }
}