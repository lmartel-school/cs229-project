import csv
import cPickle
import numpy as np
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer

X = []
y = []
DELIM = ","
vectorizer = None
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
    errors = predicted_y.astype(int) - y.astype(int)

    print y.shape
    print predicted_y.shape
    print errors.shape
    
    print("[TESTING] Residual sum of squares: %.2f"
        % np.mean(errors ** 2))
    # Explained variance score: 1 is perfect prediction
    print('[TESTING] Variance score: %.2f' % clf.score(X, y))