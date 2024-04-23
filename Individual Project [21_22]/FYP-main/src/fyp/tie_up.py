# %%
import datetime
import os
import pickle
import time

from tqdm import tqdm

from fyp.crypto import Crypto
from fyp.db import Database, User

# %%
from fyp.secrets import SECRETS
from fyp.twitter_api import convert_datetime_to_ISO_8601, ratelimit_wait, twitter_api

headers = {"Authorization": f"Bearer {SECRETS.TWITTER_BEARER_TOKEN}"}


# %%
def save_data(data, name):
    base = "/its/home/ep396/Documents/FYP/data/tie_up/"
    e = base + f"encrypted_{name}.pickle"
    d = base + f"decrypted_{name}.pickle"

    with open(d, "wb") as handle:
        pickle.dump(data, handle, protocol=pickle.HIGHEST_PROTOCOL)

    crypto.age_encrypt_file(d, e)

    os.remove(d)


# %%
def load_data(name):
    base = "/its/home/ep396/Documents/FYP/data/tie_up/"
    e = base + f"encrypted_{name}.json"
    d = base + f"decrypted_{name}.json"

    crypto.age_decrypt_file(e, d)

    with open(d, "rb") as handle:
        data = pickle.load(handle)

    os.remove(d)

    return data


# %%
start_hop = 2
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
save_data(users, "test")

# %%
raw_timeframe = (
    datetime.datetime(2021, 1, 1, 0, 0, 0),
    datetime.datetime(2021, 12, 31, 23, 59, 59),
)
timeframe = tuple(
    [convert_datetime_to_ISO_8601(timeframe) for timeframe in raw_timeframe]
)


# %%
base_seed_user_query = (
    '("trans" OR "enby" OR "transgender" OR "nonbinary" OR '
    + '"genderist" OR "genderism" OR "gender cult" OR '
    + '"adult human female" OR "#SexNotGender" OR '
    + '"#IStandWithJKRowling" OR "#SexMatters" OR '
    + '"#BiologyNotBigotry" OR "#WarOnWomen" OR '
    + '"#IStandWithJKR" OR "Gender Critical" OR '
    + '"#IStandWithMayaForstater") REPLACEME -"eng trans" '
    + '-"#transporn" -"#porn" -is:nullcast '
    + "lang:en -is:retweet"
)


# %%
tweets = {}


# %%
def get_user_tweets(user_id, base_query):
    query = base_query.replace("REPLACEME", f"from:{user_id}")
    concat_data, next_token, cont = [], None, True
    start, end = timeframe

    while cont:
        params = {
            "query": query,
            "next_token": next_token,
            "start_time": start,
            "end_time": end,
            "tweet.fields": "public_metrics,conversation_id,referenced_tweets,reply_settings,in_reply_to_user_id,created_at",  # noqa: E501
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

        time.sleep(1.5)

    return concat_data


# %%
# Loop through seed users, get relevant tweet IDs of a timespan of a year
for user_twitter_id, user_db_id in tqdm(users.items()):
    print(f"==> User {user_db_id}")
    tweets[user_twitter_id] = get_user_tweets(user_twitter_id, base_seed_user_query)


# %%
save_data(tweets, "tweets")
