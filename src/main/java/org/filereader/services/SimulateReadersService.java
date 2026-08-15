package org.filereader.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor
public class SimulateReadersService {
    public final FileReaderService fileReaderService;

    public void executeFileReading() throws IOException {
       for(int i=0;i<5000;i++){
                   fileReaderService.readFile(i);
       }
       System.out.println("All readers are pushed in queue");
    }
}
