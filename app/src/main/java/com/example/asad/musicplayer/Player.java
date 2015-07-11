package com.example.asad.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class Player extends ActionBarActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    Thread updateSeekBar;
    SeekBar ab;
    Button buttonPlay,buttonBW,buttonFF,buttonNext,buttonPrevious;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        buttonPlay= (Button)findViewById(R.id.buttonPlay);
        buttonBW= (Button)findViewById(R.id.buttonBW);
        buttonFF= (Button)findViewById(R.id.buttonFF);
        buttonNext= (Button)findViewById(R.id.buttonNext);
        buttonPrevious= (Button)findViewById(R.id.buttonPrevious);

        buttonPlay.setOnClickListener(this);
        buttonBW.setOnClickListener(this);
        buttonFF.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        ab=(SeekBar) findViewById(R.id.seekBar);
        updateSeekBar=new Thread(){
            @Override
            public void run(){
                int totalDuration=mp.getDuration();
                int currentPosition=0;

                while(currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition=mp.getCurrentPosition();
                        ab.setProgress(currentPosition);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                // super.run();
            }
        };
        if(mp!=null){
            mp.stop();
            mp.release();;
        }
        Intent i= getIntent();
        Bundle b= i.getExtras();
        mySongs= (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);
        u= Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();

        ab.setMax(mp.getDuration());
        updateSeekBar.start();
        ab.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
        public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){

            }
            @Override
        public void onStartTrackingTouch(SeekBar seekBar){

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
          mp.seekTo(seekBar.getProgress());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public  void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    public  void  onClick(View v){
        int id=v.getId();
        switch(id){
            case R.id.buttonPlay:
                if(mp.isPlaying()){
                    buttonPlay.setText(">");
                    mp.pause();
                    toast("pause the song");
                }
                else{
                    buttonPlay.setText("||");
                    mp.start();
                    toast("play the song");
                }
                break;
            case  R.id.buttonFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                toast("forward song 5 second");
                break;
            case R.id.buttonBW:
                mp.seekTo(mp.getCurrentPosition()-5000);
                toast("backward song 5 second");
                break;
            case R.id.buttonNext:
                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                u= Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                ab.setMax(mp.getDuration());
                toast("play next song ");
                break;
            case R.id.buttonPrevious:
                mp.stop();
                mp.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                u= Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                ab.setMax(mp.getDuration());
                toast("play previous song ");
                break;
        }
    }
}
