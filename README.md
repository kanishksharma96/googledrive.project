# Google Drive Plugin
## Overview
The following repo contains a Google drive activity monitoring service.

## Guidelines
Follow the steps below to run the application and monitor google drive location:

1. Clone this repository

2. Add Google Drive api Oauth credentials in file in the following path : src/main/resources/credentials.json

3. Provide folder (item_id) to be monitored in the properties file

## Enhancements
1. Add kafka publisher for batch processing
2. Improve test coverage
3. Concurrent Kafka Consumer to process files and metadata
