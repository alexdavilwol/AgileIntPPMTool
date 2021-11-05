package com.alex.ppmtool.services;

import com.alex.ppmtool.domain.Backlog;
import com.alex.ppmtool.domain.ProjectTask;
import com.alex.ppmtool.repositories.BacklogRepository;
import com.alex.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        //Exceptions: Project is not found

        //Project Tasks (PTs) to be added to a specific existing project,
        // project != null then backlog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        //set backlog to project task
        projectTask.setBacklog(backlog);

        //we want our project sequence to be like this, IDProject-1 - IDProject-2  ...100, 101
        Integer BacklogSequence = backlog.getPTSequence();

        // Update backlog sequence
        BacklogSequence++;

        //Add sequence to project task
        projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL Priority when priority is null
//        if(projectTask.getPriority()==0 || projectTask.getPriority()==null){
//            projectTask.setPriority(3);
//        }
        //INITIAL Status when status is null
        if(projectTask.getStatus()==""||projectTask.getStatus()==null){
            projectTask.setStatus("TO-DO");
        }

        return projectTaskRepository.save(projectTask);

    }
}
