/*
 * Copyright 2021 HM Revenue & Customs
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

package v1.connectors

import mocks.MockAppConfig
import uk.gov.hmrc.domain.Nino
import v1.mocks.MockHttpClient
import v1.models.outcomes.ResponseWrapper
import v1.models.request.amendOtherDeductions.{AmendOtherDeductionsBody, AmendOtherDeductionsRequest, Seafarers}

import scala.concurrent.Future

class AmendOtherDeductionsConnectorSpec extends ConnectorSpec {

  val taxYear = "2018-04-06"
  val nino = Nino("AA123456A")
  val body = AmendOtherDeductionsBody(
    Some(Seq(Seafarers(
      Some("myRef"),
      2000.99,
      "Blue Bell",
      "2018-04-06",
      "2019-04-06"
    )))
  )

  class Test extends MockHttpClient with MockAppConfig {
    val connector: AmendOtherDeductionsConnector = new AmendOtherDeductionsConnector(http = mockHttpClient, appConfig = mockAppConfig)

    val desRequestHeaders: Seq[(String, String)] = Seq("Environment" -> "des-environment", "Authorization" -> s"Bearer des-token")
    MockedAppConfig.desBaseUrl returns baseUrl
    MockedAppConfig.desToken returns "des-token"
    MockedAppConfig.desEnvironment returns "des-environment"
  }

  "connector" must {
    val request = AmendOtherDeductionsRequest(nino, taxYear, body)

    "put a body and return 204 no body" in new Test {
      val outcome = Right(ResponseWrapper(correlationId, ()))
      MockedHttpClient
        .put(
          url = s"$baseUrl/income-tax/deductions/${request.nino}/${request.taxYear}",
          body = body,
          requiredHeaders = "Environment" -> "des-environment", "Authorization" -> s"Bearer des-token"
        )
        .returns(Future.successful(outcome))

      await(connector.amend(request)) shouldBe outcome
    }
  }
}
