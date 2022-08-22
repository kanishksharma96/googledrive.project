package com.assignment.gdrive.plugin.drive.controller;

import com.assignment.gdrive.plugin.drive.service.ActivityMonitorScheduler;
import com.assignment.gdrive.plugin.drive.service.DriveActivityService;
import com.google.api.services.driveactivity.v2.model.DriveActivity;
import com.google.api.services.driveactivity.v2.model.QueryDriveActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
public class PollController {

    private String latestEpochTs = String.valueOf(Instant.now().toEpochMilli());

    @Autowired
    private ActivityMonitorScheduler activityMonitorScheduler;

    @PostMapping("/start")
    public String startPolling() throws Exception {
        if(activityMonitorScheduler.startPolling()) {
            return "Started polling";
        }
        else throw new Exception("Couldn't start polling");
    }

    @PostMapping("/stop")
    public String stopPolling() throws Exception {
        if(activityMonitorScheduler.stopPolling()) {
            return "Stopped polling";
        }
        else throw new Exception("Couldn't stop polling, please kill the app");
    }

    @Autowired
    private DriveActivityService driveActivityService;
    @PostMapping("/test")
    public List<DriveActivity> test() throws IOException {
        return driveActivityService.queryOnDrive(buildQueryDriveAcitivityRequest());
    }

    private QueryDriveActivityRequest buildQueryDriveAcitivityRequest() {
        QueryDriveActivityRequest driveActivityRequest = new QueryDriveActivityRequest();
        driveActivityRequest.setAncestorName("items/19f1MCEty9_mdDhR0B_IwVLcLYuk3vHVH");
        driveActivityRequest.setPageSize(20);
        driveActivityRequest.setFilter(buildQueryFilter());
        return driveActivityRequest;
    }

    private String buildQueryFilter() {
        return String.format("time > %s AND detail.action_detail_case:(CREATE)", latestEpochTs);
    }
}
