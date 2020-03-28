package com.yatseniuk.taskmanager.dto;

import com.yatseniuk.taskmanager.documents.User;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDTO {
    public String title;
    public String description;
    //    public User user;
    public String user;
    public LocalDateTime creationDateTime;
    public LocalDateTime modificationDateTime;
    public List<User> viewers;
}