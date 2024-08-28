
package cavapy.api.py.continental.enums;

/**
 * Enum representing user profiles within the system.
 * Each profile is associated with a unique identifier and a description.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * PermittedProfiles profile = PermittedProfiles.PROFILE_CVP_AYF_SUP;
 * System.out.println("ID: " + profile.getIdSystemProfilePk() + ", Description: " + profile.getDescription());
 * }
 * </pre>
 *
 * @author RodrigoRamirez
 * @version 1.0
 */
public enum PermittedProfiles {

    /**
     * Profile for CAVAPY - Administration and Finance Supervisor.
     * Associated with ID 232.
     */
    PROFILE_CVP_AYF_SUP(232, "CAVAPY - ADMINISTRACION Y FINANZAS SUPERVISOR"),

    /**
     * Profile for CAVAPY - Administration and Finance Treasury.
     * Associated with ID 2.
     */
    PROFILE_CVP_AYF_TES(2, "CAVAPY ADMINISTRACION Y FINANZAS TESORERIA"),

    /**
     * Profile for CAVAPY - System Administrator.
     * Associated with ID 324.
     */
    PROFILE_CVP_ADM_GRAL(324, "CAVAPY - ADMINISTRADOR DEL SISTEMA ");

    /**
     * Constructor for the {@code PermittedProfiles} enum.
     *
     * @param idSystemProfilePk the unique identifier for the profile
     * @param description       the description of the profile
     */
    PermittedProfiles(Integer idSystemProfilePk, String description) {
        this.idSystemProfilePk = idSystemProfilePk;
        this.description = description;
    }

    /**
     * The unique identifier for the system profile.
     */
    private final Integer idSystemProfilePk;

    /**
     * The description of the system profile.
     */
    private final String description;

    /**
     * Gets the unique identifier for the system profile.
     *
     * @return the profile's unique identifier
     */
    public Integer getIdSystemProfilePk() {
        return idSystemProfilePk;
    }

    /**
     * Gets the description of the system profile.
     *
     * @return the profile's description
     */
    public String getDescription() {
        return description;
    }
}