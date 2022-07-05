/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedWorkflows {

  /**
   * Indicate that an app's step in a workflow completed execution.
   * @see https://api.slack.com/methods/workflows.stepCompleted
   */
  def stepCompletedWorkflows(req: StepCompletedWorkflowsRequest): Request[Unit, AccessToken] =
    request("workflows.stepCompleted").jsonBody(req).auth.accessToken

  /**
   * Indicate that an app's step in a workflow failed to execute.
   * @see https://api.slack.com/methods/workflows.stepFailed
   */
  def stepFailedWorkflows(req: StepFailedWorkflowsRequest): Request[Unit, AccessToken] =
    request("workflows.stepFailed").jsonBody(req).auth.accessToken

  /**
   * Update the configuration for a workflow extension step.
   * @see https://api.slack.com/methods/workflows.updateStep
   */
  def updateStepWorkflows(req: UpdateStepWorkflowsRequest): Request[Unit, AccessToken] =
    request("workflows.updateStep").jsonBody(req).auth.accessToken

}
