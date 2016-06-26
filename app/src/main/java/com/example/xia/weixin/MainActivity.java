package com.example.xia.weixin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String version="";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = " v" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setTitle("微信自动化"+version);
        setContentView(R.layout.activity_main);
        this.context=this;
        findViewById(R.id.service_button).setOnClickListener(new View.OnClickListener() {
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
                Weixin.PYQstring="自动说说";
                Weixin.upLoadFlag =0;
                myToast("发送一次朋友圈后会暂停服务");
                startService(5);
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                startService(6);
            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评论
                startService(7);
            }
        });
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMM();
            }
        });
        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂停服务
                Weixin.kind=0;
            }
        });
    }



public void myToast(String msg)
{
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
    private void startMM()
    {
        PackageManager packageManager=context.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        context.startActivity(intent);
    }

    private void startService(int kind)
    {
        if(Weixin.isRunning())
        {
            Weixin.kind=kind;
            startMM();
        }
        else {
            myToast("开");
        }

    }




    /** 显示未开启辅助服务的对话框*/
    private void showOpenAccessibilityServiceDialog() {
        myToast("未开启服务");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem setting=menu.add(0,1,2,"设置");
        setting.setShowAsAction(0);

 //       MenuItem about=menu.add(0,2,2,"关于");
     //   about.setShowAsAction(0);
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        myToast(id+"");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            myToast("触发设置："+ R.id.action_settings);
            return true;
        }

        if(id==1)
        {
            myToast("触发设置");
            Intent intent=new Intent(this,settingActivity.class);
            context.startActivity(intent);
        }

        if(id==2)
        { myToast("触发about");
            Intent intent=new Intent(this,aboutMeActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }


}
