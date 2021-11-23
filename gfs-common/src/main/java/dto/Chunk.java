package dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Chunk implements Serializable {

    private static final long serialVersionUID = 7421916881162189998L;

    /**
     * chunk的id
     */
    private String chunkId;

    /**
     * chunk的备份服务器
     */
    private List<String> replicationServers;

    /**
     * chunk的hash
     */
    private Long hash;

    /**
     * chunk的大小
     */
    private int size;

    /**
     * chunk的文件名
     */
    private String filename;

}
