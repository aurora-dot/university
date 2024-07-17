# FYP

## Project Files Breakdown
```
FYP
├─ .flake8 : For code linting in src
├─ .github : For automated testing and checking code is formatted correctly in src
│  └─ workflows
│     └─ python_test.yml
├─ .gitignore : To ignore all sensitive info and other uneeded files for the github repo
├─ .pre-commit-config.yaml : A pre-commit hook to make sure when committing it follows the code formatting
├─ README.md : This document
├─ environment.yml : Used for conda to specify the enviroment to use
├─ notebooks : Jupyter notebooks
│  ├─ aa-test.ipynb : Tests that the feature extraction class works
│  ├─ aa_prototype.ipynb : Used for developing a pattern mining version for authorship atribution
│  ├─ aa_prototype_2.ipynb : Extended aa_prototype
│  ├─ aa_prototype_v3.ipynb : Using the approach used in the dissertation for feature extraction
│  ├─ combine_snowball_data.ipynb : Combines the retweeters and repliers data as one for an experiment
│  ├─ data-set-freq-analysis.ipynb : Frequency analysis of the data set
│  ├─ data-split.ipynb : Used to create the data split for machine learning
│  ├─ dataset_to_aa_features.ipynb : Converts the dataset tweet text into feature sets with pseudo-anonymised IDs
│  ├─ dev_select_seed_users.ipynb : Developmental version of selecting the initial seed users
│  ├─ find_suspicous_users.ipynb : Find the suspicious users within the dataset
│  ├─ find_suspicous_users_v2.ipynb : Adapted version of find_suspicous_users
│  ├─ fix_old_encrypted_data.ipynb : At the beginning of the project this was used to experiment with encryption
│  ├─ from_colab
│  │  ├─ get_predictions.ipynb : Get predictions from train_ai ml
│  │  ├─ get_predictions_network.ipynb : Get predictions from network_train_ai
│  │  ├─ network_train_ai.ipynb : Train SVM using network data group one
│  │  ├─ train_ai.ipynb : Train SVM using intersection users
│  │  ├─ v2_get_predictions_network.ipynb : Get predictions from v2_network_train_ai
│  │  └─ v2_network_train_ai.ipynb : Train SVM using network data group two
│  ├─ get_data_length.ipynb : Used to find data length before purges
│  ├─ graph-analysis.ipynb : Used to analyse the network early on
│  ├─ graph.ipynb : Early version of turning users relationships anonamised relationships into a csv file for gephi
│  ├─ influence.ipynb : Get influence measures data from initial users
│  ├─ initial_seed_users_to_database.ipynb : Get encrypted best users from first initial seed users stage and add to database
│  ├─ limit-graph-output.ipynb : Code to purge data from the user database 
│  ├─ network_influence.ipynb : Get the PageRank influence of users within the network
│  ├─ other_prod_select_seed_users.ipynb : Get initial users using non-neutral terms
│  ├─ prod_select_seed_users.ipynb : Get initial users using neutral terms
│  ├─ reply-graph-weighted.ipynb : Convert database to include the counts of interactions
│  ├─ snowball_prototype.ipynb : Snowball Via Retweet
│  ├─ snowball_prototype_v2.ipynb : Snowball Via Reply
│  ├─ snowball_prototype_v3.ipynb : Document worked in used for snowballing
│  ├─ subgraph_sockpuppet.ipynb : Find sockpuppets via subgraph similarity
│  ├─ subgraph_sockpuppet_v2.ipynb : Find sockpuppets via subgraph similarity altered
│  ├─ test-load-data.ipynb : Find which file type loads the fastest with same data
│  ├─ tie_up.ipynb : Get tweets for the last snowball
│  ├─ topic-mention.ipynb : Early prototype for getting discourse count
│  ├─ train_ai.ipynb : First prototype for SVM
│  ├─ train_ai_.ipynb : Second prototype for SVM
│  ├─ tweet_data_set : Used to convert each snowball collected tweets stage into a dataset
│  │  ├─ create_tweet_dataset_database_one.ipynb : Gets tweets from encrypted file and adds to dataset for hop 1
│  │  ├─ create_tweet_dataset_database_two.ipynb : Gets tweets from encrypted file and adds to dataset for hop 2
│  │  ├─ create_tweet_dataset_database_zero.ipynb : Gets tweets from encrypted file and adds to dataset for hop 0
│  │  ├─ one.py : AFK version of create_tweet_dataset_database_one to be ran away from computer
│  │  ├─ two.py : AFK version of create_tweet_dataset_database_two to be ran away from computer
│  │  └─ zero.py : AFK version of create_tweet_dataset_database_zero to be ran away from computer
│  └─ twitter_api_examples.ipynb : Examples of the api endpoints being used
├─ poetry.lock : Poetry dependency manager file
├─ pyproject.toml : Poetry project for managing dependencies and other options
├─ src : Library for commonly used code
│  └─ fyp
│     ├─ CollectInitialTweets.py : Early prototype of collecting the initial seed users
│     ├─ SnowballSampling.py : Used for initial followers method for sampling
│     ├─ __init__.py : Used to create python package
│     ├─ aa_features.py : Class for extracting text features
│     ├─ add_features_to_dataset.py : AFK for converting tweets to anonymised featur esets
│     ├─ crypto.py : Encryption tools class
│     ├─ db.py : Database and class for managing users
│     ├─ db_dataset.py : The database schema for the dataset and feature set databases
│     ├─ export-1.py : Used to export users database to anonymous csv for gephi AFK
│     ├─ get_best_tweets_big_data.py : Honestly cannot remember
│     ├─ get_predictions.py : Initial version of notebook in colab folder
│     ├─ graph-measures.py : AFK version of notebook of same name
│     ├─ import_to_db.py : Working document for adding new relationships and users to user database
│     ├─ influence_measures.py : Methods to calculate influence measures 
│     ├─ json_to_msgpack.py : Convert json to msgpack and pickle
│     ├─ main.py : Used for project beginning, small scripts for generating secure key for encryption and the start of finding the first 30 users
│     ├─ reply-graph-weighted.py : AFK version of notebook of same name
│     ├─ secrets.py: Class used to get the enviromental variable secret for Twitter API
│     ├─ snowball-reply.py : Active document version of snowball_prototype_v3 notebook
│     ├─ tie_up.py : AFK version of notebook
│     └─ twitter_api.py : API Wrapper for Twitter
└─ tests
   ├─ __init__.py
   └─ test_secrets.py
```
