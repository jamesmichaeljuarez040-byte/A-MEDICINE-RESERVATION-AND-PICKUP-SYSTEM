package model;

import java.util.List;

/** Single place to edit the roles from Section 2 of your System Proposal. */
public final class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String PHARMACIST = "PHARMACIST";
    public static final String CUSTOMER = "CUSTOMER";

    /** All valid roles (used by AdminMenu validation). */
    public static final List<String> ALL = List.of(ADMIN, PHARMACIST, CUSTOMER);

    /** Roles a person may pick when self-registering (ADMIN is assigned by an existing admin). */
    public static final List<String> REGISTRABLE = List.of(PHARMACIST, CUSTOMER);

    private Roles() {
    }
}
