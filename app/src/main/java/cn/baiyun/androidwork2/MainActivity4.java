package cn.baiyun.androidwork2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.baiyun.androidwork2.entity.UserEntity;
import cn.baiyun.androidwork2.face.OnDataReceivedListener;
import cn.baiyun.androidwork2.face.OnKeysReceivedListener;
import cn.baiyun.androidwork2.face.OnSkuReceivedListener;
import cn.baiyun.androidwork2.face.OnSkuSaveReceivedListener;
import cn.baiyun.androidwork2.face.OnStarReceivedListener;
import cn.baiyun.androidwork2.http.HttpTask;
import cn.baiyun.androidwork2.http.KeysHttpTask;
import cn.baiyun.androidwork2.http.SkuHttpTask;
import cn.baiyun.androidwork2.http.SkuSaveHttpTask;
import cn.baiyun.androidwork2.http.StarHttpTask;

public class MainActivity4 extends AppCompatActivity implements OnSkuReceivedListener, OnDataReceivedListener,OnKeysReceivedListener, OnSkuSaveReceivedListener, OnStarReceivedListener {

    private TextView tv4;
    private EditText ed1;
    private ImageView iv1;

    private Button bt;
    private Button bt2;

    private String[]source;

    private String req;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = getIntent();
        int skuId = intent.getIntExtra("skuId", 0);
        int uId = intent.getIntExtra("uId", 0);

        Log.i("my", "4号MainActivity的商品Id:" + skuId);
        if (skuId == 0 || uId == 0) {//关闭活动
            finish();
        }

        tv4 = findViewById(R.id.a4tv4);
        ed1 = findViewById(R.id.a4et1);
        iv1 = findViewById(R.id.a4iv1);
        bt = findViewById(R.id.a4bt1);
        bt2 = findViewById(R.id.a4bt2);
        ratingBar = findViewById(R.id.a4ratingbar);


        //发请求获取keys
        String url1 = "http://169.254.33.144:9000/user/keys";
        new KeysHttpTask(MainActivity4.this).execute(url1);
        //获取用户信息
        String loginUrl = "http://169.254.33.144:9000/user/loginById/"+uId;
        new HttpTask(MainActivity4.this).execute(loginUrl);


        //创建订单
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //不为数字
                int num = Integer.parseInt(ed1.getText().toString());
                String url = "http://169.254.33.144:9000/sku/buy/" + skuId + "/" + uId + "/" + num;
                new SkuSaveHttpTask(MainActivity4.this).execute(url);
            }
        });

        //返回
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity3.class);

                Bundle bundle = new Bundle();
                bundle.putStringArray("data",source);
                intent.putExtras(bundle);
                intent.putExtra("user",req);
                Log.i("my","A4的keys:"+source.toString());
                Log.i("my","A4的用户:"+req);
                startActivity(intent);
                finish();
            }
        });

        String url = "http://169.254.33.144:9000/sku/one/" + skuId;
        new SkuHttpTask(MainActivity4.this).execute(url);

        String starUrl="http://169.254.33.144:9000/sku/star";
        new StarHttpTask(MainActivity4.this).execute(starUrl);

    }

    @Override
    public void onSkuReceived(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            if (jsonObject.getInt("state") == 200) {
                JSONObject data = jsonObject.getJSONObject("data");
                String name = data.getString("name");
                String image = data.getString("image");
                tv4.setText(name);
                int resourceId = Integer.parseInt(image);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                iv1.setImageBitmap(bitmap);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSkuSaveReceived(String res) {
        Log.i("my", "下单:" + res);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(res);
            if (jsonObject.getInt("state") == 200) {
                Toast.makeText(MainActivity4.this, "购买成功!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity4.this, MainActivity3.class);

                Bundle bundle = new Bundle();
                bundle.putStringArray("data",source);
                intent.putExtras(bundle);
                intent.putExtra("user",req);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity4.this, "购买失败!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //当前焦点
    @Override
    protected void onResume() {
        super.onResume();
    }
    //星级
    @Override
    public void onStarReceived(String res) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(res);
            int state = jsonObject.getInt("state");
            if (state == 200) {
                double data = jsonObject.getDouble("data");
                Log.i("my", "星级为:" + data);
                ratingBar.setRating((float) data);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onKeysReveived(String result) {
        Log.i("my","关键字信息"+result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject.getInt("state")==200){
                JSONArray data = jsonObject.getJSONArray("data");
                source=new String[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    String key = data.getString(i);
                    source[i]=key;
                    Log.i("my","关键字:"+key);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDataReceived(String result) {
        Log.i("my", "Json:" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            int state = jsonObject.getInt("state");
            Log.i("my", "响应的状态:" + state);
            if (state != 200) {//登录失败
                if (state == 300) {//无用户
                    Toast.makeText(MainActivity4.this, "不存在该用户", Toast.LENGTH_SHORT).show();
                } else if (state == 400) {//密码错误
                    Toast.makeText(MainActivity4.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            } else {//登录成功,跳转activity
                JSONObject userEntity = jsonObject.getJSONObject("userEntity");
                String name = userEntity.getString("name");
                Log.i("my", "响应的名称:" + name);
                //封装为实体类
                UserEntity user = new UserEntity();
                user.setId(userEntity.getInt("id"));
                user.setName(name);
                user.setGender(userEntity.getInt("gender"));
                user.setAge(userEntity.getInt("age"));
                user.setPassword(userEntity.getString("password"));
                user.setFlavor(userEntity.getString("flavor"));
                String jsonstring = user.toString();
                Log.i("my", "user对象:" + jsonstring);

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("id", user.getId());
                jsonobj.put("name", user.getName());
                jsonobj.put("password", user.getPassword());
                jsonobj.put("age", user.getAge());
                jsonobj.put("gender", user.getGender());
                jsonobj.put("flavor", user.getFlavor());
                req = jsonobj.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}