package com.cinemastudio.cinemastudioapp;

import com.cinemastudio.cinemastudioapp.model.*;
import com.cinemastudio.cinemastudioapp.repository.ChairRepository;
import com.cinemastudio.cinemastudioapp.repository.HallRepository;
import com.cinemastudio.cinemastudioapp.repository.RowRepository;
import com.cinemastudio.cinemastudioapp.repository.TicketTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootApplication
public class CinemaStudioAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaStudioAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(HallRepository hallRepository, RowRepository rowRepository, ChairRepository chairRepository, TicketTypeRepository ticketTypeRepository) {
        return args -> {
            loadHalls(hallRepository, rowRepository, chairRepository);
            loadTicketTypes(ticketTypeRepository);
        };
    }

    private void loadTicketTypes(TicketTypeRepository ticketTypeRepository) {
        List<TicketType> ticketTypes = ticketTypeRepository.findAll();
        if (ticketTypes.size() == 0) {
            TicketType regular = TicketType.builder()
                    .name("regular")
                    .price(BigDecimal.valueOf(10))
                    .build();

            TicketType halfPrice = TicketType.builder()
                    .name("half_price")
                    .price(BigDecimal.valueOf(5))
                    .build();

            ticketTypeRepository.saveAll(List.of(regular, halfPrice));
        }
    }

    private void loadHalls(HallRepository hallRepository, RowRepository rowRepository, ChairRepository chairRepository) {
        List<Hall> halls = hallRepository.findAll();
        if (halls.size() == 0) {
            int rowsNumberInBigHall = 8;
            int rowsNumberInSmallHall = 4;
            int chairsNumberInRow = 8;

            Hall bigHall = new Hall();
            bigHall.setName("Big");
            bigHall.setRowNumber(rowsNumberInBigHall);


            Hall smallHall = new Hall();
            smallHall.setName("Small");
            smallHall.setRowNumber(rowsNumberInSmallHall);

            hallRepository.saveAll(List.of(smallHall, bigHall));

            createAudience(smallHall, rowsNumberInSmallHall, chairsNumberInRow, rowRepository, chairRepository);
            createAudience(bigHall, rowsNumberInBigHall, chairsNumberInRow, rowRepository, chairRepository);
        }
    }

    private void createAudience(Hall hall, int rowsNumber, int chairsNumberInRow, RowRepository rowRepository, ChairRepository chairRepository) {

        for (int i = 1; i <= rowsNumber; i++) {
            Row row = new Row();
            row.setNumber(i);
            row.setCharNumber(chairsNumberInRow);
            row.setHall(hall);
            rowRepository.save(row);

            for (int j = 1; j <= chairsNumberInRow; j++) {
                Chair chair = new Chair();
                if (i == 1) {
                    chair.setNumber(i * j);
                } else {
                    chair.setNumber(j + ((i - 1) * 8));
                }

                chair.setRow(row);
                chair.setHall(hall);
                chairRepository.save(chair);
            }
        }
    }
}
