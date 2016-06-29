package com.example.xia.weixin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private Context context;

    private EditText editText1;//没用
    private EditText editText2;//附近的人
    private EditText editText3;//添加通讯录
    private EditText editText4;//通过好友
    private EditText editText5;//朋友圈
    private EditText editText6;//点赞
    private EditText editText7;//评论

    private EditText editText12;//朋友圈内容

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String version = "";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = " v" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setTitle("微信自动化" + version);
        setContentView(R.layout.activity_main);
        this.context = this;
        initPara();
        findViewById(R.id.button_startService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openService();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自动抢红包
                startService(1);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //附近的人
                Weixin.limit2 = String2Num(editText2.getText().toString());
                startService(2);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通讯录
                startService(3);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过好友
                startService(4);
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weixin.PYQstring = editText12.getText().toString();
                Weixin.upLoadFlag = 0;
                Weixin.photo = String2Num(editText5.getText().toString());
                startService(5);
                myToast("发送一次朋友圈后会暂停服务");
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                Weixin.limit6 = String2Num(editText6.getText().toString());
                startService(6);
            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评论
                Weixin.limit7 = String2Num(editText7.getText().toString());
                startService(7);
            }
        });
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMM();
            }
        });
        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂停服务
                Weixin.kind = 0;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = this.getSharedPreferences("PREFS_CONF",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edtor = settings.edit();
        edtor.putString("key", "EditText的内容");
        edtor.commit();
    }

    protected void onPause() {
        super.onPause();
        //保存值
//        String edit1 = editText1.getText().toString();
        String edit2 = editText2.getText().toString();
////        String edit3 = editText3.getText().toString();
////        String edit4 = editText4.getText().toString();
        String edit5 = editText5.getText().toString();
        String edit6 = editText6.getText().toString();
        String edit7 = editText7.getText().toString();
        String edit12 = editText12.getText().toString();
//
        SharedPreferences.Editor editor = sharedPref.edit();
////        editor.putString("editText1", edit1);
        editor.putString("editText2", edit2);
////        editor.putString("editText3", edit3);
////        editor.putString("editText4", edit4);
        editor.putString("editText5", edit5);
        editor.putString("editText6", edit6);
        editor.putString("editText7", edit7);
        editor.putString("editText12", edit12);
        editor.commit();
    }

    private void initPara() {
//        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
//        editText3 = (EditText) findViewById(R.id.editText3);
//        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText12 = (EditText) findViewById(R.id.editText12);

        editText2.requestFocus();


//        String editText1String1 = sharedPref.getString("editText1", "");
        String editText1String2 = sharedPref.getString("editText2", "");
//        String editText1String3 = sharedPref.getString("editText3", "");
//        String editText1String4 = sharedPref.getString("editText4", "");
        String editText1String5 = sharedPref.getString("editText5", "");
        String editText1String6 = sharedPref.getString("editText6", "");
        String editText1String7 = sharedPref.getString("editText7", "");
        String editText1String12 = sharedPref.getString("editText12", "");


//        editText1.setText(editText1String1);
        editText2.setText(editText1String2);
//        editText3.setText(editText1String3);
//        editText4.setText(editText1String4);
        editText5.setText(editText1String5);
        editText6.setText(editText1String6);
        editText7.setText(editText1String7);
        editText12.setText(editText1String12);

    }


    public void myToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void openService() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找到微信自动服务，开启服务即可", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMM() {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void startService(int kind) {
        if (Weixin.isRunning()) {
            Weixin.kind = kind;
            startMM();
        } else {
            showOpenAccessibilityServiceDialog();
        }
    }

    private int String2Num(String numString) {
        if (!numString.isEmpty()) {
            return (int) Long.parseLong(numString);
        }
        return 1;
    }


    /**
     * 显示未开启辅助服务的对话框
     */
    private void showOpenAccessibilityServiceDialog() {
        myToast("未开启服务");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);

//        MenuItem setting = menu.add(0, 1, 2, "设置");
//        setting.setShowAsAction(0);

        MenuItem about = menu.add(0, 2, 2, "关于");
        about.setShowAsAction(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            myToast("触发设置：" + R.id.action_settings);
            return true;
        }
        if (id == 2) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


}
