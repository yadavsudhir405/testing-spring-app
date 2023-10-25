package com.example.testingspringapp.config.batch.common;

import java.util.List;

public interface StepDataProvider<T> {
    List<T> getData();
}
