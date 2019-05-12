package ru.otus.library.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.library.repository.BookRepository;

@Component
public class CountBooksHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    public CountBooksHealthIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        long bookCount = bookRepository.count();
        if (bookCount < 1) {
            return Health.down()
                    .status(Status.DOWN)
                    //.withDetail("Books count", bookCount)
                    .build();
        } else {
            return Health.up().build();
        }
    }
}
