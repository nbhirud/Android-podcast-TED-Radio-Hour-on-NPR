package com.nbhirud.tedradiohouronnpr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Episode> episodeList = new ArrayList<>();
    ImageButton play;
    Button btn_list, btn_grid;
    public static ImageButton mPlaynPause;
    public static ProgressBar pb;
    public static int m_state = 0;
    public static final int STOPPED = 0;
    public static final int STARTING = 1;
    public static final int PLAYING = 2;
    public static final int PAUSED = 3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        btn_list = (Button) findViewById(R.id.list_layout);
        btn_grid = (Button) findViewById(R.id.grid_layout);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlaynPause = (ImageButton) findViewById(R.id.imageButtonPlayPause);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(episodeList, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        new GetEpisodeAsyncTask().execute("http://www.npr.org/rss/podcast.php?id=510298");

        mPlaynPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_state == PAUSED) {
                    Log.d("demo","in True");
                    pausePPImage();
                    Log.d("demo",MyAdapter.length+"");
                    if(MyAdapter.length > 0) {
                        MyAdapter.resumePod();
                        m_state = PLAYING;
                    }
                    if(MyGridAdapter.length > 0) {
                        MyGridAdapter.resumePod();
                        m_state = PLAYING;
                    }

                } else if(m_state == PLAYING) {
                    Log.d("demo","in False");
                    m_state = PAUSED;
                    if(MyAdapter.isPlaying) {
                        playPPImage();
                    } else if(MyGridAdapter.isGridPlaying) {
                        playPPImage();
                    } else {
                        Toast.makeText(MainActivity.this, "Click on PODCAST to start playing!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Click on PODCAST to start playing!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void pausePPImage() {
        if(mPlaynPause.getVisibility() != View.VISIBLE) {
            mPlaynPause.setVisibility(View.VISIBLE);
        }
        mPlaynPause.setImageResource(R.drawable.pause);
    }

    public static void playPPImage() {
        if(mPlaynPause.getVisibility() != View.VISIBLE) {
            mPlaynPause.setVisibility(View.VISIBLE);
        }
        mPlaynPause.setImageResource(R.drawable.play);
        if(m_state == STOPPED) {
            MyAdapter.stopAudio();
            MyGridAdapter.stopAudio();
        } else {
            MyAdapter.pausePod();
            MyGridAdapter.pausePod();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.list_layout:

                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(this);
                mAdapter = new MyAdapter(episodeList, getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                new GetEpisodeAsyncTask().execute("http://www.npr.org/rss/podcast.php?id=510298");
                Toast.makeText(MainActivity.this,"List View",Toast.LENGTH_SHORT).show();
                break;
            case R.id.grid_layout:
                if(mRecyclerView != null){

                }
                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new GridLayoutManager(this, 2);
                mAdapter = new MyGridAdapter(episodeList, getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                Toast.makeText(MainActivity.this,"Grid View",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyAdapter.stopAudio();
        MyGridAdapter.stopAudio();
    }

    public class GetEpisodeAsyncTask extends AsyncTask<String, Void, ArrayList<Episode>> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMax(100);
            pd.setMessage("Loading Podcasts...");
            pd.show();

        }

        @Override
        protected ArrayList<Episode> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    return XMLPullParser.EpisodePullParser.parseEpisode(in);
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<Episode> result) {
            super.onPostExecute(result);
            if(result != null) {
                for (int i = 0; i < result.size(); i++) {
                    episodeList.add(result.get(i));
                    mAdapter.notifyItemInserted(episodeList.size());

                }
            }

            pd.dismiss();
        }

    }

}
