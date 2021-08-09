package com.learn.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZkGet {

    public static Stat getData(String path ,Boolean watch) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        Stat stat = new Stat();
        zooKeeper.getData(path,watch,stat);
        return stat;
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        Stat stat = getData("/hadoop/node12",false);
        System.out.println("version : " + stat.getVersion() + ", data aversion : " + stat.getAversion());
    }

}
