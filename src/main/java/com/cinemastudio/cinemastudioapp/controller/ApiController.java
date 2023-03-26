package com.cinemastudio.cinemastudioapp.controller;


import java.util.List;
import java.util.Optional;

public interface ApiController {
    List<?> getAll();
    Optional<?> getOneById();

    Optional<?> update();
    void remove();
}
