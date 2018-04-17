package com.asmjahid.fileuploadfirebase;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoStreamActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView Playbutton;
    private TextView StartTime,DurationTime;
    private ProgressBar VideoProgressBar;
    private ProgressBar bufferProgress;

    private boolean isPlaying = false;

    private Uri Videouri;

    private int current = 0;
    private int duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_stream);

        videoView = findViewById(R.id.videoView);
        Playbutton = findViewById(R.id.Play_btn);
        StartTime = findViewById(R.id.start_time);
        DurationTime = findViewById(R.id.duration_time);
        VideoProgressBar = findViewById(R.id.video_progress);
        VideoProgressBar.setMax(100);
        bufferProgress = findViewById(R.id.bufferProgress);

        Videouri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fileuploadfirebase-2f126.appspot.com/o/9GAG%20-%20%E0%B2%A0_%E0%B2%A0(1080p).mp4?alt=media&token=cb1d1bb1-d362-4e7e-a3ea-0cffc58e1511");
        videoView.setVideoURI(Videouri);
        videoView.requestFocus();

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {

                if (i == mediaPlayer.MEDIA_INFO_BUFFERING_START) {

                    bufferProgress.setVisibility(View.VISIBLE);

                } else if(i == mediaPlayer.MEDIA_INFO_BUFFERING_END) {

                    bufferProgress.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                duration = mediaPlayer.getDuration()/1000;
                String durationString = String.format("%02d:%02d", duration/60, duration%60);

                DurationTime.setText(durationString);
            }
        });

        videoView.start();
        isPlaying = true;
        Playbutton.setImageResource(R.drawable.ic_pause_black_24dp);

        new VideoProgress().execute();

        Playbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(isPlaying) {

                    videoView.pause();
                    isPlaying = false;
                    Playbutton.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                } else {

                    videoView.start();
                    Playbutton.setImageResource(R.drawable.ic_pause_black_24dp);
                    isPlaying = true;
                }
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();
        isPlaying = false;
    }

    public class VideoProgress extends AsyncTask<Void, Integer,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            do {

                current  = videoView.getCurrentPosition()/1000;

                try {

                    int currentPercent = current * 100 / duration;
                    publishProgress(currentPercent);

                } catch(Exception ex) {

                }

            } while (VideoProgressBar.getProgress()<=100) ;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            //VideoProgressBar.setProgress(values[0]);

            try {

                int currentPercetn = values[0] * 100 / duration;
                VideoProgressBar.setProgress(values[0]);

                String currentString = String.format("%02d:%02d", values[0] / 60, values[0] % 60);

                StartTime.setText(currentString);

            } catch(Exception ex) {

            }
        }
    }
}
