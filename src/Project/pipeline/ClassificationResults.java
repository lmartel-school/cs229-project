package Project.pipeline;

import Project.models.Comment;
import Project.models.CommentClass;

import java.util.Map;

public class ClassificationResults implements Results {

    private final Map<Comment, CommentClass> classifications;

    public ClassificationResults(Map<Comment, CommentClass> classifications){
        this.classifications = classifications;
    }

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

    public Map<Comment, CommentClass> getClassifications() { return classifications; }
}
