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
                        handle2();
                        break;
                    case 3:
                        handle3();//通讯录添加好友
                        break;
                }

                break;
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
        list = nodeInfo.findAccessibilityNodeInfosByText("附近的人");
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击发现
            }
        }

        nodeInfo = getRootInActiveWindow();
        list = nodeInfo.findAccessibilityNodeInfosByText("开始查看");
        if(list.size()>0)
        {
            AccessibilityNodeInfo node = list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击发现
        }





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

        for(AccessibilityNodeInfo node:list2)//有新的朋友添加，点通过
        {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            //通过了点击返回
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

        }

    }

    @Override
    public void onInterrupt() {

    }
}
