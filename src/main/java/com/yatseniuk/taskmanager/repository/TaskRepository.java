package com.yatseniuk.taskmanager.repository;

import com.yatseniuk.taskmanager.documents.Task;
import com.yatseniuk.taskmanager.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findAllByOwnerId(String ownerId);

    List<Task> findAllByViewersContains(User user);
}