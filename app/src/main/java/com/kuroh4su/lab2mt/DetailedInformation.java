package com.kuroh4su.lab2mt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedInformation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_information);

        Bundle arguments = getIntent().getExtras();

        String nickname = "";
        int skill = 0;
        int hours = 0;
        if (arguments != null) {
            nickname = arguments.getString("nickname");
            skill = arguments.getInt("skill");
            hours = arguments.getInt("hours");
        }

        TextView tvNickname = findViewById(R.id.showNick);
        tvNickname.setText(String.format(getResources().getString(R.string.nickname), nickname));
        TextView tvSkill = findViewById(R.id.showSkill);
        tvSkill.setText(String.format(getResources().getString(R.string.skill), skill));
        TextView tvHours = findViewById(R.id.showHours);
        tvHours.setText(String.format(getResources().getString(R.string.hours), hours));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actBack) {
            actBack();
        }
        return true;
    }

    public void actBack() {
        finish();
    }
}