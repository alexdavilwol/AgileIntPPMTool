package com.alex.ppmtool.services;

import com.alex.ppmtool.domain.Project;
import com.alex.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    //this will pass in a project object and save it on the database
    public Project saveOrUpdateProject(Project project){

        //Logic in here once we have users

        return projectRepository.save(project);
    }
}
