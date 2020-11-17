package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.database.SQLiteHelper;
import com.example.notebook.utils.DBUtils;

import java.util.logging.Level;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView note_back;
    TextView note_time;
    EditText content;
    ImageView delete;
    ImageView note_save;
    SQLiteHelper sqLiteHelper;
    TextView noteName;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initview();
        initData();
    }

    private void initview() {
        note_back = findViewById(R.id.note_back);
        note_time = findViewById(R.id.tv_time);
        content = findViewById(R.id.note_content);
        delete = findViewById(R.id.delete);
        note_save = findViewById(R.id.note_save);
        noteName = findViewById(R.id.note_name);

        note_back.setOnClickListener(this);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);


    }

    private void initData() {
        sqLiteHelper = new SQLiteHelper(this);
        noteName.setText("添加记录");
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            if (id != null) {
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.note_back:        //后退按钮的点击事件
                finish();
                break;
            case R.id.delete:           //清空按钮的点击事件
                content.setText("");
                break;
            case R.id.note_save:        //保存按钮的点击事件
                sava();
                break;
        }
    }

    private void sava() {
        String noteContent = content.getText().toString().trim();
        if (id != null) {       //修改界面的保存操作
            if (noteContent.length() > 0) {
                if (sqLiteHelper.updateData(id, noteContent, DBUtils.getTime())) {
                    showToast("修改成功");
                    setResult(2);
                    finish();
                } else {
                    showToast("修改失败");
                }
            } else {
                showToast("修改内容不能为空！");
            }
        } else {//添加记录页面的保存操作
            //向数据库中添加数据
            if (noteContent.length() > 0) {
                if (sqLiteHelper.insertData(noteContent, DBUtils.getTime())) {
                    showToast("保存成功");
                    setResult(2);
                    finish();
                } else {
                    showToast("保存失败");
                }
            } else {
                showToast("修改内容不能为空！");
            }
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}