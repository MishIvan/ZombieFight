package com.app.zombiefight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.Size;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;

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
            int id = 0;
            while(true)
            {
                if(System.currentTimeMillis() >= beginMillisecs+500L)
                {
                    id = GameField.seedZombie(false);
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
                try {
                    Thread.sleep(delayMoving);
                }
                catch(Exception ex)
                {
                    continue;
                }
                Zombie zombie = GameField.findZombieById(entityId);
                if(zombie == null) break;
                if(zombie.getStatus() == GameField.KILLED) break;
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
            if(zombie.getStatus() == GameField.KILLED) return;
            Bundle data = msg.getData();
            Size _old = data.getSize(getResources().getString(R.string.oldc));
            Size _new = data.getSize(getResources().getString(R.string.newc));
            ImageView img = field.findViewWithTag(_old);
            if(img != null)
            {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                img.setImageBitmap(bmp);
            }

            if(zombie.getStatus() != GameField.KILLED) {
                img = field.findViewWithTag(_new);
                if (img != null) {
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
                    img.setImageBitmap(bmp);
                }
            }
            Creature creature = GameField.getCreature();
            if(creature == null) return;
            if(creature.getStatus() == GameField.KILLED) {
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
                        Toast tst = Toast.makeText(context, getResources().getString(R.string.game_over),
                                Toast.LENGTH_LONG);
                        tst.setGravity(Gravity.CENTER, 0, 0);
                        tst.show();
                    }
                }
            }
        }

    }
    private android.content.Context context;
    private GridLayout field;
    private long delayMoving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setIcon(R.drawable.zombie_icon);
        }
        context = this;
        delayMoving = 1000L;
        field = findViewById(R.id.idField);
        android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels*160/dm.densityDpi-2*5*dm.densityDpi/160;
        int width = dm.widthPixels*160/dm.densityDpi-5*dm.densityDpi/160;
        int size = Math.min(width/32, height/32);
        InitGame(new Size(size,size));

    }
    //инициализация игры
    private void InitGame(Size fieldSize) {
        GameField.setGameField(fieldSize);
        int n = GameField.getRows();
        int m = GameField.getColumns();
        field.setRowCount(n);
        field.setColumnCount(m);
        GameField.seedZombie(true);
        GameField.seedZombie(false);
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
                    Creature creature = GameField.getCreature();
                    if(creature == null) return false;
                    if(creature.getStatus() == GameField.KILLED) return false;
                    ImageView img = (ImageView) v;
                    Size tag = (Size) img.getTag();
                    int x = tag.getWidth();
                    int y = tag.getHeight();
                    int row = creature.getRow();
                    int column = creature.getColumn();
                    boolean beaten = creature.move(x, y);
                    if(creature.getStatus() == GameField.MOVING)
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
                        label.setText("Beaten: "+ GameField.getZombieBeaten());
                        Toast tst = Toast.makeText(this, getResources().getString(R.string.baeten),
                                Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER, 0, 0);
                        tst.show();
                        if(delayMoving >100L) delayMoving -= 100L;
                        Timer tm = new Timer(System.currentTimeMillis());
                        tm.start();

                    }
                    return true;
                });

                field.addView(image, pars);
            }
        }
    }
    // начинает новую игру
    private void startNewGame()
    {
        GameField.getZombies().clear();
        GameField.clearGameField();
        System.gc();
        GameField.seedZombie(true);
        GameField.seedZombie(false);
        GameField.seedPerson();
        ArrayList<Zombie> zombies = GameField.getZombies();
        delayMoving = 1000L;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}