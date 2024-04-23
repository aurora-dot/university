# %%
import json
import os

from tqdm import tqdm

from fyp.aa_features import aa_feature_extractor
from fyp.crypto import Crypto

# %%
crypto = Crypto()
base = "/its/home/ep396/Documents/FYP/"
name = "dataset"


# %%
def load_db(name, base):
    e = base + f"encrypted_{name}.db"
    d = base + f"decrypted_{name}.db"
    crypto.age_decrypt_file(e, d)


# %%
def unload_db(name, base):
    e = base + f"encrypted_{name}.db"
    d = base + f"decrypted_{name}.db"
    crypto.age_encrypt_file(d, e)

    os.remove(d)


# %%
load_db(name, base)


from fyp.db import User  # noqa: E402

# %%
from fyp.db_dataset import (  # noqa: E402
    DataSplit,
    ExtractedFeaturesOne,
    ExtractedFeaturesThree,
    ExtractedFeaturesTwo,
    Tweet,
)

print("decrypt user ids")
twitter_id_to_abstracted = {
    int(user.id): int(crypto.fernet_decrypt(user.twitter_user_id))
    for user in User.select(User.id, User.twitter_user_id)
}

# %%
feature_extractor = aa_feature_extractor()


# %%
inserts = []

# %%
print("create query")
query = (
    Tweet.select(Tweet.author_id, Tweet.text, DataSplit.split_type)
    .join(DataSplit)
    .dicts()
)
x = 1000

# %%
print("get length")
length = len(query)
part = length / 3

# %%
print("start enum")
for i, tweet in enumerate(tqdm(query)):
    extracted_features = feature_extractor.extract_features_from_document(tweet["text"])

    tweet_dict = {}
    tweet_dict["user_id"] = twitter_id_to_abstracted[int(tweet["author_id"])]
    tweet_dict["data_split_type"] = int(tweet["split_type"])
    tweet_dict["feature_array"] = json.dumps(extracted_features)

    inserts.append(tweet_dict)

    if i % x == 0 and i >= x:
        if i <= part:
            ExtractedFeaturesOne.insert_many(inserts).execute()

        elif i > part and i <= (part * 2):
            ExtractedFeaturesTwo.insert_many(inserts).execute()

        elif i > (part * 2) and i <= (part * 3):
            ExtractedFeaturesThree.insert_many(inserts).execute()

        inserts = []

ExtractedFeaturesThree.insert_many(inserts).execute()
# %%
