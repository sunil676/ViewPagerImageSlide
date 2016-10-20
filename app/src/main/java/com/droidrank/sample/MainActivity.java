package com.droidrank.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button previous, next;
    private ProgressBar mProgressBar;
    private String URL = "http://52e4a06a.ngrok.io/fetch.php?offset=2";
    private List<ImageModel> list;
    private ViewPager mViewPager;
    private int clickPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        previous = (Button) findViewById(R.id.previous);
        //onclick of previous button should navigate the user to previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("", "click position: "+clickPosition);
                if (clickPosition > 1) {
                    mViewPager.setCurrentItem(getItem(-1), true);
                    clickPosition--;
                }
            }
        });
        next = (Button) findViewById(R.id.next);
        //onclick of next button should navigate the user to next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("", "click position: "+clickPosition);
                if (clickPosition < list.size()) {;
                    mViewPager.setCurrentItem(getItem(+1), true);
                    clickPosition++;

                }
            }
        });
        previous.setEnabled(false);
        next.setEnabled(false);
        initAPICall();
    }

    private void initAPICall() {
        if (Utility.isNetworkAvailable(this)) {
            Utility.execute(new ParseTask());
        } else {
            Utility.showToastMessage(this, "Internet Error.");
        }
    }

    // Title AsyncTask
    private class ParseTask extends AsyncTask<Void, Void, String> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                // Connect to the web site
                response = Utility.getResponse(URL);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        for (int index = 0; index < jsonArray.length(); index++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(index);
                            String url = jsonObject1.getString("imageUrl");
                            String decs = jsonObject1.getString("imageDescription");
                            ImageModel imageModel = new ImageModel();
                            imageModel.setImageUrl(url);
                            imageModel.setImageDescription(decs);
                            list.add(imageModel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            previous.setEnabled(true);
            next.setEnabled(true);
            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(MainActivity.this, list);
            mViewPager.setAdapter(customPagerAdapter);

        }

    }

    private int getItem(int i) {
        Log.v("", "Curent postion: "+mViewPager.getCurrentItem());
        return mViewPager.getCurrentItem() + i;
    }

}
