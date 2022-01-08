package com.vetapp.data.models.vet;

import java.util.List;

public class Schedule {

    private TimePoint startTime;
    private TimePoint endTime;
    private List<TimeSlot> timeSlots;

    public Schedule(TimePoint startTime, TimePoint endTime, List<TimeSlot> timeSlots) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeSlots = timeSlots;
    }

    public Schedule() {
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public TimePoint getStartTime() {
        return startTime;
    }

    public TimePoint getEndTime() {
        return endTime;
    }

    public static class TimeSlot {
        private TimePoint start;
        private TimeSlotStatus status;

        public TimeSlot() {
        }

        public TimeSlot(TimePoint start, TimeSlotStatus status) {
            this.start = start;
            this.status = status;
        }

        public TimePoint getStart() {
            return start;
        }

        public TimeSlotStatus getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return start.toString();
        }

        public int compare(TimeSlot o2) {
            return start.compare(o2.getStart());
        }

        public enum TimeSlotStatus {
            FREE,
            BUSY
        }
    }

    public static class TimePoint {
        private int hour;
        private int minute;

        public TimePoint() {
        }

        public TimePoint(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public int compare(TimePoint o) {
            if (this.hour < o.getHour())
                return -1;
            if (this.hour > o.getHour())
                return 1;
            if (this.minute < o.getMinute())
                return -1;
            if (this.minute > o.getMinute())
                return 1;
            return 0;
        }

        @Override
        public String toString() {
            return hour + ":" + minute;
        }
    }

}
