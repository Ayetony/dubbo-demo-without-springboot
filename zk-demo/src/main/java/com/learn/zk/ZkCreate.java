package com.learn.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ZkCreate {

    public String createNode(String path, String data, ArrayList<ACL> acl) throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper disconnected error");
        }
        String nodeResp = zooKeeper.create(path,data.getBytes(StandardCharsets.UTF_8), acl, CreateMode.PERSISTENT);
        return nodeResp;
    }

    public void asyncCreateNode(String path, String data, ArrayList<ACL> acl) throws InterruptedException {
        ZooKeeper zooKeeper = ZKConn.getZkClientConnection();
        if(zooKeeper == null){
            throw new RuntimeException("zookeeper connect error");
        }
        zooKeeper.create(path, data.getBytes(StandardCharsets.UTF_8), acl, CreateMode.PERSISTENT,
                (rc, ctxPath, ctx, name) -> System.out.println("rc:"+rc+",path:"+ ctxPath +",ctx:"+ctx+"name,"+name),
                "async context");
        Thread.sleep(1000);// need wait
    }


    public static void main(String[] args) throws InterruptedException, KeeperException {

//        ZkCreate creator = new ZkCreate();
//        String resp = creator.createNode("/hadoop/node1","node1 to create", ZooDefs.Ids.OPEN_ACL_UNSAFE);
//        System.out.println(resp);

        //[zk: localhost(CONNECTED) 4] getAcl /hadoop/node1
        //'world,'anyone
        //: cdrwa

        ZkCreate creator = new ZkCreate();
        creator.asyncCreateNode("/hadoop/node12","node12 to create", ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

}
