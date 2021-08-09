package com.learn.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class WatcherExist {

    static String ADDR = "localhost:2181";
    static int TIMEOUT = 5000;
    static final CountDownLatch latch = new CountDownLatch(1);

    public void exist(String path, boolean watch) throws InterruptedException, KeeperException, IOException {
        ZooKeeper zooKeeper = new ZooKeeper(ADDR, TIMEOUT, watchedEvent -> {
            if(watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                System.out.println("connected");
                latch.countDown();
            }
            System.out.println("event type : " + watchedEvent.getType());
            System.out.println("event path : " + watchedEvent.getPath());
        });
        latch.await();
        zooKeeper.exists(path,watch);
        Thread.sleep(10000);
    }

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
//        WatcherExist watcherExist = new WatcherExist();  // only one time run
//        watcherExist.exist("/watcher02", true);
        ZooKeeper zooKeeper = ZkConnWatcher.getZookeeper();
        Watcher watcher = new Watcher() {// register
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println(watchedEvent.getPath());
                    System.out.println(watchedEvent.getType());
                    System.out.println(watchedEvent.getState());
                    // multi times watch node events
                    zooKeeper.exists("/watcher03", this);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.exists("/watcher03", watcher);
        Thread.sleep(60000);
        zooKeeper.close();
    }


}
