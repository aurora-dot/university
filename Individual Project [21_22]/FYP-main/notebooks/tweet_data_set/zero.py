# %%
import json
import os
import pickle

from tqdm import tqdm

from fyp.crypto import Crypto
from fyp.db import Database
from fyp.db_dataset import Tweet

# %%
crypto = Crypto()
database = Database(crypto)


# %%
def load_data(name, base) -> dict:

    e_pickle = base + f"encrypted_{name}.pickle"
    e_json = base + f"encrypted_{name}.json"

    if os.path.exists(e_pickle):
        print(name + ": loading pickle")

        d = base + f"decrypted_{name}.pickle"
        crypto.age_decrypt_file(e_pickle, d)

        with open(d, "rb") as handle:
            data = pickle.load(handle)

        os.remove(d)

        return dict(data)

    elif os.path.exists(e_json):
        print(name + ": loading json")
        d = base + f"decrypted_{name}.json"
        crypto.age_decrypt_file(e_json, d)

        file = open(d, encoding="utf8")
        data = json.load(file)
        file.close()

        os.remove(d)

        return dict(data)

    else:
        print("none")
        return dict()


# %%
zero_hop_tweets = load_data(
    "tweets", "/its/home/ep396/Documents/FYP/data/snowball/"
)  # tweets from hop one


# %%
for tweets in tqdm(zero_hop_tweets.values()):
    for tweet in tqdm(tweets):
        try:
            db_tweet = Tweet.create(
                tweet_id=tweet["id"],
                author_id=tweet["author_id"],
                conversation_id=tweet["conversation_id"],
                created_at=tweet["created_at"],
                text=tweet["text"],
                reply_settings=tweet["reply_settings"],
                referenced_tweets=True if "referenced_tweets" in tweet else False,
                referenced_tweets_data=json.dumps(tweet["referenced_tweets"])
                if "referenced_tweets" in tweet
                else None,
                reply_count=tweet["public_metrics"]["reply_count"],
                like_count=tweet["public_metrics"]["like_count"],
                quote_count=tweet["public_metrics"]["quote_count"],
                retweet_count=tweet["public_metrics"]["retweet_count"],
            )
        except Exception as e:
            print(tweet)
            raise e
