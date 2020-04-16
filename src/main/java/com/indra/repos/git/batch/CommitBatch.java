package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Commit;
import com.indra.repos.git.model.dto.Commits;
import com.indra.repos.git.model.service.CommitMongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

@Slf4j
@Configuration
public class CommitBatch {

    private static final int MAX_ERRORS = 5;
    private static final int TRANSACTION_SIZE = 1;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private CommitMongoService commitMongoService;

    @Bean
    public Job jobGitCommit(JobCompletionNotificationListener listener, Step stepGitCommit) {
        return jobBuilderFactory.get("jobGitCommit")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new JobResultListener())
                .flow(stepGitCommit).end().build();
    }

    @Bean
    public Step stepGitCommit() {
        return stepBuilderFactory.get("stepGitCommit")
                .<Collection<Commits>, Collection<Commit>>chunk(TRANSACTION_SIZE)
                .reader(new CommitItemReader())
                .processor(new CompositeItemProcessor())
                .writer(new CompositeItemWriter()).build();
    }

    public Tasklet tasklet() {
        return ((stepContribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        });
    }

    public class JobResultListener implements JobExecutionListener {

        @Override
        public void beforeJob(JobExecution jobExecution) {
            log.info("{} - Projects beforeJob: execute", JobResultListener.class.getSimpleName());
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            log.info("{} - Projects afterJob: execute", JobResultListener.class.getSimpleName());
        }
    }

    /**
     *
     */
    public class CommitItemReader implements ItemReader<Collection<Commits>>, StepExecutionListener {

        @Override
        public Collection<Commits> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            log.info("CommitItemReader.read execute.");
            return commitMongoService.restGetCommits();

        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("CommitItemReader.read initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("CommitItemReader.read ended.");
            return ExitStatus.COMPLETED;
        }
    }

    /**
     *
     */
    public class CommitItemProcessor implements ItemProcessor<Collection<Commits>, Collection<Commit>> {

        @Override
        public Collection<Commit> process(Collection<Commits> commits) throws Exception {
            log.info("CommitItemProcessor.process execute.");
            return commitMongoService.mongoGetCommits(commits);

        }
    }

    /**
     *
     */
    public class CommitItemWriter implements ItemWriter<Collection<Commit>>, StepExecutionListener {

        @Override
        public void write(List<? extends Collection<Commit>> commits) throws Exception {
            log.info("CommitItemWriter.write execute.");
            commits.forEach(commit -> {
                log.info("CommitItemWriter.write execute: {}", commit.toString());
                commitMongoService.saveAllCommits(commit);
            });
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("CommitItemWriter.write initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("CommitItemWriter.write ended.");
            return ExitStatus.COMPLETED;
        }
    }
}
