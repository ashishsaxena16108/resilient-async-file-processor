package org.filereader.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "file_log")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String threadName;
    private String status;

}
