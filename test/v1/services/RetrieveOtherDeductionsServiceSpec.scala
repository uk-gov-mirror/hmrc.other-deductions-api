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

package v1.services

import support.UnitSpec
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import v1.controllers.EndpointLogContext
import v1.mocks.connectors.MockRetrieveOtherDeductionsConnector
import v1.models.errors.{DesErrorCode, DesErrors, DownstreamError, ErrorWrapper, MtdError, NinoFormatError, NotFoundError, TaxYearFormatError}
import v1.models.outcomes.ResponseWrapper
import v1.models.request.retrieveOtherDeductions.RetrieveOtherDeductionsRequest
import v1.models.response.retrieveOtherDeductions.{RetrieveOtherDeductionsResponse, Seafarers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RetrieveOtherDeductionsServiceSpec extends UnitSpec {

  private val nino = "AA123456A"
  private val taxYear = "2017-18"
  private val correlationId = "X-123"

  private val responseModel = RetrieveOtherDeductionsResponse(
    "2019-04-04T01:01:01Z",
    Some(Seq(Seafarers(Some("SEAFARERS1234"), 2543.32, "Blue Bell", "2019-04-06", "2020-04-05"))))

  private val requestData = RetrieveOtherDeductionsRequest(Nino(nino), taxYear)

  trait Test extends MockRetrieveOtherDeductionsConnector {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    implicit val logContext: EndpointLogContext = EndpointLogContext("c", "ep")

    val service = new RetrieveOtherDeductionsService(
      connector = mockRetrieveOtherDeductionsConnector
    )
  }

  "service" should {
    "return a successful response" when {
      "a successful response is passed through" in new Test {
        MockRetrieveOtherDeductionsConnector.retrieve(requestData)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, responseModel))))

        await(service.retrieve(requestData)) shouldBe Right(ResponseWrapper(correlationId, responseModel))
      }
    }
    "map errors according to spec" when {
      def serviceError(desErrorCode: String, error: MtdError): Unit =
        s"a $desErrorCode error is returned from the service" in new Test {

          MockRetrieveOtherDeductionsConnector.retrieve(requestData)
            .returns(Future.successful(Left(ResponseWrapper(correlationId, DesErrors.single(DesErrorCode(desErrorCode))))))

          await(service.retrieve(requestData)) shouldBe Left(ErrorWrapper(Some(correlationId), error))
        }

      val input = Seq(
        ("INVALID_TAXABLE_ENTITY_ID", NinoFormatError),
        ("INVALID_TAX_YEAR", TaxYearFormatError),
        ("NO_DATA_FOUND", NotFoundError),
        ("SERVER_ERROR", DownstreamError),
        ("SERVICE_UNAVAILABLE", DownstreamError)
      )

      input.foreach(args => (serviceError _).tupled(args))
    }
  }
}
