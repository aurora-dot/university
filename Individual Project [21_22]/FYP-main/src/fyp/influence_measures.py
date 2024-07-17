import numpy as np


def ri(num_of_unique_tweets, num_of_unique_users_who_retweeted):
    return num_of_unique_tweets * np.log(num_of_unique_users_who_retweeted)


def snp(user):
    try:
        Ir = (
            user.num_of_unique_users_who_retweeted
            / user.num_of_unique_users_mentioning_the_user
        ) / user.num_of_followers
    except ZeroDivisionError:
        Ir = 0

    try:
        RMr = (
            user.tweets_of_user_retweeted + user.tweets_of_user_replied
        ) / user.tweets_by_user
    except ZeroDivisionError:
        RMr = 0

    return (Ir + RMr) / 2


# workflow plan OUTDATED:
#     collect all stats
#     use crap self made method to limit users to 500 or 250 or something
#     remaining users use RI, limit down to 150
#     remaining users use snp, limit down to 15 or so
#
# sample from three points in time to see if their relevance stays the same over time
# we are doing this to try and reduce the amount of tweets being taken as it could
# easily blow up into the millions

# Mermaid graph NEW:
# graph TD
#     A[Construct query to send to twitter, including initial time span] --> B[Send query to twitter 'all' endpoint] # noqa: E501
#     B --> C[Use naive influence measure]
#     C --> D[Get top 100 users from naive measure]
#     D --> E[Collect all tweets from selected users from three time spans]
#     E --> F[Collect all unique retweeters from user tweets]
#     F --> G[Calculate ri measure for top 100]
#     G --> H[Get top 50 ri measure users]
#     H --> I[Collect all user mentions from time span from top 50]
#     I --> J[Collect the total follower count from top 50]
#     J --> K[Calculate snp for top 50]
#     K --> L[Get top 10 users from snp measure]
