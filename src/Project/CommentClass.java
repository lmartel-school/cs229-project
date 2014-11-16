package Project;

public enum CommentClass {
    BAD(0),
    GOOD(1);

    private final int value;


    private CommentClass(int value){
        this.value = value;
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
}
