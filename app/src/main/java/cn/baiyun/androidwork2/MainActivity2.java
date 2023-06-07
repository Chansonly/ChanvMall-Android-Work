package cn.baiyun.androidwork2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.baiyun.androidwork2.entity.UserEntity;
import cn.baiyun.androidwork2.face.OnDataReceivedListener;
import cn.baiyun.androidwork2.http.RegisterHttpTask;

public class MainActivity2 extends AppCompatActivity implements OnDataReceivedListener {

    private EditText reged1;
    private EditText reged2;
    private EditText reged3;
    private Integer gender;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        reged1 = findViewById(R.id.reged1);
        reged2 = findViewById(R.id.reged2);
        reged3 = findViewById(R.id.reged3);

        //单选框设置事件
        RadioGroup radioGroup = findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());

        //复选框设置事件
        CheckBox cb1 = findViewById(R.id.cb1);
        CheckBox cb2 = findViewById(R.id.cb2);
        CheckBox cb3 = findViewById(R.id.cb3);
        cb1.setOnCheckedChangeListener(new CheckBoxListener());
        cb2.setOnCheckedChangeListener(new CheckBoxListener());
        cb3.setOnCheckedChangeListener(new CheckBoxListener());

        //注册按钮
        Button regbt1 = findViewById(R.id.regbt1);
        regbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reged1.getText().toString().equals("") || reged1.getText().toString() == null) {
                    Toast.makeText(MainActivity2.this, "名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (reged2.getText().toString().equals("") || reged2.getText().toString() == null) {
                    Toast.makeText(MainActivity2.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (reged3.getText().toString().equals("") || reged3.getText().toString() == null) {
                    Toast.makeText(MainActivity2.this, "年龄不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gender == null || gender <= 0 || gender >= Integer.MAX_VALUE) {
                    Toast.makeText(MainActivity2.this, "性别不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserEntity userEntity = new UserEntity();
                userEntity.setName(reged1.getText().toString());
                userEntity.setPassword(reged2.getText().toString());
                userEntity.setAge(Integer.parseInt(reged3.getText().toString()));
                userEntity.setGender(gender);
                String sb = "";
                for (String s : list) {
                    if (sb.equals("")) {
                        sb = sb + s;
                    } else {
                        sb = sb + "," + s;
                    }
                }
                if (sb.equals("")) {
                    userEntity.setFlavor(null);
                } else {
                    userEntity.setFlavor(sb);
                }
                String json = userEntity.toString();

                String s1 = "";
                // 添加键值对到 JSON 对象
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", userEntity.getName());
                    jsonObject.put("password", userEntity.getPassword());
                    jsonObject.put("age", userEntity.getAge());
                    jsonObject.put("gender", userEntity.getGender());
                    jsonObject.put("flavor", userEntity.getFlavor());
                    s1 = jsonObject.toString();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.i("my", "注册Json:" + s1);
                String registerUrl = "http://169.254.33.144:9000/user/register";
                new RegisterHttpTask(MainActivity2.this, s1).execute(registerUrl, s1);
            }
        });
    }

    //接受返回的结果
    @Override
    public void onDataReceived(String result) {
        Log.i("my", "注册返回Json:" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            int state = jsonObject.getInt("state");
            Log.i("my", "响应的状态:" + state);
            if (state != 200) {
                Toast.makeText(MainActivity2.this, "注册失败用户名已存在", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity2.this, "注册成功", Toast.LENGTH_SHORT).show();
                //跳转
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
//                intent.putExtra("user",user.toString()); // 设置传递的数据
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //单选框
    class RadioGroupListener implements android.widget.RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb1) {
                System.out.println("男");
                gender = 1;
            } else if (checkedId == R.id.rb2) {
                System.out.println("女");
                gender = 2;
            }

        }
    }

    //复选框
    class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String s = compoundButton.getText().toString();
            if (s.equals("日常商品")) {
                s = "1";
            } else if (s.equals("黑科技")) {
                s = "2";
            } else if (s.equals("游戏")) {
                s = "3";
            }
//            if (b) {
//                if (list.contains(s)) {
//                    list.remove(s);
//                    return;
//                }
//                list.add(s);
//            }
            if (list.contains(s)) {
                list.remove(s);
            } else {
                list.add(s);
            }

            Log.i("my", "集合中的元素:" + list.toString());
        }
    }

}