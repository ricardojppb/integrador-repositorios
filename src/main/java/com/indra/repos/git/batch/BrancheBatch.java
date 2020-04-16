package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Branche;
import com.indra.repos.git.model.dto.Branches;
import com.indra.repos.git.model.service.BranchMongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

@Slf4j
@Configuration
public class BrancheBatch {

    private static final int MAX_ERRORS = 5;
    private static final int TRANSACTION_SIZE = 1;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BranchMongoService branchMongoService;

    @Bean
    public Job jobGitBranche(JobCompletionNotificationListener listener, Step stepGitBranche) {
        return jobBuilderFactory.get("jobGitBranch")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new JobResultListener())
                .flow(stepGitBranche).end().build();
    }

    @Bean
    public Step stepGitBranche() {
        return stepBuilderFactory.get("stepGitBranche")
                .<Collection<Branches>, Collection<Branche>>chunk(TRANSACTION_SIZE)
                .reader(new BrancheItemReader())
                .processor(new BrancheItemProcessor())
                .writer(new BrancheItemWriter()).build();
    }

    public Tasklet tasklet() {
        return ((stepContribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        });
    }

    public class JobResultListener implements JobExecutionListener {

        @Override
        public void beforeJob(JobExecution jobExecution) {
            log.info("{} - JobResultListener Branch beforeJob: execute");
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            log.info("{} - JobResultListener Branch afterJob: execute");
        }
    }

    /**
     *
     */
    public class BrancheItemReader implements ItemReader<Collection<Branches>>, StepExecutionListener {

        @Override
        public Collection<Branches> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            log.info("BrancheItemReader.read execute.");
            return branchMongoService.restGetBranches();

        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("BrancheItemReader.read initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("BrancheItemReader.read ended.");
            return ExitStatus.COMPLETED;
        }
    }

    /**
     *
     */
    public class BrancheItemProcessor implements ItemProcessor<Collection<Branches>, Collection<Branche>> {

        @Override
        public Collection<Branche> process(Collection<Branches> branches) throws Exception {
            log.info("BrancheItemProcessor.process execute.");
            return branchMongoService.mongoGetBranches(branches);
        }
    }

    /**
     *
     */
    public class BrancheItemWriter implements ItemWriter<Collection<Branche>>, StepExecutionListener {

        @Override
        public void write(List<? extends Collection<Branche>> branches) throws Exception {
            log.info("BrancheItemWriter.write execute.");
            branches.forEach(branche -> {
                log.info("BrancheItemWriter.write execute: {}", branche.toString());
                branchMongoService.saveAllBranches(branche);
            });
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("BrancheItemWriter.write initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("BrancheItemWriter.write ended.");
            return ExitStatus.COMPLETED;
        }
    }
}
