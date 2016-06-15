package com.example.xia.weixin;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by xia on 2016/6/15.
 */
public class fujinderen extends  Weixin
{
    public void aaaaa()
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
}
