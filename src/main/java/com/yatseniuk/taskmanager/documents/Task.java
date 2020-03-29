package com.yatseniuk.taskmanager.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "tasks")
public class Task {
    @Id
    public String id;
    public String title;
    public String description;
    public LocalDateTime creationDateTime;
    public LocalDateTime modificationDateTime;
    @DBRef
    public User owner;
    @DBRef
    public Set<User> viewers;
}