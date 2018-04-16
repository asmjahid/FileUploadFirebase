package com.asmjahid.fileuploadfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadActivity extends AppCompatActivity {

    private Button mMultipleFile,mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mMultipleFile = findViewById(R.id.multiple_btn);
        mFile = findViewById(R.id.file_btn);

        mMultipleFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent FileIntent = new Intent(UploadActivity.this, MultipleFileUploadActivity.class);
                startActivity(FileIntent);
            }
        });

        mFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent FileIntent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(FileIntent);
            }
        });
    }
}
