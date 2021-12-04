package com.xcq.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServerInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String ip;
    private Integer port;

    public static ServerInfo parseServerStr(String server) {
        String[] parts = server.split(":");
        if (parts.length != 2) {
            return null;
        }
        ServerInfo info = ServerInfo.builder().ip(parts[0]).port(Integer.valueOf(parts[1])).build();
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerInfo)) return false;
        ServerInfo that = (ServerInfo) o;
        return Objects.equals(ip, that.ip) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
