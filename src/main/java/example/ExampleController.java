package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ExampleController {

    private final PersonRepository personRepository;
    private final WeatherClient weatherClient;

    @Autowired
    public ExampleController(final PersonRepository personRepository, final WeatherClient weatherClient) {
        this.personRepository = personRepository;
        this.weatherClient = weatherClient;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/hello/{lastName}")
    public String hello(@PathVariable final String lastName) {
        Optional<Person> foundPerson = personRepository.findByLastName(lastName);

        return foundPerson
                .map(person -> String.format("Hello %s %s!", person.firstName, person.lastName))
                .orElse(String.format("Who is this '%s' you're talking about?", lastName));
    }

    @GetMapping("/yesterdaysWeather")
    public String yesterdaysWeather() {
        return weatherClient.yesterdaysWeather().getDescription();
    }
}