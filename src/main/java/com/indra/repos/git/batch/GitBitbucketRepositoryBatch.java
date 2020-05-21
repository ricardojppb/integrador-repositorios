package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.mysql.Repository;
import com.indra.repos.git.model.dto.mysql.Repositories;
import com.indra.repos.git.model.service.RepositoryService;
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
public class GitBitbucketRepositoryBatch {

    Collection<Repositories> repositoriesCollection = null;
    Collection<Repository> repositoryCollection = null;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private RepositoryService repositoryService;

    @Bean
    public Step stepGitBitbucketRepositoryReaderTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketRepositoryReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepRepositoryReaderTasklet execute.");
                repositoriesCollection = repositoryService.restGetRepositories();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketRepositoryProcessorTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketRepositoryProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepRepositoryProcessorTasklet execute.");
                repositoryCollection = repositoryService.mongoGetRepositories(repositoriesCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketRepositoryWriterTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketRepositoryWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepRepositoryWriterTasklet execute.");
                repositoryService.saveAllRepositories(repositoryCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobGitBitbucketRepositoryTasklet(JobCompletionNotificationListener listener,
                                                Step stepGitBitbucketRepositoryReaderTasklet,
                                                Step stepGitBitbucketRepositoryProcessorTasklet,
                                                Step stepGitBitbucketRepositoryWriterTasklet) {

        return jobBuilderFactory.get("jobGitBitbucketRepositoryTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketRepositoryBatch.JobResultListener())
                .start(stepGitBitbucketRepositoryReaderTasklet)
                .next(stepGitBitbucketRepositoryProcessorTasklet)
                .next(stepGitBitbucketRepositoryWriterTasklet).build();
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
