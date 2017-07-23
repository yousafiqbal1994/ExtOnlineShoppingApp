package com.project.eos;

import android.content.Context;
import android.content.Intent;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by YouCaf Iqbal on 3/7/2017.
 */

public class AllItemsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    ProgressBar progressBar;
    public ArrayList<AllItemsDetails> data= new ArrayList<AllItemsDetails>();
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allitemactivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        id = getIntent().getStringExtra("id");
        //new JsonTask(getApplicationContext(), null).execute(getResources().getString(R.string.searchSpecificProducts)+id);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private ArrayList<AllItemsDetails> dataSet;

        class MyViewHolder extends RecyclerView.ViewHolder {
            public View view;
            LinearLayout linearLayout;
            TextView titleText;
            TextView shopName,Price,Discount;
            Button link,local;
            ProgressBar progressbar;
            ImageView imageViewIcon;

            MyViewHolder(final View itemView) {
                super(itemView);
                this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
                this.titleText = (TextView) itemView.findViewById(R.id.textViewName);
                //this.Discount = (TextView) itemView.findViewById(R.id.discount);
                this.shopName = (TextView) itemView.findViewById(R.id.shopName);
                this.link = (Button) itemView.findViewById(R.id.link);
                this.local = (Button) itemView.findViewById(R.id.local);
                this.Price = (TextView) itemView.findViewById(R.id.price);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
                this.progressbar = (ProgressBar) itemView.findViewById(R.id.progressssss);
            }
        }

        CustomAdapter(ArrayList<AllItemsDetails> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardlayout_allitemactivity, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            LinearLayout linearLayout = holder.linearLayout;

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AllItemsActivity.this,"Nothing", Toast.LENGTH_SHORT).show();
                }
            });
            TextView textViewName = holder.titleText;
            ImageView imageView = holder.imageViewIcon;

            TextView shopName = holder.shopName;
            Button online = holder.link;
            Button local = holder.local;
            TextView price = holder.Price;
            //TextView discountttt = holder.Discount;

            final ProgressBar progressBar = holder.progressbar;
            textViewName.setText(dataSet.get(listPosition).getTitle());
            Glide.with(AllItemsActivity.this).load(dataSet.get(listPosition).getImageURL())
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
                    .override(100, 100)
                    .placeholder(R.drawable.noimage).transform(new CenterCrop(AllItemsActivity.this), new CustomCenterCrop(AllItemsActivity.this)).into(imageView);
            String currentPrice = dataSet.get(listPosition).getNewPrice();
            String priiice = null;
            String parts [] = new String[2];
            shopName.setText("Available at "+dataSet.get(listPosition).getShopName()+"");
            price.setText("Current Price: "+dataSet.get(listPosition).getNewPrice()+" Rs");

            String oldPrice = dataSet.get(listPosition).getOldPrice();
            online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataSet.get(listPosition).getLink()));
                    startActivity(browserIntent);
                }
            });

            local.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(AllItemsActivity.this,shopDetailsActivity.class);
                    intent.putExtra("id",dataSet.get(listPosition).getShopID());
                    startActivity(intent);
                    //Toast.makeText(AllItemsActivity.this,"Local",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }
    private class JsonTask extends AsyncTask<String, Integer, String> {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        Context ctx;
        View v;
        int i=0;
        String j=null;
        JsonTask(Context ctx, String j) {
            this.ctx = ctx;
            this.j=j;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data.clear();
            progressBar.setVisibility(View.VISIBLE);
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
                StringBuilder buffers = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffers.append(line);
                }
                return buffers.toString();

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
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
            progressBar.setVisibility(View.GONE);
            if(s!=null){
                JSONArray productsArray = null;
                try {
                    productsArray = new JSONArray(s);
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject jObject = productsArray.getJSONObject(i);
                        String shop_id = jObject.getString("shop_id");
                        String shop_name = jObject.getString("shop_name");
                        String mobile_id = jObject.getString("mobile_id");
                        String link = jObject.getString("link");
                        String old_price = jObject.getString("old_price");
                        String new_price = jObject.getString("new_price");
                        String local_online = jObject.getString("local_online");
                        String title = jObject.getString("title");
                        String image_url = jObject.getString("image_url");
                        String brand_id = jObject.getString("brand_id");
                        AllItemsDetails productdetail  =new AllItemsDetails(shop_id,shop_name,mobile_id,link,old_price,new_price,local_online,title,image_url,brand_id);
                        data.add(productdetail);
                    }
                    RecyclerView.Adapter adapter = new CustomAdapter(data);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return  true;
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
}
