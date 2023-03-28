package com.cinemastudio.cinemastudioapp;

import com.cinemastudio.cinemastudioapp.model.Chair;
import com.cinemastudio.cinemastudioapp.model.Hall;
import com.cinemastudio.cinemastudioapp.model.Row;
import com.cinemastudio.cinemastudioapp.repository.ChairRepository;
import com.cinemastudio.cinemastudioapp.repository.HallRepository;
import com.cinemastudio.cinemastudioapp.repository.RowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
    public CommandLineRunner loadData(HallRepository hallRepository, RowRepository rowRepository, ChairRepository chairRepository) {
        return args -> {

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

                createAudience(smallHall, rowsNumberInSmallHall, chairsNumberInRow, rowRepository, chairRepository, hallRepository);
                createAudience(bigHall, rowsNumberInBigHall, chairsNumberInRow, rowRepository, chairRepository, hallRepository);
            }
        };
    }

    private void createAudience(Hall hall, int rowsNumber, int chairsNumberInRow, RowRepository rowRepository, ChairRepository chairRepository, HallRepository hallRepository) {

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
