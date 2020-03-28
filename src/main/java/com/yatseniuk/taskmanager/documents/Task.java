package com.yatseniuk.taskmanager.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@Document
public class Task {
    @Id
    public String id;
    public String title;
    public String description;
    public User owner;
    @Indexed(direction = IndexDirection.ASCENDING)
    public LocalDateTime creationDateTime;
    public LocalDateTime modificationDateTime;
    public List<User> viewers;
}