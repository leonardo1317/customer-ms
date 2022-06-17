package co.com.customer.application.mapper;

import co.com.customer.application.dto.CustomerDto;
import co.com.customer.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);

    default Mono<CustomerDto> toMonoDto(Mono<Customer> customerMono) {
        return customerMono.map(this::toDto);
    }

    default Flux<CustomerDto> toFluxDto(Flux<Customer> customerFlux) {
        return customerFlux.map(this::toDto);
    }
}
