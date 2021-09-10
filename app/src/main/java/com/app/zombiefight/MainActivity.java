package com.app.zombiefight;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    public class EntityHandler extends Handler
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
            Size _old = data.getSize("oldCoords");
            Size _new = data.getSize("newCoords");
            GridLayout field = findViewById(R.id.idField);
            ImageView img = field.findViewWithTag("Img"+ _old.getWidth() + "x" + _old.getHeight());
            if(img != null)
            {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                img.setImageBitmap(bmp);
            }

            if(zombie.getStatus() != GameField.KILLED) {
                img = field.findViewWithTag("Img" + _new.getWidth() + "x" + _new.getHeight());
                if (img != null) {
                    int id = R.drawable.zombie;
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
                    img.setImageBitmap(bmp);
                }
            }

        }

    }
    private GridLayout field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setIcon(R.drawable.zombie_icon);
        }
        field = findViewById(R.id.idField);
        InitGame(new Size(10,10));

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
                    Entity creature = GameField.getCreature();
                    if(creature == null) return false;
                    if(creature.getStatus() == GameField.KILLED) return false;
                    ImageView img = (ImageView) v;
                    Size tag = (Size) img.getTag();
                    int x = tag.getWidth();
                    int y = tag.getHeight();
                    int row = creature.getRow();
                    int column = creature.getColumn();
                    creature.move(x, y);
                    if(creature.getStatus() == GameField.MOVING)
                    {
                        TextView label = findViewById(R.id.idMessage);
                        label.setText("Row:" + x + " Column:" + y);
                        ImageView oldimg = field.findViewWithTag(new Size(row,column));
                        Bitmap bmpold = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                        oldimg.setImageBitmap(bmpold);
                        Bitmap bmpnew = BitmapFactory.decodeResource(getResources(), R.drawable.man);
                        img.setImageBitmap(bmpnew);
                    }
                    return true;
                });

                field.addView(image, pars);
            }
        }
    }

}