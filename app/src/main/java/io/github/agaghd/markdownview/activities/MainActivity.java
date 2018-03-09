package io.github.agaghd.markdownview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.agaghd.markdownview.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View view) {
        //前往解析source.md的页面
        Intent intent = new Intent(this, MarkDownDisplayActivity.class);
        intent.putExtra("isParseSourceDotMD", true);
        startActivity(intent);
    }

    public void btn2Click(View view) {
        //前往测试输入的页面
        Intent intent = new Intent(this, MarkDownEditActivity.class);
        startActivity(intent);
    }
}
