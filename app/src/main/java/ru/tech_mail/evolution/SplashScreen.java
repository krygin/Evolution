package ru.tech_mail.evolution;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import ru.tech_mail.evolution.content_provider.EvolutionDatabaseContract;


public class SplashScreen extends ActionBarActivity {
    private boolean timePassed = false;
    private boolean dataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final String URL_ROOT = getString(R.string.url_root);
        final String PATH_TO_JSON_FILE = getString(R.string.path_to_json_file);
        new WaitSplashScreenTask().execute();
        new LoadJsonFileTask(URL_ROOT, PATH_TO_JSON_FILE).execute();
    }


    @Override
    public void onBackPressed() {

    }

    private class WaitSplashScreenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dataLoaded) {
                MainActivity.start(SplashScreen.this);
                finish();
            } else {
                timePassed = true;
            }
        }
    }


    private class LoadJsonFileTask extends AsyncTask<Void, Void, Void> {
        private final String URL_ROOT;
        private final String PATH_TO_JSON_FILE;

        private LoadJsonFileTask(String URL_ROOT, String PATH_TO_JSON_FILE) {
            this.URL_ROOT = URL_ROOT;
            this.PATH_TO_JSON_FILE = PATH_TO_JSON_FILE;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject response = getTechnologies(URL_ROOT + PATH_TO_JSON_FILE);
                JSONObject technologiesJsonObject = response.getJSONObject("technology");
                Iterator<String> technologyIterator = technologiesJsonObject.keys();
                while (technologyIterator.hasNext()) {
                    JSONObject technologyJsonObject = technologiesJsonObject.getJSONObject(technologyIterator.next());
                    long id = technologyJsonObject.has("id")?technologyJsonObject.getLong("id"):-1;
                    String picture = technologyJsonObject.has("picture")?technologyJsonObject.getString("picture"):null;
                    String title = technologyJsonObject.has("title")?technologyJsonObject.getString("title"):null;
                    String info = technologyJsonObject.has("info")?technologyJsonObject.getString("info"):null;
                    ContentValues values = getTechnologyContentValues(id, URL_ROOT + picture, title, info);
                    getContentResolver().insert(EvolutionDatabaseContract.Technologies.CONTENT_URI, values);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (timePassed) {
                MainActivity.start(SplashScreen.this);
                finish();
            } else {
                dataLoaded = true;
            }
        }

        private JSONObject getTechnologies(String urlString) throws JSONException, IOException {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String responseString = "";
            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine())
                responseString += scanner.nextLine();
            httpURLConnection.disconnect();
            return new JSONObject(responseString);
        }

        private ContentValues getTechnologyContentValues(long id, String picture, String title, String info) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(EvolutionDatabaseContract.Technologies.COLUMN_ID, id);
            contentValues.put(EvolutionDatabaseContract.Technologies.COLUMN_PICTURE, picture);
            contentValues.put(EvolutionDatabaseContract.Technologies.COLUMN_TITLE, title);
            contentValues.put(EvolutionDatabaseContract.Technologies.COLUMN_INFO, info);
            return contentValues;
        }
    }
}
