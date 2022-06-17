package co.com.customer.application.services;

import co.com.customer.application.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDto> save(CustomerDto customerDto);

    Flux<CustomerDto> findAll();
}
