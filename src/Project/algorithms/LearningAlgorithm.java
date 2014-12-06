package Project.algorithms;

import Project.models.Comment;

import java.util.List;

public interface LearningAlgorithm {
    public void train(List<Comment> trainingData);
}
