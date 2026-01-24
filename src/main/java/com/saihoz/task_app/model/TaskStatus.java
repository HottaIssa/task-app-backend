package com.saihoz.task_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "task_status")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 7) // #FFFFFF
    private String color;

    @Column(name = "order_index", nullable = false)
    @Builder.Default
    private Integer orderIndex = 0; // ✅ Para ordenar las columnas del Kanban

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project; // ✅ null = estado global, no-null = estado del proyecto

    @Column(name = "is_system", nullable = false)
    @Builder.Default
    private Boolean isSystem = false; // ✅ Los estados del sistema no se pueden eliminar

    @OneToMany(mappedBy = "status")
    private List<Task> tasks = new ArrayList<>(); // ✅ Relación bidireccional
}
