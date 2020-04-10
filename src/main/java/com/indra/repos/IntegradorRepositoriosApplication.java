package com.indra.repos;


import com.indra.repos.git.model.dto.Branches;
import com.indra.repos.git.model.repository.BrancheMongoRepository;
import com.indra.repos.properties.GitProperties;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.http.MediaType;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ConfigurationPropertiesScan("com.indra.repos.properties")
//@EnableConfigurationProperties(GitProperties.class)
public class IntegradorRepositoriosApplication implements CommandLineRunner {

    @Autowired
    private BrancheMongoRepository brancheMongoRepository;

    @Autowired
    private GitProperties gitProperties;

    public static void main(String[] args) {
        SpringApplication.run(IntegradorRepositoriosApplication.class, args);
    }

    @Override
    public void run(String... args) {

        getAllCommitsBitBucket();
    }

    public void getAllCommitsBitBucket() {

        System.out.println("*********** Get All Commits for Rest ***********");

        String url = gitProperties.getEndPoint(); // "https://bitbucket.indra.es/rest/api/1.0/"
        String urlCommits = url + "/projects/jpa/repos/sefazpb-atf/commits?limit=1000";
        String urlBranchs = gitProperties.getBranches(); //url + "/projects/JPA/repos/sefazpb-atf/branches?limit=1000"; //MessageFormat.format(gitProperties.getBranches(), Arrays.asList("jpa", "sefazpb-atf").toArray());
        String accessToken = gitProperties.getToken(); //"Bearer NzU0MjE0Nzg0Njk2Ouxl5cMM3a11g6W5dPtQmM7WMu1N";

        try {

            HttpResponse<Branches> response = Unirest.get(urlBranchs).routeParam("projects", "jpa").routeParam("repos", "sefazpb-atf")
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", accessToken).asObject(Branches.class);


            Branches branches = response.getBody();

            System.out.println("Status:" + response.getStatus());
            System.out.println("Size:" + branches.getSize());
            System.out.println("Limit:" + branches.getLimit());
            System.out.println("Start:" + branches.getStart());
            System.out.println("NextPageStart:" + branches.getNextPageStart());
            System.out.println("IsLastPage:" + branches.getIsLastPage());

            System.out.println("Branch:" + branches.getValues().toString());

            Thread.sleep(30000);

            brancheMongoRepository.saveAll(branches.getValues());


        } catch (Exception e) {

            System.out.println("Message: " + e.getMessage());
            System.out.println("LocalizedMessage:" + e.getLocalizedMessage());

        } finally {


        }


    }

//    private HttpHeaders createHeaders(String username, String password) {
//        return new HttpHeaders() {{
//            String auth = username + ":" + password;
//            byte[] encodedAuth = Base64.encodeBase64(
//                    auth.getBytes(StandardCharsets.US_ASCII));
//            String authHeader = "Basic " + new String(encodedAuth);
//            set("Authorization", authHeader);
//        }};
//    }
}
