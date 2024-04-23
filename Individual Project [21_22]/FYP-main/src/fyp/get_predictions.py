# %%
import json
import pickle

from tqdm import tqdm, trange

from fyp.db_dataset import (
    ExtractedFeaturesOne,
    ExtractedFeaturesThree,
    ExtractedFeaturesTwo,
)


# %%
def get_data(query, X, y):
    for feature in tqdm(query):
        X.append(json.loads(feature.feature_array.decode("utf-8")))
        y.append(feature.user_id)


# %%

file = open("pipe.obj", "rb")
pipe = pickle.load(file)
file.close()

# %%
test_X = []
test_y = []

# %%
query = ExtractedFeaturesOne.select().where(ExtractedFeaturesOne.data_split_type == 1)
get_data(query, test_X, test_y)


# %%
query = ExtractedFeaturesTwo.select().where(ExtractedFeaturesTwo.data_split_type == 1)
get_data(query, test_X, test_y)


# %%
query = ExtractedFeaturesThree.select().where(
    ExtractedFeaturesThree.data_split_type == 1
)
get_data(query, test_X, test_y)


# %%
predictions = {}


# %%
print("Get predictions:")


# %%
for i in trange(len(test_X)):
    user_id = test_y[i]
    predicted_user = pipe.predict([test_X[i]])[0]
    if user_id in predictions:
        predictions[user_id].append(predicted_user)
    else:
        predictions[user_id] = [predicted_user]


# %%
filehandler = open("predictions.obj", "wb")
pickle.dump(predictions, filehandler)
filehandler.close()
