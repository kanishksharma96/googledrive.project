package com.assignment.gdrive.plugin.drive.service;


import com.google.api.services.driveactivity.v2.model.DriveActivity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Slf4j
public class DriveActivityProcessor {

    public static final String TIMESTAMP = "timestamp";
    @Value("${google.drive.processor.batch.size:10}")
    private String BATCH_SIZE;

    public String processLatestActivities(List<DriveActivity> activites, String existingLastTs) throws ParseException {
        if (activites.size() < Integer.parseInt(BATCH_SIZE)) {
            return existingLastTs;
        }

        activites.stream().sorted((activity1, activity2) -> {
            try {
                Long epoch1 = convertUtcToEpoch((String) activity1.get(TIMESTAMP));
                Long epoch2 = convertUtcToEpoch((String) activity2.get(TIMESTAMP));
                return epoch1.compareTo(epoch2);
            } catch (Exception e) {
                log.error("Couldn't parse timestamp");
            }
            return 0;
        });

        publishBatchToKafka(activites);

        Long epoch = convertUtcToEpoch((String) activites.get(0).get(TIMESTAMP));
        return epoch.toString();
    }

    private Long convertUtcToEpoch(String ts) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = df.parse(ts);
        long epoch = date.getTime();
        return epoch;
    }


    private Boolean publishBatchToKafka(List<DriveActivity> batchOfDriveActivities) {
        //TODO kafka publisher
        return true;
    }

    private Long convertRfc3339ToEpoch(String timestamp) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timestamp).getTime();
    }
}
