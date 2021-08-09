package com.learn.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;

public class ZkDel {

    public void asyncDelNode(String path,int version) throws InterruptedException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper disconnected error");
        }
        zooKeeper.delete(path, version, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int i, String s, Object o) {
                System.out.println(i);
            }
        },"del node context");
        Thread.sleep(1000);
    }

    public static void main(String[] args) throws InterruptedException {
        ZkDel del = new ZkDel();
        del.asyncDelNode("/hadoop/node1",-1);
//        [zk: localhost(CONNECTED) 6] get /hadoop/node1
//        Node does not exist: /hadoop/node1
    }

}
