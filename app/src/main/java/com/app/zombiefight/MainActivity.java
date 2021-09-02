package com.app.zombiefight;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

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
        // расположить картинки по сетке
        for(int i=0;i<n;i++) {
            for(int j=0;j<m;j++)
            {
                ImageView image = new ImageView(this);
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                image.setImageBitmap(bmp);
                GridLayout.LayoutParams pars = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
                pars.width = GridLayout.LayoutParams.WRAP_CONTENT;
                pars.height = GridLayout.LayoutParams.WRAP_CONTENT;
                image.setLayoutParams(pars);
                image.setTag("Img"+Integer.toString(i)+"x"+Integer.toString(j));
                field.addView(image, pars);
            }
        }

    }

}