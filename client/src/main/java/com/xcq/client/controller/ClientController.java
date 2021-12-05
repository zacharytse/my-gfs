package com.xcq.client.controller;

import com.xcq.client.service.IFileService1;
import com.xcq.client.service.IOperationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/index")
@Log4j2
public class ClientController {
    @Autowired
    private IFileService1 fileService;

    @Autowired
    private IOperationService operationService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("fileList", fileService.getFileList("test"));
        return "index";
    }

    @GetMapping("{filename}/download")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        operationService.download("D:\\cotton3.txt", response);
    }

    @GetMapping("/pre")
    public String getPreFile(Model model) {
        return "index";
    }

    @GetMapping("/list")
    public String getFileList(String parent, Model model) {
        return "index";
    }
}
