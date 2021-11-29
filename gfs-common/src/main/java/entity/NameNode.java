package entity;

import java.util.HashMap;
import java.util.Map;

import dto.ChunkInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NameNode {
    // 对应的文件名
    private String filename;
    // chunk的信息
    @Builder.Default
    private ChunkInfo chunkInfo = new ChunkInfo();

    @Builder.Default
    Map<String, NameNode> children = new HashMap<String, NameNode>();

    // 加前驱目录向上查找可能会比较方便
    NameNode parent;

    /**
     * 添加孩子节点
     *
     * @param node
     */
    public boolean addChildren(NameNode node) {
        if (containsChild(node.filename)) {
            System.out.println("重复的孩子");
            return false;
        }
        children.put(node.filename, node);
        node.parent = this;
        updateChunkSize(node, 1);
        return true;
    }

    public NameNode addChildren(String filename) {
        NameNode node = NameNode.builder().filename(filename).build();
        if (addChildren(node)) {
            return node;
        } else {
            return null;
        }
    }

    public NameNode addChildren(String filename, ChunkInfo chunkInfo) {
        NameNode node = NameNode.builder().filename(filename).chunkInfo(chunkInfo).build();
        if (addChildren(node)) {
            return node;
        } else {
            return null;
        }
    }

    public NameNode getChild(String name) {
        return children.get(name);
    }

    public boolean containsChild(String filename) {
        return children.containsKey(filename);
    }

    public boolean removeChildren(NameNode node) {
        if (!containsChild(node.filename)) {
            System.out.println("不存在的孩子");
            return false;
        }
        children.remove(node.filename);
        updateChunkSize(node, -1);
        return true;
    }

    /**
     * 删除自己
     */
    public boolean removeSelf() {
        return parent.removeChildren(this);
    }

    /**
     * 更新文件大小
     * op为1增加文件，为-1删除文件
     * 
     * @param node
     */
    private void updateChunkSize(NameNode node, int op) {
        NameNode pre = this;
        NameNode cur = node;
        long size = cur.chunkInfo.getSize();
        while (pre != null) {
            pre.chunkInfo.setSize(pre.chunkInfo.getSize() + op * size);
            pre = pre.parent;
            cur = cur.parent;
        }
    }
}
