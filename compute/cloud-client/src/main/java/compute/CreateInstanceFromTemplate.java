/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package compute;
// [START compute_instances_create_from_template]

import com.google.cloud.compute.v1.InsertInstanceRequest;
import com.google.cloud.compute.v1.Instance;
import com.google.cloud.compute.v1.InstancesClient;
import com.google.cloud.compute.v1.Operation;
import com.google.cloud.compute.v1.ZoneOperationsClient;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CreateInstanceFromTemplate {

  public static void main(String[] args)
      throws IOException, ExecutionException, InterruptedException {
    /*  TODO(developer): Replace these variables before running the sample.
        projectId - ID or number of the project you want to use.
        zone - Name of the zone you want to check, for example: us-west3-b
        instanceName - Name of the new instance.
        instanceTemplateURL - URL of the instance template using for creating the new instance.
        It can be a full or partial URL.
        Examples:
        - https://www.googleapis.com/compute/v1/projects/project/global/instanceTemplates/example-instance-template
        - projects/project/global/instanceTemplates/example-instance-template
        - global/instanceTemplates/example-instance-template
     */
    String projectId = "your-project-id";
    String zone = "zone-name";
    String instanceName = "instance-name";
    String instanceTemplateURL = "instance-template-url";
    createInstanceFromTemplate(projectId, zone, instanceName, instanceTemplateURL);
  }

  // Create a new instance from template in the specified project and zone.
  public static void createInstanceFromTemplate(String projectId, String zone, String instanceName,
      String instanceTemplateURL)
      throws IOException, ExecutionException, InterruptedException {

    try (InstancesClient instancesClient = InstancesClient.create();
        ZoneOperationsClient zoneOperationsClient = ZoneOperationsClient.create()) {

      InsertInstanceRequest insertInstanceRequest = InsertInstanceRequest.newBuilder()
          .setProject(projectId)
          .setZone(zone)
          .setInstanceResource(Instance.newBuilder().setName(instanceName).build())
          .setSourceInstanceTemplate(instanceTemplateURL).build();

      Operation operation = instancesClient.insertCallable().futureCall(insertInstanceRequest)
          .get();
      Operation response = zoneOperationsClient.wait(projectId, zone, operation.getName());

      if (response.hasError()) {
        System.out.println("Instance creation from template failed ! ! " + response);
        return;
      }
      System.out
          .println("Instance creation from template: Operation Status: " + response.getStatus());
    }
  }
}
// [END compute_instances_create_from_template]