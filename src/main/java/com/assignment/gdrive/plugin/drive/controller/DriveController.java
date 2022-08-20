package com.assignment.gdrive.plugin.drive.controller;

import com.assignment.gdrive.plugin.drive.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriveController {

    @Autowired
    private DriveService driveService;

    @PostMapping
    public String getListOfFiles() {
        return "xx";
    }
}
