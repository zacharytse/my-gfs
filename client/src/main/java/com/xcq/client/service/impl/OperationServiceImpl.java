package com.xcq.client.service.impl;

import com.xcq.client.service.IOperationService;
import com.xcq.common.dto.Chunk;
import com.xcq.common.remote.IMasterRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationServiceImpl implements IOperationService {
    @DubboReference
    private IMasterRpc masterRpc;

    @Override
    public void upload(String filename) {

    }

    @Override
    public void download(String filename) {

    }

    @Override
    public void append(String filename, List<Chunk> chunkList) {

    }
}
