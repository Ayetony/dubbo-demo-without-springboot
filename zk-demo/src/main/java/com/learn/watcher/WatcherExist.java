package com.learn.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class WatcherExist {

    public static int TIMEOUT = 6000;

    public static void exist(String path) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZkConnWatcher.getZookeeper();
        Watcher watcher = new Watcher() {// register
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println(watchedEvent.getPath());
                    System.out.println(watchedEvent.getType());
                    System.out.println(watchedEvent.getState());
                    // multi times watch node events
                    zooKeeper.exists(path, this);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.exists(path, watcher);
        Thread.sleep(TIMEOUT);
        zooKeeper.close();
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
//        WatcherExist watcherExist = new WatcherExist();
//        watcherExist.exist("/watcher02", true);

//        ZooKeeper zooKeeper = ZkConnWatcher.getZookeeper();
//        zooKeeper.exists("/watcher02", true);
//        Thread.sleep(10000);
        exist("/watcher02");
    }


}
