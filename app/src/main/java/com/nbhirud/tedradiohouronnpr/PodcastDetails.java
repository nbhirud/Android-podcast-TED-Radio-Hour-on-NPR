package com.nbhirud.hw4;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PodcastDetails extends AppCompatActivity {

    Intent intent=getIntent();
    TextView title,duration,date,description;
    ImageView mainImage,btnImage;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_details);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        TextView title, description, duration, date;
        ImageView mainImage;

        title = (TextView) findViewById(R.id.textViewPTitle);
        description = (TextView) findViewById(R.id.textViewDescPr);
        duration = (TextView) findViewById(R.id.textViewPrDuration);
        date = (TextView) findViewById(R.id.textViewPrPub);
        mainImage = (ImageView) findViewById(R.id.imageViewPr);
        btnImage = (ImageButton) findViewById(R.id.imageButtonPrPlay);

        title.setText(getIntent().getStringExtra("title"));
        description.setText("Description: " + getIntent().getStringExtra("description"));
        date.setText("Pub Date: " +getIntent().getStringExtra("date"));
        duration.setText("Duration: " + getIntent().getStringExtra("duration"));
        btnImage.setImageResource(R.drawable.play);
        String url=getIntent().getStringExtra("podcast");

        Picasso.with(this).load(getIntent().getStringExtra("image")).into(mainImage);
//        progressBar.setProgress(0);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","play clicked"+getIntent().getStringExtra("podcast"));
                MediaPlayer player = new MediaPlayer();
                try {
                    Log.d("demo","da nu "+String.valueOf(player.isPlaying()));
                    if(player!=null && player.isPlaying()){
                        player.stop();
                        btnImage.setImageResource(R.drawable.play);
                        Log.d("demo", String.valueOf(player!=null)+" "+String.valueOf(player.isPlaying()));
                    }else{
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(getIntent().getStringExtra("podcast"));
                        player.prepare();
                        btnImage.setImageResource(R.drawable.pause);
                        player.start();}
                    Log.d("demo", String.valueOf(player!=null)+" "+String.valueOf(player.isPlaying()));

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

    }
}
