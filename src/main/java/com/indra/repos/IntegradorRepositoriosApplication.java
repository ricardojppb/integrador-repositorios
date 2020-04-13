package com.indra.repos;


import com.indra.repos.git.batch.JobProjectBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ConfigurationPropertiesScan("com.indra.repos.git.model.properties")
@EnableBatchProcessing
@EnableCaching
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {"com.indra.repos"})
//@EnableConfigurationProperties(GitProperties.class)
public class IntegradorRepositoriosApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobProjectBatch jobProjectBatch;

    public static void main(String[] args) {
        SpringApplication.run(IntegradorRepositoriosApplication.class, args);
    }

    @Override

    public void run(String... args) {


    }

    @Scheduled(fixedRate = 60000)
    public void run() {
        try {
            JobExecution execution = jobLauncher.run(
                    jobProjectBatch.jobProject()
                    , new JobParametersBuilder().toJobParameters());

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
