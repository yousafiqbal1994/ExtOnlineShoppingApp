package com.project.eos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YouCaf Iqbal on 3/7/2017.
 */

public class shopDetailsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    public RecyclerView recyclerView;
    String fromServer = "";
    public ArrayList<ShopDetail> data= new ArrayList<ShopDetail>();
    String id,image,title,brand;
    ImageView imageViw;
    TextView titleText,brandText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopviewlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        titleText = (TextView) findViewById(R.id.title);
        brandText = (TextView) findViewById(R.id.brand);
        imageViw = (ImageView) findViewById(R.id.imageView);
        id = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        brand = getIntent().getStringExtra("brand");
        setValues();
        new sendRequest().execute();
    }

    private void setValues() {
        titleText.setText(title);
        brandText.setText(brand);
        Glide.with(shopDetailsActivity.this).load(image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .override(100, 100)
                .placeholder(R.drawable.noimage).transform(new CenterCrop(shopDetailsActivity.this), new CustomCenterCrop(shopDetailsActivity.this)).into(imageViw);

    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private ArrayList<ShopDetail> dataSet;

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            public View view;
            TextView shopName;
            TextView shopPrice;
            TextView shopDiscount;
            FloatingActionButton shopInfo;


            public MyViewHolder(final View itemView) {
                super(itemView);
                this.shopName = (TextView) itemView.findViewById(R.id.shopName);
                this.shopPrice = (TextView) itemView.findViewById(R.id.shopPrice);
                this.shopInfo = (FloatingActionButton) itemView.findViewById(R.id.shopInfo);
                this.shopDiscount = (TextView) itemView.findViewById(R.id.shopDiscount);

            }
        }

        public CustomAdapter(ArrayList<ShopDetail> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shopviewsingleitem, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            TextView shopName = holder.shopName;
            TextView shopPrice = holder.shopPrice;
            FloatingActionButton shopInfo= holder.shopInfo;
            TextView shopDiscount= holder.shopDiscount;
            shopName.setText(""+dataSet.get(listPosition).getShop_name()+"");
            shopPrice.setText(""+dataSet.get(listPosition).getPrice()+" Rs");
            shopDiscount.setText("0%");
            String oldpric = dataSet.get(listPosition).getOldprice();
            String currentPrice = dataSet.get(listPosition).getPrice();
            if(oldpric.equals("") || oldpric.equals("0") || currentPrice.equals("0")){
                shopDiscount.setText("0%");
            }else{
                String oldpricee = oldpric.replace(",", "");
                String curprice = currentPrice.replace(",", "");
                double  discount = (Double.parseDouble(oldpricee)-Double.parseDouble(curprice));
                discount = discount/Double.parseDouble(oldpricee);
                discount = discount*100;
                int x = (int) discount;
                String finals = String.valueOf(x);
                shopDiscount.setText("-"+finals+"%");
            }

            shopInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dataSet.get(listPosition).getAvailablility().equals("o")){
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataSet.get(listPosition).getLink()));
                        startActivity(browserIntent);
                    }else if (dataSet.get(listPosition).getAvailablility().equals("l")){
                        showDialogg(dataSet.get(listPosition).getLat(),dataSet.get(listPosition).getLongi(),dataSet.get(listPosition).getShop_name(),dataSet.get(listPosition).getPhone(),dataSet.get(listPosition).getLocation(),dataSet.get(listPosition).getMarket_plaza());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    private void showDialogg(final String lat, final String longi, final String shopname, final  String num, final  String loc, final String plaza) {

        LayoutInflater li = LayoutInflater.from(shopDetailsActivity.this);
        View promptsView;
        promptsView = li.inflate(R.layout.shopinfodialog, null);
        final TextView shopName = (TextView) promptsView
                .findViewById(R.id.shopName);

        final TextView number = (TextView) promptsView
                .findViewById(R.id.number);

        final TextView location = (TextView) promptsView
                .findViewById(R.id.location);

        final TextView plazaa = (TextView) promptsView
                .findViewById(R.id.plaza);

        shopName.setText(shopname);
        number.setText(num);
        location.setText(loc);
        plazaa.setText(plaza);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(shopDetailsActivity.this, R.style.myDialog));
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setPositiveButton("Map",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("geo:0,0?q=" + (""+lat+","+longi)));
                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, shopDetailsActivity.this);
                String uri = "http://maps.google.com/maps?saddr=" +place.getLatLng().latitude +","+place.getLatLng().longitude+"&daddr="+34.0150+","+71.5805;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        }
    }

    private void showDialogBox(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(shopDetailsActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Location");
        // Setting Dialog Message
        alertDialog.setMessage("Select your current location");
        // On pressing Settings button
        alertDialog.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(shopDetailsActivity.this), 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public class sendRequest extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //data.clear();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build();
            FormBody.Builder formBuilder = new FormBody.Builder();

            RequestBody formBody = formBuilder.build();
            Log.e("finalAPIlast",getResources().getString(R.string.allShops)+id+"/aMlENVPuzKwmNwrS7b2xya44jAM2wsa816cH5WR0");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.allShops)+id+"/aMlENVPuzKwmNwrS7b2xya44jAM2wsa816cH5WR0")
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                fromServer = response.body().string();
            } catch (IOException e) {
                Log.e("finalAPIlast",e.toString());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("finalAPIlast","Response \n" +fromServer);
            if(fromServer!=null){
                try {
                    JSONArray productsArray = new JSONArray(fromServer);
                    Log.e("finalAPIlast",productsArray.length()+" is the length");
                    Log.e("finalAPIlast", productsArray.getJSONArray(0).length()+" is the length inside");
                    int length = productsArray.getJSONArray(0).length();
                    for(int i = 0 ; i <length; i++){
                        JSONObject shopDetails = productsArray.getJSONArray(0).getJSONObject(i).getJSONObject("shop");
                        String shop_id = shopDetails.getString("id");
                        String shop_name = shopDetails.getString("shop_name");
                        String phone = shopDetails.getString("phone");
                        String location = shopDetails.getString("location");
                        String market_plaza = shopDetails.getString("market_plaza");
                        String city = shopDetails.getString("city");
                        String lat = shopDetails.getString("lat");
                        String longi = shopDetails.getString("long");
                        JSONObject json = productsArray.getJSONArray(0).getJSONObject(i);
                        String local_online = json.getString("local_online");
                        String link = json.getString("link");
                        String new_price = json.getString("new_price");
                        String old_price = json.getString("old_price");
                        ShopDetail shop  =new ShopDetail(shop_id,shop_name,location,link,phone,local_online,old_price,new_price,market_plaza,city,lat,longi);
                        data.add(shop);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("finalAPIlast",e.toString());
                }

                RecyclerView.Adapter adapterss = new CustomAdapter(data);
                recyclerView.setAdapter(adapterss);
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}
