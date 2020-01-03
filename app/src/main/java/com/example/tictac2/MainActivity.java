package com.example.tictac2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.app.AlertDialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1turn = true;
    private int roundCount = 0;
    private int player1points;
    private int player2points;
    private TextView textViewplayer1;
    private TextView textViewplayer2;
    ObjectAnimator objectAnimator;
    int player1choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewplayer1 = findViewById(R.id.text_view_p1);
        textViewplayer2 = findViewById(R.id.text_view_p2);

        alertDialogMaker();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void alertDialogMaker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player 1: First (X) or Second (0)").setCancelable(false).setNeutralButton("First (X)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player1choice = 0;
                dialog.cancel();
            }
        }).setPositiveButton("Second (0)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player1choice = 1;
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {

        if(!((Button) v).getText().toString().equals("")){
            return;
        }

        if(roundCount == 7) {
            Button randomButton = eliminate();
            randomButton.setText("");
            objectAnimator = ObjectAnimator.ofFloat(randomButton, "rotation", 0, 180);
            objectAnimator.setDuration(200);
            objectAnimator.start();
            roundCount -= 1;
        }


        if(player1turn){
            ((Button) v).setText("X");
        }else{
            ((Button) v).setText("O");
        }

        roundCount++;

        if(checkForWin()) {
            if (player1turn && player1choice == 0) {
                player1Wins();
                alertDialogMaker();
            }else if (!player1turn && player1choice == 1){
                player1Wins();
                alertDialogMaker();
            }else if (!player1turn && player1choice == 0){
                player2Wins();
                alertDialogMaker();
            }else{
                player2Wins();
                alertDialogMaker();
            }
        } else {
            player1turn = !player1turn;
        }

    }

    private Boolean checkForWin(){
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for(int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) &&
                    field[i][0].equals(field[i][2]) &&
                    !field[i][0].equals("")){
                return true;
            }
        }

        for(int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) &&
                    field[0][i].equals(field[2][i]) &&
                    !field[0][i].equals("")){
                return true;
            }
        }


        if (field[0][0].equals(field[1][1]) &&
                field[0][0].equals(field[2][2]) &&
                !field[0][0].equals("")){
            return true;
        }

        if (field[0][2].equals(field[1][1]) &&
                field[0][2].equals(field[2][0]) &&
                !field[0][2].equals("")){
            return true;
        }

        return false;
    }

    private void player1Wins(){
        player1points++;
        Toast.makeText(this, "Player 1 wins", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins(){
        player2points++;
        Toast.makeText(this, "Player 2 wins", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void updatePointsText(){
        textViewplayer1.setText("Player 1: " + player1points);
        textViewplayer2.setText("Player 2: " + player2points);
    }

    private void resetBoard(){
        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1turn = true;
        alertDialogMaker();
    }

    private Button eliminate(){
        int k = 0;
        while (k == 0){
            int randomJ = getRandomNumberInRange(0, 2);
            int randomI = getRandomNumberInRange(0, 2);
            if(!buttons[randomI][randomJ].getText().toString().equals("")){
                k = 1;
                return buttons[randomI][randomJ];
            }
        }
        return buttons[0][0];
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void resetGame(){
        resetBoard();
        player1points = 0;
        player2points = 0;
        updatePointsText();
        resetBoard();
    }
}

