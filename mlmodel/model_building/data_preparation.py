
import os
import gzip
import numpy as np


mnist_image_size = 28
#mnist_num_images = 5


def mnist_load_gzip(filepath):
    f.read(16)
    f = gzip.open(filepath,'r')
    buf = f.read(image_size * image_size * num_images)
    data = np.frombuffer(buf, dtype=np.uint8).astype(np.float32)
    data = data.reshape(num_images, image_size, image_size, 1)


def mnist_load_data(filepath):
    x_train = filepath
    x_test = 
    y_test =
    return x_train, y_train, x_test, y_test

