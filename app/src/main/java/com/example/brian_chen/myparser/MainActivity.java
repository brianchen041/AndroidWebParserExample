package com.example.brian_chen.myparser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private EditText mEditText;
    private TextView mTextView;

    private final ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.url);
        mTextView = findViewById(R.id.text);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                mTextView.setText("");
                mSingleThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Connection.Response response;
                            response = Jsoup.connect(mEditText.getText().toString()).execute();
                            final String responseBody = response.body()
                                    .trim().replaceAll("\n+", "\n");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextView.setText(responseBody);
                                }
                            });
                        } catch (IOException e) {
                            Log.e(TAG, "IOException e=" + e);
                        }
                    }
                });
                break;
            default:
                Log.e(TAG, "Unknown id=" + v.getId());
        }
    }
}
