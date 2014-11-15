package Project.Features;

import java.util.List;

public class RegressionFeatureVector {
    private final List<Double> x;
    private final double y;

    public RegressionFeatureVector(List<Double> x, double y) {
        this.x = x;
        this.y = y;
    }

    public List<Double> getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
