package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Repository;
import com.indra.repos.git.model.dto.Repositories;
import com.indra.repos.git.model.service.RepositoryMongoService;
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

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private RepositoryMongoService repositoryMongoService;

    Collection<Repositories> repositoriesCollection = null;
    Collection<Repository> repositoryCollection = null;


    @Bean
    public Step stepRepositoryReaderTasklet() {
        return this.stepBuilderFactory.get("stepRepositoryReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepRepositoryReaderTasklet execute.");
                repositoriesCollection = repositoryMongoService.restGetRepositories();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepRepositoryProcessorTasklet() {
        return this.stepBuilderFactory.get("stepRepositoryProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepRepositoryProcessorTasklet execute.");
                repositoryCollection = repositoryMongoService.mongoGetRepositories(repositoriesCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepRepositoryWriterTasklet() {
        return this.stepBuilderFactory.get("stepRepositoryWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepRepositoryWriterTasklet execute.");
                repositoryMongoService.saveAllRepositories(repositoryCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobRepositoryTasklet(JobCompletionNotificationListener listener,
                                    Step stepRepositoryReaderTasklet,
                                    Step stepRepositoryProcessorTasklet,
                                    Step stepRepositoryWriterTasklet) {

        return jobBuilderFactory.get("jobRepositoryTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketRepositoryBatch.JobResultListener())
                .start(stepRepositoryReaderTasklet)
                .next(stepRepositoryProcessorTasklet)
                .next(stepRepositoryWriterTasklet).build();
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
