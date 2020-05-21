package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.mysql.Project;
import com.indra.repos.git.model.dto.mysql.Projects;
import com.indra.repos.git.model.service.ProjectService;
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
    private ProjectService projectService;

    private Projects projects = null;
    private Collection<Project> projectCollection = null;

    @Bean
    public Step stepGitBitbucketProjectReaderTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketProjectReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepProjectReaderTasklet execute.");
                projects = projectService.restGetProject();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketProjectProcessorTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketProjectProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepProjectProcessorTasklet execute.");
                projectCollection = projectService.mongoGetProjects(projects);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketProjectWriterTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketProjectWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepProjectWriterTasklet execute.");
                projectService.saveAllProject(projectCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobGitBitbucketProjectTasklet(JobCompletionNotificationListener listener,
                                             Step stepGitBitbucketProjectReaderTasklet,
                                             Step stepGitBitbucketProjectProcessorTasklet,
                                             Step stepGitBitbucketProjectWriterTasklet) {

        return jobBuilderFactory.get("jobGitBitbucketProjectTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketProjectBatch.JobResultListener())
                .start(stepGitBitbucketProjectReaderTasklet)
                .next(stepGitBitbucketProjectProcessorTasklet)
                .next(stepGitBitbucketProjectWriterTasklet).build();
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
