package com.example.xia.weixin;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by xia on 2016/6/13.
 */
public class Weixin extends AccessibilityService{

    public static int kind=0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType=event.getEventType();
        switch (eventType) {
            case 64:
                List<CharSequence> t64=event.getText();
                break;

            case 32:
                switch (kind)
                {
                    case 1:
                        //抢红包
                    case 2://附近好友
                        // com.tencent.mm.ui.LauncherUI 刚进微信的时候
                        // com.tencent.mm.ui.base.o：加载附近的人列表,meiy
                        // com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI 附近人列表
                        //com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI 附近的人打招呼
                        // com.tencent.mm.plugin.profile.ui.ContactInfoUI 打招呼按钮界面
                        // com.tencent.mm.ui.contact.SayHiEditUI 发送打招呼界面
                        // com.tencent.mm.plugin.profile.ui.ContactInfoUI
                        String type= event.getClassName().toString();
                        if(type.equals("com.tencent.mm.ui.LauncherUI"))    // 刚进微信的时候
                        {
                            entry("发现");
                            fujinderen a=new fujinderen();
                            a.aaaaa();

                        }
                        if(type.equals("com.tencent.mm.ui.NearbyFriendShowSayHiUI")){//有人打招呼

                        }
                        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI")){

                        }
                        if(type.equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")){

                        }
                        if(type.equals("com.tencent.mm.ui.contact.SayHiEditUI")){

                        }

                        break;
                    case 3:
                        handle3();//通讯录添加好友
                        break;
                }

                break;
        }
    }

    private void tiShi()
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("提示");
        List<AccessibilityNodeInfo> cancel= nodeInfo.findAccessibilityNodeInfosByText("取消");
        if(list.size()>0&&cancel.size()>0)
        {
            list = nodeInfo.findAccessibilityNodeInfosByText("下次不提示");
            AccessibilityNodeInfo node = list.get(0);
            if(list.size()>0)
            {//点击不再提示选项
                AccessibilityNodeInfo parent = node.getParent();
                parent.getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            list = nodeInfo.findAccessibilityNodeInfosByText("确定");
            node = list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//
        }
    }

    private void handleUpdate()
    {

    }
    private void handle2()
    {
//1不涉及界面变化11
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("发现");
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击发现
            }
        }
        nodeInfo = getRootInActiveWindow();
        list = nodeInfo.findAccessibilityNodeInfosByText("附近的人");//有点小问题
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击发现
            }
        }

        tiShi();//第一次可能会有提示

        //附近的人页面，查找 米以内
        nodeInfo=getRootInActiveWindow();
        list=nodeInfo.findAccessibilityNodeInfosByText("米以内");
        if (list.size()>0)//找到米以内了
        {
            for(int i=0;i<list.size();i++)
            {
                if(list.get(0).getParent().findAccessibilityNodeInfosByText("朋友").size()>0)
                {
                    //如果附近的人里面有自己的好友，下一个
                     continue;
                }
                list.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

        }


        //判断界面是否有打招呼，有说明是新人
        nodeInfo = getRootInActiveWindow();
        list = nodeInfo.findAccessibilityNodeInfosByText("打招呼");
        if(list.size()>0)
        {
            AccessibilityNodeInfo node=list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }

//        nodeInfo = getRootInActiveWindow();
//        list = nodeInfo.findAccessibilityNodeInfosByText("发送");
//        if(list.size()>0)
//        {
//            AccessibilityNodeInfo node=list.get(0);
//            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//        }

//        nodeInfo = getRootInActiveWindow();
//        list = nodeInfo.findAccessibilityNodeInfosByText("开始查看");
//        if(list.size()>0)
//        {
//            AccessibilityNodeInfo node = list.get(0);
//            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击发现
//        }
        //update ui




//会有无法定位的提示 text:提高微信定位精确度。
//        nodeInfo = getRootInActiveWindow();
//        list = nodeInfo.findAccessibilityNodeInfosByText("提高微信定位精确度");
//        if(list.size()>0)
//        {
//            list = nodeInfo.findAccessibilityNodeInfosByText("下次不提示");
//            AccessibilityNodeInfo node = list.get(0);
//            list = nodeInfo.findAccessibilityNodeInfosByText("跳过");
//            node = list.get(0);
//            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//
//        }


        //判断是否有打招呼的人
        nodeInfo = getRootInActiveWindow();
        list = nodeInfo.findAccessibilityNodeInfosByText("个打招呼消息");
        if(list.size()>0)
        {
            AccessibilityNodeInfo node=list.get(0);
            node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }


        //返回到上层


    }
    private void handle3()
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("通讯录");
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击通讯录
            }
        }

        List<AccessibilityNodeInfo> list1=nodeInfo.findAccessibilityNodeInfosByText("新的朋友");
        if(list.size()>0)
        {
            AccessibilityNodeInfo node=list1.get(0);
            AccessibilityNodeInfo parent=node.getParent().getParent();
            if(parent!=null) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        //判断是否有接收字样
        AccessibilityNodeInfo nodeInfo2 = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list2 = nodeInfo2.findAccessibilityNodeInfosByText("接受");
        if(list2.size()>0)//如果有待通过好友
        {
            for(int i=0;i<list2.size();i++)
            {
                list2.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }

    }

    private void sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void entry(String a)
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(a);
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

    }

    @Override
    public void onInterrupt() {

    }
}
