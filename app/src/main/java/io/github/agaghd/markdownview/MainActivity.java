package io.github.agaghd.markdownview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View view) {
        // TODO: 2018/3/9 前往解析source.md的页面
    }

    public void btn2Click(View view) {
        // TODO: 2018/3/9 前往测试输入的页面
    }
}
