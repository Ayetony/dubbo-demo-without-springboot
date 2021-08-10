package com.learn.practice;

import com.learn.watcher.ZkConnWatcher;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class DistIDGenerator implements Watcher {

    static CountDownLatch latch = new CountDownLatch(1);
    static String DIST_ID = "/dist_id";

    public String generate() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper(ZkConnWatcher.address, ZkConnWatcher.SESSION_TIMEOUT, this);
            latch.await();
            String path = zooKeeper.create(DIST_ID, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            zooKeeper.close();
            return path.substring(DIST_ID.length());
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
        return null;
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
        }
    }

    public static void main(String[] args) {
        DistIDGenerator generator = new DistIDGenerator();
        System.out.println(generator.generate());
    }
}
