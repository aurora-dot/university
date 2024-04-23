import peewee

db = peewee.SqliteDatabase("/its/home/ep396/Documents/FYP/decrypted_dataset.db")

db_features_one = peewee.SqliteDatabase("/its/home/ep396/Documents/FYP/features_one.db")
db_features_two = peewee.SqliteDatabase("/its/home/ep396/Documents/FYP/features_two.db")
db_features_three = peewee.SqliteDatabase(
    "/its/home/ep396/Documents/FYP/features_three.db"
)


class BaseModel(peewee.Model):
    class Meta:
        database = db
        legacy_table_names = False


class Tweet(BaseModel):
    tweet_id = peewee.IntegerField(primary_key=True)
    author_id = peewee.IntegerField()
    conversation_id = peewee.IntegerField()
    text = peewee.TextField()
    created_at = peewee.DateTimeField()
    reply_settings = peewee.TextField()
    referenced_tweets = peewee.BooleanField()
    reply_count = peewee.IntegerField()
    like_count = peewee.IntegerField()
    quote_count = peewee.IntegerField()
    retweet_count = peewee.IntegerField()


class DataSplit(BaseModel):
    tweet_original_id = peewee.ForeignKeyField(Tweet, backref="split")
    split_type = peewee.IntegerField()


class ExtractedFeaturesOne(BaseModel):
    user_id = peewee.IntegerField()
    data_split_type = peewee.IntegerField()
    feature_array = peewee.BlobField()

    class Meta:
        database = db_features_one
        legacy_table_names = False


class ExtractedFeaturesTwo(BaseModel):
    user_id = peewee.IntegerField()
    data_split_type = peewee.IntegerField()
    feature_array = peewee.BlobField()

    class Meta:
        database = db_features_two
        legacy_table_names = False


class ExtractedFeaturesThree(BaseModel):
    user_id = peewee.IntegerField()
    data_split_type = peewee.IntegerField()
    feature_array = peewee.BlobField()

    class Meta:
        database = db_features_three
        legacy_table_names = False


db.create_tables(
    [
        Tweet,
        DataSplit,
        ExtractedFeaturesOne,
        ExtractedFeaturesTwo,
        ExtractedFeaturesThree,
    ]
)
