package com.xcq.gfsmaster;

import com.xcq.gfsmaster.service.impl.FileSystem;
import dto.Chunk;
import dto.ChunkInfo;
import entity.NameNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = GfsMasterApplication.class)
@RunWith(SpringRunner.class)
public class GfsMasterApplicationTests {

    @Autowired
    private FileSystem fileSystem;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAddNameNode() {
        fileSystem.addNameNode("/test/123");
        fileSystem.addNameNode("/test/123");
        fileSystem.addNameNode("/test1/123");
        fileSystem.addNameNode("test/123");
    }

    @Test
    public void testGetNameNode() {
        fileSystem.addNameNode("/test/123");
        NameNode node = fileSystem.getNameNode("/test/123");
        if(node == null) {
            System.out.println("node is null");
        } else {
            System.out.println(node.getFilename());
        }
    }

    @Test
    public void testDeleteNameNode() {
        fileSystem.addNameNode("/test/123");
        if(fileSystem.deleteNameNode("/test/123")){
            System.out.println("delete success");
        } else {
            System.out.println("delete fail");
        }
        fileSystem.deleteNameNode("/test");
        fileSystem.deleteNameNode("/test");
    }

    @Test
    public void testUpdateNameNode() {
        fileSystem.addNameNode("/test/123");
        ChunkInfo chunkInfo = ChunkInfo.builder().filename("/test/1234").size(100l).build();
        fileSystem.updateNameNode("/test/123",chunkInfo);
    }
}
