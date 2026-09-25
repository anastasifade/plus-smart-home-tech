package ru.yandex.practicum.telemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.telemetry.runner.HubEventProcessor;
import ru.yandex.practicum.telemetry.runner.SnapshotProcessor;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Analyzer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Analyzer.class, args);
        startHubEventThread(context);
        startSnapshotThread(context);
    }

    private static void startHubEventThread(ConfigurableApplicationContext context) {
        final HubEventProcessor processor = context.getBean(HubEventProcessor.class);
        Thread hubEventsThread = new Thread(processor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();
    }

    private static void startSnapshotThread(ConfigurableApplicationContext context) {
        final SnapshotProcessor processor = context.getBean(SnapshotProcessor.class);
        Thread snapshotsThread = new Thread(processor);
        snapshotsThread.setName("SnapshotHandlerThread");
        snapshotsThread.start();
    }
}
