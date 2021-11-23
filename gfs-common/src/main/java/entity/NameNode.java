package entity;

import dto.ChunkInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    // 当前节点的孩子节点
    @Builder.Default
    List<NameNode> children = new ArrayList<>();
    // 加前驱目录向上查找可能会比较方便
    NameNode parent;

    /**
     * 添加孩子节点
     *
     * @param node
     */
    public void addChildren(NameNode node) {
        children.add(node);
        updateChunkSize(node,1);
    }

    public void removeChildren(NameNode node) {
        children.remove(node);
        updateChunkSize(node,-1);
    }

    /**
     * 更新文件大小
     * op为1增加文件，为-1删除文件
     * @param node
     */
    private void updateChunkSize(NameNode node,int op) {
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
