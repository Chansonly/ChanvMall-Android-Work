package cn.baiyun.androidwork2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.baiyun.androidwork2.R;

public class FirstFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("acttag",FirstFragment.this.toString()+" onCreateView被执行了");
        View inflate = inflater.inflate(R.layout.first_fragment, null);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("acttag",FirstFragment.this.toString()+" onActivityCreated被执行了");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        Log.i("acttag",FirstFragment.this.toString()+" onDetach被执行了");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.i("acttag",FirstFragment.this.toString()+" onDestroyView被执行了");
        Log.i("acttag",FirstFragment.this.toString()+" onDestroy被执行了");
        Log.i("acttag",FirstFragment.this.toString()+" onDetach被执行了");
        super.onDestroyView();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("acttag",FirstFragment.this.toString()+" onCreate被执行了");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("acttag",FirstFragment.this.toString()+" onStart被执行了");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("acttag",FirstFragment.this.toString()+" onResume被执行了");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i("acttag",FirstFragment.this.toString()+" onAttach被执行了");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("acttag",FirstFragment.this.toString()+" onPause被执行了");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("acttag",FirstFragment.this.toString()+" onStop被执行了");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("acttag",FirstFragment.this.toString()+" onDestroy被执行了");
    }



}
