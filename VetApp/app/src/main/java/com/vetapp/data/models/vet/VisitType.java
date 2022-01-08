package com.vetapp.data.models.vet;

public class VisitType {
    private String name;
    private Schedule.TimePoint duration;

    public VisitType(String name, Schedule.TimePoint duration) {
        this.name = name;
        this.duration = duration;
    }

    public VisitType() {
    }

    public String getName() {
        return name;
    }

    public Schedule.TimePoint getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return name;
    }
}
