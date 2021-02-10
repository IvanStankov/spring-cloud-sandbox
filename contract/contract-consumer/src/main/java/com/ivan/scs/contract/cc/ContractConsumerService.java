package com.ivan.scs.contract.cc;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class ContractConsumerService {

    private static final String PRODUCER_URL = "http://localhost:8020/accounts";

    private final RestTemplate restTemplate;

    public Map<Integer, Long> countPeopleGroupedByYear() {
        final ResponseEntity<List<Person>> response = this.restTemplate.exchange(PRODUCER_URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody()
                .stream()
                .collect(groupingBy(p -> p.getDob().getYear(), counting()));
    }

    public Person updatePerson(final Person person) {
        final ResponseEntity<Person> response = this.restTemplate.exchange(
                RequestEntity.put(PRODUCER_URL + "/" + person.getId())
                        .body(person),
                Person.class);
        return response.getBody();
    }

}
