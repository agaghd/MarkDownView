package io.github.agaghd.markdownview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import io.github.agaghd.markdownview.R;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : 测试自定义输入MarkDown语句的页面
 */
public class MarkDownEditActivity extends AppCompatActivity {

    private EditText markDownEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_down_edit);
        markDownEt = (EditText) findViewById(R.id.markdown_et);
        String testStr = getString(R.string.markdown_et_test);
        markDownEt.setText(testStr);
        markDownEt.setSelection(testStr.length());
    }

    public void parse(View view) {
        Intent intent = new Intent(this, MarkDownDisplayActivity.class);
        intent.putExtra("content", markDownEt.getText().toString());
        startActivity(intent);
    }

    public void clear(View view) {
        markDownEt.setText("");
        markDownEt.setSelection(0);
    }
}
