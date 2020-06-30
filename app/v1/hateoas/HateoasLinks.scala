/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.hateoas

import config.AppConfig
import v1.models.hateoas.Link
import v1.models.hateoas.Method._
import v1.models.hateoas.RelType._

trait HateoasLinks {

  //Domain URIs
  private def sampleUri(appConfig: AppConfig, nino: String): String =
    s"/${appConfig.apiGatewayContext}/$nino/sample-endpoint"

  //API resource links
  def sampleLink(appConfig: AppConfig, nino: String): Link =
    Link(href = sampleUri(appConfig, nino), method = GET, rel = SAMPLE_ENDPOINT_REL)

  private def otherDeductionsUri(appConfig: AppConfig, nino: String, taxYear: String): String =
    s"/${appConfig.apiGatewayContext}/$nino/$taxYear"

  def amendOtherDeductions(appConfig: AppConfig, nino: String, taxYear: String): Link =
    Link(href = otherDeductionsUri(appConfig, nino, taxYear), method = PUT, rel = AMEND_DEDUCTIONS_OTHER)

  def deleteOtherDeductions(appConfig: AppConfig, nino: String, taxYear: String): Link =
    Link(href = otherDeductionsUri(appConfig, nino, taxYear), method = DELETE, rel = DELETE_DEDUCTIONS_OTHER)

  def retrieveOtherDeductions(appConfig: AppConfig, nino: String, taxYear: String): Link =
    Link(href = otherDeductionsUri(appConfig, nino, taxYear), method = GET, rel = SELF)
}
