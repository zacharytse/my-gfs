package com.xcq.dto;

import java.io.Serializable;

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
public class ServerInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String ip;
    private Integer port;

}
