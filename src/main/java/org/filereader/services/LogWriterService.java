package org.filereader.services;

import lombok.RequiredArgsConstructor;
import org.filereader.entities.FileLog;
import org.filereader.repositories.FileLogRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogWriterService {
    public final FileLogRepository fileLogRepository;
    @Transactional
    @Retryable(
            retryFor = { java.sql.SQLException.class, org.springframework.dao.DataAccessException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            listeners = {"logRetryListener"}
    )
    public void saveToDB(String threadName,String status){
        fileLogRepository.save(FileLog.builder()
                .threadName(threadName)
                .status(status)
                .build());
    }
}
