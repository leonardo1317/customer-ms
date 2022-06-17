package co.com.customer.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ErrorDto {

    private LocalDateTime date;

    private Integer status;

    private Optional<String> code;

    private Optional<String> message;

    private Optional<List<String>> errors;

    private Optional<String> uri;
}
