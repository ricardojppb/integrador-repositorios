package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Repository;
import com.indra.repos.git.model.dto.Repositories;
import com.indra.repos.git.model.service.RepositoryMongoService;
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
public class RepositoryBatch {

    private static final int MAX_ERRORS = 5;
    private static final int TRANSACTION_SIZE = 1;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private RepositoryMongoService repositoryMongoService;

    @Bean
    public Job jobGitRepository(JobCompletionNotificationListener listener, Step stepGitRepository) {
        return jobBuilderFactory.get("jobGitRepository")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new JobResultListener())
                .flow(stepGitRepository).end().build();
    }

    @Bean
    public Step stepGitRepository() {
        return stepBuilderFactory.get("stepGitRepository")
                .<Collection<Repositories>, Collection<Repository>>chunk(TRANSACTION_SIZE)
                .reader(new RepositoryItemReader())
                .processor(new RepositoryItemProcessor())
                .writer(new RepositoryItemWriter()).build();
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
    public class RepositoryItemReader implements ItemReader<Collection<Repositories>>, StepExecutionListener {

        @Override
        public Collection<Repositories> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            log.info("RepositoryItemReader.read execute.");
            Collection<Repositories> repositories = repositoryMongoService.restGetRepositories();
            return repositories;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("RepositoryItemReader.read initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("RepositoryItemReader.read ended.");
            return ExitStatus.COMPLETED;
        }
    }

    /**
     *
     */
    public class RepositoryItemProcessor implements ItemProcessor<Collection<Repositories>, Collection<Repository>> {

        @Override
        public Collection<Repository> process(Collection<Repositories> repositories) throws Exception {
            log.info("RepositoryItemProcessor.process execute.");
            Collection<Repository> repos = repositoryMongoService.mongoGetRepositories(repositories);
            return repos;
        }
    }

    /**
     *
     */
    public class RepositoryItemWriter implements ItemWriter<Collection<Repository>>, StepExecutionListener {

        @Override
        public void write(List<? extends Collection<Repository>> repositories) throws Exception {
            log.info("RepositoryItemWriter.write execute.");
            repositories.forEach(r -> {
                log.info("RepositoryItemWriter.write execute: {}", r.toString());
                repositoryMongoService.saveAllRepositories(r);
            });
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("RepositoryItemWriter.write initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("RepositoryItemWriter.write ended.");
            return ExitStatus.COMPLETED;
        }
    }
}
