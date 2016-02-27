package com.example.kevin.contentresolvemusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvSongs;
    private List<MusicLoader.MusicInfo> musiclist;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MusicLoader musicLoader= MusicLoader.instance(getContentResolver());
        musiclist=musicLoader.getMusicList();

    }
}
