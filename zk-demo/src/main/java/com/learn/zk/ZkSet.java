package com.learn.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;

public class ZkSet {


    public Stat setNodeData(String path, String data, int version) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper disconnected error");
        }
        return zooKeeper.setData(path,data.getBytes(StandardCharsets.UTF_8),version);
    }


    public void asyncSetNodeData(String path, String data, int version) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper disconnected error");
        }
        zooKeeper.setData(path,data.getBytes(StandardCharsets.UTF_8),version,
                (i, s, o, stat) -> System.out.println(stat.getVersion()),"async context set");
        Thread.sleep(10000);
    }




    public static void main(String[] args) throws InterruptedException, KeeperException {
        ZkSet setter = new ZkSet();
        // version=-1 ,do not assign the val directly
//        Stat stat = setter.setNodeData("/hadoop/node12","hello new version node12",-1);
//        System.out.println("node set version code:" + stat.getVersion());

        Stat stat = setter.setNodeData("/hadoop/node12","hello next version node",-1);
        System.out.println("node set version code:" + stat.getVersion());

//        [zk: localhost(CONNECTED) 2] get -s  /hadoop/node12
//        hello next version node
//        cZxid = 0x87
//        ctime = Mon Aug 09 23:30:40 CST 2021
//        mZxid = 0x9b
//        mtime = Mon Aug 09 23:52:30 CST 2021
//        pZxid = 0x87
//        cversion = 0
//        dataVersion = 3
//        aclVersion = 0
//        ephemeralOwner = 0x0
//        dataLength = 23
//        numChildren = 0
    }



}
