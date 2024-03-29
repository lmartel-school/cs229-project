import csv
import cPickle
import numpy as np
from sklearn import linear_model

X = []
y = []

with open('lin_reg/linear_regression.test.inputs', 'rb') as f:
    read = csv.reader(f)
    for row in read:
        values = [ float(x) for x in row ]
        klass = values.pop(0)
        X.append(values)
        y.append(klass)

with open('lin_reg/linear_regression.model', 'rb') as f:
    regr = cPickle.load(f)

    predictions = regr.predict(X)

    with open('lin_reg/linear_regression.test.predictions', 'wb') as outfile:
        for prediction in predictions:
            print >> outfile, prediction

    # The coefficients
    # print "Coefficients: \n", regr.coef_
    # The mean square error
    print("[TESTING] Residual sum of squares: %.2f"
        % np.mean((predictions - y) ** 2))
    # Explained variance score: 1 is perfect prediction
    print('[TESTING] Variance score: %.2f' % regr.score(X, y))

