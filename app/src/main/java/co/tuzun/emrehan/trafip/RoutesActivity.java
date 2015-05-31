package co.tuzun.emrehan.trafip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RoutesActivity extends TrafipActivity {
    String url;
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        webView = new WebView(this);
        setContentView(webView);

        new LoadRoutesTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadRoutesTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                url = "http://web.trafi.com/#!route-search/";
                url += currentLatLng.latitude;
                url += "%3B";
                url += currentLatLng.longitude;
                url += "/";
                url += destinationLatLng.latitude;
                url += "%3B";
                url += destinationLatLng.longitude;
                url += "/";
//        url += new SimpleDateFormat("yyyy-MM-dd/HH:mm").format(new Date());
                url += new SimpleDateFormat("yyyy-MM-dd/").format(new Date());
                url += "12:00";
                Log.e("url: ", url);

                Document doc = Jsoup.connect(url)
                        .data("query", "Java")
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .timeout(3000)
                        .post();

                Elements results =  doc.select("div#result-list");

                if (results == null) {
                    // "Route not found :("
                } else {
                    String html = results.toString();
                    String mime = "text/html";
                    String encoding = "utf-8";
                    webView.loadData(html, mime, encoding);
                }
            } catch (Exception e) {
                Log.d("myapp", Log.getStackTraceString(e));
                Log.e("wrong url: ", url);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }
}
