import java.lang.Math;

public class Formula {
    private double sum = 0;

    public Double calculate1(double x, double y, double z) {
        return 	((1 / Math.sqrt(x)) + Math.pow(Math.cos(Math.E), y) + Math.pow(Math.cos(z), 2)) / (Math.pow(Math.pow(Math.log(1 + z), 2) + Math.sqrt(Math.pow(Math.E, Math.cos(y)) + Math.pow(Math.sin(Math.PI * x), 2)), 1/3));
    }
    public Double calculate2(double x, double y, double z) {
        return Math.pow(y + x*x*x, 1/z) / Math.log(z);
    }

    public void addSum(double M) {
        sum += M;
    }

    public void clearSum() {
        sum = 0;
    }

    public Double getSum() {
        return sum;
    }
}
