package com.learn.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

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

    public static void asyncGetData(String path ,Boolean watch) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        zooKeeper.getData(path, watch, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println(new String(bytes));
            }
        },"async data callback context");
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

    public static void main(String[] args) throws InterruptedException, KeeperException {
        asyncGetData("/hadoop/node12",false);
        DataStat dataStat = getData("/hadoop/node12",false);
        System.out.println("Data msg : " +  dataStat.getData()  + " version : " + dataStat.getStat().getVersion() + ", data aversion : " + dataStat.getStat().getAversion());
    }

}
