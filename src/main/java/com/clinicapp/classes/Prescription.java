package com.clinicapp.classes;

import java.time.LocalDate;

public class Prescription {
    private String recipeId;
    private LocalDate issueDate;
    private String drugs;
    private String description;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getDrugs() {
        return drugs;
    }

    public void setDrugs(String drugs) {
        this.drugs = drugs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Prescription(String recipeId, LocalDate issueDate, String drugs, String description) {
        this.recipeId = recipeId;
        this.issueDate = issueDate;
        this.drugs = drugs;
        this.description = description;
    }
}
