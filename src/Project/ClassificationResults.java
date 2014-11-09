package Project;

public class ClassificationResults implements Results {

    public int truePositives;
    public int trueNegatives;
    public int falsePositives;
    public int falseNegatives;

    public double getRecall() {
        return  truePositives / (double) (truePositives + falseNegatives);
    }

    public double getPrecision() {
        return  truePositives / (double) (falsePositives + truePositives);
    }
}
