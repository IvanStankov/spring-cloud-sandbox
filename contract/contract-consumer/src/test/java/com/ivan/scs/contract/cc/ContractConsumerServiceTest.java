package com.ivan.scs.contract.cc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
        ids = "com.ivan.scs:contract-producer:::8020",
        stubsMode = StubRunnerProperties.StubsMode.REMOTE,
        repositoryRoot = "stubs://file:///D:/Projects/spring-cloud-sandbox/contract/contract-producer/build/stubs/"
)
public class ContractConsumerServiceTest {

    @Autowired
    private ContractConsumerService contractConsumerService;

    @Test
    public void countPeopleGroupedByYear() {
        // When
        final Map<Integer, Long> map = this.contractConsumerService.countPeopleGroupedByYear();

        // Then
        assertThat(map.get(1983)).isEqualTo(1);
        assertThat(map.get(1984)).isEqualTo(1);
    }

    @Test
    public void updatePerson() {
        // Given
        final Person person = new Person(744L, "Marty", "McFly", LocalDate.of(1968, Month.JUNE, 12));

        // When
        final Person result = this.contractConsumerService.updatePerson(person);

        // Then
        assertThat(result.getId()).isEqualTo(person.getId());
        assertThat(result.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(result.getLastName()).isEqualTo(person.getLastName());
        assertThat(result.getDob()).isEqualTo(person.getDob());
    }

}