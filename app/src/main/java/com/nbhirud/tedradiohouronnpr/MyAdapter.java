package com.nbhirud.tedradiohouronnpr;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sumeesh on 19/06/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    MainActivity activity;
    ArrayList<Episode> mEpisode;
    Context mContext;
    public static MediaPlayer mP = null;
    public static boolean isPlaying = false;
    public static int length = 0;
    public static final int STOPPED = 0;
    public static final int STARTING = 1;
    public static final int PLAYING = 2;
    public static final int PAUSED = 3;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle, mPosted;
        public ImageView mImage;
        public ImageButton mPlay;
        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.textViewTitle2);
            mPosted = (TextView) v.findViewById(R.id.textViewPub2);
            mImage = (ImageView) v.findViewById(R.id.imageViewIcon2);
            mPlay = (ImageButton) v.findViewById(R.id.imageButton);
        }
    }

    public MyAdapter(ArrayList<Episode> mEpisode, Context mContext) {
        this.mContext = mContext;
        this.mEpisode = mEpisode;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_episode_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Episode episode = mEpisode.get(position);
        episode.toString();
        holder.mTitle.setText(episode.getTitle());
        holder.mPosted.setText("Posted: " + episode.getPubDate());

        String url = episode.getImgUrl();
        final String url_pop = episode.getMp3Url();

        Picasso.with(mContext).load(url).into(holder.mImage);


        holder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","button clicked"+ episode.getTitle());
                Intent intent =new Intent(mContext,PodcastDetails.class);
                intent.putExtra("title",episode.getTitle());
                intent.putExtra("image",episode.getImgUrl());
                intent.putExtra("description",episode.getDescription());
                intent.putExtra("date",episode.getPubDate());
                intent.putExtra("duration",episode.getDuration());
                intent.putExtra("podcast",episode.getMp3Url());
                mContext.startActivity(intent);
            }
        });

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","button clicked"+ episode.getTitle());
                Intent intent =new Intent(mContext,PodcastDetails.class);
                intent.putExtra("title",episode.getTitle());
                intent.putExtra("image",episode.getImgUrl());
                intent.putExtra("description",episode.getDescription());
                intent.putExtra("date",episode.getPubDate());
                intent.putExtra("duration",episode.getDuration());
                intent.putExtra("podcast",episode.getMp3Url());
                mContext.startActivity(intent);
            }
        });

        holder.mPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                Toast.makeText(mContext, "Podcast will Play soon!!", Toast.LENGTH_LONG).show();
                if(activity.m_state == PLAYING) {
                    mP.release();
                    activity.m_state = STOPPED;
                    mP = null;
                }

                if(mP == null) {
                    if(MyGridAdapter.isGridPlaying == true) {
                        MyGridAdapter.stopAudio();
                        activity.m_state = STOPPED;
                    }
                    mP = new MediaPlayer();
                    try {
                        mP.setDataSource(url_pop);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mP.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mP.prepareAsync();
                    activity.pb.setMax(mP.getDuration());
                    activity.pb.setProgress(0);
                    activity.pb.setVisibility(View.VISIBLE);
                    mP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            activity.m_state = PLAYING;
                            isPlaying = true;
                            activity.pausePPImage();

                            activity.pb.setMax(mp.getDuration());
                            new CountDownTimer(mp.getDuration(), 500) {
                                public void onTick(long millisUntilFinished) {
                                    activity.pb.setProgress(activity.pb.getProgress() + 500);
                                }
                                public void onFinish() {}
                            }.start();

                        }
                    });
                    mP.setScreenOnWhilePlaying(true);
                }
            }
        });
    }


    public static void pausePod() {
        if(mP != null) {
            mP.pause();
            MainActivity.m_state = PAUSED;
            length = mP.getCurrentPosition();
        }
    }

    public static void resumePod() {
        if(mP != null) {
            mP.seekTo(length);
            mP.start();
        }
    }

    public static void stopAudio(){
        isPlaying = false;
        if(mP !=null) {
            mP.stop();
        }
        MainActivity.m_state = STOPPED;
        //mP.release();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEpisode.size();
    }


}

