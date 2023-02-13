package com.example.myapplication.ui;

import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private final List<String> urlList = new ArrayList<>();
    private String difficulty = "easy", time = "15 SECONDS";
    private int rows;
    private int columns, gameTime;
    private int score=0;
    private int best_score, width = 100;
    private int[][] best_score_all = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    TextView scoreboard, bestScoreboard, countDown;
    Button Back;
    CountDownTimer cd;
    ImageView loader1, loader2, loading;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.game_activity);

        loader1 = findViewById(R.id.idLoaderOne);
        loader2 = findViewById(R.id.idLoaderTwo);
        loading = findViewById(R.id.idLoading);

        countDown = findViewById(R.id.idTVCountDown);
        scoreboard = findViewById(R.id.idTVScore);
        tableLayout = findViewById(R.id.idTLEmojis);
        time = getIntent().getStringExtra("time");
        difficulty = getIntent().getStringExtra("difficulty");
        bestScoreboard = findViewById(R.id.idTVScoreBest);

        if(getIntent().getSerializableExtra("best_score_all") != null) {
            best_score_all = (int[][]) getIntent().getSerializableExtra("best_score_all");
        }


        Toast.makeText(this, "Starting game with difficulty: " + difficulty, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Starting game with time: " + time, Toast.LENGTH_SHORT).show();
        setDifficulty(difficulty);
        columns = setDifficulty(difficulty);
        gameTime = setTime(time);
        best_score = best_score_all[columns-4][(gameTime / 15)-1];
        bestScoreboard.setText(Integer.toString(best_score));

        Back = findViewById(R.id.idBTBack);
        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(score > best_score){
                    best_score = score;
                }
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                best_score_all[(setDifficulty(difficulty) - 4)][(gameTime/15)-1] = best_score;
                intent.putExtra("best_score_all", best_score_all);
                cd.cancel();
                startActivity(intent);
            }
        });



         cd = new CountDownTimer(gameTime*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDown.setText("Time: " + String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                countDown.setText("Done!");
                cd.cancel();
                if(score > best_score){
                    best_score = score;
                }
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                best_score_all[(setDifficulty(difficulty) - 4)][(gameTime/15)-1] = best_score;
                intent.putExtra("best_score_all", best_score_all);
                Log.d("Score", String.valueOf(best_score));
                startActivity(intent);


            }
        };
         cd.start();

         String eventKey = getIntent().getStringExtra("eventKey");



        databaseReference.child("urls").child(eventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urlList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String url = child.getValue(String.class);
                    urlList.add(url);
                }

                int[] widthRows = setHeightWidth(difficulty, urlList.get(0));
                rows = widthRows[1];
                width = widthRows[0];

                createMatrix((int) (columns*1.3), columns, 100);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", "Fuck");
            }
        });


    }




    private int setTime(String time) {
        switch (time) {
            case "30s":
                return 30;
            case "45s":
                return 45;
            case "60s":
                return 60;
            default:
                return 15;
        }
    }

    private int setDifficulty(String difficulty) {
        switch (difficulty) {
            case "Hard":
                return 5;
            case "Insane":
                return 6;
            case "Impossible":
                return 7;
            default:
                return 4;
        }
    }


    private int[] setHeightWidth(String difficulty, String imageUrl){

        int[] widthHeight = new int[2];
        Picasso.get().load(imageUrl).into(loading);

        int width = loading.getMeasuredWidth();
        int height = loading.getMeasuredHeight();


        tableLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int tablewidth = tableLayout.getMeasuredWidth();
        int tableheight = tableLayout.getMeasuredHeight();

        Log.d("tablewidth", String.valueOf(tablewidth));
        Log.d("width", String.valueOf(width));
        int requiredWidth = 100, requiredRows = setDifficulty(difficulty);

        float aspectratio = (float) width/height;

        int setWidth = (int) ((tablewidth/requiredRows) * 0.85);
        int setRows = (int) (tableheight / (setWidth/aspectratio)) - 1;

        widthHeight[0] = setWidth;
        widthHeight[1] = setRows;


        return widthHeight;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createMatrix(int rows, int columns, int width) {

        Picasso picasso = Picasso.get();
        for (String url : urlList) {
            picasso.load(url).fetch();
        }

        Random random = new Random();

        int randomImage1 = random.nextInt(urlList.size());
        int randomImage2;
        do {
            randomImage2 = random.nextInt(urlList.size());
        } while (randomImage2 == randomImage1);
        String url1 = urlList.get(randomImage1);

        int randomRow = random.nextInt(rows);
        int randomCol = random.nextInt(columns);


        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(CENTER);
            for (int j = 0; j < columns; j++) {
                ImageButton image = new ImageButton(this);
                Picasso.get()
                        .load(url1)
                        .resize(width,width)
                        .centerInside()
                        .into(image);


                image.setBackground(null);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(GameActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                        tableLayout.removeAllViews();
                        if(best_score<score){
                            bestScoreboard.setText(Integer.toString(score));
                            best_score_all[columns-4][(gameTime/15)-1] = score;
                            best_score = score;

                        }
                        score = 0;
                        scoreboard.setText(Integer.toString(score));
                        createMatrix(rows, columns, width);
                    }
                });



                if (i == randomRow && j == randomCol) {

                    Picasso.get()
                            .load(urlList.get(randomImage2))
                            .resize(100,100)
                            .centerInside()
                            .into(image);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(GameActivity.this, "You won!", Toast.LENGTH_SHORT).show();
                            tableLayout.removeAllViews();
                            score++;
                            scoreboard.setText(Integer.toString(score));
                            createMatrix(rows, columns, width);
                        }
                    });
                }



                tableRow.addView(image);
            }
            tableLayout.addView(tableRow);
        }
    }
}