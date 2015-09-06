package com.multimedia.eformatic.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multimedia.eformatic.EFormatic;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.managers.HistoryManager;

/**
 * Created by Sandra on 23/08/15.
 */
public class QcmTabFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout mQCMContainer;
    private TextView mMessage;
    private WebView mWebView;
    private Button mValidButton;

    private String mEAN;
    private String mQcmUrl;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEAN = getArguments().getString("ean", "");
        mQcmUrl = getArguments().getString("qcm", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_training_qcm, container, false);

        mWebView = (WebView) v.findViewById(R.id.qcm_webview);
        mMessage = (TextView) v.findViewById(R.id.qcm_msg);
        mQCMContainer = (RelativeLayout) v.findViewById(R.id.qcm_container);
        mValidButton = (Button) v.findViewById(R.id.qcm_valid_button);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateDisplay();
    }

    public void updateDisplay() {
        mQCMContainer.setVisibility(mQcmUrl != null && !mQcmUrl.isEmpty() ? View.VISIBLE : View.GONE);
        mMessage.setVisibility(mQcmUrl != null && !mQcmUrl.isEmpty() ? View.GONE : View.VISIBLE);

        if (mQcmUrl != null && !mQcmUrl.isEmpty()) {
            mWebView.loadUrl(mQcmUrl);
        }

        updateButtonDisplay();
    }

    private void updateButtonDisplay() {
        boolean qcmIsDone = HistoryManager.getInstance().qcmIsDone(mEAN);
        mValidButton.setOnClickListener(qcmIsDone ? null : this);
        mValidButton.setBackgroundColor(EFormatic.RESOURCES.getColor(qcmIsDone ? R.color.gray : R.color.green));
        mValidButton.setText(EFormatic.RESOURCES.getString(qcmIsDone ? R.string.qcm_already_done : R.string.qcm_done));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qcm_valid_button:
                HistoryManager.getInstance().setQcmDone(mEAN);

                updateButtonDisplay();
                break;
            default:
                break;
        }
    }
}