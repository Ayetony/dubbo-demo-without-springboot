package com.learn.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ZkGet {

    public static DataStat getData(String path ,Boolean watch) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        Stat stat = new Stat();
        byte[] bytes = zooKeeper.getData(path,watch,stat);
        DataStat dataStat = new DataStat();
        dataStat.setStat(stat);
        dataStat.setData(new String(bytes));
        return dataStat;
    }

    public static void asyncGetData(String path ,Boolean watch) throws InterruptedException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        zooKeeper.getData(path, watch, (i, s, o, bytes, stat) -> System.out.println(new String(bytes)),
                "async data callback context");
        Thread.sleep(300);
    }

    public static List<String> getChildren(String path, boolean watch) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        return zooKeeper.getChildren(path, watch);
    }

    public static void asyncGetChildren(String path, boolean watch) throws InterruptedException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        zooKeeper.getChildren(path, watch, (i, s, o, list) -> list.forEach(System.out::println),
                "get children node context");
        Thread.sleep(300);
    }


    static class DataStat{

        private Stat stat;
        private String data;

        public Stat getStat() {
            return stat;
        }

        public void setStat(Stat stat) {
            this.stat = stat;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static boolean exist(String path, boolean watch) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        return zooKeeper.exists(path, watch) != null;
    }

    public static void asyncExist(String path, boolean watch) throws InterruptedException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        zooKeeper.exists(path, watch, (i, s, o, stat) -> System.out.println(stat.getVersion() + ", i = " + i),
                "exist async ctx");
        Thread.sleep(300);
    }


    public static void main(String[] args) throws InterruptedException, KeeperException {
        asyncGetData("/hadoop/node12",false);
        DataStat dataStat = getData("/hadoop/node12",false);
        System.out.println("Data msg : " +  dataStat.getData()  + " version : " + dataStat.getStat().getVersion()
                + ", data aversion : " + dataStat.getStat().getAversion());
        List<String> childrenNodeList = getChildren("/hadoop", false);
        childrenNodeList.forEach(System.out::println);
        //[zk: localhost(CONNECTED) 14] ls /hadoop
        //[node1, node12, node2, node3, node4, node5]
        //[zk: localhost(CONNECTED) 15]

        asyncGetChildren("/hadoop", false);
        System.out.println(exist("/hadoop/node1", false));
        asyncExist("/hadoop", false);
    }

}
