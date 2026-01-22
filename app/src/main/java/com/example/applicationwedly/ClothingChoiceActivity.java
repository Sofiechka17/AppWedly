package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ClothingChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_clothing_choice);

        findViewById(R.id.btnMen).setOnClickListener(v ->
                startActivity(new Intent(this, MensClothingActivity.class)));

        findViewById(R.id.btnWomen).setOnClickListener(v ->
                android.widget.Toast.makeText(this,"Женская одежда",android.widget.Toast.LENGTH_SHORT).show());
    }
}
