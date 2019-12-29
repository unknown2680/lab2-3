package com.kuroh4su.lab2mt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;

public class InformationList extends AppCompatActivity {
    private ArrayList<Player> mPlayersList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdaptor mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_list);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new GetTask().execute();
        if (initializeData()) {
            initializeAdaptor();

            mAdapter.setOnItemClickListener(new RecyclerViewAdaptor.OnItemClickListener() {
                // Go to details.
                @Override
                public void onItemClick(int position) {
                    goToDetailedInf(position);
                }
            });
        }
    }

    static class Player {

        @SerializedName("nickname")
        String playerNickname;

        @SerializedName("skill")
        int playerSkill;

        @SerializedName("hours")
        int playerHours;

        Player(String playerNickname, int playerSkill, int playerHours) {
            this.playerNickname = playerNickname;
            this.playerSkill = playerSkill;
            this.playerHours = playerHours;
        }
    }

    private boolean initializeData() {
        mPlayersList = new ArrayList<>();
        DataBaseHelper dbHelper = new DataBaseHelper();
        // Если есть доступ к бд и бд не пуста, то идет берется информация с бд
        // для последующего вывода. Если нет доступа к бд или бд пуста, то выводится ошибка.
        if (dbHelper.createDB(InformationList.this)) {
            mPlayersList = dbHelper.getAllData(mPlayersList, InformationList.this);
            return true;
        }
        if (!mPlayersList.isEmpty()) return true;
        else {
            Toast.makeText(getApplicationContext(),
                    "Нет локальных данных!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initializeAdaptor() {
        mAdapter = new RecyclerViewAdaptor(mPlayersList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    private void goToDetailedInf(int position) {
        Intent intent = new Intent(this, DetailedInformation.class);
        intent.putExtra("nickname", mPlayersList.get(position).playerNickname);
        intent.putExtra("skill", mPlayersList.get(position).playerSkill);
        intent.putExtra("hours", mPlayersList.get(position).playerHours);
        startActivity(intent);
    }

    private class GetTask extends AsyncTask<Void, Void, String> {

        // Считывание из файла по ссылке url в строку strJson. После этого метода
        // будет вызван метод onPostExecute, куда будет передана строка.
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection;
            BufferedReader reader;
            String strJson = "";
            try {
                URL url = new URL("https://doc-10-40-docs.googleusercontent.com/docs/securesc/ha0ro937gcuc7l7deffksulhg5h7mbp1/2nd0vvt3opd8g276c1pjpda0c0mikptg/1577635200000/09309465131503975824/*/16ae4bnf4aZ_brw9JxPEXNNz-vHEz-vy2?e=download");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                strJson = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                strJson += e.getMessage();
            }
            return strJson;
        }

        // Парсинг строки и запись в локальную бд.
        // Если была передана неверная строка, то строка с вызовом метода builder.create()
        // вызовет ошибку и программа продолжит выполнение в методе catch пропуская
        // перезапись бд.
        @Override
        protected void onPostExecute(String answer) {
            try {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                // Парсинг.
                Player[] p1 = gson.fromJson(answer, Player[].class);
                DataBaseHelper dbHelper = new DataBaseHelper();
                // Создание/открытие бд.
                dbHelper.createDB(InformationList.this);
                // Перезапись всех данных.
                dbHelper.deleteData(InformationList.this);
                for (int i = 0; i < p1.length; i++){
                    dbHelper.addElement(p1[i].playerNickname, p1[i].playerSkill, p1[i].playerHours,
                            InformationList.this);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Нет доступа к интернет-ресурсу!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}