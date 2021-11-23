package remote;

import dto.Chunk;
import dto.ChunkInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * rpc发送的消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request implements Serializable {
    private static final long serialVersionUID = -4753666713171127424L;

    /**
     * 请求的id
     */
    private String reqId;
    private Long seq;
    /**
     * 消息体
     */
    Map<String,Object> map;
}
