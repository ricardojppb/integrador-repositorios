package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.dto.Projects;
import com.indra.repos.git.model.service.ProjectMongoService;
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
public class ProjectBatch {

    private static final int MAX_ERRORS = 5;
    private static final int TRANSACTION_SIZE = 1;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ProjectMongoService projectMongoService;

    @Bean
    public Job jobGitProject(JobCompletionNotificationListener listener, Step stepGitProject) {
        return jobBuilderFactory.get("jobGitProject")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .listener(new JobResultListener())
                .flow(stepGitProject).end().build();
    }

    @Bean
    public Step stepGitProject() {
        return stepBuilderFactory.get("stepGitProject").<Projects, Collection<Project>>chunk(TRANSACTION_SIZE)
                .reader(new ProjectItemReader())
                .processor(new ProjectItemProcessor())
                .writer(new ProjectItemWriter()).build();
    }

    public Tasklet tasklet() {
        return ((stepContribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        });
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

    /**
     *
     */
    public class ProjectItemReader implements ItemReader<Projects>, StepExecutionListener {

        @Override
        public Projects read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            log.info("ProjectItemReader.read execute.");
            Projects projects = projectMongoService.restGetProject();
            return projects;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("ProjectItemReader.read initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("ProjectItemReader.read ended.");
            return ExitStatus.COMPLETED;
        }
    }

    /**
     *
     */
    public class ProjectItemProcessor implements ItemProcessor<Projects, Collection<Project>> {

        @Override
        public Collection<Project> process(Projects projects) throws Exception {
            log.info("ProjectItemProcessor.process execute.");
            Collection<Project> projs = projectMongoService.mongoGetProjects(projects);
            return projs;
        }
    }

    /**
     *
     */
    public class ProjectItemWriter implements ItemWriter<Collection<Project>>, StepExecutionListener {

        @Override
        public void write(List<? extends Collection<Project>> projects) throws Exception {
            log.info("ProjectItemWriter.write execute.");
            projects.forEach(p -> {
                log.info("ProjectItemWriter.write execute: {}", p.toString());
                projectMongoService.saveAllProject(p);
            });
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            log.info("ProjectItemWriter.write initialized.");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            log.info("ProjectItemWriter.write ended.");
            return ExitStatus.COMPLETED;
        }
    }
}
