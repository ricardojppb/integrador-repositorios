package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Commit;
import com.indra.repos.git.model.dto.Commits;
import com.indra.repos.git.model.service.CommitMongoService;
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
public class GitBitbucketCommitBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private CommitMongoService commitMongoService;

    Collection<Commits> commitsCollection = null;
    Collection<Commit> commitCollection = null;

    @Bean
    public Step stepCommitReaderTasklet() {
        return this.stepBuilderFactory.get("stepCommitReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepCommitReaderTasklet execute.");
                commitsCollection = commitMongoService.restGetCommits();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepCommitProcessorTasklet() {
        return this.stepBuilderFactory.get("stepCommitProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepCommitProcessorTasklet execute.");
                commitCollection = commitMongoService.mongoGetCommits(commitsCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepCommitWriterTasklet() {
        return this.stepBuilderFactory.get("stepCommitWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepCommitWriterTasklet execute.");
                commitMongoService.saveAllCommits(commitCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobCommitTasklet(JobCompletionNotificationListener listener,
                                Step stepCommitReaderTasklet,
                                Step stepCommitProcessorTasklet,
                                Step stepCommitWriterTasklet) {

        return jobBuilderFactory.get("jobCommitTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketCommitBatch.JobResultListener())
                .start(stepCommitReaderTasklet)
                .next(stepCommitProcessorTasklet)
                .next(stepCommitWriterTasklet).build();
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
