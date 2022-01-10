package com.vetapp.data.models.vet;

import com.vetapp.data.models.appointment.Appointment;

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
        private Appointment appointment;

        public TimeSlot() {
        }

        public TimeSlot(TimePoint start, TimeSlotStatus status, Appointment appointment) {
            this.start = start;
            this.status = status;
            this.appointment = appointment;
        }

        public Appointment getAppointment() {
            return appointment;
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
            BUSY,
            TRANSIENT,
            VACATION,
            DONE
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

        public void setHour(int hour) {
            this.hour = hour;
        }

        public void setMinute(int minute) {
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

        public TimePoint add(TimePoint o) {
            TimePoint nt = new TimePoint(this.getHour(), this.getMinute());
            nt.minute += o.getMinute();
            nt.hour += o.getHour();

            if (nt.minute >= 60) {
                nt.hour++;
                nt.minute -= 60;
            }

            if (nt.hour >= 24)
                nt.hour -= 24;
            return nt;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (hour < 10)
                sb.append("0");
            sb.append(hour);
            sb.append(":");
            if (minute < 10)
                sb.append("0");
            sb.append(minute);

            return sb.toString();
        }
    }

}
