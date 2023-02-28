package com.clinicapp.classes;

/**
 * Class used to store specialization data
 */
public class Specialization {
    /**
     * Id of specialization
     */
    private String specializationId;
    /**
     * Name of specialization
     */
    private String name;

    /**
     * @return id of specialization
     */
    public String getSpecializationId() {
        return specializationId;
    }

    /**
     * Sets id of specialization.
     * @param specializationId id of specialization.
     */
    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    /**
     * @return name of specialization.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of specialization
     * @param name name of specialization.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Creates new specialization
     * @param name name of specialization.
     * @param specializationId id of specializaton.
     */
    public Specialization(String specializationId, String name) {
        this.specializationId = specializationId;
        this.name = name;
    }
}
