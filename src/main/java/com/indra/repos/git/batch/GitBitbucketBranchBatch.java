package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Branche;
import com.indra.repos.git.model.dto.Branches;
import com.indra.repos.git.model.service.BranchMongoService;
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

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BranchMongoService branchMongoService;

    Collection<Branches> branchesCollection = null;
    Collection<Branche> brancheCollection = null;


    @Bean
    public Step stepBranchReaderTasklet() {
        return this.stepBuilderFactory.get("stepBranchReaderTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepBranchReaderTasklet execute.");
                branchesCollection = branchMongoService.restGetBranches();
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepBranchProcessorTasklet() {
        return this.stepBuilderFactory.get("stepBranchProcessorTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepBranchProcessorTasklet execute.");
                brancheCollection = branchMongoService.mongoGetBranches(branchesCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepBranchWriterTasklet() {
        return this.stepBuilderFactory.get("stepBranchWriterTasklet").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution,
                                        ChunkContext chunkContext) throws Exception {
                log.info("stepBranchWriterTasklet execute.");
                branchMongoService.saveAllBranches(brancheCollection);

                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Job jobBranchTasklet(JobCompletionNotificationListener listener,
                                Step stepBranchReaderTasklet,
                                Step stepBranchProcessorTasklet,
                                Step stepBranchWriterTasklet) {

        return jobBuilderFactory.get("jobBranchTasklet")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketBranchBatch.JobResultListener())
                .start(stepBranchReaderTasklet)
                .next(stepBranchProcessorTasklet)
                .next(stepBranchWriterTasklet).build();
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
