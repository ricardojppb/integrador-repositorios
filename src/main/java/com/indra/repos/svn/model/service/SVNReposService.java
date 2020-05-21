package com.indra.repos.svn.model.service;

import com.indra.repos.svn.model.repository.SVNReposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SVNReposService {

    @Autowired
    private SVNReposRepository svnReposRepository;
}
