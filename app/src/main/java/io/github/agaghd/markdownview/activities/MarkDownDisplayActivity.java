package io.github.agaghd.markdownview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.agaghd.markdownview.R;
import io.github.agaghd.markdownview.customview.MarkDownView;

/**
 * author   :   wjy
 * time     :   2018/3/9
 * desc     :   解析source.md并显示的页面
 */
public class MarkDownDisplayActivity extends AppCompatActivity {

    private MarkDownView markDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_dot_md_display);
        viewInit();
        Intent intent = getIntent();
        boolean isParseSourceDotMD = intent.getBooleanExtra("isParseSourceDotMD", false);
        if (isParseSourceDotMD) {
            parseSourceDotMD();
        } else {
            String content = intent.getStringExtra("content");
            parseInputMarkDownText(content);
        }
    }

    private void viewInit() {
        markDownView = (MarkDownView) findViewById(R.id.mark_down_view);
    }

    private void parseSourceDotMD() {
        // TODO: 2018/3/9 解析source.md
        List<String> fakeData = new ArrayList<>();
        fakeData.add("Hello MarkDown");
        fakeData.add("Hello Everyone");
        markDownView.setData(fakeData);
        markDownView.addData("wtf");
    }

    /**
     * 解析自己输入的MarkDown语句
     *
     * @param content 输入内容
     */
    private void parseInputMarkDownText(String content) {
        if (!TextUtils.isEmpty(content)) {
            String[] array = content.split("\\n");
            for (String line : array) {
                markDownView.addData(line);
            }
        }
    }

}
