package Project.algorithms;

import Project.Constants;
import Project.features.Feature;
import Project.io.IO;
import Project.io.WordLabeledFeatureFormatter;
import Project.models.Comment;
import Project.models.CommentClass;
import Project.models.Labeling;
import hr.irb.fastRandomForest.FastRandomForest;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 12/6/14.
 */
public class WekaClassifier implements ClassificationAlgorithm {

    private final List<Feature> features;
    private final Labeling threshold;

    private WekaAdapter model;
    private CommentClass firstTrainComment;
    private Instances train;

    private WekaClassifier(WekaAdapter model, Labeling threshold, List<Feature> features){
        this.model = model;
        this.threshold = threshold;
        this.features = features;
    }

    public static WekaClassifier build(Class<? extends Classifier> klass, Labeling threshold, List<Feature> features) throws InstantiationException, IllegalAccessException {
        return new WekaClassifier(new WekaClassifierAdapter(klass), threshold, features);
    }

    public static WekaClassifier buildForest(FastRandomForest frf, Labeling threshold, List<Feature> features) {
        return new WekaClassifier(new WekaFastRandomForestAdapter(frf), threshold, features);
    }

    private String getName() { return model.getName(); }


    @Override
    public CommentClass classify(Comment comment) {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        return classify(comments).get(comment);
    }

    @Override
    public Map<Comment, CommentClass> classify(List<Comment> comments) {
        for(Comment c : comments){
            if(this.threshold.label(c) == firstTrainComment){
                comments.remove(c);
                comments.add(0, c);
                break;
            }
        }

        assert(this.threshold.label(comments.get(0)) == this.firstTrainComment);

        System.out.println("Testing " + getName() + " on " + comments.size() + " comments...");
        IO.writeInputFileWithHeaders(Constants.WEKA_TEST_FILENAME, new WordLabeledFeatureFormatter(this.threshold, this.features, ","), comments, this.features);


        Map<Comment, CommentClass> classifications = new HashMap<>();
        try {
            Evaluation eval = new Evaluation(this.train);

            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(Constants.WEKA_TEST_FILENAME));
            Instances test = loader.getDataSet();
            test.setClassIndex(0);

            try {
                this.model.evaluate(eval, test);
                System.out.println(eval.toSummaryString());
                System.out.println(eval.weightedFMeasure());
                System.out.println(eval.weightedPrecision());
                System.out.println(eval.weightedRecall());
            } catch(UnableToEvaluateException e) {
                System.out.println("Unable to use Weka builtin evaluation. Continuing to our evaluation...");
            }

            for (int i = 0; i < comments.size(); i++) {
                Instance datum = test.instance(i);
                double[] result = model.distributionForInstance(datum);
                int index = Utils.maxIndex(result);
                classifications.put(comments.get(i), index == 0 ? firstTrainComment : CommentClass.other(firstTrainComment));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classifications;
    }

    @Override
    public void train(List<Comment> comments) {
        this.firstTrainComment = this.threshold.label(comments.get(0));
        System.out.println("Training " + getName() + " on " + comments.size() + " comments...");
        IO.writeInputFileWithHeaders(Constants.WEKA_TRAIN_FILENAME, new WordLabeledFeatureFormatter(this.threshold, this.features, ","), comments, features);

        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(Constants.WEKA_TRAIN_FILENAME));
            this.train = loader.getDataSet();
            this.train.setClassIndex(0);
            this.model.buildClassifier(this.train);

            OutputStream os = new FileOutputStream(new File("weka/ " + getName() + ".model"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(this.model);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
