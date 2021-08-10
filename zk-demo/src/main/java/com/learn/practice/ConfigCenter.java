package com.learn.practice;

import com.learn.watcher.ZkConnWatcher;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class ConfigCenter implements Watcher {

    static ZooKeeper zooKeeper;
    static CountDownLatch latch = new CountDownLatch(1);
    private String remoteAddr;
    private String userName;
    private String pass;

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void init(){
        try {
            zooKeeper = new ZooKeeper(ZkConnWatcher.address,ZkConnWatcher.SESSION_TIMEOUT,this);
            latch.await();
            this.remoteAddr = new String(zooKeeper.getData("/config/remoteAddr",true,null));
            this.userName = new String(zooKeeper.getData("/config/userName",true,null));
            this.pass = new String(zooKeeper.getData("/config/pass",true,null));
        } catch (KeeperException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.None) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    System.out.println("sync connected");
                    latch.countDown();
                    break;
                case Disconnected:
                    System.out.println("disconnected");
                    break;
                case Expired:
                    System.out.println("expired");
                    break;
                case AuthFailed:
                    System.out.println("auth failed");
                    break;
            }
        }else if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
            init();
        }
        System.out.println("event type : " + watchedEvent.getType());
        System.out.println("event path : " + watchedEvent.getPath());
    }

    public static void main(String[] args) {
        ConfigCenter configCenter = new ConfigCenter();
        configCenter.init();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("user name : " + configCenter.getUserName());
                System.out.println("pass : " + configCenter.getPass());
                System.out.println("remote addr : " + configCenter.getRemoteAddr());
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
        },1000,5000);
    }
}
