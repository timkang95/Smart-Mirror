package Database;

public class User {
    private int id;
    private String name, nickName, homeAddr, workAddr;

    // Constructors
    public User(int id, String name, String homeAddr, String workAddr) {
        this.id = id;
        this.name = name;
        this.homeAddr = homeAddr;
        this.workAddr = workAddr;
    }

    // Accessors
    public int getId()      { return id; }
    public String getName() { return name; }
    public String getNickname() {return nickName; }
    public String getHomeAddress()  { return homeAddr; }
    public String getWorkAddress()  { return workAddr; }

    // Mutators
    public void setName(String name)  { this.name = name; }
    public void setUserId(String userId) { this.homeAddr = homeAddr; }
    public void setSongId(String songId) { this.workAddr = workAddr; }
}
