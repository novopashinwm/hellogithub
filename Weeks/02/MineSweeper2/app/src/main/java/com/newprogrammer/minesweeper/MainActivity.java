package com.newprogrammer.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.newprogrammer.minesweeper.sweeper.Game;

public class MainActivity extends AppCompatActivity {

    private TextView txtMineCount;
    private TextView txtTimer;
    private TableLayout tableMineField;

    private Game game;
    private final int COLS = 16;
    private final int ROWS = 16;
    private final int BOMBS = 40;
    private final int IMAGE_SIZE = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game (COLS,ROWS,BOMBS);
        game.start();
        initControls();
    }

    private void initControls() {
        txtMineCount = findViewById(R.id.MineCount);
        txtTimer = findViewById(R.id.Timer);
        tableMineField = findViewById(R.id.MineField);
    }
}
