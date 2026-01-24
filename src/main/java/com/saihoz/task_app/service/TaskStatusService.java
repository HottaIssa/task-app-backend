package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.TaskDTO.TaskStatusRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.model.TaskStatus;
import com.saihoz.task_app.repo.TaskStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepo repository;

    public List<TaskStatusResponse> getAllTaskStatus(){
        List<TaskStatus> taskStatusList = repository.findAll();
        return taskStatusList.stream()
                .map(taskStatus ->
                        new TaskStatusResponse(
                                taskStatus.getId(),
                                taskStatus.getName(),
                                taskStatus.getColor(),
                                taskStatus.getOrderIndex()))
                .collect(Collectors.toList());
    }

    public TaskStatusResponse addTaskStatus(TaskStatusRequest request){
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(request.name());
        taskStatus.setColor(request.color());
        repository.save(taskStatus);
        return new TaskStatusResponse(
                taskStatus.getId(),
                taskStatus.getName(),
                taskStatus.getColor(),
                taskStatus.getOrderIndex());
    }

    public void deleteTaskStatus(Long id){
        repository.deleteById(id);
    }
}
