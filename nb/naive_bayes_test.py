import csv
import cPickle
import numpy as np
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer

X = []
y = []
DELIM = ","
vectorizer = None

def show_most_informative_features(vectorizer, clf, n=20):
    feature_names = vectorizer.get_feature_names()
    coefs_with_fns = sorted(zip(clf.coef_[0], feature_names))
    top = zip(coefs_with_fns[:n], coefs_with_fns[:-(n + 1):-1])
    for (coef_1, fn_1), (coef_2, fn_2) in top:
        print "\t%.4f\t%-15s\t\t%.4f\t%-15s" % (coef_1, fn_1, coef_2, fn_2)

with open('nb/naive_bayes.vectorizer', 'rb') as f:
    vectorizer = cPickle.load(f)

with open('nb/naive_bayes.test.inputs') as f:
    scoresAndFeatures = f.readlines()
    partitionedScoresAndFeatures = [entry.partition(DELIM) for entry in scoresAndFeatures]
    scores = [partitionedEntry[0] for partitionedEntry in partitionedScoresAndFeatures]
    corpus = [partitionedEntry[2] for partitionedEntry in partitionedScoresAndFeatures]
    X = vectorizer.transform(corpus)
    y = np.array(scores)

with open('nb/naive_bayes.model', 'rb') as f:
    clf = cPickle.load(f)

    # The coefficients
    # print "Coefficients: \n", clf.coef_
    # The mean square error
    predicted_y = clf.predict(X)
    with open('nb/naive_bayes.test.predictions', 'wb') as outfile:
        for prediction in predicted_y:
            print >> outfile, prediction

    show_most_informative_features(vectorizer, clf)

    errors = predicted_y.astype(int) - y.astype(int)

    print y.shape
    print predicted_y.shape
    print errors.shape
    
    print("[TESTING] Residual sum of squares: %.2f"
        % np.mean(errors ** 2))
    # Explained variance score: 1 is perfect prediction
    print('[TESTING] Variance score: %.2f' % clf.score(X, y))
