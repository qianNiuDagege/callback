package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notebook.adapter.NotepadAdapter;
import com.example.notebook.bean.NotepadBean;
import com.example.notebook.database.SQLiteHelper;

import java.util.List;

public class MainActivity extends Activity {

    private ListView listView;
    private List<NotepadBean> list;
    SQLiteHelper sqLiteHelper;
    NotepadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //用于显示记录的列表
        ImageView add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                startActivityForResult(intent,1);
            }
        });

        initData();
    }

    private void initData() {
        sqLiteHelper = new SQLiteHelper(this);//创建数据库
        showQueryData();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("是否删除此记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                if(sqLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);//删除对应的item
                                    adapter.notifyDataSetChanged();//更行记事本界面
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void showQueryData() {
        if(list!=null){
            list.clear();
        }
        //查询数据库中保存的记录
        list = sqLiteHelper.query();
        adapter = new NotepadAdapter(this,list);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode ==2){
            showQueryData();
        }
    }

    private void initView() {
        listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                intent.putExtra("id",notepadBean.getId());                  //记录id
                intent.putExtra("time",notepadBean.getNotepadTime());       //记录事件
                intent.putExtra("content",notepadBean.getNotepadContent());//记录内容
                MainActivity.this.startActivityForResult(intent,1); //跳转到修改记录页面
            }
        });
    }



}

