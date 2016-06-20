package com.example.xia.weixin;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xia on 2016/6/13.
 */

public class Weixin extends AccessibilityService{

    public static int kind=0;


    public static int pauseService=0;

    public static int upLoadFlag =0;
    //1代表第一次进
    //2 一瞬间
    //3 完成发送朋友圈

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType=event.getEventType();
        String type= event.getClassName().toString();
        switch (eventType) {
            case 64:
                List<CharSequence> texts=event.getText();
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
                switch (kind)
                {
                    case 0:
//                        getWindowList("啦啦啦").get(1).getParent().getParent().getChild(1);//第一条
//                        getWindowList("朋友圈").get(0).getParent().getParent().getChild(1);
//                        getWindowList("啦啦啦").get(1).getParent().getParent().performAction(4096);
//
//                        //接下来
//                        getWindowList("朋友圈").get(0).getParent().getParent().getParent().getChild(1).getChild(0);
//                        getWindowList("朋友圈").get(0).getParent().getParent().getParent().getChild(1).getChild(1).getChild(5).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//
//                        //具体一条说说
//                        getWindowList("朋友圈").get(0).getParent().getParent().getParent().getChild(1).getChild(1);
//
//                        //是否评论
//                        getWindowList("朋友圈").get(0).getParent().getParent().getParent().getChild(1).getChild(1).findAccessibilityNodeInfosByText("啦啦啦");
//                        getWindowList("朋友圈").get(0).getParent().getParent().getParent().getChild(1).getChild(1).getChild(6).getText().toString().contains("啦啦啦");

                        break;

                    case 1: //抢红包
                        hongbao1(type);
                        break;
                    case 2://附近好友
                        fuJinRen2(type);
                        break;
                    case 3:
                        //可能出现的提示
                        if(type.equals("com.tencent.mm.ui.base.g"))
                        {
                            upLoadContack();//第一次会有上传通讯录的提示消息
                            cancelUpDate();
                            break;
                        }
                        // 刚进微信的时候
                        if(type.equals("com.tencent.mm.ui.LauncherUI"))
                        {
                            entry("通讯录");
                            List<AccessibilityNodeInfo> list = getWindowList("群聊");
                            if(list.size()>0)
                            {
                                list.get(0).getParent().getParent().getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                            break;
                        }
                        //点击新的朋友
                        if(type.equals("com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI"))
                        {
                            List<AccessibilityNodeInfo> list = getWindowList("添加手机联系人");
                            if(!list.isEmpty()){
                                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                            break;
                        }

                        if(type.equals("com.tencent.mm.ui.bindmobile.BindMContactIntroUI"))
                        {
                            getWindowList("上传通讯录");
                            break;
                        }
                        if(type.equals("com.tencent.mm.ui.bindmobile.MobileFriendUI"))
                        {
                            List<AccessibilityNodeInfo> list = getWindowList("添加");
                            for (int i=0;i<list.size();i++)
                            {
                                list.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                            kind=0;
                            sleep(500);
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        }


                        break;
                    case 4:
                        if(type.equals("com.tencent.mm.ui.base.g"))
                        {
                            cancelUpDate();
                        }
                        if(type.equals("com.tencent.mm.ui.LauncherUI"))    // 刚进微信的时候
                        {
                            entry("通讯录");
                            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("新的朋友");
                            nodeInfo.findAccessibilityNodeInfosByText("群聊").get(0).getParent().getParent().getParent().getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                        if (type.equals("com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI"))
                        {
                            accept4();//看是否有接收字样，有的话说明有人要加你
                        }
                    case 5:
                        PengYouQuan5(type);
                        break;

                    default:
                        //没有开启任何服务的情况
                        if(type.equals("com.tencent.mm.ui.base.g"))
                        {
                           cancelUpDate();
                        }
                        break;
                }

                break;
        }
    }



    private void hongbao1(String type){
        if(type.equals("com.tencent.mm.ui.base.g"))
        {
            cancelUpDate();
        }else if (type.equals("com.tencent.mm.ui.LauncherUI"))
        {//通过通知栏进入了微信
            clickLastPacket();
        }else if (type.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")){
            //领取完红包的
//            kind=0;
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//            sleep(2000);
//            kind=1;

        }else if(type.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")){
            openpacket();
            //打开红包并且返回到微信主界面
        }
    }


    private void clickLastPacket()
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if(!list.isEmpty())//list不为空
        {
            for (int i = list.size() - 1; i >= 0; i--)
            {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                if (parent != null)
                {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }
    private void openpacket()
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");//
        if(!list.isEmpty())
        {
            list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sleep(1000);
        }
        else {
             list = nodeInfo.findAccessibilityNodeInfosByText("開");
            nodeInfo.findAccessibilityNodeInfosByText("给你").get(0).getParent().getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        //    kind=0;
        }
      //  performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
//        kind=0;
//        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//        sleep(2000);
//        kind=1;
    }

    private void fuJinRen2(String type){
        if(type.equals("com.tencent.mm.ui.base.g"))
        {
            cancelUpDate();
        }
        if(type.equals("com.tencent.mm.ui.LauncherUI"))
        {
            entry("发现");
            clickFujinDeRen();
        }
        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI")){
            otherSayHiUi();
        }
        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbySayHiListUI")){

        }
        if(type.equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI")){
            NearbyFriendsList();
        }
        if(type.equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")){

        }
        //异常判断
        if(type.equals("com.tencent.mm.ui.base.g")){
            //提高微信定位精度的，点击返回。。。
        }

    }

    private void clickFujinDeRen()
    {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("附近的人");//有点小问题
        if(list.size()>0)
        {
            AccessibilityNodeInfo parent = list.get(0).getParent();
            if(parent != null)
            {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }


    private void NearbyFriendsList(){
        List<String> requestList=new ArrayList<String>();

        //附近的人页面，查找 米以内
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("米以内");
        if (list.size()>0)//找到米以内了
        {
            for(;requestList.size()<40;)
//          while (requestList.size()<40)
            {
               sleep(1000);
                nodeInfo = getRootInActiveWindow();
                list = nodeInfo.findAccessibilityNodeInfosByText("米以内");

                for (int i = 0; i < list.size(); i++)
                {
                    if (list.get(i).getParent().findAccessibilityNodeInfosByText("(朋友)").size() > 0)
                    {
                        //如果附近的人里面有自己的好友，下一个
                        continue;
                    }
                    String name=list.get(i).getParent().getChild(0).getText().toString();
                    if(requestList.contains(name))
                    {
                        continue;
                    }
                    requestList.add(requestList.size(),name);
                    list.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sleep(1000);
                    SayHiUi();
                    sleep(2000);
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    sleep(1000);
                }

                list.get(0).getParent().getParent().performAction(4096);
            }
            kind = 0;
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
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
            AccessibilityNodeInfo node = list.get(0);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }
    }
    private void otherSayHiUi()
    {
        //判断是否有打招呼的人
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("个打招呼消息");//你收到2个打招呼消息
        if (list.size() > 0)
        {
            list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//界面更新
        }


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

    private void PengYouQuan5(String type){

        List<AccessibilityNodeInfo> list;


        if(type.equals("com.tencent.mm.ui.LauncherUI")){
            entry("发现");
            list=getWindowList("朋友圈");
            if(list.size()>0)
            {
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
//这个地方问题比较大，发送朋友圈成功后也会进入这边
        else if (type.equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI"))
        {
            upLoadFlag++;


            if(upLoadFlag==1)
            {//第一次进 //点击相机按钮
                list=getWindowList("朋友圈");
                if(list.size()>0)
                {
                    list.get(1).getParent().getParent().getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
            else if (upLoadFlag==2)
            {//第二次进是进入图片预览的瞬间，不处理
            //vivo第二次进入是编辑朋友圈界面 之前的
            }
            else if(upLoadFlag==3)
            {
                //第三次是发完照片
                kind=0;
            }





            else {
                kind=0;
                //说明发生成功之后进入的
                //暂时不做处理，显示在朋友圈界面
              //  performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }

        else if(type.equals("com.tencent.mm.ui.base.g"))
        {
            //第一次进入时的提示
            list = getWindowList("每个人只能看到自己朋友的评论");
            if(list.size()>0)
            {
                list=getWindowList("我知道了");
                //可能点的不对，没有严重
                list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        else if (type.equals("com.tencent.mm.ui.base.j")) //j k ?
        {
            list=getWindowList("照片");
            if(list.size()>0){
                list.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        //点第一张图片
        else if(type.equals("com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI")){
            list=getWindowList("图片");
            if(list.size()>0){
                list.get(1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sleep(1000);
            }
        }
        else if(type.equals("com.tencent.mm.plugin.gallery.ui.ImagePreviewUI")){

            list=getWindowList("完成");
            if(list.size()>0)
            {
                list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        else if(type.equals("com.tencent.mm.plugin.sns.ui.SnsUploadUI")){
            //编辑文字，
            // 点击发送
            list=getWindowList("这一刻的想法");
            if (list.size()>0){
        //code
            }

            list=getWindowList("发送");
            if (list.size()>0)
            {
                list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                upLoadFlag =2;
            }
        }

    }

private void common()
{

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

    private void upLoadContack(){
        if(getWindowList("仅使用特征码").size()>0)
        {
            getWindowList("是").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
    private void cancelUpDate(){
        List<AccessibilityNodeInfo> list=getWindowList("立刻安装");
        if(!list.isEmpty()){
            list=getWindowList("取消");
            list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        list=getWindowList("是否取消安装");
        if(!list.isEmpty()){
            list=getWindowList("是");
            list.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
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

    private List<AccessibilityNodeInfo> getWindowList(String findText)
    {
        AccessibilityNodeInfo nodeInfo=getRootInActiveWindow();
        List<AccessibilityNodeInfo> list =null;
        if (nodeInfo==null)
        {
            return null;
        }
         list = nodeInfo.findAccessibilityNodeInfosByText(findText);
 //如何才能保证返回的list不为空呢？。。。可以没有数据，但是不能为null
        return list;
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "服务已关闭", Toast.LENGTH_LONG).show();
    }

}
