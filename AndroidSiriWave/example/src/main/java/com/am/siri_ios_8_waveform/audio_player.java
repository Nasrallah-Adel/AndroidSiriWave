package com.am.siri_ios_8_waveform;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.am.siriview.DrawView;
import com.am.siriview.UpdaterThread;

import java.io.IOException;

public class audio_player extends AppCompatActivity {
    long REFRESH_INTERVAL_MS = 30;
    private boolean keepGoing = true;
    LinearLayout layout;
    MediaRecorder mRecorder;
    float tr = 400.0f;
    UpdaterThread up;
    DrawView view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        final MediaPlayer SoundMediaPlayer = MediaPlayer.create(this, R.raw.happy);

        final Button play= (Button) this.findViewById(R.id.play);
        final Button red= (Button) this.findViewById(R.id.red);
        final Button blue= (Button) this.findViewById(R.id.blue);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view2.setWaveColor(Color.RED);

                System.out.println("cmvbjksjhjksdkjhjsdhkjhjkfh");
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view2.setWaveColor(Color.BLUE);

                System.out.println("cmvbjksjhjksdkjhjsdhkjhjkfh");
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hi i start");
               SoundMediaPlayer.start();
            }
        });
        final Button stop= (Button) this.findViewById(R.id.stop);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundMediaPlayer.stop();

            }
        });
        this.view2 = (DrawView) findViewById(R.id.root);
        startVoiceRecorder();
        new Thread(new Runnable() {
            public void run() {
                while (audio_player.this.keepGoing) {
                    try {
                        Thread.sleep(Math.max(0, audio_player.this.REFRESH_INTERVAL_MS - audio_player.this.redraw()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        this.up = new UpdaterThread(30, this.view2, this);
        this.up.start();
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(Math.max(0, audio_player.this.REFRESH_INTERVAL_MS - audio_player.this.redraw()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    public void startVoiceRecorder() {
        this.mRecorder = new MediaRecorder();
        this.mRecorder.setAudioSource(1);
        this.mRecorder.setOutputFormat(0);
        this.mRecorder.setAudioEncoder(0);
        this.mRecorder.setOutputFile("/dev/null");
        try {
            this.mRecorder.prepare();
        } catch (IOException e) {
            Log.e("sada", "prepare() failed");
        }
        this.mRecorder.start();
    }



    private long redraw() {
        long t = System.currentTimeMillis();
        display_game();
        return System.currentTimeMillis() - t;
    }

    private void display_game() {
        runOnUiThread(new Runnable() {
            public void run() {
                audio_player.this.view2.setMaxAmplitude(((float)  audio_player.this.mRecorder.getMaxAmplitude()) * 1.6f);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRecorder.stop();
    }
}
