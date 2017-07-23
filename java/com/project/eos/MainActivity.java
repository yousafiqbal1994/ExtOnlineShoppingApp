package com.project.eos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    public RecyclerView appleProducts;
    public RecyclerView samsungProducts;
    public RecyclerView latestProducts;
    public RecyclerView lgProducts;
    public RecyclerView htcProducts;
    public ProgressBar progressBarRecent;
    public LinearLayout myLineraLayout;
    public  ArrayList<searchedProductDetails> appleItems = new ArrayList<>();
    public  ArrayList<searchedProductDetails> samsungItems = new ArrayList<>();
    public  ArrayList<searchedProductDetails> htcItems = new ArrayList<>();
    public  ArrayList<searchedProductDetails> latestItems = new ArrayList<>();
    public  ArrayList<searchedProductDetails> lgItems = new ArrayList<>();
    public Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLineraLayout = (LinearLayout) findViewById(R.id.myLineraLayout);
        progressBarRecent = (ProgressBar) findViewById(R.id.progressBarRecent);
        c = getApplicationContext();

        latestProducts = (RecyclerView) findViewById(R.id.latestProducts);
        lgProducts = (RecyclerView) findViewById(R.id.lgProducts);
        appleProducts = (RecyclerView) findViewById(R.id.appleProducts);
        samsungProducts = (RecyclerView) findViewById(R.id.samsungProducts);
        htcProducts = (RecyclerView) findViewById(R.id.htcProducts);

        RecyclerView.LayoutManager layoutManagerApple = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerSamsung = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerHTC = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerLatest = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerLG = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        appleProducts.setLayoutManager(layoutManagerApple);
        appleProducts.setItemAnimator(new DefaultItemAnimator());

        samsungProducts.setLayoutManager(layoutManagerSamsung);
        samsungProducts.setItemAnimator(new DefaultItemAnimator());

        htcProducts.setLayoutManager(layoutManagerHTC);
        htcProducts.setItemAnimator(new DefaultItemAnimator());

        latestProducts.setLayoutManager(layoutManagerLatest);
        latestProducts.setItemAnimator(new DefaultItemAnimator());

        lgProducts.setLayoutManager(layoutManagerLG);
        lgProducts.setItemAnimator(new DefaultItemAnimator());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myLineraLayout.setVisibility(View.GONE);
        new JsonTaskHome(getApplicationContext(), null).execute(getResources().getString(R.string.Home));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchingActivity.class);
                startActivity(intent);

            }
        });
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private ArrayList<searchedProductDetails> dataSet;

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            public View view;
            LinearLayout linearLayout;
            ImageView distanceimage;
            TextView titleText;
            ImageView imageViewIcon;
            TextView textViewBrand;
            TextView textViewAvailableLocal,textViewAvailableOnline;
            TextView textViewPrice;
            ImageView priceimage;
            TextView textViewAvailabledistance;
            ProgressBar progressbar;
            ImageView onlineImage,localImage;

            public MyViewHolder(final View itemView) {
                super(itemView);
                this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
                this.titleText = (TextView) itemView.findViewById(R.id.textViewTitle);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
                this.progressbar = (ProgressBar) itemView.findViewById(R.id.progressssss);
                this.textViewBrand = (TextView)itemView.findViewById(R.id.textViewModel);
                this.distanceimage = (ImageView) itemView.findViewById(R.id.distanceimage);
                this.textViewAvailabledistance = (TextView) itemView.findViewById(R.id.textViewdistance);
                this.textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
                this.priceimage = (ImageView) itemView.findViewById(R.id.priceimage);
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
                    .inflate(R.layout.cards_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            LinearLayout linearLayout = holder.linearLayout;

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(MainActivity.this,shopDetailsActivity.class);
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
            TextView textViewBrand = holder.textViewBrand;
            TextView textViewAvailableOnline = holder.textViewAvailableOnline;
            TextView textViewAvailableLocal = holder.textViewAvailableLocal;
            TextView textViewPrice = holder.textViewPrice;
            ImageView localImage = holder.localImage;
            ImageView onlineImage = holder.onlineImage;
            ImageView distanceimage = holder.distanceimage;
            ImageView priceimage = holder.priceimage;

            String dis = dataSet.get(listPosition).getDisount();
            if(dataSet.get(listPosition).getMinPrice().equals("999999999999")){
                priceimage.setImageResource(android.R.color.transparent);
                textViewPrice.setTextColor(Color.WHITE);
            }
            textViewPrice.setText(""+dataSet.get(listPosition).getMinPrice()+" Rs");
            textViewBrand.setText(dataSet.get(listPosition).getBrandName());
            textViewName.setText(dataSet.get(listPosition).getTitle());

            if(dataSet.get(listPosition).getMainPageDistance().equals("999999999999")){
                distanceimage.setImageResource(android.R.color.transparent);
                textViewAvailabledistance.setTextColor(Color.WHITE);
            }

            if(dataSet.get(listPosition).getAvailability().equals("both")){
                textViewAvailableOnline.setText("Online");
                textViewAvailableLocal.setText("Local");
                localImage.setBackgroundResource(R.drawable.tick);
                onlineImage.setBackgroundResource(R.drawable.tick);
                textViewAvailabledistance.setText(""+dataSet.get(listPosition).getMainPageDistance()+" km");
            }else if(dataSet.get(listPosition).getAvailability().equals("local")){
                textViewAvailableLocal.setText("Local");
                textViewAvailableOnline.setText("Online");
                localImage.setBackgroundResource(R.drawable.tick);
                onlineImage.setBackgroundResource(R.drawable.cross);
                textViewAvailabledistance.setText(""+dataSet.get(listPosition).getMainPageDistance()+" km");

            }else if (dataSet.get(listPosition).getAvailability().equals("online")){
                textViewAvailableLocal.setText("Local");
                textViewAvailableOnline.setText("Online");
                localImage.setBackgroundResource(R.drawable.cross);
                onlineImage.setBackgroundResource(R.drawable.tick);
                textViewPrice.setText(""+dataSet.get(listPosition).getOnlinePrice()+" Rs");
            }else{
                textViewAvailableLocal.setText("Local");
                textViewAvailableOnline.setText("Online");
                localImage.setBackgroundResource(R.drawable.cross);
                onlineImage.setBackgroundResource(R.drawable.cross);
            }




            final ProgressBar progressBar = holder.progressbar;
            Glide.with(MainActivity.this).load(dataSet.get(listPosition).getImage())
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
                    .placeholder(R.drawable.noimage).transform(new CenterCrop(MainActivity.this), new CustomCenterCrop(MainActivity.this)).into(imageView);

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }



    private class JsonTaskHome extends AsyncTask<String, Integer, String> {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        Context ctx;
        View v;
        int i=0;
        String j=null;
        JsonTaskHome(Context ctx, String j) {
            this.ctx = ctx;
            this.j=j;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarRecent.setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(String... params) {
            ////////////////
            String context1 = params[0];
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream streams = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(streams));
                StringBuffer buffers = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffers.append(line);
                }
                return buffers.toString();

            }catch (IOException e) {
                e.printStackTrace();
                Log.e("No", " Internet");

            } finally{
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            myLineraLayout.setVisibility(View.VISIBLE);
            if(s!=null){
                JSONArray productsArray = null;
                JSONObject appleObject = null;
                JSONObject samsungObject = null;
                JSONObject latestObject = null;
                JSONObject lgObject = null;
                JSONObject htcObject = null;
                try {
                    productsArray = new JSONArray(s);
                    int apple = productsArray.getJSONObject(0).getJSONArray("apple").length();
                    int samsung = productsArray.getJSONObject(0).getJSONArray("samsung").length();
                    int htc = productsArray.getJSONObject(0).getJSONArray("htc").length();
                    int latest = productsArray.getJSONObject(0).getJSONArray("latest").length();
                    int lg = productsArray.getJSONObject(0).getJSONArray("lg").length();

                    for(int i =0; i<latest;i++) {
                        String type = "latest";
                        latestObject = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getJSONObject("mobile");
                        String distance = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("distance");
                        String webLink = null;
                        String discount = null;
                        if (latestObject.getJSONArray("data").length() > 0) {
                            JSONObject jsonObject = latestObject.getJSONArray("data").getJSONObject(0);
                            discount = jsonObject.getString("discount");
                            webLink = jsonObject.getString("link");
                        }

                        String minimumPrice = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("price");
                        String availability = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("available");
                        String online_price = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("online_price");

                        String id = latestObject.getString("id");
                        String title = latestObject.getString("title");
                        String image = latestObject.getString("image");
                        String brandName = latestObject.getJSONObject("brand").getString("name");
                        Log.e("finalAPI", "Brand = " + latestObject.getJSONObject("brand").getString("name"));
//                        if (!availability.equals("null")) {
//                            searchedProductDetails searchedProductDetail = new searchedProductDetails(id, title, image, webLink, discount, minimumPrice, availability, brandName,online_price,distance);
//                            latestItems.add(searchedProductDetail);
//                        }

                        searchedProductDetails searchedProductDetail = new searchedProductDetails(id, title, image, webLink, discount, minimumPrice, availability, brandName,online_price,distance);
                        latestItems.add(searchedProductDetail);

                    }

                    RecyclerView.Adapter adapterrrrrr = new CustomAdapter(latestItems);
                    latestProducts.setAdapter(adapterrrrrr);

                    for(int i =0; i<lg;i++){
                        String type="lg";
                        lgObject = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getJSONObject("mobile");

                        String distance = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("distance");

                        String webLink = null;
                        String discount = null;
                        if (lgObject.getJSONArray("data").length() > 0) {
                            JSONObject jsonObject = lgObject.getJSONArray("data").getJSONObject(0);
                            discount = jsonObject.getString("discount");
                            webLink = jsonObject.getString("link");
                        }

                        String minimumPrice = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("price");
                        String availability = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("available");
                        String online_price = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("online_price");
                        String id = lgObject.getString("id");
                        String title = lgObject.getString("title");
                        String image = lgObject.getString("image");
                        String brandName = lgObject.getJSONObject("brand").getString("name");
                        Log.e("finalAPI","Brand = "+lgObject.getJSONObject("brand").getString("name"));
//                        if (!availability.equals("null")) {
//                            searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
//                            lgItems.add(searchedProductDetail);
//                        }

                        searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
                        lgItems.add(searchedProductDetail);



                    }

                    RecyclerView.Adapter adaptesr = new CustomAdapter(lgItems);
                    lgProducts.setAdapter(adaptesr);


                    for(int i =0; i<apple;i++){
                        String type="apple";
                        appleObject = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getJSONObject("mobile");

                        String distance = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("distance");

                        String webLink = null;
                        String discount = null;
                        if (appleObject.getJSONArray("data").length() > 0) {
                            JSONObject jsonObject = appleObject.getJSONArray("data").getJSONObject(0);
                            discount = jsonObject.getString("discount");
                            webLink = jsonObject.getString("link");
                        }

                        String minimumPrice = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("price");
                        String availability = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("available");
                        String online_price = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("online_price");
                        String id = appleObject.getString("id");
                        String title = appleObject.getString("title");
                        String image = appleObject.getString("image");
                        String brandName = appleObject.getJSONObject("brand").getString("name");
                        Log.e("finalAPI","Brand = "+appleObject.getJSONObject("brand").getString("name"));

//                        if (!availability.equals("null")) {
//                            searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
//                            appleItems.add(searchedProductDetail);
//                        }

                        searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
                        appleItems.add(searchedProductDetail);


                    }

                    RecyclerView.Adapter adapter = new CustomAdapter(appleItems);
                    appleProducts.setAdapter(adapter);

                    for(int i =0; i<samsung;i++){
                        String type="samsung";
                        samsungObject = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getJSONObject("mobile");
                        String distance = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("distance");
                        String webLink = null;
                        String discount = null;
                        if (samsungObject.getJSONArray("data").length() > 0) {
                            JSONObject jsonObject = samsungObject.getJSONArray("data").getJSONObject(0);
                            discount = jsonObject.getString("discount");
                            webLink = jsonObject.getString("link");
                        }
                        String minimumPrice = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("price");
                        String availability = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("available");
                        String online_price = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("online_price");
                        String id = samsungObject.getString("id");
                        String title = samsungObject.getString("title");
                        String image = samsungObject.getString("image");
                        String brandName = samsungObject.getJSONObject("brand").getString("name");
                        Log.e("finalAPI","Brand = "+samsungObject.getJSONObject("brand").getString("name"));

//                        if (!availability.equals("null")) {
//                            searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
//                            samsungItems.add(searchedProductDetail);
//                        }

                        searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
                        samsungItems.add(searchedProductDetail);


                    }

                    RecyclerView.Adapter adapters = new CustomAdapter(samsungItems);
                    samsungProducts.setAdapter(adapters);

                    for(int i =0; i<htc;i++){
                        String type="htc";
                        htcObject = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getJSONObject("mobile");
                        String distance = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("distance");


                        String webLink = null;
                        String discount = null;
                        if (htcObject.getJSONArray("data").length() > 0) {
                            JSONObject jsonObject = htcObject.getJSONArray("data").getJSONObject(0);
                            discount = jsonObject.getString("discount");
                            webLink = jsonObject.getString("link");
                        }
                        String minimumPrice = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("price");
                        String availability = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("available");
                        String online_price = productsArray.getJSONObject(0).getJSONArray(type).getJSONObject(i).getString("online_price");
                        String id = htcObject.getString("id");
                        String title = htcObject.getString("title");
                        String image = htcObject.getString("image");
                        String brandName = htcObject.getJSONObject("brand").getString("name");
                        Log.e("finalAPI","Brand = "+htcObject.getJSONObject("brand").getString("name"));

//                        if (!availability.equals("null")) {
//                            searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
//                            htcItems.add(searchedProductDetail);
//                        }

                        searchedProductDetails searchedProductDetail  =new searchedProductDetails(id,title,image,webLink,discount,minimumPrice,availability,brandName,online_price,distance);
                        htcItems.add(searchedProductDetail);

                    }
                    RecyclerView.Adapter adapterss = new CustomAdapter(htcItems);
                    htcProducts.setAdapter(adapterss);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            progressBarRecent.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
