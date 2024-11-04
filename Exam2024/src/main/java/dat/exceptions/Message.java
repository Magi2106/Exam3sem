package dat.exceptions;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record Message(int status, String message, String timestamp) {

    public Message(int status, String message) {
        this(status, message, DateTimeFormatter.ISO_INSTANT.format(Instant.now().atOffset(ZoneOffset.UTC)));
    }
}
