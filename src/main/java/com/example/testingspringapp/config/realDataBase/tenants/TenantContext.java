package com.example.testingspringapp.config.realDataBase.tenants;

import lombok.NonNull;

public final class TenantContext {

    private TenantContext() {};

    private static final ThreadLocal<String> tenantContext = new ThreadLocal<>();

   public static void setTenant(@NonNull String tenant) {
        tenantContext.set(tenant);
   }

   public static String getCurrentTenant() {
        return tenantContext.get();
   }

   public static void clearTenant() {
       tenantContext.remove();
   }
}
