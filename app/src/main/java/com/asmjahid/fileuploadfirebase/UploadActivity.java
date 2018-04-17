package com.asmjahid.fileuploadfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadActivity extends AppCompatActivity {

    private Button mMultipleFile,mFile,mVideoStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mMultipleFile = findViewById(R.id.multiple_btn);
        mFile = findViewById(R.id.file_btn);
        mVideoStream = findViewById(R.id.video_btn);

        mMultipleFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent MultipleFileIntent = new Intent(UploadActivity.this, MultipleFileUploadActivity.class);
                startActivity(MultipleFileIntent);
            }
        });

        mFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent FileIntent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(FileIntent);
            }
        });

        mVideoStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent VideoStreamIntent = new Intent(UploadActivity.this, VideoStreamActivity.class);
                startActivity(VideoStreamIntent);
            }
        });
    }
}
