package com.indra.repos.git.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    //private final MongoTemplate mongoTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("JOB {} CONCLUIDO", jobExecution.getJobInstance().getJobName());
        } else {
            log.info("Job: {}, Status: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus().toString());
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job: {}, Status: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus().toString());
    }
}
