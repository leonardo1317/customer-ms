package co.com.customer.infrastructure.controller;

import co.com.customer.application.dto.CustomerDto;
import co.com.customer.application.services.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("customers")
public class CustomerController {

    private static final Logger LOG = LogManager.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;


    @PostMapping
    public Mono<ResponseEntity<Mono<CustomerDto>>> save(@Valid @RequestBody CustomerDto customerDto) {
        LOG.info("Crear clientes");
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.save(customerDto)));
    }


    @GetMapping
    public Mono<ResponseEntity<Flux<CustomerDto>>> findAll() {
        LOG.info("obtener todos los clientes");
        return Mono.just(ResponseEntity.ok(customerService.findAll()));
    }


}
