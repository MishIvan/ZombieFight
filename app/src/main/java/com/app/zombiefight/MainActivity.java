package com.app.zombiefight;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.Size;
import android.widget.ImageView;
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
            img = field.findViewWithTag("Img"+ _new.getWidth() + "x" + _new.getHeight());
            if(img != null)
            {
                int id = entityId == 1 ? R.drawable.man : R.drawable.zombie;
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
                img.setImageBitmap(bmp);
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_HOME_AS_UP);
            ab.setIcon(R.drawable.zombie_icon);
        }
        GameField.setGameField("10x10");
        GridLayout field = findViewById(R.id.idField);
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
                image.setTag("Img"+i+"x"+j);
                field.addView(image, pars);
            }
        }

    }

}