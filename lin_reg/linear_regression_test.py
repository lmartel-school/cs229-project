import csv
import cPickle
import numpy as np
from sklearn import linear_model
from sklearn.feature_extraction.text import CountVectorizer

X = None
y = None
DELIM = ","
vectorizer = None

with open('lin_reg/linear_regression.vectorizer', 'rb') as f:
    vectorizer = cPickle.load(f)

# Note: this class assumes data has been cleaned
with open('src/data/lin_reg/linear_regression.test.inputs', 'rb') as f:
    scoresAndFeatures = f.readlines()
    partitionedScoresAndFeatures = [entry.partition(DELIM) for entry in scoresAndFeatures]
    scores = [float(partitionedEntry[0]) for partitionedEntry in partitionedScoresAndFeatures]
    corpus = [partitionedEntry[2] for partitionedEntry in partitionedScoresAndFeatures]
    X = vectorizer.transform(corpus)
    y = np.array(scores)

with open('lin_reg/linear_regression.model', 'rb') as f:
    regr = cPickle.load(f)

    predictions = regr.predict(X)
    print predictions

    # The coefficients
    print "Coefficients: \n", regr.coef_
    # The mean square error
    print("[TESTING] Residual sum of squares: %.2f"
        % np.mean((predictions - y) ** 2))
    # Explained variance score: 1 is perfect prediction
    print('[TESTING] Variance score: %.2f' % regr.score(X, y))

