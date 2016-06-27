package com.example.xia.weixin;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xia on 2016/6/13.
 */

public class Weixin extends AccessibilityService {

    public static String PYQstring = "";//发朋友圈的内容
    public static String MYname = "啦啦啦";

    private static Weixin service;

    public static int kind = 0;
    //3 完成发送朋友圈
    //2 一瞬间
    //1代表第一次进
    public static int upLoadFlag = 0;

    public static int limit2 = 10;//附近的人一次加几个
    public static int limit3 = 20;//通讯录
    public static int limit6 = 20;//点赞
    public static int limit7 = 10;//评论

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        Toast.makeText(this, "已连接服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String type = event.getClassName().toString();
        switch (eventType) {
            case 64:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains("[微信红包]")) {
                            //模拟打开通知栏消息
                            if (event.getParcelableData() != null
                                    && event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    pendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;

            case 32:
                switch (kind) {
                    case 0:
                        if (type.equals("com.tencent.mm.ui.base.g")) {
                            cancelUpDate();
                        }
                        break;
                    case 1: //抢红包
                        HongBao1(type);
                        break;
                    case 2://附近好友
                        FuJinRen2(type);
                        break;
                    case 3:
                        //加通讯录
                        TongxunLu3(type);
                        break;
                    case 4:
                        TongGuo4(type);
                        break;
                    case 5:
                        PengYouQuan5(type);
                        break;
                    case 6:
//                        DianZan6(type);
                        newDianZan(type);
                        break;
                    case 7:
                        PingLun7(type);
                        break;

                    default:
                        //没有开启任何服务的情况
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.myToast("未知服务类型");
                        break;
                }

                break;
        }
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "服务已关闭", Toast.LENGTH_LONG).show();
    }


    private void HongBao1(String type) {
        switch (type) {
            case "com.tencent.mm.ui.base.g":
                cancelUpDate();
                break;
            case "com.tencent.mm.ui.LauncherUI": //通过通知栏进入了微信
                clickLastPacket();
                break;
            case "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI":
                //领取完红包的
                break;
            case "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI":
                openpacket();
                //打开红包并且返回到微信主界面
                break;
        }
    }


    private void clickLastPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if (!list.isEmpty())//list不为空
        {
            for (int i = list.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }

    private void openpacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");//
        if (!list.isEmpty()) {
            list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sleep(1000);
        } else {
            nodeInfo.findAccessibilityNodeInfosByText("给你").get(0).getParent().getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            //    kind=0;
        }

    }

