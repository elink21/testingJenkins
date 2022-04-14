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

// These jobs list details about each beam runner, to clarify what software
// is on each machine.


job("Rotate clusters credentials") {
    description("Performs an IP and credentials rotation on clusters 'metrics' and 'io-datastores'")
    // Set common parameters.
    // Sets that this is a cron job.
    steps {
        shell("export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;printf 'yes'| gcloud container clusters update metrics-upgrade-clone --zone=us-central1-a --maintenance-window=06:00")
        shell("export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;printf 'yes'| gcloud container clusters update cluster-io-datastores-clone --zone=us-central1-c --maintenance-window=06:00")


        shell("export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;printf 'yes' | gcloud container clusters update metrics-upgrade-clone --start-credential-rotation --zone=us-central1-a")
        shell("export PATH='/Users/elias.segundo/Documents/google-cloud-sdk/bin/:'$PATH;printf 'yes' | gcloud container clusters update cluster-io-datastores-clone --start-credential-rotation --zone=us-central1-c")

    }
}


