package com.app.zombiefight;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.Size;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private class Timer extends Thread
    {
        private long beginMillisecs;
        private TimerHandler handler;

        public Timer(long beginMillisecs)
        {
            this.beginMillisecs = beginMillisecs;
            handler = new TimerHandler();
        }
        public void run()
        {
            int id;
            long interval = 500L;
            if(level >=1 && level<4) interval = 500L;
            else if(level >=4 && level <8) interval = 300L;
            else interval = 200L;
            while(true)
            {
                if(stopped) continue;
                if(System.currentTimeMillis() >= beginMillisecs+interval)
                {
                    id = GameField.seedZombie(Entity.LEFT);
                    Zombie zombie = GameField.findZombieById(id);
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSize("coords", new Size(zombie.getRow(), zombie.getColumn()));
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    break;
                }
            }
            if(id != 0) {
                ZombieThread th = new ZombieThread(id);
                th.start();
            }

        }
    }
    private class TimerHandler extends Handler
    {
        public TimerHandler()
        {
            super(Looper.getMainLooper());
        }
        @Override
        public void handleMessage(Message msg)
        {
            Bundle data = msg.getData();
            Size coords = data.getSize("coords");
            ImageView img = field.findViewWithTag(coords);
            if(img != null)
            {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
                img.setImageBitmap(bmp);

            }

        }


    }
    //
    // поток и Handler управляющий движением зомби
    //
    private class ZombieThread extends Thread
    {
        private int entityId;
        private EntityHandler handler;
        public ZombieThread(int entityId)
        {
            this.entityId = entityId;
            handler = new EntityHandler(entityId);
        }

        public void run()
        {
            while(true)
            {
                if(stopped) continue;
                try {
                    Thread.sleep(delayMoving);
                }
                catch(Exception ex)
                {
                    continue;
                }
                Zombie zombie = GameField.findZombieById(entityId);
                if(zombie == null) break;
                if(zombie.getStatus() == Entity.KILLED) break;
                int row = zombie.getRow();
                int column = zombie.getColumn();
                zombie.move();
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putSize(getResources().getString(R.string.oldc),
                        new Size(row, column));
                bundle.putSize(getResources().getString(R.string.newc),
                        new Size(zombie.getRow(), zombie.getColumn()));
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    }
    private class EntityHandler extends Handler
    {
        private int entityId;
        public EntityHandler(int entityId)
        {
            super(Looper.getMainLooper());
            this.entityId = entityId;
        }

        @Override
        public void handleMessage(Message msg)
        {
            Entity zombie = GameField.findZombieById(entityId);
            if(zombie == null) return;
            if(zombie.getStatus() == Entity.KILLED) return;
            Bundle data = msg.getData();
            Size _old = data.getSize(getResources().getString(R.string.oldc));
            Size _new = data.getSize(getResources().getString(R.string.newc));
            ImageView img = field.findViewWithTag(_old);
            if(img != null)
            {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                img.setImageBitmap(bmp);
            }

            if(zombie.getStatus() != Entity.KILLED) {
                img = field.findViewWithTag(_new);
                if (img != null) {
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
                    img.setImageBitmap(bmp);
                }
            }
            Creature creature = GameField.getCreature();
            if(creature == null) return;
            if(creature.getStatus() == Entity.KILLED) {
                int row = creature.getRow();
                int column = creature.getColumn();
                img = field.findViewWithTag(new Size(row ,column ));
                if (img != null) {
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
                    img.setImageBitmap(bmp);
                    GameField.killCreature();
                    Zombie newZombie = GameField.findZombieByCoordinates(row, column);
                    if(newZombie != null)
                    {
                        int id = newZombie.getId();
                        ZombieThread th = new ZombieThread(id);
                        th.start();
                        String sover = getResources().getString(R.string.game_over);
                        TextView level_text = findViewById(R.id.level);
                        level_text.setText(sover);
                        int beaten = GameField.getZombieBeaten();
                        if( beaten > maxBeaten )
                        {
                            maxBeaten = beaten;
                            writeSharedPreferences();
                        }
                    }
                }
            }
        }

    }
    //
    // методы и члены деятельности
    //
    final long INITIAL_DELAY = 2000L;
    final long DELAY_STEP = 200L;
    private android.content.Context context;
    private GridLayout field;
    private long delayMoving;
    private int level = 1;
    private int maxBeaten;
    private boolean start;
    private boolean stopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setIcon(R.drawable.zombie_icon);
        }
        context = this;
        delayMoving = INITIAL_DELAY;
        level = 1;
        field = findViewById(R.id.idField);
        readSharedPreferences();
        start = true;
        stopped = false;

        Locale locale = new Locale("fi");
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        //getBaseContext().createConfigurationContext(configuration);
        getResources().updateConfiguration(configuration,
                getResources()
                .getDisplayMetrics());
        ((TextView)findViewById(R.id.idZCount)).setText(getResources().getString(R.string.beaten)+": 0");
        ((TextView)findViewById(R.id.idZCountMax)).setText(getResources().getString(R.string.max_beaten)+": "+maxBeaten);
        ((TextView)findViewById(R.id.level)).setText(getResources().getString(R.string.level)+": "+level);
        if(ab != null) ab.setTitle(R.string.app_name);

        // выпадающий список выбора языка
        Spinner spinner = findViewById(R.id.lang_choice);
        String [] langs = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

    }
    //инициализация игры
    private void InitGame(Size fieldSize) {
        GameField.setGameField(fieldSize);
        int n = GameField.getRows();
        int m = GameField.getColumns();
        field.setRowCount(n);
        field.setColumnCount(m);
        GameField.seedZombie(Entity.LEFT);
        GameField.seedZombie(Entity.DOWN);
        GameField.seedZombie(Entity.UP);
        GameField.seedPerson();
        ArrayList<Zombie> zombies = GameField.getZombies();
        for(Zombie zombie : zombies)
        {
            ZombieThread zt = new ZombieThread(zombie.getId());
            zt.start();
        }
        // расположить картинки по сетке
        for(int i=0;i<n;i++) {
            for(int j=0;j<m;j++)
            {
                ImageView image = new ImageView(this);
                int id;
                if(GameField.isPerson(i, j)) id = R.drawable.man;
                else if(GameField.isZombie(i, j)) id = R.drawable.zombie;
                else id = R.drawable.empty;
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
                image.setImageBitmap(bmp);
                GridLayout.LayoutParams pars = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
                pars.width = GridLayout.LayoutParams.WRAP_CONTENT;
                pars.height = GridLayout.LayoutParams.WRAP_CONTENT;
                image.setLayoutParams(pars);
                image.setTag(new Size(i,j));

                image.setOnTouchListener((v, event) -> {
                    if(stopped) return true;
                    Creature creature = GameField.getCreature();
                    if(creature == null) return false;
                    if(creature.getStatus() == Entity.KILLED) return false;
                    ImageView img = (ImageView) v;
                    Size tag = (Size) img.getTag();
                    int x = tag.getWidth();
                    int y = tag.getHeight();
                    int row = creature.getRow();
                    int column = creature.getColumn();
                    boolean beaten = creature.move(x, y);
                    if(creature.getStatus() == Entity.MOVING)
                    {
                        ImageView oldimg = field.findViewWithTag(new Size(row,column));
                        Bitmap bmpold = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                        oldimg.setImageBitmap(bmpold);
                        Bitmap bmpnew = BitmapFactory.decodeResource(getResources(), R.drawable.man);
                        img.setImageBitmap(bmpnew);
                    }
                    // уничтожен зомби
                    if(beaten)
                    {
                        TextView label = findViewById(R.id.idZCount);
                        label.setText(getResources().getString(R.string.beaten)+": "+ GameField.getZombieBeaten());
                        Toast tst = Toast.makeText(this, getResources().getString(R.string.destroyed),
                                Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.TOP, 0, 0);
                        tst.show();
                        int zbeaten = GameField.getZombieBeaten();
                        level  = zbeaten / 3 + 1;
                        if(level >= 10) level = 10;
                        delayMoving = level < 10 ? INITIAL_DELAY - (level - 1)*DELAY_STEP : 200L;
                        TextView text_level = findViewById(R.id.level);
                        text_level.setText(getResources().getString(R.string.level)+": "+level);
                        Timer tm = new Timer(System.currentTimeMillis());
                        tm.start();

                    }
                    return true;
                });

                field.addView(image, pars);
            }
        }
        TextView level_text = findViewById(R.id.level);
        level_text.setText(getResources().getString(R.string.level) +": " + level);
        TextView maxbeaten_text = findViewById(R.id.idZCountMax);
        maxbeaten_text.setText(getResources().getString(R.string.max_beaten)+": "+maxBeaten);
    }
    // начинает новую игру
    private void startNewGame()
    {
        GameField.getZombies().clear();
        GameField.clearGameField();
        System.gc();
        GameField.seedZombie(Entity.LEFT);
        GameField.seedZombie(Entity.DOWN);
        GameField.seedZombie(Entity.UP);
        GameField.seedPerson();
        ArrayList<Zombie> zombies = GameField.getZombies();
        delayMoving = INITIAL_DELAY;
        for(Zombie zombie : zombies)
        {
            ZombieThread zt = new ZombieThread(zombie.getId());
            zt.start();
        }
        int n = GameField.getRows();
        int m = GameField.getColumns();
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                int id;
                if(GameField.isPerson(i, j)) id = R.drawable.man;
                else if(GameField.isZombie(i, j)) id = R.drawable.zombie;
                else id = R.drawable.empty;
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
                ImageView image = field.findViewWithTag(new Size(i, j));
                image.setImageBitmap(bmp);

            }

        }
        TextView cnttxt = findViewById(R.id.idZCount);
        cnttxt.setText(getResources().getString(R.string.beaten)+": " + GameField.getZombieBeaten());
        TextView level_text = findViewById(R.id.level);
        readSharedPreferences();
        level = 1;
        delayMoving = INITIAL_DELAY;
        level_text.setText(getResources().getString(R.string.level)+": " + level);
        TextView maxbeaten_text = findViewById(R.id.idZCountMax);
        maxbeaten_text.setText(getResources().getString(R.string.max_beaten)+": "+maxBeaten);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater minf = getMenuInflater();
        minf.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.start_new_game:
                startNewGame();
                return true;
            case R.id.stop_start:
                if(stopped)
                {
                    item.setTitle(getResources().getString(R.string.stop_item));
                    stopped = false;
                }
                else
                {
                    item.setTitle(getResources().getString(R.string.start_item));
                    stopped = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        stopped = false;
        if(start)
        {
            android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
            float density = dm.density;
            int _w = dm.widthPixels;
            int _h = dm.heightPixels;
            GridLayout beaten = findViewById(R.id.beaten);
            beaten.measure(0,0);
            int hg = (int)((float)beaten.getMeasuredHeight()*density);

            TextView _level =  findViewById(R.id.level);
            _level.measure(0,0);
            int h =  (int)((float)_level.getMeasuredHeight()*density);
            int nw = (_w - (int)(2.0f*5.0f*density))/ ((int)(32.0f*density));
            int nh = (_h - hg - h - (int)(4.0f*5.0f*density))/((int)(32.0f*density));
            InitGame(new Size(nw,nh));
            start = false;
        }

    }

    protected void OnStart()
    {
        super.onStart();
        stopped = false;
    }
    protected void onStop()
    {
        super.onStop();
        stopped = true;
    }


    // считывать параметры приложения
    private void readSharedPreferences()
    {
        String name = getResources().getString(R.string.Settings);
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        maxBeaten = sharedPreferences.getInt("MaxBeaten", 0);

    }
    // записать параметры приложения
    private void writeSharedPreferences()
    {
        String name = getResources().getString(R.string.Settings);
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("MaxBeaten", maxBeaten);
        edit.commit();
    }
}