package Project.models;

public enum CommentClass {
    BAD(0, "BAD"),
    GOOD(1, "GOOD");

    private final int value;
    private final String name;


    private CommentClass(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public static CommentClass toEnum(int value){
        for(CommentClass cc : values()){
            if(cc.getValue() == value) return cc;
        }
        System.err.println("ENUM NOT FOUND WITH VALUE " + value);
        return null;
    }

    public String getName() {
        return name;
    }

    public static CommentClass other(CommentClass klass) {
        return (klass == BAD) ? GOOD : BAD;
    }
}
