package com.learn.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class WatcherChildren {

    public static void watcherWithChildren(String path) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZkConnWatcher.getZookeeper();
            zooKeeper.getChildren(path, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println(watchedEvent.getType());
                    System.out.println(watchedEvent.getState());
                    System.out.println(watchedEvent.getPath());
                    if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                        try {
                            zooKeeper.getChildren(path, this);
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        });
    }


    public static void main(String[] args) throws InterruptedException, KeeperException {
//        ZooKeeper zooKeeper = ZkConnWatcher.getZookeeper();
//        zooKeeper.getChildren("/watcher02", true);
//        Thread.sleep(9000);

        watcherWithChildren("/watcher02");
        Thread.sleep(9000);
    }


}
