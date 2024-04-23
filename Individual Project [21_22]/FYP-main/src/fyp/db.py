import peewee
from cryptography.fernet import InvalidToken
from tqdm import tqdm

from fyp.crypto import Crypto

db = peewee.SqliteDatabase("/its/home/ep396/Documents/FYP/data.db")


class BaseModel(peewee.Model):
    class Meta:
        database = db
        legacy_table_names = False


class User(BaseModel):
    twitter_user_id = peewee.CharField(unique=True)
    seed = peewee.BooleanField()
    hop = peewee.IntegerField()


class UserInteractorRelationships(BaseModel):
    user = peewee.ForeignKeyField(User, backref="following")
    interactor = peewee.ForeignKeyField(User, backref="followers")
    count = peewee.IntegerField()

    indexes = ((("user", "interactor"), True),)


db.create_tables([User, UserInteractorRelationships])


class Database:
    def __init__(self, crypto: Crypto) -> None:
        self.crypto = crypto

    @staticmethod
    def create_tables():
        db.create_tables([User, UserInteractorRelationships])

    def check_key(self):
        self.crypto.fernet_encrypt("hello world")

    def encrypted(self):
        self.check_key()
        user = User.select().first()

        if user is None:
            return None

        try:
            self.crypto.fernet_decrypt(user.twitter_user_id)
            encrypted = True
        except InvalidToken:
            encrypted = False

        return encrypted

    def encrypt_or_decrypt(self, user_model=True):
        encrypted = self.encrypted()

        if encrypted:
            func = self.crypto.fernet_decrypt
        else:
            func = self.crypto.fernet_encrypt

        if user_model:
            for user in tqdm(User.select()):
                query = User.update(twitter_user_id=func(user.twitter_user_id)).where(
                    User.twitter_user_id == user.twitter_user_id
                )
                query.execute()

    def add_user(self, twitter_user_id, seed, hop):
        twitter_id_exists = User.select(User.id).where(
            User.twitter_user_id == twitter_user_id
        )

        if not twitter_id_exists:
            user = User.create(
                twitter_user_id=twitter_user_id,
                seed=seed,
                hop=hop,
            )
            user.save()

    def add_interactor_relation(self, user, interactor, count):
        relation = UserInteractorRelationships.create(
            user=user, interactor=interactor, count=count
        )
        relation.save()
