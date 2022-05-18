# Python Exemplary Client

This is an exemplary command-line tool to simplify the process of running a
Batch SQL Translation job using the Google Cloud Bigquery Migration Python
client package.

## Installation

Clone or download this repository and
install the Python packages listed in the [requirements.txt](requirements.txt) file through: 

```
cd client
pip install -r requirements.txt
```

Install the gcloud CLI following the [instructions](http://cloud.google.com/sdk/docs/install).

### [Optional] gcloud login and authentication

The program will first validate the login and credential status of
gcloud. If the validation steps failed, the program will run the following two
commands automatically and bring up a page on browser for your agreement of
using your Google account.

However, it's recommended that you run these two commands on the terminal first as a one-time setup requirement:

Log in to gcloud:

```
gcloud auth login
```

Generate an application-default credential file so that you can use gcloud API
programmatically:

```
gcloud auth application-default login
```

## User Manual

Open the [config.yaml](config.yaml) file and fill all the required fields. If you are a first
time user who just wants to give it a try, we recommend to create a new [GCP
project](https://console.cloud.google.com/) and put the project_number (or project_id) in the `project_number` field in 
the config.

If you want to use an existing project, make sure you have all the required [IAM
permissions](https://cloud.google.com/bigquery/docs/batch-sql-translator#required_permissions).

## input_directory

The input folder is supposed to contain files with pure SQL statements (comments
are OK). The file extension can be in any format like .txt or .sql.

TODO: add instruction about how to use the metadata dumper outputs as inputs here.

## output_directory

In the config, specify a local directory to store the outputs of the translation job. 
Every input SQL file will have a corresponding output file under the same name in 
the output directory.

## Run a translation job

Simply run the following commands in Python3:

```
python main.py
```

### [Optional] macros replacement mapping

This tool can also perform macros substitution before/after the translation job
through an option flag.

To enable macros substitution, pass the arg '-m macros.yaml' when
running the tool:

```
python main.py -m macros.yaml
```

Here is an example of the macros.yaml file:

```
# This is an example of a macros.yaml file
macros:
  '*.sql':
    '${MACRO_1}': 'macro_replacement_1'
    '%MACRO_2%': 'macro_replacement_2'
  '2.sql':
    'templated_column': 'replacing_column'
```

The tool will perform the following operations on the SQL files:

Before translation starts (the pre-processing step): For every input file ended
with '.sql', the '${MACRO_1}' and '%MACRO_2%' strings will be replaced with
'macro_replacement_1' and 'macro_replacement_2', respectively. For the file
'2.sql', this tool will also replace 'templated_column' with 'replacing_column'.

After translation finishes (the post-processing step): The tool will reverse the
substitution for all the output SQL files by replacing the values with keys in
the map.

Notes that the tool just performs strict string replacement for all the macros
(keys defined in the map) in a single path. During post-processing, a reverse
map is first computed by simply swapping the keys and values for each file in
the map. Unexpected behavior can happen if different macros are mapped to the
same value.