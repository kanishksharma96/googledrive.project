package com.assignment.gdrive.plugin.drive.service;

import com.google.api.services.driveactivity.v2.model.DriveActivity;
import com.google.api.services.driveactivity.v2.model.QueryDriveActivityRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ActivityMonitorScheduler {

    @Value("${google.drive.poll.interval.ms}")
    private String POLL_INTERVAL;

    private final DriveActivityProcessor driveActivityProcessor;

    private final TaskScheduler taskScheduler;

    private final DriveActivityService driveActivityService;

    private ScheduledFuture<?> scheduledFuture;

    private static String latestEpoch;

    @PostConstruct
    public void init() {
        latestEpoch = String.valueOf(Instant.now().toEpochMilli());
    }

    public boolean startPolling() {
        this.scheduledFuture = taskScheduler.scheduleAtFixedRate(monitorDriveActivity(), Long.parseLong(POLL_INTERVAL));
        return true;
    }

    public boolean stopPolling() {
        return scheduledFuture.cancel(false);
    }

    private Runnable monitorDriveActivity() throws RuntimeException {
        return this::processDriveActivity;
    }

    private QueryDriveActivityRequest buildQueryDriveAcitivityRequest() {
        QueryDriveActivityRequest driveActivityRequest = new QueryDriveActivityRequest();
        driveActivityRequest.setAncestorName("items/19f1MCEty9_mdDhR0B_IwVLcLYuk3vHVH");
        driveActivityRequest.setPageSize(100);
        driveActivityRequest.setFilter(buildQueryFilter());
        return driveActivityRequest;
    }

    private String buildQueryFilter() {
        System.out.println(new Date(Long.parseLong(latestEpoch) * 1000));
        return String.format("time < %s AND detail.action_detail_case:(CREATE)", latestEpoch);
    }

    private void processDriveActivity() {
        try {
            List<DriveActivity> activities = driveActivityService.queryOnDrive(buildQueryDriveAcitivityRequest());
            if (activities != null && activities.size() != 0) {
                String latestTs = driveActivityProcessor.processLatestActivities(activities, latestEpoch);
                if (latestTs != null) {
                    this.latestEpoch = latestTs;
                }
            }
        } catch (Exception e) {
            log.error("Error with drive activity monitoring");
        }
    }

}
