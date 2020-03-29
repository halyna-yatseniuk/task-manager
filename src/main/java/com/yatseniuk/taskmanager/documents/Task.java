package com.yatseniuk.taskmanager.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tasks")
public class Task {
    @Id
    public String id;
    public String title;
    public String description;
    public User owner;
    public LocalDateTime creationDateTime;
    public LocalDateTime modificationDateTime;
    public List<User> viewers;
}