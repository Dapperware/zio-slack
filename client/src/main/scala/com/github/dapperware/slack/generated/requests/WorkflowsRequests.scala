/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param workflow_step_execute_id Context identifier that maps to the correct workflow step execution.
 * @param outputs Key-value object of outputs from your step. Keys of this object reflect the configured `key` properties of your [`outputs`](/reference/workflows/workflow_step#output) array from your `workflow_step` object.
 */
case class StepCompletedWorkflowsRequest(workflow_step_execute_id: String, outputs: Option[String])

object StepCompletedWorkflowsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[StepCompletedWorkflowsRequest] = deriveEncoder[StepCompletedWorkflowsRequest]
}

/**
 * @param workflow_step_execute_id Context identifier that maps to the correct workflow step execution.
 * @param error A JSON-based object with a `message` property that should contain a human readable error message.
 */
case class StepFailedWorkflowsRequest(workflow_step_execute_id: String, error: String)

object StepFailedWorkflowsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[StepFailedWorkflowsRequest] = deriveEncoder[StepFailedWorkflowsRequest]
}

/**
 * @param workflow_step_edit_id A context identifier provided with `view_submission` payloads used to call back to `workflows.updateStep`.
 * @param inputs A JSON key-value map of inputs required from a user during configuration. This is the data your app expects to receive when the workflow step starts. **Please note**: the embedded variable format is set and replaced by the workflow system. You cannot create custom variables that will be replaced at runtime. [Read more about variables in workflow steps here](/workflows/steps#variables).
 * @param outputs An JSON array of output objects used during step execution. This is the data your app agrees to provide when your workflow step was executed.
 * @param step_name An optional field that can be used to override the step name that is shown in the Workflow Builder.
 * @param step_image_url An optional field that can be used to override app image that is shown in the Workflow Builder.
 */
case class UpdateStepWorkflowsRequest(
  workflow_step_edit_id: String,
  inputs: Option[String],
  outputs: Option[String],
  step_name: Option[String],
  step_image_url: Option[String]
)

object UpdateStepWorkflowsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[UpdateStepWorkflowsRequest] = deriveEncoder[UpdateStepWorkflowsRequest]
}
