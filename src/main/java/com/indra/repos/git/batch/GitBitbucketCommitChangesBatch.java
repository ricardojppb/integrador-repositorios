package com.indra.repos.git.batch;

@lombok.extern.slf4j.Slf4j
@org.springframework.context.annotation.Configuration
public class GitBitbucketCommitChangesBatch {

    java.util.Collection<com.indra.repos.git.model.dto.mysql.CommitChanges> commitChangesCollection = null;
    java.util.Collection<com.indra.repos.git.model.domain.mysql.CommitChange> commitChangeCollection = null;
    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.batch.core.configuration.annotation.JobBuilderFactory jobBuilderFactory;
    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.batch.core.configuration.annotation.StepBuilderFactory stepBuilderFactory;
    @org.springframework.beans.factory.annotation.Autowired
    private com.indra.repos.git.model.service.CommitChangeService commitChangeService;

    @org.springframework.context.annotation.Bean
    public org.springframework.batch.core.Step stepGitBitbucketCommitChangeReaderTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketCommitChangeReaderTasklet").tasklet(new org.springframework.batch.core.step.tasklet.Tasklet() {
            @Override
            public org.springframework.batch.repeat.RepeatStatus execute(org.springframework.batch.core.StepContribution stepContribution,
                                                                         org.springframework.batch.core.scope.context.ChunkContext chunkContext) throws Exception {
                log.info("stepGitBitbucketCommitChangeReaderTasklet execute.");
                commitChangesCollection = commitChangeService.restGetCommitChanges();
                return org.springframework.batch.repeat.RepeatStatus.FINISHED;
            }
        }).build();
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.batch.core.Step stepGitBitbucketCommitChangeProcessorTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketCommitChangeProcessorTasklet").tasklet(new org.springframework.batch.core.step.tasklet.Tasklet() {
            @Override
            public org.springframework.batch.repeat.RepeatStatus execute(org.springframework.batch.core.StepContribution stepContribution,
                                                                         org.springframework.batch.core.scope.context.ChunkContext chunkContext) throws Exception {
                log.info("stepGitBitbucketCommitChangeProcessorTasklet execute.");
                commitChangeCollection = commitChangeService.findCommitChanges(commitChangesCollection);

                return org.springframework.batch.repeat.RepeatStatus.FINISHED;
            }
        }).build();
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.batch.core.Step stepGitBitbucketCommitChangeWriterTasklet() {
        return this.stepBuilderFactory.get("stepGitBitbucketCommitChangeWriterTasklet").tasklet(new org.springframework.batch.core.step.tasklet.Tasklet() {
            @Override
            public org.springframework.batch.repeat.RepeatStatus execute(org.springframework.batch.core.StepContribution stepContribution,
                                                                         org.springframework.batch.core.scope.context.ChunkContext chunkContext) throws Exception {
                log.info("stepGitBitbucketCommitChangeWriterTasklet execute.");
                commitChangeService.saveAllCommitChanges(commitChangeCollection);

                return org.springframework.batch.repeat.RepeatStatus.FINISHED;
            }
        }).build();
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.batch.core.Job jobGitBitbucketCommitChangeTasklet(JobCompletionNotificationListener listener,
                                                                                 org.springframework.batch.core.Step stepGitBitbucketCommitChangeReaderTasklet,
                                                                                 org.springframework.batch.core.Step stepGitBitbucketCommitChangeProcessorTasklet,
                                                                                 org.springframework.batch.core.Step stepGitBitbucketCommitChangeWriterTasklet) {

        return jobBuilderFactory.get("jobGitBitbucketCommitChangeTasklet")
                .incrementer(new org.springframework.batch.core.launch.support.RunIdIncrementer())
                .listener(listener)
                .listener(new GitBitbucketCommitChangesBatch.JobResultListener())
                .start(stepGitBitbucketCommitChangeReaderTasklet)
                .next(stepGitBitbucketCommitChangeProcessorTasklet)
                .next(stepGitBitbucketCommitChangeWriterTasklet).build();
    }

    public class JobResultListener implements org.springframework.batch.core.JobExecutionListener {

        @Override
        public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
            //log.info("{} - Projects beforeJob: execute", JobResultListener.class.getSimpleName());
        }

        @Override
        public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
            //log.info("{} - Projects afterJob: execute", JobResultListener.class.getSimpleName());
        }
    }

}
