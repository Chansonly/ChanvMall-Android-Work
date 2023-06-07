package cn.baiyun.androidwork2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import cn.baiyun.androidwork2.entity.UserEntity;
import cn.baiyun.androidwork2.face.OnDataReceivedListener;
import cn.baiyun.androidwork2.face.OnKeysReceivedListener;
import cn.baiyun.androidwork2.face.OnOrderReceivedListener;
import cn.baiyun.androidwork2.face.OnScoreReceivedListener;
import cn.baiyun.androidwork2.http.HttpTask;
import cn.baiyun.androidwork2.http.KeysHttpTask;
import cn.baiyun.androidwork2.http.OrderHttpTask;

public class MainActivity extends AppCompatActivity implements OnDataReceivedListener, OnKeysReceivedListener, OnOrderReceivedListener, OnScoreReceivedListener {

    private EditText ed1;
    private EditText ed2;

    private String[] source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //账号密码
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);

        Button bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("my", "账号:" + ed1.getText().toString());
                Log.i("my", "密码:" + ed2.getText().toString());
                String username = ed1.getText().toString();
                String password = ed2.getText().toString();
                Log.i("my", "账号" + username);
                Log.i("my", "密码" + password);
                //发请求获取keys
                String url = "http://169.254.33.144:9000/user/keys";
                new KeysHttpTask(MainActivity.this).execute(url);
                //登录
                String loginUrl = "http://169.254.33.144:9000/user/login/" + username + "/" + password;
                new HttpTask(MainActivity.this).execute(loginUrl);
            }
        });

        //跳转到注册页面
        Button bt2 = findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
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
                    Toast.makeText(MainActivity.this, "不存在该用户", Toast.LENGTH_SHORT).show();
                } else if (state == 400) {//密码错误
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
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
                String s1 = jsonobj.toString();


                //跳转
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                intent.putExtra("user", s1); // 设置传递的数据
                Bundle bundle = new Bundle();
                bundle.putStringArray("data", source);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //关键字
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

    //接收订单信息
    @Override
    public void onOrderReveived(String result) {

    }

    @Override
    public void onScoreReceived(String res) {

    }
}