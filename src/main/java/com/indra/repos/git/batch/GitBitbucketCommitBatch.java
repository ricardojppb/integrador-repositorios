package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.mysql.Commit;
import com.indra.repos.git.model.dto.mysql.Commits;
import com.indra.repos.git.model.service.CommitService;
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

    Collection<Commits> commitsCollection = null;
    Collection<Commit> commitCollection = null;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private CommitService commitService;

    @Bean
    public Step stepGitBitbucketCommitReaderTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketCommitReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepCommitReaderTasklet execute.");
                commitsCollection = commitService.restGetCommits();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketCommitProcessorTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketCommitProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepCommitProcessorTasklet execute.");
                commitCollection = commitService.mongoGetCommits(commitsCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketCommitWriterTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketCommitWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepCommitWriterTasklet execute.");
                commitService.saveAllCommits(commitCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobGitBitbucketCommitTasklet(JobCompletionNotificationListener listener,
                                            Step stepGitBitbucketCommitReaderTasklet,
                                            Step stepGitBitbucketCommitProcessorTasklet,
                                            Step stepGitBitbucketCommitWriterTasklet) {

        return jobBuilderFactory.get("jobGitBitbucketCommitTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketCommitBatch.JobResultListener())
                .start(stepGitBitbucketCommitReaderTasklet)
                .next(stepGitBitbucketCommitProcessorTasklet)
                .next(stepGitBitbucketCommitWriterTasklet).build();
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
