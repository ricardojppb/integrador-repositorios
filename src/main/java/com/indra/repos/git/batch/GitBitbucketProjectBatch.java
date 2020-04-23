package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.dto.Projects;
import com.indra.repos.git.model.service.ProjectMongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Slf4j
@Configuration
public class GitBitbucketProjectBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ProjectMongoService projectMongoService;

    private Projects projects = null;
    private Collection<Project> projectCollection = null;

    @Bean
    public Step stepProjectReaderTasklet() {
        return this.stepBuilderFactory.get("stepProjectReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepProjectReaderTasklet execute.");
                projects = projectMongoService.restGetProject();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepProjectProcessorTasklet() {
        return this.stepBuilderFactory.get("stepProjectProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepProjectProcessorTasklet execute.");
                projectCollection = projectMongoService.mongoGetProjects(projects);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepProjectWriterTasklet() {
        return this.stepBuilderFactory.get("stepProjectWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepProjectWriterTasklet execute.");
                projectMongoService.saveAllProject(projectCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobProjectTasklet(JobCompletionNotificationListener listener,
                                 Step stepProjectReaderTasklet,
                                 Step stepProjectProcessorTasklet,
                                 Step stepProjectWriterTasklet) {

        return jobBuilderFactory.get("jobProjectTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketProjectBatch.JobResultListener())
                .start(stepProjectReaderTasklet)
                .next(stepProjectProcessorTasklet)
                .next(stepProjectWriterTasklet).build();
    }

    public class JobResultListener implements JobExecutionListener {

        @Override
        public void beforeJob(JobExecution jobExecution) {
            //log.info("{} - Projects beforeJob: execute", JobResultListener.class.getSimpleName());
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            //log.info("{} - Projects afterJob: execute", JobResultListener.class.getSimpleName());
        }
    }

}
