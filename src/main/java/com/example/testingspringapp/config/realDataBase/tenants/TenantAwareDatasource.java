package com.example.testingspringapp.config.realDataBase.tenants;

import lombok.NonNull;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TenantAwareDatasource extends AbstractRoutingDataSource {

    private final Map<Object, Object> tenantDatasorceMap = new ConcurrentHashMap<>();

    public TenantAwareDatasource() {
        this.setTargetDataSources(this.tenantDatasorceMap);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        final String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new RuntimeException("Current Tenant is " + currentTenant);
        }

        final DataSource dataSource = (DataSource) this.tenantDatasorceMap.get(currentTenant);
        if(dataSource == null) {
           throw new RuntimeException("No datasource configured for tenant "+currentTenant);
        }
        return dataSource;
    }

    public void addDataSource(@NonNull String tenant, @NonNull DataSource dataSource) {
        this.tenantDatasorceMap.put(tenant, dataSource);
    }

    public DataSource removeDataSource(@NonNull String tenant) {
        return (DataSource) this.tenantDatasorceMap.remove(tenant);
    }
}
