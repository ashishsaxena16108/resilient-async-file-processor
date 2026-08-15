package org.filereader.repositories;

import org.filereader.entities.FileLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileLogRepository  extends JpaRepository<FileLog, Long> {
}
