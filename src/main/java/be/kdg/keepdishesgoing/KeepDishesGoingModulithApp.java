package be.kdg.keepdishesgoing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.Modulith;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.scheduling.annotation.EnableScheduling;

@Modulith
@EnableScheduling
@SpringBootApplication
public class KeepDishesGoingModulithApp {

    private static final Logger log = LoggerFactory.getLogger(KeepDishesGoingModulithApp.class);

    public static void main(String[] args) {
        SpringApplication.run(KeepDishesGoingModulithApp.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    void onApplicationStarted() {
        ApplicationModules modules = ApplicationModules.of(KeepDishesGoingModulithApp.class);
        modules.forEach(module -> log.info("\n{}", module));
    }

}
