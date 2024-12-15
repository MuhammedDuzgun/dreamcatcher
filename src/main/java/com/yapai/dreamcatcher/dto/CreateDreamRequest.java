package com.yapai.dreamcatcher.dto;

public class CreateDreamRequest {

    private String dream;
    private String dreamInterpretation;

    public CreateDreamRequest() {
    }

    public CreateDreamRequest(String dream, String dreamInterpretation) {
        this.dream = dream;
        this.dreamInterpretation = dreamInterpretation;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }

    public String getDreamInterpretation() {
        return dreamInterpretation;
    }

    public void setDreamInterpretation(String dreamInterpretation) {
        this.dreamInterpretation = dreamInterpretation;
    }

}
