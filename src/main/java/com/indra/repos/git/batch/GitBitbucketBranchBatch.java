package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.mysql.Branch;
import com.indra.repos.git.model.dto.mysql.Branches;
import com.indra.repos.git.model.service.BranchService;
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
public class GitBitbucketBranchBatch {

    Collection<Branches> branchesCollection = null;
    Collection<Branch> branchCollection = null;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BranchService branchService;


    @Bean
    public Step stepGitBitbucketBranchReaderTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketBranchReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepBranchReaderTasklet execute.");
                branchesCollection = branchService.restGetBranches();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketBranchProcessorTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketBranchProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepBranchProcessorTasklet execute.");
                branchCollection = branchService.mongoGetBranches(branchesCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepGitBitbucketBranchWriterTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketBranchWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepBranchWriterTasklet execute.");
                branchService.saveAllBranches(branchCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobGitBitbucketBranchTasklet(JobCompletionNotificationListener listener,
                                            Step stepGitBitbucketBranchReaderTasklet,
                                            Step stepGitBitbucketBranchProcessorTasklet,
                                            Step stepGitBitbucketBranchWriterTasklet) {

        return jobBuilderFactory.get("jobGitBitbucketBranchTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketBranchBatch.JobResultListener())
                .start(stepGitBitbucketBranchReaderTasklet)
                .next(stepGitBitbucketBranchProcessorTasklet)
                .next(stepGitBitbucketBranchWriterTasklet).build();
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
