import csv
import cPickle
import numpy as np
from sklearn import linear_model

X = []
y = []

with open('lin_reg/naive_bayes.test.inputs', 'rb') as f:
    read = csv.reader(f)
    for row in read:
        values = [ float(x) for x in row ]
        klass = values.pop(0)
        X.append(values)
        y.append(klass)

with open('lin_reg/naive_bayes.model', 'rb') as f:
    clf = cPickle.load(f)

    # The coefficients
    # print "Coefficients: \n", clf.coef_
    # The mean square error
    print("[TESTING] Residual sum of squares: %.2f"
        % np.mean((clf.predict(X) - y) ** 2))
    # Explained variance score: 1 is perfect prediction
    print('[TESTING] Variance score: %.2f' % clf.score(X, y))

