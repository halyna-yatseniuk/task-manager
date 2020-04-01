package com.yatseniuk.taskmanager.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "tasks")
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    @DBRef
    private User owner;
    private List<ViewPermission> viewers;
}