    private void FuJinRen2(String type) {
        switch (type) {
            case "com.tencent.mm.ui.base.g":
                //提高微信定位精度的，点击返回。。。

                if (!getWindowList("提高微信定位精确度").isEmpty()) {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
                cancelUpDate();
                break;
            case "com.tencent.mm.ui.LauncherUI": {
                entry("发现");
                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("附近的人");//点击附近的人

                if (list.size() > 0) {
                    AccessibilityNodeInfo parent = list.get(0).getParent();
                    if (parent != null) {
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                break;
            }
            case "com.tencent.mm.plugin.nearby.ui.NearbyFriendsIntroUI":

                break;
            case "com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI": {
//            otherSayHiUi();
                //判断是否有打招呼的人
//                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("个打招呼消息");//你收到2个打招呼消息
//
//                if (list.size() > 0) {
//                    list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
//                }
                break;
            }
            case "com.tencent.mm.plugin.nearby.ui.NearbySayHiListUI": {
                List<AccessibilityNodeInfo> list = getWindowList("查看更多");
                if (list != null && !list.isEmpty()) {
                    AccessibilityNodeInfo requestListNode = list.get(0).getParent().getParent();
                    for (int i = 0; i < requestListNode.getChildCount() - 1; i++) {
                        requestListNode.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        sleep(1000);
                        getWindowList("通过验证").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        sleep(1000);
                    }
                }
                break;
            }
            case "com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI":
                NearbyFriendsList();
                break;
            case "com.tencent.mm.plugin.profile.ui.ContactInfoUI":
                //通过验证界面
                break;
        }

    }


    private void NearbyFriendsList() {
        List<String> RequestDoneList = new ArrayList<>();

        //附近的人页面，查找 米以内
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("米以内");
        if (list.size() > 0)//找到米以内了
        {
            // for (; requestList.size() < limit2; )
            while (RequestDoneList.size() < limit2) {
                sleep(1000);
                nodeInfo = getRootInActiveWindow();
                list = nodeInfo.findAccessibilityNodeInfosByText("米以内");

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getParent().findAccessibilityNodeInfosByText("(朋友)").size() > 0) {
                        //如果附近的人里面有自己的好友，下一个
                        continue;
                    }
                    String name = list.get(i).getParent().getChild(0).getText().toString();
                    if (RequestDoneList.contains(name)) {
                        continue;
                    }
                    RequestDoneList.add(RequestDoneList.size(), name);
                    list.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sleep(1000);
                    SayHiUi();
                    sleep(2000);
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    sleep(1000);
                    if (RequestDoneList.size() >= limit2) {
                        break;
                    }
                }

                list.get(0).getParent().getParent().performAction(4096);
            }

            kind = 0;
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    private void SayHiUi() {
        //判断界面是否有打招呼，有的话点击打招呼
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("打招呼");
        if (list.size() > 0) {
            AccessibilityNodeInfo node = list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }
        sleep(2000);
        nodeInfo = getRootInActiveWindow();
        list = nodeInfo.findAccessibilityNodeInfosByText("发送");
        if (list.size() > 0) {
            AccessibilityNodeInfo node = list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }
    }

    /*
        private void handle3() {
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("通讯录");
            if (list.size() > 0) {
                AccessibilityNodeInfo parent = list.get(0).getParent();
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击通讯录
                }
            }

            List<AccessibilityNodeInfo> list1 = nodeInfo.findAccessibilityNodeInfosByText("新的朋友");
            if (list.size() > 0) {
                AccessibilityNodeInfo node = list1.get(0);
                AccessibilityNodeInfo parent = node.getParent().getParent();
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    */
    private void TongxunLu3(String type) {
//可能出现的提示
        if (type.equals("com.tencent.mm.ui.base.g")) {
            upLoadContackTishi();//第一次会有上传通讯录的提示消息
            cancelUpDate();
        }
        // 刚进微信的时候
        if (type.equals("com.tencent.mm.ui.LauncherUI")) {
            entry("通讯录");
            List<AccessibilityNodeInfo> list = getWindowList("群聊");
            if (list != null && list.size() > 0) {
                list.get(0).getParent().getParent().getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        //点击新的朋友
        if (type.equals("com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI")) {
            List<AccessibilityNodeInfo> list = getWindowList("添加手机联系人");
            if (list != null && !list.isEmpty()) {
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        if (type.equals("com.tencent.mm.ui.bindmobile.BindMContactIntroUI")) {
            getWindowList("上传通讯录");
        }
        if (type.equals("com.tencent.mm.ui.bindmobile.MobileFriendUI")) {
            List<AccessibilityNodeInfo> list = getWindowList("添加");
            for (int i = 0; i < list.size(); i++) {
                list.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            kind = 0;
            sleep(500);
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    private void TongGuo4(String type) {
        //通过好友
        if (type.equals("com.tencent.mm.ui.base.g")) {
            cancelUpDate();
        }
        if (type.equals("com.tencent.mm.ui.LauncherUI"))    // 刚进微信的时候
        {
            entry("通讯录");
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("新的朋友");
            nodeInfo.findAccessibilityNodeInfosByText("群聊").get(0).getParent().getParent().getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        if (type.equals("com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI")) {
            //看是否有接收字样，有的话说明有人要加你
            //判断是否有接收字样
            AccessibilityNodeInfo nodeInfo2 = getRootInActiveWindow();
            List<AccessibilityNodeInfo> list2 = nodeInfo2.findAccessibilityNodeInfosByText("接受");
            if (list2.size() > 0)//如果有待通过好友
            {
                for (int i = 0; i < list2.size(); i++) {
                    list2.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sleep(5000);
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
            }
            kind = 0;
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    @SuppressLint("NewApi")
    private void PengYouQuan5(String type) {

        List<AccessibilityNodeInfo> list;


        switch (type) {
            case "com.tencent.mm.ui.LauncherUI":
                entry("发现");
                list = getWindowList("朋友圈");
                if (list != null && list.size() > 0) {
                    list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                break;
            //这个地方问题比较大，发送朋友圈成功后也会进入这边
            case "com.tencent.mm.plugin.sns.ui.SnsTimeLineUI":
                upLoadFlag++;
                if (upLoadFlag == 1) {//第一次进 //点击相机按钮
                    list = getWindowList("朋友圈");
                    if (list != null && list.size() > 0) {
                        list.get(list.size() - 1).getParent().getParent().getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                if (upLoadFlag == 11) {
                    //发完进入的，不处理
                }
                break;
            case "com.tencent.mm.ui.base.g":
                //第一次进入时的提示
                list = getWindowList("每个人只能看到自己朋友的评论");
                if (list != null && list.size() > 0) {
                    list = getWindowList("我知道了");
                    //可能点的不对，没有严重
                    if (list != null) {
                        list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                break;
            case "com.tencent.mm.ui.base.j":
                list = getWindowList("照片");
                if (list != null && list.size() > 0) {
                    list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                break;
            //点第一张图片
            case "com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI":
                list = getWindowList("图片");
                if (list != null && list.size() > 0) {
                    list.get(1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sleep(1000);
                }
                break;
            case "com.tencent.mm.plugin.gallery.ui.ImagePreviewUI":

                list = getWindowList("完成");
                if (list != null && list.size() > 0) {
                    list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                break;
            case "com.tencent.mm.plugin.sns.ui.SnsUploadUI":
                //编辑文字，
                // 点击发送

                //这一刻的想法
                list = getWindowList("所在位置");
                getWindowList("所在位置").get(0).getParent().getParent().getChild(0);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(PYQstring);
                getWindowList("所在位置").get(0).getParent().getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                list = getWindowList("发送");
                if (list != null && list.size() > 0) {
                    list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    upLoadFlag = 10;
                    kind = 0;
                }
                break;
        }

    }

    /*
        private void DianZan6(String type) {
            List<AccessibilityNodeInfo> list;
            AccessibilityNodeInfo window;

            if (type.equals("com.tencent.mm.ui.LauncherUI")) {
                entry("发现");
                list= getWindowList("朋友圈");
                if (list.size() > 0) {
                    list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }else if(type.equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI")){
                list=getWindowList("朋友圈");
                if(list.size()>0)
                {
                    //windows为整个窗口
                    window = list.get(0).getParent().getParent().getParent();
                    List<AccessibilityNodeInfo> DZlist = window.findAccessibilityNodeInfosByText("评论");
                    int m=10;
                    while(m-->0)
                    {
                        for(int i=0;i<DZlist.size();i++){
                            //点击点赞按钮
                            DZlist.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            sleep(1000);

                            DZlist = window.findAccessibilityNodeInfosByText("赞");

                            if(DZlist.size()>0&&window.findAccessibilityNodeInfosByText("评论").size()>0)
                            {
                                DZlist.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                sleep(1000);
                            }
                            else if(window.findAccessibilityNodeInfosByText("取消").size()>0&&window.findAccessibilityNodeInfosByText("评论").size()>0){
                                //点过赞了，下一个
                                DZlist.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                continue;
                            }
                            //重新获取评论的按钮数，一般是大于1
                            DZlist = window.findAccessibilityNodeInfosByText("评论");
                        }
                        DZlist.get(0).getParent().getParent().performAction(4096);
                    }

                }

            }
           }
    */
    private void newDianZan(String type) {
        List<AccessibilityNodeInfo> list;

        if (type.equals("com.tencent.mm.ui.LauncherUI")) {
            entry("发现");
            list = getWindowList("朋友圈");
            if (list != null && list.size() > 0) {
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else if (type.equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI")) {
            list = getWindowList("com.tencent.mm:id/bta");
            while (limit6 > 0) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) == null || limit6 == 0)
                            break;
                        list.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        sleep(1000);
                        List<AccessibilityNodeInfo> clickZanList = getWindowList("com.tencent.mm:id/bso");
                        if (clickZanList != null && clickZanList.size() > 0) {
                            if (clickZanList.get(0).getChild(0).getText().toString().contains("赞")) {
                                clickZanList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                sleep(1000);
                                limit6--;
                            }
                        }
                    }
                }
                //判断是否 结束、翻页并更新list,
                if (limit7 == 0)
                    break;
                else {
                    if (list != null) {
                        list.get(0).getParent().getParent().performAction(4096);
                    }
                    sleep(1000);
                    list = getWindowList("com.tencent.mm:id/bta");
                }
            }
            kind = 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void PingLun7(String type) {
        List<AccessibilityNodeInfo> list;

        if (type.equals("com.tencent.mm.ui.LauncherUI")) {
            entry("发现");
            list = getWindowList("朋友圈");
            if (list != null && list.size() > 0) {
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else if (type.equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI")) {
            list = getWindowList("com.tencent.mm:id/bta");
            while (limit7 > 0) {
                list = getWindowList("com.tencent.mm:id/bwy");//说说列表
                if (list != null && !list.isEmpty()) {
                    int flagFY = 1;
                    for (int i = 0; i < list.size(); i++) {
                        //找第一个未评论
                        //没有人评论过
                        if (list.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bwn").isEmpty()) {
                            list.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bta").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            List<AccessibilityNodeInfo> clickZanList = getWindowList("com.tencent.mm:id/bsr");
                            if (clickZanList != null && clickZanList.size() > 0) {
                                clickZanList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                            sleep(1000);//等待评论框出现
                            List<AccessibilityNodeInfo> sendList = getWindowList("com.tencent.mm:id/btk");//没输内容
                            if (sendList != null && sendList.size() > 0) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText("自动评论/::)" + limit7);
                                getWindowList("com.tencent.mm:id/bti").get(0).performAction(32768);
                                sendList = getWindowList("com.tencent.mm:id/btl");//输了内容
                                sendList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                limit7--;
                                flagFY = 0;
                                sleep(1000);
                            }
                            break;
                        }
                        //有人评论，判断有没有自己
                        else {
                            List<AccessibilityNodeInfo> PLlist = list.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bwn");
                            for (AccessibilityNodeInfo node : PLlist) {
                                if (!node.findAccessibilityNodeInfosByText(MYname).isEmpty()) {
                                    //自己评论过，下一个
                                    break;
                                } else {
                                    list.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bta").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    List<AccessibilityNodeInfo> clickZanList = getWindowList("com.tencent.mm:id/bsr");
                                    if (clickZanList != null && clickZanList.size() > 0) {
                                        clickZanList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    }
                                    sleep(1000);//等待评论框出现
                                    List<AccessibilityNodeInfo> sendList = getWindowList("com.tencent.mm:id/btk");//没输内容
                                    if (sendList != null && sendList.size() > 0) {
                                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        clipboard.setText("自动评论/::)" + limit7);
                                        getWindowList("com.tencent.mm:id/bti").get(0).performAction(32768);
                                        sendList = getWindowList("com.tencent.mm:id/btl");//输了内容
                                        sendList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        limit7--;
                                        flagFY = 0;
                                        sleep(1000);
                                    }
                                    break;
                                }
                            }


                        }
                    }
                    if (flagFY == 1) {
                        //fanye
                        list.get(0).getParent().getParent().performAction(4096);
                        sleep(1000);
                    }
                }
                //判断是否 结束、翻页并更新list,
                if (limit7 == 0) {
                    kind = 0;
                    break;
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void newPingLun() {
        List<AccessibilityNodeInfo> list = getWindowList("com.tencent.mm:id/bwy");//说说列表

        if (list != null && !list.isEmpty()) {
            int needFanYE = 1;
            for (int i = 0; i < list.size(); i++) {
                //找第一个未评论
                //没有人评论过
                if (list.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bwn").isEmpty()) {
                    list.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bta").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    List<AccessibilityNodeInfo> clickZanList = getWindowList("com.tencent.mm:id/bsr");
                    if (clickZanList != null && clickZanList.size() > 0) {
                        clickZanList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    sleep(1000);//等待评论框出现
                    List<AccessibilityNodeInfo> sendList = getWindowList("com.tencent.mm:id/btk");//没输内容
                    if (sendList != null && sendList.size() > 0) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText("自动评论/::)" + limit7);
                        getWindowList("com.tencent.mm:id/bti").get(0).performAction(32768);
                        sendList = getWindowList("com.tencent.mm:id/btl");//输了内容
                        sendList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        limit7--;
                        needFanYE = 0;
                        sleep(1000);
                    }
                    break;
                }
                //有人评论，判断有没有自己
                else {

                }
            }
            if (needFanYE == 1) {
                //fanye
                list.get(0).getParent().getParent().performAction(4096);
                sleep(1000);
            }
        }
    }


    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void upLoadContackTishi() {
        if (getWindowList("仅使用特征码").size() > 0) {
            getWindowList("是").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void tiShi() {

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("提示");
        List<AccessibilityNodeInfo> cancel = nodeInfo.findAccessibilityNodeInfosByText("取消");
        if (list.size() > 0 && cancel.size() > 0) {
            list = nodeInfo.findAccessibilityNodeInfosByText("下次不提示");
            AccessibilityNodeInfo node = list.get(0);
            if (list.size() > 0) {//点击不再提示选项
                AccessibilityNodeInfo parent = node.getParent();
                parent.getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            list = nodeInfo.findAccessibilityNodeInfosByText("确定");
            node = list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//
        }
    }

    private void cancelUpDate() {
        List<AccessibilityNodeInfo> list = getWindowList("立刻安装");
        if (list != null && !list.isEmpty()) {
            list = getWindowList("取消");
            list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        list = getWindowList("是否取消安装");
        if (!list.isEmpty()) {
            list = getWindowList("是");
            if (list != null) {
                list.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

    }

    private void entry(String a) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(a);
        if (list.size() > 0) {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if (parent != null) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 返回查找文字的list
     *
     * @param findText
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private List<AccessibilityNodeInfo> getWindowList(String findText) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = null;
        if (nodeInfo == null) {
            return null;
        }
        if (findText.contains("com.tencent.mm:id")) {
            list = nodeInfo.findAccessibilityNodeInfosByViewId(findText);
        } else {
            list = nodeInfo.findAccessibilityNodeInfosByText(findText);
        }
        //如何才能保证返回的list不为空呢？。。。可以没有数据，但是不能为null
        return list;
    }


    //判断服务是否开启
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if (service == null) {
            return false;
        }

        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();
//
        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }

        return true;

    }


}
