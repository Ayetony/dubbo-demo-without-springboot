package com.learn.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKConn implements Watcher {

    private static final String ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public void process(WatchedEvent event) {
        System.out.println("received event name : " + event.getState());
        if(Event.KeeperState.SyncConnected == event.getState())
            countDownLatch.countDown();// decrement of count
    }

    public static ZooKeeper getZkClientConnection(){
        try {
            ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, SESSION_TIMEOUT, new ZKConn());
            System.out.println(zooKeeper.getState());
            // returns immediately until latch counting down to zero
            boolean flag =countDownLatch.await(SESSION_TIMEOUT,TimeUnit.SECONDS);
            if(!flag){
                zooKeeper.close();
                throw new RuntimeException("session timeout happens");
            }else{
                return zooKeeper;
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
