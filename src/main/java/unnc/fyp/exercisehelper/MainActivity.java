package unnc.fyp.exercisehelper;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.view.View;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MusicService musicService;
    private MusicService.MyBinder binder;
    private LinearLayout music_list;
    private RelativeLayout slow_music_1;
    private RelativeLayout slow_music_2;
    private RelativeLayout slow_music_3;
    private RelativeLayout slow_music_4;

    private RelativeLayout fast_music_1;
    private RelativeLayout fast_music_2;
    private RelativeLayout fast_music_3;
    private RelativeLayout fast_music_4;
    private RelativeLayout fast_music_5;


    private ImageView slow_music_pic_1;
    private ImageView slow_music_pic_2;
    private ImageView slow_music_pic_3;
    private ImageView slow_music_pic_4;

    private ImageView fast_music_pic_1;
    private ImageView fast_music_pic_2;
    private ImageView fast_music_pic_3;
    private ImageView fast_music_pic_4;
    private ImageView fast_music_pic_5;


    private Button btn_slow_tempo;
    private Button btn_fast_tempo;
    private Spinner spinner_exercise;
    private String spinner_exercise_selected_string;
    private String spinner_bpm_selected_string;
    private Spinner spinner_bpm;
    private Button btn_play_or_pause;
    private Button btn_stop;
    private SeekBar seekBar;
    private TextView musicTime;
    private TextView musicTotal;
    private String username;
    private TextView username_view;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("ID");
        //Log.i("Exercise helper", username + "^^^^^");

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headview = navigationView.getHeaderView(0);

        music_list = findViewById(R.id.music_list);
        slow_music_1 = findViewById(R.id.slow_music_1);
        slow_music_2 = findViewById(R.id.slow_music_2);
        slow_music_3 = findViewById(R.id.slow_music_3);
        slow_music_4 = findViewById(R.id.slow_music_4);

        fast_music_1 = findViewById(R.id.fast_music_1);
        fast_music_2 = findViewById(R.id.fast_music_2);
        fast_music_3 = findViewById(R.id.fast_music_3);
        fast_music_4 = findViewById(R.id.fast_music_4);
        fast_music_5 = findViewById(R.id.fast_music_5);

        slow_music_pic_1 = findViewById(R.id.music_intm_image);
        slow_music_pic_2 = findViewById(R.id.music_intm_image2);
        slow_music_pic_3 = findViewById(R.id.music_intm_image3);
        slow_music_pic_4 = findViewById(R.id.music_intm_image4);

        fast_music_pic_1 = findViewById(R.id.fast_music_intm_image1);
        fast_music_pic_2 = findViewById(R.id.fast_music_intm_image2);
        fast_music_pic_3 = findViewById(R.id.fast_music_intm_image3);
        fast_music_pic_4 = findViewById(R.id.fast_music_intm_image4);
        fast_music_pic_5 = findViewById(R.id.fast_music_intm_image5);

        btn_fast_tempo = findViewById(R.id.BtnFastMusic);
        btn_slow_tempo = findViewById(R.id.BtnSlowMusic);

        btn_play_or_pause = findViewById(R.id.BtnPlayOrPause);
        btn_stop = findViewById(R.id.BtnStop);
        seekBar = findViewById(R.id.MusicSeekBar);
        musicTime = (TextView) findViewById(R.id.MusicTime);
        musicTotal = (TextView) findViewById(R.id.MusicTotal);

        username_view = (TextView) headview.findViewById(R.id.username_view);
        username_view.setText(username);

        setBtnTempo();
        setSpinner();
        bindServiceConnection();
        preSetMusics();
        musicStatusBtn();               // control the play pause stop buttons
        seekBarMonitor();               // the podcast progress/timeline seekbar controller

    }

    private void setBtnTempo() {
        btn_fast_tempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_list.removeAllViewsInLayout();
                music_list.addView(fast_music_1);
                music_list.addView(fast_music_2);
                music_list.addView(fast_music_3);
                music_list.addView(fast_music_4);
                music_list.addView(fast_music_5);

            }
        });

        btn_slow_tempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_list.removeAllViewsInLayout();
                music_list.addView(slow_music_1);
                music_list.addView(slow_music_2);
                music_list.addView(slow_music_3);
                music_list.addView(slow_music_4);
            }
        });
    }

    private void setSpinner() {
        String[] e_type = new String[]{"All exercise..","Treadmill", "Elliptical Machine", "Sit-ups", "Squat", "Step-ups"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, e_type);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        spinner_exercise = super.findViewById(R.id.spinner_exercise);
        spinner_exercise.setAdapter(adapter);
        spinner_exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_exercise_selected_string = parent.getItemAtPosition(position).toString();          //store the exercise name selected in string
                //spinner_bpm.setSelection(0);
                setMusicBaseExercise();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        String[] bpm_type = new String[]{"All tempos..","<60 bpm (beat per minutes", "60-90 bpm", "90-120 bpm", ">120 bpm"};
        ArrayAdapter<String> adapter_bpm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bpm_type);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        spinner_bpm = super.findViewById(R.id.spinner_bpm);
        spinner_bpm.setAdapter(adapter_bpm);
        spinner_bpm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_bpm_selected_string = parent.getItemAtPosition(position).toString();          //store the bpm selected in string
                //spinner_exercise.setSelection(0);
                setMusicBaseBpm();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setMusicBaseExercise() {
        if( spinner_exercise_selected_string == "All exercise.." ) {
            music_list.removeAllViewsInLayout();
            music_list.addView(slow_music_1);
            music_list.addView(slow_music_2);
            music_list.addView(slow_music_3);
            music_list.addView(slow_music_4);

            music_list.addView(fast_music_1);
            music_list.addView(fast_music_2);
            music_list.addView(fast_music_3);
            music_list.addView(fast_music_4);
            music_list.addView(fast_music_5);
        }else if( spinner_exercise_selected_string == "Treadmill" ) {
            music_list.removeAllViewsInLayout();
            music_list.addView(fast_music_1);
            music_list.addView(fast_music_2);
            music_list.addView(fast_music_3);
        }else if( spinner_exercise_selected_string == "Elliptical Machine" ) {
            music_list.removeAllViewsInLayout();
            music_list.addView(slow_music_1);
            music_list.addView(slow_music_2);
            music_list.addView(slow_music_3);
            music_list.addView(slow_music_4);
            music_list.addView(fast_music_2);
        }else if( spinner_exercise_selected_string == "Sit-ups" ) {
            music_list.removeAllViewsInLayout();
            music_list.addView(fast_music_1);
            music_list.addView(fast_music_2);
            music_list.addView(fast_music_3);
        }else if( spinner_exercise_selected_string == "Squat" ) {
            music_list.removeAllViewsInLayout();
            music_list.addView(slow_music_3);
            music_list.addView(fast_music_2);
            music_list.addView(slow_music_4);
        }else if( spinner_exercise_selected_string == "Step-ups" ) {
            music_list.removeAllViewsInLayout();
            music_list.addView(fast_music_1);
            music_list.addView(fast_music_2);
            music_list.addView(fast_music_3);
        }
    }

    private void setMusicBaseBpm() {
        if(spinner_bpm_selected_string == "All tempos.."){
            music_list.removeAllViewsInLayout();
            music_list.addView(slow_music_1);
            music_list.addView(slow_music_2);
            music_list.addView(slow_music_3);
            music_list.addView(slow_music_4);

            music_list.addView(fast_music_1);
            music_list.addView(fast_music_2);
            music_list.addView(fast_music_3);
            music_list.addView(fast_music_4);
            music_list.addView(fast_music_5);
        }else if(spinner_bpm_selected_string == "<60 bpm (beat per minutes"){
            music_list.removeAllViewsInLayout();
            music_list.addView(slow_music_1);
            music_list.addView(slow_music_3);
        }else if(spinner_bpm_selected_string ==  "60-90 bpm"){
            music_list.removeAllViewsInLayout();
            music_list.addView(slow_music_2);
            music_list.addView(fast_music_2);
            music_list.addView(slow_music_4);

        }else if(spinner_bpm_selected_string ==  "90-120 bpm"){
            music_list.removeAllViewsInLayout();

        }else if(spinner_bpm_selected_string == ">120 bpm"){
            music_list.removeAllViewsInLayout();
            music_list.addView(fast_music_1);
            music_list.addView(fast_music_3);
            music_list.addView(fast_music_4);
            music_list.addView(fast_music_5);
        }


    }

    /**
     * the two pre-selected podcasts
     * click the picture, play the music
     */
    private void preSetMusics() {
        slow_music_pic_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                musicService.streamingPlayer.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.slow_music_1);
                musicService.streamingPlayer.state = StreamingPlayer.StreamingPlayerState.PLAYING;
                musicService.streamingPlayer.mediaPlayer.start();**/
                loadMusic("http://music.163.com/song/media/outer/url?id=34341360.mp3");
            }
        });

        slow_music_pic_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=34341358.mp3");
            }
        });

        slow_music_pic_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=494645920.mp3");
            }
        });
        slow_music_pic_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=478029284.mp3");
            }
        });

        fast_music_pic_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=30064765.mp3");
            }
        });
        fast_music_pic_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=29947420.mp3");
            }
        });

        fast_music_pic_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=32317104.mp3");
            }
        });

        fast_music_pic_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=32192191.mp3");
            }
        });
        fast_music_pic_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMusic("http://music.163.com/song/media/outer/url?id=27417449.mp3");
            }
        });
    }

    private void loadMusic(String str) {

        if(musicService.streamingPlayer.getFilePath() != str) {
            musicService.streamingPlayer.stop();
            musicService.streamingPlayer.load(str);
/*
            musicService.streamingPlayer.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.slow_music_1);
            musicService.streamingPlayer.state = StreamingPlayer.StreamingPlayerState.PLAYING;
            musicService.streamingPlayer.mediaPlayer.start();
*/
            //binder.loadMusic(str);
            btn_play_or_pause.setText("PAUSE");
            handler.post(runnable);
        }else if (musicService.streamingPlayer.getState() == StreamingPlayer.StreamingPlayerState.STOPPED) {

            musicService.streamingPlayer.load(str);
            //binder.loadMusic(str);
            btn_play_or_pause.setText("PAUSE");
            handler.post(runnable);
        }
    }

    // Use Handler to refresh the UI status for podcast progress
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(musicService.streamingPlayer.mediaPlayer != null) {
                musicTime.setText(time.format(musicService.streamingPlayer.mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(musicService.streamingPlayer.mediaPlayer.getCurrentPosition());
                seekBar.setMax(musicService.streamingPlayer.mediaPlayer.getDuration());
                musicTotal.setText(time.format(musicService.streamingPlayer.mediaPlayer.getDuration()));
                handler.postDelayed(runnable, 600);
            }
        }
    };


    /**
     *  bind the service with activity
     *  if the target service not exist, start service at first
     */
    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        if(MusicService.started != true) {
            startService(intent);
        }
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
        Log.i("mdpcw2.2", "Bind service connection here *****");

    }

    /**
     *  Callback onServiceConnected function, get the Service object through IBinder, realize the binding of Activity and Service
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService();
            binder = (MusicService.MyBinder) service;

            if (musicService.streamingPlayer.getState() == StreamingPlayer.StreamingPlayerState.PLAYING) {
                btn_play_or_pause.setText("PAUSE");
                handler.post(runnable);
            }
            if (musicService.streamingPlayer.getState() == StreamingPlayer.StreamingPlayerState.PAUSED) {
                btn_play_or_pause.setText("PLAY");
                handler.post(runnable);
            }
            Log.i("Exercise helper", musicService + "");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Exercise Helper", "MainActivity onServiceDisconnected");
            musicService = null;
        }
    };


    /**
     * Handle three buttons at the bottom
     * Play/pause, stop, comments
     */
    private void musicStatusBtn() {
        btn_play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicService.streamingPlayer.getState() == StreamingPlayer.StreamingPlayerState.PLAYING) {
                    musicService.streamingPlayer.pause();
                    btn_play_or_pause.setText("PLAY");
                }else if(musicService.streamingPlayer.getState() == StreamingPlayer.StreamingPlayerState.PAUSED) {
                    musicService.streamingPlayer.play();
                    btn_play_or_pause.setText("PAUSE");
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(musicService.streamingPlayer.getState() != StreamingPlayer.StreamingPlayerState.STOPPED) {
                    handler.removeCallbacks(runnable);
                    binder.stopMusic();
                    btn_play_or_pause.setText("PLAY");
                    musicTime.setText("00:00");
                    seekBar.setProgress(1);
                    seekBar.setMax(100);
                    musicTotal.setText("00:00");
                }
            }
        });
    }

    /**
     * Control the seekBar to show the podcast progress and handle the user action
     */
    private void seekBarMonitor() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    if(musicService.streamingPlayer.mediaPlayer != null) {
                        if(musicService.streamingPlayer.getState() != StreamingPlayer.StreamingPlayerState.ERROR) {
                            musicService.streamingPlayer.mediaPlayer.seekTo(progress);
                        }
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_music) {

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this, ExerciseMonitorActivity.class);
            Bundle b = new Bundle();
            b.putString("username", username);
            intent.putExtras(b);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            Bundle b = new Bundle();
            b.putString("username", username);
            intent.putExtras(b);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(serviceConnection != null) {
            handler.removeCallbacks(runnable);
            musicService.streamingPlayer.stop();
            Intent intent = new Intent(MainActivity.this, MusicService.class);
            stopService(intent);
            unbindService(serviceConnection);
            serviceConnection = null;
            Log.d("Exercise", "********MainActivity onServiceDisconnected");
        }

    }
}
