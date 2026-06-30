package com.example.daycare.model;

/**
 * Application role assigned at registration.
 *
 * <p>Future RBAC: restrict educator endpoints (e.g. {@code /api/attendance/**})
 * to {@link #NANNIE} and {@link #MANAGER} only. {@link #PARENT} accounts should
 * receive a separate parent-facing API surface.
 */
public enum UserRole {
    PARENT,
    NANNIE,
    MANAGER
}
