package com.project.eos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Infinal on 5/10/2017.
 */

public class SearchingActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public ArrayList<searchedProductDetails> data= new ArrayList<searchedProductDetails>();
    FloatingSearchView mSearchView;
    ProgressBar progressBar;
    String fromServer = "";
    String id="";
    int radius;
    String marketLat,marketLong;
    CharSequence searchLoc;
    private String mLastQuery = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        marketLat ="0.0";
        marketLong ="0.0";
        radius = 0;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSearchView =  (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {}
            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                if(isNetworkAvailable()){
                    if(query.equals("")){
                        Toast.makeText(SearchingActivity.this,"Enter a valid search string",Toast.LENGTH_LONG).show();
                    }else{
                        new sendRequest().execute();
                    }
                }else{
                    Toast.makeText(SearchingActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                }


            }
        });
    }




    public class sendRequest extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            data.clear();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build();
            FormBody.Builder formBuilder = new FormBody.Builder();
            if (!marketLat.equals("0.0") || !marketLong.equals("0.0")) {
                formBuilder.add("lat",  marketLat);
                formBuilder.add("long", marketLong);
                formBuilder.add("market_location", String.valueOf(searchLoc));
                Log.e("valueexxxxxxxxxxxx",marketLat);
                Log.e("valueexxxxxxxxxxxx",marketLong);
                Log.e("valueexxxxxxxxxxxx",String.valueOf(searchLoc));
            }
            if (radius != 0) {
                formBuilder.add("radius", String.valueOf(radius));
                Log.e("valueexxxxxxxxxxxx",String.valueOf(radius));
            }
            RequestBody formBody = formBuilder.build();
            Log.e("finalAPI",getResources().getString(R.string.searchAllProducts)+mLastQuery+"/aMlENVPuzKwmNwrS7b2xya44jAM2wsa816cH5WR0");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.searchAllProducts)+mLastQuery+"/aMlENVPuzKwmNwrS7b2xya44jAM2wsa816cH5WR0")
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                fromServer = response.body().string();
            } catch (IOException e) {
                Log.e("stringtest IO",e.toString());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            JSONArray productsArray = null;
            Log.e("finalAPI","Response \n" +fromServer);
            if(fromServer!=null){
                try {
                    productsArray = new JSONArray(fromServer);
                    Log.e("finalAPI",productsArray.length()+" is the length");

                    for (int i = 0; i < productsArray.length()-1; i++) {
                        String availability = productsArray.getJSONObject(i).getString("available");
                        String minimumPrice = productsArray.getJSONObject(i).getString("price");

                        String distance =productsArray.getJSONObject(i).getString("distance");


                        JSONObject mobile = productsArray.getJSONObject(i).getJSONObject("mobile");

                        String webLink = null;
                        String discount = null;
                        if (mobile.getJSONArray("data").length() > 0) {
                            JSONObject jsonObject = mobile.getJSONArray("data").getJSONObject(0);
                            discount = jsonObject.getString("discount");
                            webLink = jsonObject.getString("link");
                        }

                        String id = productsArray.getJSONObject(i).getJSONObject("mobile").getString("id");
                        String title = productsArray.getJSONObject(i).getJSONObject("mobile").getString("title");
                        String image = productsArray.getJSONObject(i).getJSONObject("mobile").getString("image");
                        String model = productsArray.getJSONObject(i).getJSONObject("mobile").getString("model");
                        //String brandName = productsArray.getJSONObject(i).getJSONObject("mobile").getJSONObject("brand").getString("name");
                        if (!availability.equals("null")) {
                            searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,model,distance,"","");
                            data.add(searchedProductDetail);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("finalAPI",e.toString());
                }

                RecyclerView.Adapter adapterss = new CustomAdapter(data);
                recyclerView.setAdapter(adapterss);
            }

            progressBar.setVisibility(View.GONE);
            resetValues();
        }
    }

    private void resetValues() {
        marketLat ="0.0";
        marketLong ="0.0";
        radius = 0;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private ArrayList<searchedProductDetails> dataSet;

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            public View view;
            LinearLayout linearLayout;
            ImageView distanceimage;
            TextView titleText;
            ImageView imageViewIcon;
            TextView textViewAvailableLocal,textViewAvailableOnline;
            TextView textViewPrice;
            TextView textViewAvailabledistance;
            ProgressBar progressbar;
            ImageView onlineImage,localImage;

            public MyViewHolder(final View itemView) {
                super(itemView);;
                this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
                this.titleText = (TextView) itemView.findViewById(R.id.textViewTitle);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
                this.progressbar = (ProgressBar) itemView.findViewById(R.id.progressssss);
                this.distanceimage = (ImageView) itemView.findViewById(R.id.distanceimage);
                this.textViewAvailabledistance = (TextView) itemView.findViewById(R.id.textViewdistance);
                this.textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
                this.textViewAvailableLocal = (TextView) itemView.findViewById(R.id.textViewAvailableLocal);
                this.textViewAvailableOnline = (TextView) itemView.findViewById(R.id.textViewAvailableOnline);
                this.onlineImage = (ImageView)itemView.findViewById(R.id.onlineImage);
                this.localImage = (ImageView)itemView.findViewById(R.id.localImage);
            }
        }

        public CustomAdapter(ArrayList<searchedProductDetails> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardlayout_searchingactivity, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            LinearLayout linearLayout = holder.linearLayout;

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent  = new Intent(SearchingActivity.this,shopDetailsActivity.class);
                    intent.putExtra("id",dataSet.get(listPosition).getID());
                    intent.putExtra("image",dataSet.get(listPosition).getImage());
                    intent.putExtra("title",dataSet.get(listPosition).getTitle());
                    intent.putExtra("brand",dataSet.get(listPosition).getBrandName());
                    startActivity(intent);
                }
            });



            TextView textViewName = holder.titleText;
            TextView textViewAvailabledistance = holder.textViewAvailabledistance;
            ImageView imageView = holder.imageViewIcon;
            TextView textViewAvailableOnline = holder.textViewAvailableOnline;
            TextView textViewAvailableLocal = holder.textViewAvailableLocal;
            TextView textViewPrice = holder.textViewPrice;
            ImageView localImage = holder.localImage;
            ImageView onlineImage = holder.onlineImage;
            ImageView distanceimage = holder.distanceimage;

            String dis = dataSet.get(listPosition).getDisount();
            textViewPrice.setText(""+dataSet.get(listPosition).getMinPrice()+" Rs");
            textViewName.setText(dataSet.get(listPosition).getTitle());

            if(dataSet.get(listPosition).getDistance().equals("999999999999")){
                distanceimage.setVisibility(View.GONE);
                textViewAvailabledistance.setVisibility(View.GONE);
            }

            if(dataSet.get(listPosition).getAvailability().equals("both")){
                textViewAvailableOnline.setText("Online");
                textViewAvailableLocal.setText("Local");
                localImage.setBackgroundResource(R.drawable.tick);
                onlineImage.setBackgroundResource(R.drawable.tick);
                textViewAvailabledistance.setText(""+dataSet.get(listPosition).getDistance()+" km");
            }else if(dataSet.get(listPosition).getAvailability().equals("local")){
                textViewAvailableLocal.setText("Local");
                textViewAvailableOnline.setText("Online");
                localImage.setBackgroundResource(R.drawable.tick);
                onlineImage.setBackgroundResource(R.drawable.cross);
                textViewAvailabledistance.setText(""+dataSet.get(listPosition).getDistance()+" km");

            }else if (dataSet.get(listPosition).getAvailability().equals("online")){
                textViewAvailableLocal.setText("Local");
                textViewAvailableOnline.setText("Online");
                localImage.setBackgroundResource(R.drawable.cross);
                onlineImage.setBackgroundResource(R.drawable.tick);
                textViewPrice.setText(""+dataSet.get(listPosition).getMinPrice()+" Rs");
            }else{
                textViewAvailableLocal.setText("Local");
                textViewAvailableOnline.setText("Online");
                localImage.setBackgroundResource(R.drawable.cross);
                onlineImage.setBackgroundResource(R.drawable.cross);
            }

            final ProgressBar progressBar = holder.progressbar;
            Glide.with(SearchingActivity.this).load(dataSet.get(listPosition).getImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .override(100, 125)
                    .placeholder(R.drawable.noimage).transform(new CenterCrop(SearchingActivity.this), new CustomCenterCrop(SearchingActivity.this)).into(imageView);


        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(SearchingActivity.this,"Filters",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SearchingActivity.this,SearchFiltersActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                marketLat=data.getStringExtra("searchLat");
                marketLong=data.getStringExtra("seacrhLong");
                searchLoc=data.getStringExtra("searchlocation");
                String parts[]=data.getStringExtra("distance").split("-");
                radius = Integer.parseInt(parts[0]);
                Log.e("valuesss","searchLat = "+ marketLat);
                Log.e("valuesss","seacrhLong = "+ marketLong);
                Log.e("valuesss","distance = "+ radius);
                Log.e("valuesss","searchLoc = "+ searchLoc);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private void setSearchViewStyle() {
        mSearchView.setBackgroundColor(Color.parseColor("#787878"));
        mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
        mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
        mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
        mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
        mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
        mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
    }
}
