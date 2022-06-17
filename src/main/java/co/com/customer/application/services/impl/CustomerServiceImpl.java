package co.com.customer.application.services.impl;

import co.com.customer.application.dto.CustomerDto;
import co.com.customer.application.mapper.CustomerMapper;
import co.com.customer.application.services.CustomerService;
import co.com.customer.domain.model.Customer;
import co.com.customer.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public Mono<CustomerDto> save(CustomerDto customerDto) {
        Mono<Customer> customerMono = customerRepository.save(CustomerMapper.INSTANCE.toEntity(customerDto));
        return CustomerMapper.INSTANCE.toMonoDto(customerMono);
    }

    @Override
    public Flux<CustomerDto> findAll() {
        return CustomerMapper.INSTANCE.toFluxDto(customerRepository.findAll());
    }
}
