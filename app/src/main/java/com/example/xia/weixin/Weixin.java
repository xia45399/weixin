package com.example.xia.weixin;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by xia on 2016/6/13.
 */
public class Weixin extends AccessibilityService{

    public static int kind=0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType=event.getEventType();
        String type= event.getClassName().toString();
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
                        // com.tencent.mm.ui.base.g 提高微信定位精度
                        // com.tencent.mm.ui.base.o：基本列表
                        // com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI 附近的人打招呼
                        //com.tencent.mm.plugin.nearby.ui.NearbySayHiListUI 附近的人打招呼列表
                        // com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI 附近人列表
                        // com.tencent.mm.plugin.profile.ui.ContactInfoUI 打招呼按钮界面
                        // com.tencent.mm.ui.contact.SayHiEditUI 发送打招呼界面
                        // com.tencent.mm.plugin.profile.ui.ContactInfoUI
                        if(type.equals("com.tencent.mm.ui.LauncherUI"))    // 刚进微信的时候
                        {
                            entry("发现");
                            fujinderen2();
                        }
                        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI")){//有人打招呼
                            otherSayHiUi();
                        }
                        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbySayHiListUI")){

                        }
                        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI")){
                            NearbyFriendsList();
                        }
                        if(type.equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")){

                        }
                        if(type.equals("com.tencent.mm.ui.contact.SayHiEditUI")){
                            //SayHiUi();
                        }

                        //异常判断
                        if(type.equals("com.tencent.mm.ui.base.g")){
                            //提高微信定位精度的，点击返回。。。
                        }

                        break;
                    case 3:
                        handle3();//通讯录添加好友

                        break;
                    case 4:
                        if(type.equals("com.tencent.mm.ui.LauncherUI"))    // 刚进微信的时候
                        {
                            entry("通讯录");
                            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("新的朋友");
                            nodeInfo.findAccessibilityNodeInfosByText("群聊").get(0).getParent().getParent().getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                        if (type.equals("com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI"))
                        {
                            accept4();//看是否有接收字样，有的话说明有人要加你
                        }

                        break;
                }

                break;
        }
    }



    private void handleUpdate()
    {

    }

    private void fujinderen2()
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("附近的人");//有点小问题
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击发现
            }
        }
    }
    private void NearbyFriendsList(){
        //附近的人页面，查找 米以内
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("米以内");
        if (list.size()>0)//找到米以内了
        {
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getParent().findAccessibilityNodeInfosByText("(朋友)").size()>0)
                {
                    //如果附近的人里面有自己的好友，下一个
                    continue;
                }
                list.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sleep(1000);
                SayHiUi();
                sleep(2000);
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                sleep(1000);
            }
            kind=0;
        }
    }
    private void SayHiUi(){
        //判断界面是否有打招呼，有的话点击打招呼
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("打招呼");
        if(list.size()>0)
        {
            AccessibilityNodeInfo node=list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }
        sleep(2000);
        nodeInfo = getRootInActiveWindow();
        list = nodeInfo.findAccessibilityNodeInfosByText("发送");
        if(list.size()>0)
        {
            AccessibilityNodeInfo node=list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }
    }
    private void otherSayHiUi()
    {
        //判断是否有打招呼的人
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("个打招呼消息");//你收到2个打招呼消息
        if(list.size()>0){
            list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }


    }
    private void handle2()
    {





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





        //返回到上层


    }
    private void accept4(){
        //判断是否有接收字样
        AccessibilityNodeInfo nodeInfo2 = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list2 = nodeInfo2.findAccessibilityNodeInfosByText("接受");
        if(list2.size()>0)//如果有待通过好友
        {
            for(int i=0;i<list2.size();i++)
            {
                list2.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sleep(5000);
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }
        kind=0;
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

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
