package me.n0rdy.hackattic.service;

import me.n0rdy.hackattic.model.HackatticServerResponse;

public interface TaskService {
    HackatticServerResponse solve();

    String getTaskName();

    void cleanUp();
}
