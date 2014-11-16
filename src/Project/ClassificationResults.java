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

    public void printSummary(){
        System.out.println("Classifier results: precision " + getPrecision() + ", recall " + getRecall());
        System.out.println("Confusion matrix ====== ");
        System.out.println("TP: " + truePositives + "            FP: " + falsePositives);
        System.out.println("FN: " + falseNegatives+ "            TN: " + trueNegatives);
    }
}
