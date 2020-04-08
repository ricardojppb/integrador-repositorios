package com.indra.repos;


import com.indra.repos.git.model.dto.Branches;
import com.indra.repos.git.model.dto.Commits;
import com.indra.repos.git.model.repository.BrancheMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class IntegradorRepositoriosApplication implements CommandLineRunner {

    RestTemplate restTemplate = null;

    @Autowired
    private BrancheMongoRepository brancheMongoRepository;

    public static void main(String[] args) {
        SpringApplication.run(IntegradorRepositoriosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        System.out.println("*********** Init Clone ***********");
//        String url = "http://10.10.253.35/gti/ATFWS.git";
//        String[] projeto = url.split("/");
//        File dir = new File("/home/ricardo/ambiente/workspace/integrador-repositorios/repos/" + projeto[projeto.length - 1]);
//        CredentialsProvider cp = new UsernamePasswordCredentialsProvider("ricardo.silva", "048LLrass265");
//        Git git = null;
//        if (!(dir.exists() && dir.isDirectory())) {
//
//            git = Git.cloneRepository()
//                    .setURI(url)
//                    .setDirectory(dir)
//                    .call();
//        } else {
//            git = Git.open(dir);
//        }


//        Map<String, Ref> asMapRefs = git.getRepository().getAllRefs();
//        asMapRefs.forEach((k,v) -> {
//            System.out.print("key: " + k);
//            System.out.print(" - ");
//            System.out.println("value: " + v.getName());
//            System.out.println("*****************************************************************************");
//        });

//        Map<String, Ref> asMapTags = git.getRepository().getTags();
//        asMapTags.forEach((k,v) -> {
//            System.out.print("key: " + k);
//            System.out.print(" - ");
//            System.out.print("value: " + v.toString());
//            System.out.println("*****************************************************************************");
//        });
//        refs.forEach(b -> {
//            System.out.print("Branch: " + b.getName());
//            System.out.println("*****************************************************************************");
//        });
//
//        Iterable<RevCommit> logs = git.log().all().call();
//        logs.forEach(e -> {
//            System.out.print("*************");
//            System.out.print("FullMessage: " + e.getFullMessage());
//            System.out.print("*************");
//            System.out.println("#############################################################################");
//            System.out.print("Name: " + e.getAuthorIdent().getName());
//            System.out.print(" - ");
//            System.out.print("Email: " + e.getAuthorIdent().getEmailAddress());
//            System.out.print(" - ");
//            System.out.print("Date: " + e.getAuthorIdent().getWhen().toString());
//            System.out.print(" - ");
//            System.out.println("Id: " + e.getId().toObjectId().getName());
//            System.out.println("*****************************************************************************");
//
//        });

        getAllCommitsBitBucket();
    }

    public void getAllCommitsBitBucket() {

//        BitbucketClient client = BitbucketClient.builder()
//                .endPoint("https://bitbucket.indra.es") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:7990
//                .token("NzU0MjE0Nzg0Njk2Ouxl5cMM3a11g6W5dPtQmM7WMu1N") // Optional and can be sourced from system/env and can be Base64 encoded.
//                .build();
//
//        BranchApi branchs = client.api().branchApi();
//        CommitsApi commitsApi = client.api().commitsApi();
//
        System.out.println("*********** Get All Commits for Rest ***********");
        String url = "https://bitbucket.indra.es/rest/api/1.0/";
        String urlCommits = url + "projects/jpa/repos/sefazpb-atf/commits?limit=1000";

        String urlBranchs = url + "projects/JPA/repos/sefazpb-atf/branches?limit=1000";

        String accessToken = "Bearer NzU0MjE0Nzg0Njk2Ouxl5cMM3a11g6W5dPtQmM7WMu1N";
        String auth = "Basic rasilvestres:048265LLrass022020";

//        HttpHeaders httpHeaders = restTemplate.headForHeaders(url);
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        //httpHeaders.setBearerAuth("NzU0MjE0Nzg0Njk2Ouxl5cMM3a11g6W5dPtQmM7WMu1N");
//        //httpHeaders.add("Authorization","Basic "+ "NzU0MjE0Nzg0Njk2Ouxl5cMM3a11g6W5dPtQmM7WMu1N");
//        //httpHeaders.setBasicAuth("rasilvestres","048265LLrass022020");
//
//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        headers.add("Authorization", auth);
//        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//
//        RequestEntity<Object> request = new RequestEntity<>(
//                headers, HttpMethod.GET, URI.create(url));
//
//        ResponseEntity<String> response
//                = restTemplate.exchange(request,String.class);
        //HttpEntity<Commit> httpEntity = new HttpEntity<Commits>(createHeaders("rasilvestres", "048265LLrass022020"))
        try {

            restTemplate = new RestTemplate();

            ResponseEntity<Commits> responseCommit = restTemplate.exchange
                    (urlCommits, HttpMethod.GET, new HttpEntity<String>(createHeaders("rasilvestres", "048265LLrass022020")), Commits.class);

            ResponseEntity<Branches> responseBranche = restTemplate.exchange
                    (urlBranchs, HttpMethod.GET, new HttpEntity<String>(createHeaders("rasilvestres", "048265LLrass022020")), Branches.class);


            if (responseCommit.getStatusCode() == HttpStatus.OK) {

                System.out.println("Status:" + HttpStatus.OK.name());
                System.out.println("Body:" + responseCommit.getBody().toString());

            }

            if (responseBranche.getStatusCode() == HttpStatus.OK) {

                System.out.println("Status:" + HttpStatus.OK.name());
                System.out.println("Body:" + responseBranche.getBody().toString());
            }

            Branches branches = responseBranche.getBody();

            if (!branches.getValues().isEmpty()) {

                if (branches.getIsLastPage() != null && !branches.getIsLastPage()) {

                }
                brancheMongoRepository.saveAll(branches.getValues());

            }


        } catch (Exception e) {

            System.out.println("Message: " + e.getMessage());
            System.out.println("LocalizedMessage:" + e.getLocalizedMessage());

        } finally {

            restTemplate = null;

        }


    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
