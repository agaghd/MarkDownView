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
 * desc     :   显示的页面
 */
public class MarkDownDisplayActivity extends AppCompatActivity {

    private MarkDownView markDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_dot_md_display);
        markDownView = (MarkDownView) findViewById(R.id.mark_down_view);
        Intent intent = getIntent();
        boolean isParseSourceDotMD = intent.getBooleanExtra("isParseSourceDotMD", false);
        if (isParseSourceDotMD) {
            //解析source.md的内容并显示
            parseSourceDotMD();
        } else {
            //如果不是是要解析source.md，就解析传过来的content的内容并显示
            String content = intent.getStringExtra("content");
            parseInputMarkDownText(content);
        }
    }

    private void parseSourceDotMD() {
        //解析source.md
        AsyncTask<Object, String, List<String>> asyncTask = new AsyncTask<Object, String, List<String>>() {

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
                    publishProgress("读取sorce.md出现错误：" + e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            publishProgress("读取sorce.md出现错误：" + e.getMessage());
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            publishProgress("读取sorce.md出现错误：" + e.getMessage());
                        }
                    }
                }
                return data;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                //发现错误，在这里弹Toast
                for (String e : values) {
                    Toast.makeText(MarkDownDisplayActivity.this, e, Toast.LENGTH_LONG).show();
                }
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
