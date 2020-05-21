package com.indra.repos.svn.model.service;

import com.indra.repos.svn.model.domain.SVNLog;
import com.indra.repos.svn.model.domain.SVNRepositories;
import com.indra.repos.svn.model.repository.SVNLogRepository;
import com.indra.repos.svn.model.repository.SVNReposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class SVNLogService {

    @Autowired
    private SVNLogRepository svnLogRepository;

    @Autowired
    private SVNReposRepository svnReposRepository;

    public List<SVNRepositories> findAllReposSVN() {
        return svnReposRepository.findAll();
    }

    public Collection<SVNLog> findAllSVNLog(Collection<SVNRepositories> svnRepositories) {
        Optional.ofNullable(svnRepositories).ifPresent(svnRepos -> {
            svnRepos.forEach(repositories -> {
                try {

                    SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositories.getUrl()));
                    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
                            repositories.getUsername(), repositories.getPassword());

                    repository.setAuthenticationManager(authManager);
                    long startRevision = 0;
                    long endRevision = -1;//HEAD (the latest) revision
                    Collection logEntries = repository.log(new String[]{""}, null,
                            startRevision, endRevision, true, true);

                    logEntries.forEach(log -> {
                        SVNLogEntry logEntry = (SVNLogEntry) log;
                        logEntry.getRevisionProperties().asMap();
                        System.out.println("Revisao: " + logEntry.getRevision());
                        System.out.println("Autor: " + logEntry.getAuthor());
                        System.out.println("Data: " + logEntry.getDate());
                        System.out.println("Mensagem: " + logEntry.getMessage());
                        System.out.println("Arquivos: " + logEntry.getChangedPaths().toString());
                    });

                } catch (SVNException e) {
                    e.printStackTrace();
                }

            });
        });

        return svnLogRepository.findAll();

    }
}
