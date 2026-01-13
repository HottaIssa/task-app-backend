package com.saihoz.task_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_status", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "project_id"})
})
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name of the state cannot be empty")
    @Size(max = 50, message = "The name of the state cannot exceed 50 characters")
    @Column(unique = true)
    private String name;

    @Size(max = 200, message = "The description cannot exceed 50 characters")
    @Column(length = 200)
    private String color = "#6B72080";

    @Min(value = 0, message = "The order cannot be negative")
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0;

    @OneToMany(mappedBy = "status", cascade = CascadeType.PERSIST)
    private List<Task> tasks = new ArrayList<>();

}
