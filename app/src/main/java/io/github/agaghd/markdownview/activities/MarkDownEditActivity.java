package io.github.agaghd.markdownview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import io.github.agaghd.markdownview.R;

public class MarkDownEditActivity extends AppCompatActivity {

    private EditText markDownEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_down_edit);
        markDownEt = (EditText) findViewById(R.id.markdown_et);
    }

    public void parse(View view) {
        Intent intent = new Intent(this, MarkDownDisplayActivity.class);
        intent.putExtra("content", markDownEt.getText().toString());
        startActivity(intent);
    }
}