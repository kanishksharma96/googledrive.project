package com.assignment.gdrive.plugin.drive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriveWebhook {

    @GetMapping("/watchData")
    public String gWatchData(@RequestBody String activityDetails) {
        System.out.println(activityDetails);
        return "xyz";
    }

    @PostMapping("/watchData")
    public String pWatchData(@RequestBody String activityDetails) {
        System.out.println(activityDetails);
        return "xyz";
    }
}
