package com.yatseniuk.taskmanager.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewPermission {
    @DBRef
    private User performer;
    @DBRef
    private User viewer;
    private String taskId;
    private LocalDateTime sharedDate;
}