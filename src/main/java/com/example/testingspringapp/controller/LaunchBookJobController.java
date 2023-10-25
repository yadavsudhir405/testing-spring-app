package com.example.testingspringapp.controller;

import com.example.testingspringapp.service.JobLauncherService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("/launchBookJob")
@RequiredArgsConstructor
public class LaunchBookJobController {

    private final JobLauncherService jobLauncherService;
    private final Job loadBookJob;

    // method that launches job
    @PostMapping
    public void launchJob(@RequestParam("tenant") String tenant) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("tenant", tenant)
                .addLocalTime("time", LocalTime.now())
                .toJobParameters();
        this.jobLauncherService.launchJob(this.loadBookJob, jobParameters);
    }
}
