package io.github.agaghd.markdownview.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        //解析source.md
        AsyncTask<Object, Object, List<String>> asyncTask = new AsyncTask<Object, Object, List<String>>() {

            @Override
            protected List<String> doInBackground(Object... params) {
                List<String> data = new ArrayList<>();
                InputStream is = null;
                BufferedReader reader = null;
                try {
                    is = MarkDownDisplayActivity.this.getResources().getAssets().open("source.md");
                    reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    MarkDownDisplayActivity.this,
                                    "读取sorce.md出现错误：" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    MarkDownDisplayActivity.this,
                                    "读取sorce.md出现错误：" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                return data;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                if (strings != null && strings.size() > 0) {
                    markDownView.setData(strings);
                }
            }
        };
        asyncTask.execute();
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
