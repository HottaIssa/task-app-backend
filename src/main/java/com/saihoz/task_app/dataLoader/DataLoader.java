package com.saihoz.task_app.dataLoader;

import com.saihoz.task_app.model.TaskStatus;
import com.saihoz.task_app.repo.TaskStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TaskStatusRepo taskStatusRepo;

    @Override
    public void run(String... args) throws Exception {
        // Solo crear si no existen
        if (taskStatusRepo.count() == 0) {
            List<TaskStatus> defaultStatuses = Arrays.asList(
                    TaskStatus.builder()
                            .name("TODO")
                            .color("#94A3B8")
                            .orderIndex(0)
                            .isSystem(true)
                            .build(),
                    TaskStatus.builder()
                            .name("IN_PROGRESS")
                            .color("#3B82F6")
                            .orderIndex(1)
                            .isSystem(true)
                            .build(),
                    TaskStatus.builder()
                            .name("IN_REVIEW")
                            .color("#F59E0B")
                            .orderIndex(2)
                            .isSystem(true)
                            .build(),
                    TaskStatus.builder()
                            .name("DONE")
                            .color("#10B981")
                            .orderIndex(3)
                            .isSystem(true)
                            .build()
            );

            taskStatusRepo.saveAll(defaultStatuses);
            System.out.println("✅ Estados del sistema creados");
        }
    }
}