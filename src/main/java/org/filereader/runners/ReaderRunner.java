package org.filereader.runners;

import lombok.RequiredArgsConstructor;
import org.filereader.services.SimulateReadersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReaderRunner implements CommandLineRunner {
    public final SimulateReadersService simulateReadersService;
    @Override
    public void run(String... args) throws Exception {
        simulateReadersService.executeFileReading();
    }
}
