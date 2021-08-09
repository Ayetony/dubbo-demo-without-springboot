package com.learn.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkConnWatcher implements Watcher {

    public static String address = "localhost:2181";
    static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType() == Event.EventType.None){
            switch (watchedEvent.getState()) {
                case SyncConnected :
                    System.out.println("connected");
                    latch.countDown();
                    break;
                case Disconnected :
                    System.out.println("disconnected");
                    break;
                case Expired:
                    System.out.println("expired");
                    break;
                case AuthFailed:
                    System.out.println("authFailed");
                    break;
            }
        }
    }

    public static ZooKeeper getZookeeper(){
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(address,5000, new ZkConnWatcher());
            latch.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }


    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper(address,5000, new ZkConnWatcher());
            latch.await();
            System.out.println(zooKeeper.getSessionId());
            // auth scheme user:pass
            zooKeeper.addAuthInfo("digest","miao:123456".getBytes());
            byte[] bytes = zooKeeper.getData("/node1",false,null);
            System.out.println(new String(bytes));
            zooKeeper.close();
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
