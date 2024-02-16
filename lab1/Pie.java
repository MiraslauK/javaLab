public class Pie extends Food {
    private String filling;
    public Pie(String filling) {
        super("Pie");
        this.filling = filling;
    }
    public void consume() {
        System.out.println(this + " eaten");
    }
    public String getFilling() {
        return filling;
    }
    public void setFilling(String filling) {
        this.filling = filling;
    }
    public String toString() {
        return super.toString() + " with filling " + filling;
    }

}
