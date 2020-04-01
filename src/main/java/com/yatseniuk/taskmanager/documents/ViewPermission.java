package com.yatseniuk.taskmanager.documents;

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
    private LocalDateTime sharedDate;
}