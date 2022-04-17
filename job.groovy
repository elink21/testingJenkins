/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import CommonJobProperties as commonJobProperties



  job("Rotate Clusters Credentials") {
    description("Rotates Certificates and performs IP rotation for metrics and io-datastores")

    // Set common parameters.
    //commonJobProperties.setTopLevelMainJobProperties(delegate)

    // Sets that this is a cron job.
    commonJobProperties.setCronJob(delegate, 'H 2 1 */2 *')// At 00:02am every second month.

    steps {

      //Credentials rotation for metrics and io-datastores

      //Set a maintenance window
      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes'| gcloud container clusters update metrics-upgrade-clone \
      --zone=us-central1-a --maintenance-window=06:00''')

      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes'| gcloud container clusters update cluster-io-datastores-clone \
      --zone=us-central1-c --maintenance-window=06:00''')

      //Starting credential rotation
      // it's necessary to rebuild the nodes after rotation to avoid apiservices issues
      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes' | gcloud container clusters update metrics-upgrade-clone \
      --start-credential-rotation --zone=us-central1-a''')

      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes' | gcloud container clusters update cluster-io-datastores-clone \
      --start-credential-rotation --zone=us-central1-c''')

      //Rebuilding the nodes
      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes' | gcloud container clusters upgrade metrics-upgrade-clone \
      --node-pool=test-pool --zone=us-central1-a''')

      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes' | gcloud container clusters upgrade cluster-io-datastores-clone \
      --node-pool=default-pool --zone=us-central1-c''')

      //Completing the rotation
      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes' | gcloud container clusters update metrics-upgrade-clone \
      --complete-credential-rotation --zone=us-central1-a''')

      shell('''export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;
      printf 'yes' | gcloud container clusters update cluster-io-datastores-clone \
      --complete-credential-rotation --zone=us-central1-c''')
    }
  }
