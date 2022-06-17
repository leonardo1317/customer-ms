package co.com.customer.infrastructure.handler;

import co.com.customer.application.dto.ErrorDto;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final String PATH = "path";
    private static final String ERROR_MESSAGE = "errorMessage";

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::getErrorResponse);
    }

    protected Mono<ServerResponse> getErrorResponse(
            ServerRequest request) {
        Throwable throwable = getError(request);

        Map<String, Object> properties = getErrorAttributes(request,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));

        if (throwable instanceof WebExchangeBindException) {
            List<String> fieldErrors = ((WebExchangeBindException) throwable).getFieldErrors().stream()
                    .map(violation -> String.join(", ", violation.getField(), violation.getDefaultMessage()))
                    .collect(Collectors.toList());
            return response(HttpStatus.BAD_REQUEST, properties, Optional.of(fieldErrors));
        } else if (throwable instanceof ServerWebInputException) {
            properties.put(ERROR_MESSAGE, throwable.getMessage());
            HttpStatus status = ((ServerWebInputException) throwable).getStatus();
            return response(status, properties);
        } else if (throwable instanceof ResponseStatusException) {
            properties.put(ERROR_MESSAGE, throwable.getMessage());
            HttpStatus status = ((ResponseStatusException) throwable).getStatus();
            return response(status, properties);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR, properties);
        }
    }

    private Mono<ServerResponse> response(HttpStatus status, Map<String, Object> properties) {
        return response(status, properties, Optional.empty());
    }

    private Mono<ServerResponse> response(HttpStatus status, Map<String, Object> properties, Optional<List<String>> errors) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(status.value());
        errorDto.setDate(LocalDateTime.now());
        errorDto.setMessage(getProperty(ERROR_MESSAGE, properties));
        errorDto.setUri(getProperty(PATH, properties));
        errorDto.setErrors(errors);
        return ServerResponse.status(status.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorDto));
    }

    private Optional<String> getProperty(String key, Map<String, Object> properties) {
        return Optional.ofNullable(properties.get(key)).map(String::valueOf);
    }
}
