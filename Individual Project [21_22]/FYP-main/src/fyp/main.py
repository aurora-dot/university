from datetime import datetime

from fyp.CollectInitialTweets import CollectInitialTweets
from fyp.crypto import Crypto
from fyp.db import Database
from fyp.secrets import SECRETS
from fyp.SnowballSampling import SnowballSampling

crypto = Crypto()
db = Database(crypto)
db.create_tables()
headers = {"Authorization": f"Bearer {SECRETS.TWITTER_BEARER_TOKEN}"}
start_time = datetime(2020, 6, 10, 0, 0, 0, 0)
end_time = datetime(2020, 6, 30, 23, 59, 59, 999999)


def collect_neutral_seed_users():
    encrypted = db.encrypted()
    if encrypted is None or encrypted is True:
        query = (
            '("trans" OR "enby" OR "transgender" OR "nonbinary") '
            + '-"eng trans" -"#transporn" -"#porn" '
            + "-is:nullcast lang:en -is:retweet -is:reply"
        )

        seed_collect = CollectInitialTweets(
            headers=headers,
            query=query,
            start_time=start_time,
            end_time=end_time,
            crypto=crypto,
            path_beginning="/its/home/ep396/Documents/FYP/data/neutral",
        )

        seed_collect.get_seed_users()


def collect_other_seed_users():
    encrypted = db.encrypted()
    if encrypted is None or encrypted is True:
        query = (
            '("genderist" OR "genderism" OR "gender cult" OR '
            + '"adult human female" OR "#SexNotGender" OR'
            + ' "#IStandWithJKRowling" OR "#SexMatters" OR "#BiologyNotBigotry"'
            + ' OR "#WarOnWomen" OR "#IStandWithJKR" OR "Gender Critical" OR'
            + ' "#IStandWithMayaForstater") -is:nullcast lang:en -is:retweet -is:reply'
        )

        seed_collect = CollectInitialTweets(
            headers=headers,
            query=query,
            start_time=start_time,
            end_time=end_time,
            crypto=crypto,
            path_beginning="/its/home/ep396/Documents/FYP/data/other",
        )

        seed_collect.get_seed_users()


def snowball_sample_db():
    snowball_sample = SnowballSampling(db, headers)
    result = snowball_sample.main()
    print(result)
