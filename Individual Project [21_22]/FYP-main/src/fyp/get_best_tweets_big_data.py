# %%
import datetime
import json
import os
import time

import ijson
from tqdm import tqdm

from fyp.crypto import Crypto
from fyp.db import Database, User

# %%
from fyp.secrets import SECRETS
from fyp.twitter_api import convert_datetime_to_ISO_8601, ratelimit_wait, twitter_api

headers = {"Authorization": f"Bearer {SECRETS.TWITTER_BEARER_TOKEN}"}


# %%
def save_data(data, name):
    base = "/its/home/ep396/Documents/FYP/data/snowball_second/"
    e = base + f"encrypted_{name}.json"
    d = base + f"decrypted_{name}.json"

    with open(d, "w", encoding="utf8") as outfile:
        json.dump(data, outfile, indent=4, ensure_ascii=False)

    crypto.age_encrypt_file(d, e)

    os.remove(d)


# %%
def load_data(name):
    base = "/its/home/ep396/Documents/FYP/data/snowball_second/"
    e = base + f"encrypted_{name}.json"
    d = base + f"decrypted_{name}.json"

    crypto.age_decrypt_file(e, d)

    file = open(d, encoding="utf8")
    data = json.load(file)
    file.close()

    os.remove(d)

    return data


# %%
start_hop = 1
crypto = Crypto()
database = Database(crypto)


# %%
# Get seed users from db
users = {
    int(crypto.fernet_decrypt(user.twitter_user_id)): user.id
    for user in User.select(User.id, User.twitter_user_id).where(User.hop == start_hop)
}

user_reverse = {value: key for key, value in users.items()}

user_twitter_ids = [user for user in users.keys()]
user_ids = [user for user in users.values()]


# %%
len(users)

# %%
raw_timeframe = (
    datetime.datetime(2021, 1, 1, 0, 0, 0),
    datetime.datetime(2021, 12, 31, 23, 59, 59),
)
timeframe = tuple(
    [convert_datetime_to_ISO_8601(timeframe) for timeframe in raw_timeframe]
)


# %%
t_base = "/its/home/ep396/Documents/FYP/data/snowball_second/"
t_e = t_base + "encrypted_tweets.json"
t_d = t_base + "decrypted_tweets.json"

crypto.age_decrypt_file(t_e, t_d)

# %%
# tweets = {}

# for user in tqdm(user_twitter_ids):
#     tweets[user] = []
#     with open(t_d, 'r', encoding='utf8') as f:
#         t_objects = ijson.items(f, f"{user}.item")
#         for o in t_objects:
#             tweets[user].append(o)


# %%
def get_conversation_tweets(user_id, tweet_conversation_id):
    query = f"conversation_id:{tweet_conversation_id} to:{user_id} -is:retweet lang:en"
    concat_data, next_token, cont = [], None, True
    start, end = timeframe

    while cont:
        params = {
            "query": query,
            "next_token": next_token,
            "start_time": start,
            "end_time": end,
            "expansions": "author_id",
            "max_results": 500,
        }

        data, limit_remaining_requests, limit_reset_time = twitter_api(
            url="https://api.twitter.com/2/tweets/search/all",
            headers=headers,
            params=params,
            data_location="data",
        )

        if data["fyp"]["error"] is True:
            if "status" in data and data["status"] == 429 and cont is True:
                ratelimit_wait(limit_reset_time, "tweets", len(concat_data))
            elif "meta" in data and data["meta"]["result_count"] == 0:
                cont = False
                print("None")
            else:
                raise Exception(data)
        else:
            concat_data += data["data"]
            print(f"Added: {len(data['data'])}")
            print(f"Total: {len(concat_data)}\n")
            next_token = (
                data["meta"]["next_token"] if "next_token" in data["meta"] else None
            )

            if next_token is None and data["fyp"]["error"] is False:
                cont = False

        time.sleep(1.15)

    return concat_data


# %%
def get_top_tweets_naive(user_twitter_ids, cap):
    best_user_tweets = {}
    for user in tqdm(user_twitter_ids):
        user_tweets = []
        user_tweets_sums = []

        with open(t_d, "r", encoding="utf8") as f:
            t_objects = ijson.items(f, f"{user}.item")
            for o in t_objects:
                user_tweets.append(o)

        for tweet in user_tweets:
            tweet_metrics = tweet["public_metrics"]
            metric_sum = sum([metric for metric in tweet_metrics.values()])
            user_tweets_sums.append(
                (tweet["id"], metric_sum, tweet_metrics, tweet["conversation_id"])
            )

        user_tweets_sums.sort(key=lambda y: y[1], reverse=True)
        best_user_tweets[user] = user_tweets_sums[:cap]

    return best_user_tweets


# # %%
# cap = 10

# # %%
# top_user_tweets = get_top_tweets_naive(user_twitter_ids, cap)


# # %%
# save_data(top_user_tweets, "top_user_tweets")


# top_user_tweets = load_data("top_user_tweets")


# # %%
# def get_repliers(top_user_tweets):
#     __concat_data = {}

#     for i, pair in enumerate(tqdm(top_user_tweets.items())):
#         user, tweets = pair
#         print(f"=> User {i}")
#         __concat_data[user] = []
#         for j, tweet in enumerate(tweets):
#             print(f"==> Tweet {j}")
#             collected_tweets = get_conversation_tweets(user, tweet[-1])
#             __concat_data[user] += [
#                 collected_tweet["author_id"] for collected_tweet in collected_tweets
#             ]

#     return __concat_data


# %%
# repliers = get_repliers(top_user_tweets)


# %%
# save_data(repliers, "repliers")

repliers = load_data("repliers")


# %%
def get_unique_repliers(users, repliers):
    unique_users = []
    relations = {}

    for user in users:
        user = str(user)
        captured_users = repliers[user]
        relations[user] = []
        for captured_user in captured_users:
            if captured_user not in unique_users:
                unique_users.append(captured_user)
            if captured_user not in relations[user]:
                relations[user].append(captured_user)

    return unique_users, relations


# %%
unique_repliers, unique_repliers_interactors = get_unique_repliers(
    user_twitter_ids, repliers
)

# %%
save_data(unique_repliers, "unique_repliers")
save_data(unique_repliers_interactors, "unique_repliers_relations")


# %%
len(unique_repliers)

# %%
sum([len(relation) for relation in unique_repliers_interactors.values()])
