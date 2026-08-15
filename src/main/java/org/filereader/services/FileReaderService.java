package org.filereader.services;

import lombok.RequiredArgsConstructor;
import org.filereader.entities.FileLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileReaderService {
    public final LogWriterService logWriterService;
    @Async("fileReadingExecutor")
    public void readFile(int taskId) throws IOException {
        String threadName = Thread.currentThread().getName();
        FileReader reader = new FileReader("src/main/resources/static/long-doc.txt");
        try(BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line=bufferedReader.readLine())!=null) {
                 System.out.println(threadName+" is reading");
                 Thread.sleep(50);
            }
            logWriterService.saveToDB(threadName,"Success reading task #"+taskId);
            System.out.println(threadName + " is done reading this task #"+taskId);
        }
        catch (Exception e){
            System.out.println("An error occurred "+e.getMessage());
        }
    }
}
