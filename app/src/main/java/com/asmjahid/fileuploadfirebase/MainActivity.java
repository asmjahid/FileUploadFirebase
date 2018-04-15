package com.asmjahid.fileuploadfirebase;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button mSelectBtn, mPauseBtn, mCancelBtn;
    private TextView mFilenameLabel, mSizeLabel, mProgressLabel;
    private ProgressBar mprogressBar;

    private final static int FILE_SELECT_CODE = 1;

    private StorageTask storageTask;
    private StorageReference mstorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectBtn = findViewById(R.id.select_btn);
        mPauseBtn = findViewById(R.id.pause_btn);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mSizeLabel = findViewById(R.id.size_label);
        mProgressLabel = findViewById(R.id.progress_label);
        mprogressBar = findViewById(R.id.upload_progress);

        mFilenameLabel = findViewById(R.id.filetype_label);

        mstorageReference = FirebaseStorage.getInstance().getReference();

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenFileSelector();
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {

                    String btnTask = mPauseBtn.getText().toString();

                    if (btnTask.equals("Pause Upload")) {

                        storageTask.pause();
                        mPauseBtn.setText("Resume Upload");

                    } else {

                        storageTask.resume();
                        mPauseBtn.setText("Pause Upload");
                    }
                } catch(Exception ex) {

                    Toast.makeText(MainActivity.this, "Please Select a file", Toast.LENGTH_LONG).show();
                }
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {

                    storageTask.cancel();
                    mFilenameLabel.setText(null);
                    mprogressBar.setProgress(0);
                    mSizeLabel.setText(null);
                    mProgressLabel.setText(null);

                } catch(Exception e) {

                    Toast.makeText(MainActivity.this, "Please Select a file", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void OpenFileSelector() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {

            startActivityForResult(Intent.createChooser(intent, "Select a file to Choose"), FILE_SELECT_CODE);

        } catch(android.content.ActivityNotFoundException ex) {

            Toast.makeText(this, "Please Install a file manager", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {

            Uri fileUri = data.getData();

            String uriString = fileUri.toString();
            File myFile = new File(uriString);

            String displayName = null;
            if (uriString.startsWith("content://")) {

                Cursor cursor = null;
                try {
                    cursor = MainActivity.this.getContentResolver().query(fileUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {

                displayName = myFile.getName();
            }

            // Progress File Name
            mFilenameLabel.setText(displayName);

            StorageReference fileRef = mstorageReference.child("files/" + displayName);

            storageTask = fileRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(MainActivity.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(MainActivity.this, "Please try again.....", Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            mprogressBar.setProgress((int) progress);

                            // Progress MB
                            String progressText = taskSnapshot.getBytesTransferred() / (1024 * 1024) + "/" + taskSnapshot.getTotalByteCount() / (1024 * 1024) + " mb";
                            mSizeLabel.setText(progressText);

                            // Progress %
                            double progresspercent = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            mProgressLabel.setText((int) progresspercent + "%");
                        }
                    });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
