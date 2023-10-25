package com.example.testingspringapp.config.batch.common;

import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;

import java.util.List;
import java.util.Objects;

public class SimpleItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {
    private final StepDataProvider<T> stepDataProvider;

    private List<T> results;

    public SimpleItemReader(String name, StepDataProvider<T> tStepNonPaginatedDataProvider) {
        this.stepDataProvider = tStepNonPaginatedDataProvider;
        this.setName(name);
    }

    @Override
    protected T doRead() {
        return this.getCurrentItemCount() > this.results.size() ? null : this.results.get(this.getCurrentItemCount() - 1);

    }


    @Override
    protected void doOpen() {
        this.results = this.stepDataProvider.getData();
    }

    @Override
    protected void doClose() {
        this.results = null;
    }

    boolean isResultSetNullified() {
        return Objects.isNull(this.results);
    }
}
