package Database;

/**
 * Created by Joe Roesinger on 11/14/2016.
 */

public class Clock {
    private int id;
    private int time;
    private String days;
    private int alarmId;

    // CONSTRUCTORS
    public Clock(int id, int time, String days, int alarmId) {
        this.id = id;
        this.time = time;
        this.days = days;
        this.alarmId = alarmId;
    }

    // Accessors
    public int getId() { return id; }
    public int getTime() { return time; }
    public String getDays() { return days; }
    public int getAlarmId() { return alarmId; }

    // Murators
    public void setTime(int time) { this.time = time; }
    public void setDays(String days) { this.days = days; }
}
