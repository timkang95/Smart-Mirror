package Database;

/**
 * Created by Joe Roesinger on 11/3/2016.
 */

public class Alarm {
    private int id,
                userId,
                songId;
    private String name;

    // Constructors
    public Alarm(int id, String name, int userId, int songId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.songId = songId;
    }

    // Accessors
    public int getId()      { return id; }
    public String getName() { return name; }
    public int getUserId()  { return userId; }
    public int getSongId()  { return songId; }

    // Mutators
    public void setName(String name)  { this.name = name; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setSongId(int songId) { this.songId = songId; }
}
