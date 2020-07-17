package com.vsiestov.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "T_TASKS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private long id;

    @Column
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name="name"))
    })
    private TaskName name;

    @Column
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name="description"))
    })
    private TaskDescription description;

    @Column
    private boolean complete;

    @Column(name = "user_id")
    private long userId;
}
