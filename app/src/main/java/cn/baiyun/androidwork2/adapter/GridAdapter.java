package cn.baiyun.androidwork2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.baiyun.androidwork2.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataList;

    public GridAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取或创建列表项的布局视图
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        }

        // 设置列表项的显示内容
        TextView textView = convertView.findViewById(R.id.itemText);
        String json = dataList.get(position);
        JSONObject jsonObject;
        int skuId;
        String skuName;
        int num;
        int id;
        try {
            jsonObject = new JSONObject(json);
            skuId = jsonObject.getInt("skuId");
            skuName = jsonObject.getString("skuName");
            num = jsonObject.getInt("num");
            id=jsonObject.getInt("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String res = "订单编号"+id+" 商品编号:"+skuId+"\n名称:"+skuName+" 数量:"+num;
        textView.setText(res);

        return convertView;
    }
}
