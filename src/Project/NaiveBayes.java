package Project;

//import Project.Features.Feature;
//import Project.Features.Features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NaiveBayes implements ClassificationAlgorithm {

    private final Labeling labeling;

    public NaiveBayes(Labeling labeling){
        this.labeling = labeling;
    }
    
    @Override
    public void train(List<Comment> trainingData) {
        System.out.println("Training naive Bayes on " + trainingData.size() + " comments...");
        IO.writeInputFile(Config.BASE_PATH + Constants.NAIVE_BAYES_TRAIN_FILENAME, new UnigramFormatter(labeling), trainingData);
        IO.runPython(Constants.NAIVE_BAYES_UNIGRAM_TRAIN);
    }

	@Override
	public CommentClass classify(Comment comment) {
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);
		return classify(comments).get(comment);
	}

	@Override
	public Map<Comment, CommentClass> classify(List<Comment> comments) {
        System.out.println("Testing naive Bayes on " + comments.size() + " comments...");
        IO.writeInputFile(Config.BASE_PATH + Constants.NAIVE_BAYES_TEST_FILENAME, new UnigramFormatter(labeling), comments);
        IO.runPython(Constants.NAIVE_BAYES_UNIGRAM_TEST);

        return IO.readClassificationOutputFile(Constants.NAIVE_BAYES_PREDICTION_FILENAME, comments);
	}
}
