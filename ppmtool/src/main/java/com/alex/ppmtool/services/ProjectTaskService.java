package com.alex.ppmtool.services;

import com.alex.ppmtool.domain.Backlog;
import com.alex.ppmtool.domain.Project;
import com.alex.ppmtool.domain.ProjectTask;
import com.alex.ppmtool.exceptions.ProjectNotFoundException;
import com.alex.ppmtool.repositories.BacklogRepository;
import com.alex.ppmtool.repositories.ProjectRepository;
import com.alex.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        //Exceptions: Project is not found
        try{
            //Project Tasks (PTs) to be added to a specific existing project,
            // project != null then backlog exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());

            //set backlog to project task
            projectTask.setBacklog(backlog);

            //we want our project sequence to be like this, IDProject-1 - IDProject-2  ...100, 101
            Integer BacklogSequence = backlog.getPTSequence();

            // Update backlog sequence
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);

            //Add sequence to project task
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL Priority when priority is null
            if(projectTask.getPriority()==null){ // in the future we need the projectTask.getPriority()==0 to handle the form
                projectTask.setPriority(3);
            }
            //INITIAL Status when status is null
            if(projectTask.getStatus()==""||projectTask.getStatus()==null){
                projectTask.setStatus("TO-DO");
            }

            return projectTaskRepository.save(projectTask);
        }catch(Exception e){
            throw new ProjectNotFoundException("Project not found");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project==null){
            throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching on an existing  backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id.toUpperCase());
        if(backlog == null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }
        //make sure the task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id.toUpperCase());
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }
        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }

        return projectTask;
    }

    //update project task
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        Backlog backlog = projectTask.getBacklog();
        List<ProjectTask> pts = backlog.getProjectTasks();
        pts.remove(projectTask);
        backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask);
    }
}
