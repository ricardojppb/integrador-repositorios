package com.indra.repos.git.batch;

import com.indra.repos.git.model.domain.Project;
import com.indra.repos.git.model.dto.Projects;
import com.indra.repos.git.model.service.ProjectMongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
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
public class JobProjectBatch {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ProjectMongoService projectMongoService;

    private static final int MAX_ERRORS = 5;

    private static final int TRANSACTION_SIZE = 20;

    @Bean
    public Job jobProject() {
        return jobBuilderFactory.get("jobProject").incrementer(new RunIdIncrementer())
                .start(stepProject()).build();
    }

    @Bean
    public Step stepProject() {
        return stepBuilderFactory.get("stepProject").<Projects, Collection<Project>>chunk(TRANSACTION_SIZE)
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
    public class ProjectItemReader implements ItemReader<Projects> {

        @Override
        public Projects read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            Projects projects = projectMongoService.restGetProject();
            log.info("{} - Projects read: {} ", ProjectItemReader.class.getSimpleName(), projects);
            return projects;
        }
    }

    /**
     *
     */
    public class ProjectItemProcessor implements ItemProcessor<Projects, Collection<Project>> {

        @Override
        public Collection<Project> process(Projects projects) throws Exception {

            Collection<Project> collection = projectMongoService.mongoGetProjects(projects);
            log.info("{} - Projects process: {} ", ProjectItemProcessor.class.getSimpleName(), collection);
            return collection;
        }
    }

    /**
     *
     */
    public class ProjectItemWriter implements ItemWriter<Collection<Project>> {


        @Override
        public void write(List<? extends Collection<Project>> list) throws Exception {
            list.forEach(l -> {
                log.info("{} - Projects write: {} ", ProjectItemWriter.class.getSimpleName(), l);
                projectMongoService.saveAllProject(l);
            });
        }
    }
}
