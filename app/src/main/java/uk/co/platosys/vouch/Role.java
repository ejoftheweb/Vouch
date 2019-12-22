package uk.co.platosys.vouch;

public class Role {

    public static final Role AUTHOR=new Role("author");
    public static final Role PUBLISHER=new Role ("publisher");
    public static final Role TAGGER=new Role("tagger");
    public static final Role STORE=new Role("store");
    private String name;
    private Role (String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
}
