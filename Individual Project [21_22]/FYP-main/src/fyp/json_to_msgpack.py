import pickle

import ijson
import msgpack
import tqdm
from tqdm import tqdm  # noqa: F811

from fyp.crypto import Crypto
from fyp.db import User

start_hop = 1
crypto = Crypto()
base = "/its/home/ep396/Documents/FYP/data/snowball_second/"
t_d = f"{base}decrypted_tweets.json"
t_d_p = f"{base}decrypted_tweets.pickle"
t_d_m = f"{base}decrypted_tweets.msgpack"


# Get seed users from db
users = {
    int(crypto.fernet_decrypt(user.twitter_user_id)): user.id
    for user in User.select(User.id, User.twitter_user_id).where(User.hop == start_hop)
}
user_twitter_ids = [user for user in users.keys()]

tweets = {}

for idx, user in enumerate(tqdm(user_twitter_ids)):
    tweets[user] = []
    with open(t_d, "r", encoding="utf8") as f:
        t_objects = ijson.items(f, f"{user}.item")
        for o in t_objects:
            tweets[user].append(o)

    if idx % 1000 == 0:
        with open(t_d_p, "wb") as handle:
            pickle.dump(tweets, handle, protocol=pickle.HIGHEST_PROTOCOL)

        with open(t_d_m, "wb") as outfile:
            packed = msgpack.packb(tweets)
            outfile.write(packed)


with open(t_d_p, "wb") as handle:
    pickle.dump(tweets, handle, protocol=pickle.HIGHEST_PROTOCOL)

with open(t_d_m, "wb") as outfile:
    packed = msgpack.packb(tweets)
    outfile.write(packed)
