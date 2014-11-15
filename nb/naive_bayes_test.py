import csv
import cPickle
import numpy as np
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer

X = []
y = []

with open('nb/naive_bayes.test.inputs') as f:
    scoresAndFeatures = f.readlines()
    partitionedScoresAndFeatures = [entry.partition(delim) for entry in scoresAndFeatures]
    scores = [partitionedEntry[0] for partitionedEntry in partitionedScoresAndFeatures]
    comments = [partitionedEntry[2] for partitionedEntry in partitionedScoresAndFeatures]
    vectorizer = CountVectorizer(min_df=1)
    X = vectorizer.fit_transform(corpus)
    y = np.array(scores)

with open('nb/naive_bayes.model', 'rb') as f:
    clf = cPickle.load(f)

    # The coefficients
    # print "Coefficients: \n", clf.coef_
    # The mean square error
    print("[TESTING] Residual sum of squares: %.2f"
        % np.mean((clf.predict(X) - y) ** 2))
    # Explained variance score: 1 is perfect prediction
    print('[TESTING] Variance score: %.2f' % clf.score(X, y))

