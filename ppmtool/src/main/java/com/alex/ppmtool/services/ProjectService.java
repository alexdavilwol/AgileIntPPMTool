package com.alex.ppmtool.services;

import com.alex.ppmtool.domain.Project;
import com.alex.ppmtool.exceptions.ProjectIdException;
import com.alex.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    //this will pass in a project object and save it on the database
    public Project saveOrUpdateProject(Project project){
        //Logic
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project Id '"+project.getProjectIdentifier().toUpperCase()+"' already exists.");
        }

    }
}
