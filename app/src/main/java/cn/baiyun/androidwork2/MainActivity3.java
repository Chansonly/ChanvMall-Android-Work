package cn.baiyun.androidwork2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.baiyun.androidwork2.adapter.GridAdapter;
import cn.baiyun.androidwork2.adapter.ImageAdapter;
import cn.baiyun.androidwork2.entity.PageRequestVo;
import cn.baiyun.androidwork2.face.OnDataReceivedListener;
import cn.baiyun.androidwork2.face.OnKeysReceivedListener;
import cn.baiyun.androidwork2.face.OnOrderReceivedListener;
import cn.baiyun.androidwork2.face.OnPageReceviedListener;
import cn.baiyun.androidwork2.face.OnScoreReceivedListener;
import cn.baiyun.androidwork2.http.OrderHttpTask;
import cn.baiyun.androidwork2.http.PageHttpTask;
import cn.baiyun.androidwork2.http.ScoreTask;
import cn.baiyun.androidwork2.http.UserUpdateHttpTask;

public class MainActivity3 extends AppCompatActivity implements
        OnDataReceivedListener,
        OnPageReceviedListener,
        OnKeysReceivedListener,
        OnScoreReceivedListener,
        OnOrderReceivedListener {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    private List<ImageView> imgList = new ArrayList<>();

    private TextView f1tv1;
    private TextView f1tv2;
    private TextView f1tv3;
    private TextView f1tv4;

    private List<TextView> tvList = new ArrayList<>();

    private String[] source;

    private Integer gender;

    private Integer uid;

    private SeekBar seekBar;

    private GridView gridView;

    private List<String> data = new ArrayList<>();

    private int[] array = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Log.i("life", "MainActivity3的onCreate()方法被执行了");

        //获取另一个activity传输的数据
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        source = intent.getExtras().getStringArray("data");
//        Log.i("my", "数组长度:" + source.length + "");
        //数据
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(user);
            uid = jsonObject.getInt("id");
            Log.i("my", "MainActivity3接受的用户信息:id=" + jsonObject.getInt("id"));
            Log.i("my", "MainActivity3接受的用户信息:name=" + jsonObject.getString("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //发送请求获取订单s
        String orderUrl = "http://169.254.33.144:9000/sku/list/" + uid;
        new OrderHttpTask(MainActivity3.this).execute(orderUrl);

        //自动发送请求获取默认页的sku信息
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("currentPage", 1);
            jsonObject1.put("pageSize", 4);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        new PageHttpTask(MainActivity3.this).execute("http://169.254.33.144:9000/user/page", jsonObject1.toString());

        //开启Fragment
        TabHost tabHost = findViewById(R.id.tabHost);
        //1号
        tabHost.setup();
        TabHost.TabSpec tag1 = tabHost.newTabSpec("tag1");
        tag1.setIndicator("商品展示");
        tag1.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View view = LayoutInflater.from(MainActivity3.this).inflate(R.layout.first_fragment, null);

                //图片
                imageView1 = view.findViewById(R.id.imageView1);
                imageView2 = view.findViewById(R.id.imageView2);
                imageView3 = view.findViewById(R.id.imageView3);
                imageView4 = view.findViewById(R.id.imageView4);

                MyClickListener myClickListener = new MyClickListener();
                imageView1.setOnClickListener(myClickListener);
                imageView2.setOnClickListener(myClickListener);
                imageView3.setOnClickListener(myClickListener);
                imageView4.setOnClickListener(myClickListener);


                imgList.add(imageView1);
                imgList.add(imageView2);
                imgList.add(imageView3);
                imgList.add(imageView4);

                //文本
                f1tv1 = view.findViewById(R.id.f1tv1);
                f1tv2 = view.findViewById(R.id.f1tv2);
                f1tv3 = view.findViewById(R.id.f1tv3);
                f1tv4 = view.findViewById(R.id.f1tv4);
                tvList.add(f1tv1);
                tvList.add(f1tv2);
                tvList.add(f1tv3);
                tvList.add(f1tv4);

                //自动填充栏
                AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.f1at1);
//                String[] course = {"kunkun", "kunge", "kunaifen"};
                Log.i("my", source.toString());
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity3.this, android.R.layout.simple_dropdown_item_1line, source);
                autoCompleteTextView.setAdapter(arrayAdapter);

                //查询按钮
                Button bt0 = view.findViewById(R.id.f1bt0);
                bt0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //发送分页查询+条件查询请求
                        String url = "http://169.254.33.144:9000/user/page";
                        String condition = autoCompleteTextView.getText().toString();
                        PageRequestVo pageRequestVo = new PageRequestVo(1, 4, condition);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("currentPage", pageRequestVo.getCurrentPage());
                            jsonObject.put("pageSize", pageRequestVo.getPageSize());
                            if (!pageRequestVo.getCondition().equals("") && pageRequestVo.getCondition() != null) {
                                jsonObject.put("condition", pageRequestVo.getCondition());
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String jsonString = jsonObject.toString();
                        Log.i("my", "分页查询的请求json:" + jsonString);
                        new PageHttpTask(MainActivity3.this).execute(url, jsonString);
                    }
                });

                //分页按钮
                Button bt1 = view.findViewById(R.id.f1bt1);
                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //发送分页查询+条件查询请求
                        String url = "http://169.254.33.144:9000/user/page";
                        String condition = autoCompleteTextView.getText().toString();
                        PageRequestVo pageRequestVo = new PageRequestVo(1, 4, condition);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("currentPage", pageRequestVo.getCurrentPage());
                            jsonObject.put("pageSize", pageRequestVo.getPageSize());
                            if (!pageRequestVo.getCondition().equals("") && pageRequestVo.getCondition() != null) {
                                jsonObject.put("condition", pageRequestVo.getCondition());
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String jsonString = jsonObject.toString();
                        Log.i("my", "分页查询的请求json:" + jsonString);
                        new PageHttpTask(MainActivity3.this).execute(url, jsonString);
                    }
                });

                //第二页
                Button bt2 = view.findViewById(R.id.f1bt2);
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //发送分页查询+条件查询请求
                        String url = "http://169.254.33.144:9000/user/page";
                        String condition = autoCompleteTextView.getText().toString();
                        PageRequestVo pageRequestVo = new PageRequestVo(2, 4, condition);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("currentPage", pageRequestVo.getCurrentPage());
                            jsonObject.put("pageSize", pageRequestVo.getPageSize());
                            if (!pageRequestVo.getCondition().equals("") && pageRequestVo.getCondition() != null) {
                                jsonObject.put("condition", pageRequestVo.getCondition());
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String jsonString = jsonObject.toString();
                        Log.i("my", "分页查询的请求json:" + jsonString);
                        new PageHttpTask(MainActivity3.this).execute(url, jsonString);
                    }
                });

                //第三页
                Button bt3 = view.findViewById(R.id.f1bt3);
                bt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //发送分页查询+条件查询请求
                        String url = "http://169.254.33.144:9000/user/page";
                        String condition = autoCompleteTextView.getText().toString();
                        String encodedText;
                        byte[] utf8Bytes = new byte[0];
                        try {
                            utf8Bytes = condition.getBytes("UTF-8");
                            encodedText = new String(utf8Bytes, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        PageRequestVo pageRequestVo = new PageRequestVo(3, 4, encodedText);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("currentPage", pageRequestVo.getCurrentPage());
                            jsonObject.put("pageSize", pageRequestVo.getPageSize());
                            if (!pageRequestVo.getCondition().equals("") && pageRequestVo.getCondition() != null) {
                                jsonObject.put("condition", pageRequestVo.getCondition());
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String jsonString = jsonObject.toString();
                        Log.i("my", "分页查询的请求json:" + jsonString);
                        new PageHttpTask(MainActivity3.this).execute(url, jsonString);
                    }
                });
                return view;
            }
        });
        tabHost.addTab(tag1);

        //2号
        tabHost.setup();
        TabHost.TabSpec tag2 = tabHost.newTabSpec("tag2");
        tag2.setIndicator("个人信息");
        tag2.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View view = LayoutInflater.from(MainActivity3.this).inflate(R.layout.second_fragment, null);

                //SeekBar
                seekBar = view.findViewById(R.id.sbar1);
                String url = "http://169.254.33.144:9000/user/score/" + uid;
                Log.i("my", "分数url:" + url);
                new ScoreTask(MainActivity3.this).execute(url);

                //显示数据
                EditText et1 = view.findViewById(R.id.f2et1);
                EditText et2 = view.findViewById(R.id.f2et2);
                RadioGroup radioGroup = view.findViewById(R.id.f2rg);
                EditText et4 = view.findViewById(R.id.f2et4);
                TextView et5 = view.findViewById(R.id.f2et5);

                //单选
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.f2rb1) {
                            System.out.println("男");
                            gender = 1;
                        } else if (i == R.id.f2rb2) {
                            System.out.println("女");
                            gender = 2;
                        }
                    }
                });

                try {
                    et1.setText(jsonObject.getString("name"));
                    et2.setText(jsonObject.getString("password"));
                    if (jsonObject.getInt("gender") == 1) {
                        radioGroup.check(R.id.f2rb1);
                        gender = 1;
                    } else {
                        radioGroup.check(R.id.f2rb2);
                        gender = 2;
                    }
                    et4.setText(jsonObject.getInt("age") + "");
                    String flavor = jsonObject.getString("flavor");
                    String[] split = flavor.split(",");
                    String res = "";
                    for (String s1 : split) {
                        if (s1.equals("1")) {
                            res = res.equals("") ? res + "日常商品" : res + ",日常商品";
                        } else if (s1.equals("2")) {
                            res = res.equals("") ? res + "黑科技" : res + ",黑科技";
                        } else if (s1.equals("3")) {
                            res = res.equals("") ? res + "游戏" : res + ",游戏";
                        }
                    }
                    et5.setText(res);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //修改用户信息事件
                Button bt1 = view.findViewById(R.id.f2bt1);
                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "http://169.254.33.144:9000/user/update";
                        try {
                            jsonObject.put("name", et1.getText().toString());
                            jsonObject.put("password", et2.getText().toString());
                            jsonObject.put("gender", gender);
                            jsonObject.put("age", et4.getText().toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        new UserUpdateHttpTask(MainActivity3.this).execute(url, jsonObject.toString());
                    }
                });

                //滑动图片
                Gallery gallery = view.findViewById(R.id.gall1);
                ArrayList<Integer> ids = new ArrayList<>();
                ids.add(R.drawable.kuntou);
                ids.add(R.drawable.giegie_bixin);
                ImageAdapter imageAdapter = new ImageAdapter(ids, MainActivity3.this);
                gallery.setAdapter(imageAdapter);

                return view;
            }
        });
        tabHost.addTab(tag2);

        //3号
        tabHost.setup();
        TabHost.TabSpec tag3 = tabHost.newTabSpec("tag3");
        tag3.setIndicator("订单信息");
        tag3.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                View view = LayoutInflater.from(MainActivity3.this).inflate(R.layout.third_fragment, null);
                Log.i("order", "发送订单!");
                //订单列表
                Context context = getApplicationContext();
                gridView = view.findViewById(R.id.gridView);
                gridView.setAdapter(new GridAdapter(context, data));
                return view;
            }
        });
        tabHost.addTab(tag3);
    }

    //处理修改用户信息的数据
    @Override
    public void onDataReceived(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int state = jsonObject.getInt("state");
            if (state == 200) {
                Toast.makeText(MainActivity3.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else if (state == 300) {
                Toast.makeText(MainActivity3.this, "修改失败,用户名已经存在", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity3.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //处理分页查询数据
    @Override
    public void onPageReceived(String result) {
        Log.i("my", "接收分页返回数据函数:" + result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject.getInt("state") == 200) {

                //清除
                for (ImageView imageView : imgList) {
                    imageView.setImageDrawable(null);
                    Log.i("my", "我是图片");
                }
                for (TextView textView : tvList) {
                    textView.setText(null);
                    Log.i("my", "我是名称");
                }

                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    //获取到sku
                    int skuId = item.getInt("id");
                    String name = item.getString("name");
                    String image = item.getString("image");
                    TextView textView = tvList.get(i);
                    textView.setText(name);
                    ImageView imageView = imgList.get(i);

                    //设置商品id
                    array[i] = skuId;

                    int resourceId = Integer.parseInt(image);
                    Log.i("my", i + "的id是:" + String.valueOf(resourceId));
                    Log.i("my","book:"+R.drawable.benzi);
                    Log.i("my","aj:"+R.drawable.aj);
                    Log.i("my","mbh:"+R.drawable.mbh);
                    Log.i("my","ml:"+R.drawable.ml);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(MainActivity3.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //处理关键字
    @Override
    public void onKeysReveived(String result) {
        Log.i("my", "关键字信息" + result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject.getInt("state") == 200) {
                JSONArray data = jsonObject.getJSONArray("data");
                source = new String[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    String key = data.getString(i);
                    source[i] = key;
                    Log.i("my", "关键字:" + key);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //设置导航栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //设置导航栏
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                finish();
                return true;
            case R.id.refresh:
                recreate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        Log.i("life", "MainActivity3的onStart()方法被执行了");
        super.onStart();
    }

    @Override
    protected void onResume() {
        //焦点
        Log.i("life", "MainActivity3的onResume()方法被执行了");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("life", "MainActivity3执行了onPause()方法");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("life", "MainActivity3执行了onStop()方法");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("life", "MainActivity3执行了onDestroy()方法");
        super.onDestroy();
    }

//    public static Bitmap getHttpBitmap(String url) {
//        URL myFileURL;
//        Bitmap bitmap = null;
//        try {
//            myFileURL = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
//            conn.setConnectTimeout(6000);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.connect(); // 建立连接
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                InputStream is = conn.getInputStream();
//                bitmap = BitmapFactory.decodeStream(is);
//                is.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//
//        private ImageView imageView;
//
//        public LoadImageTask(ImageView imageView) {
//            this.imageView = imageView;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            String url = strings[0];
//            Bitmap bitmap = null;
//            try {
//                URL myFileURL = new URL(url);
//                HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
//                conn.setConnectTimeout(6000);
//                conn.setDoInput(true);
//                conn.setUseCaches(false);
//                conn.connect();
//                int responseCode = conn.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    InputStream is = conn.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//                    is.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (bitmap != null) {
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }

    //查询分数
    @Override
    public void onScoreReceived(String res) {
        Log.i("my", "分数接收的回调:" + res);
        JSONObject jsonObject = null;
        int score;
        try {
            jsonObject = new JSONObject(res);
            score = jsonObject.getInt("score");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        seekBar.setMax(100);
        seekBar.setProgress(score);
    }

    //查询用户订单
    @Override
    public void onOrderReveived(String result) {
        JSONObject jsonObject;
        if (result == null || result.equals("")) {
            return;
        }

        try {
            jsonObject = new JSONObject(result);
            int state = jsonObject.getInt("state");
            if (state == 200) {
                JSONArray data1 = jsonObject.getJSONArray("data");
                for (int i = 0; i < data1.length(); i++) {
                    JSONObject item = data1.getJSONObject(i);
                    Log.i("my", item.toString());
                    data.add(item.toString());
                    Log.i("order", "订单执行");
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    //图片点击事件
    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            int skuId;
            Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
            switch (id) {
                case R.id.imageView1:
                    skuId = array[0];
                    Log.i("my","商品图片跳SkuId:"+skuId);
                    intent.putExtra("skuId",skuId);
                    intent.putExtra("uId",uid);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.imageView2:
                    skuId = array[1];
                    Log.i("my","商品图片跳SkuId:"+skuId);
                    intent.putExtra("skuId",skuId);
                    intent.putExtra("uId",uid);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.imageView3:
                    skuId = array[2];
                    Log.i("my","商品图片跳SkuId:"+skuId);
                    intent.putExtra("skuId",skuId);
                    intent.putExtra("uId",uid);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.imageView4:
                    skuId = array[3];
                    Log.i("my","商品图片跳SkuId:"+skuId);
                    intent.putExtra("skuId",skuId);
                    intent.putExtra("uId",uid);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }

}