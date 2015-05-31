package co.tuzun.emrehan.trafip;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends TrafipActivity implements SurfaceHolder.Callback {

    private Button mainButton;

    private Intent intent;

    private MediaPlayer mp = null;
    SurfaceView mSurfaceView=null;
    private SurfaceHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DestinationActivity.class);
                startActivity(intent);
            }
        });

        mSurfaceView = (SurfaceView)findViewById(R.id.surface);
        mp = new MediaPlayer();

        holder = mSurfaceView.getHolder();
        holder.setFixedSize(800, 480);
        holder.addCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Uri video = Uri.parse("android.resource://co.tuzun.emrehan.trafip/"
                + R.raw.newyork);

        try {
            mp.setDisplay(holder);
            mp.setDataSource(this, video);
            mp.prepare();
            mp.setLooping(true);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setVolume(0, 0);
            Log.d("video", "" + video.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the dimensions of the video
        int videoWidth = mp.getVideoHeight();
        int videoHeight = mp.getVideoWidth();
        Log.d("video", "" + videoWidth + " " + videoHeight);


        //Start video
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
