package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;


@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
}



@Component
class SampleDataCLR implements CommandLineRunner {

    private final ReservationRepository reservationRepository ;

    @Autowired
    public SampleDataCLR(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        Stream.of("Karthik", "Sugan", "Vishnu", "Kumar", "Kushagra").forEach(name -> reservationRepository.save(new Reservation(name)));
        reservationRepository.findAll().forEach(System.out::println);

    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path ="by-name")
    Collection <Reservation> findByReservationName( @Param("rn") String rn);
}

@Component
class LiveLessonsHealthIndicator implements HealthIndicator {

    private Health health = Health.status("I <3 LiveLessons").build();

    @Override
    public Health health() {
        return health;
    }
}

@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private String reservationName;

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }

    Reservation() {

    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    public Long getId() {
        return id;
    }

    public String getReservationName() {
        return reservationName;
    }
}