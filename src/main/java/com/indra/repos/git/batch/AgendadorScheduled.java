package com.indra.repos.git.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AgendadorScheduled {

    private final long MILESEGUNDO = 1;
    private final long SEGUNDO = MILESEGUNDO * 1000;
    private final long MINUTO = SEGUNDO * 60;
    private final long HORA = MINUTO * 60;

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    ApplicationContext context;


    //@Scheduled(fixedDelay = HORA * 12, initialDelay = MINUTO)
    public void agendadorJobProject() {
        executarBatch("jobGitProject");
    }

    //@Scheduled(fixedDelay = MINUTO * 30, initialDelay = MINUTO)
    public void agendadorJobRepository() {
        executarBatch("jobGitRepository");
    }

    //@Scheduled(fixedDelay = HORA * 12, initialDelay = MINUTO)
    public void agendadorJobBranche() {
        executarBatch("jobGitBranche");
    }

    //@Scheduled(fixedDelay = MINUTO * 30, initialDelay = MINUTO)
    public void agendadorJobCommit() {
        executarBatch("jobGitCommit");
    }

    public void start() {

    }

    public void executarBatch(String jobId) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("ID", System.currentTimeMillis()).toJobParameters();
            Job job = (Job) context.getBean(jobId);
            jobLauncher.run(job, params);
        } catch (Exception e) {
            log.info("ocorreu um erro ao tentar iniciar o Batch {} ", jobId, e);
        }
    }

}
