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

package v1.controllers.requestParsers

import play.api.libs.json.Json
import support.UnitSpec
import uk.gov.hmrc.domain.Nino
import v1.mocks.validators.MockAmendOtherDeductionsValidator
import v1.models.errors.{BadRequestError, ErrorWrapper, NinoFormatError, TaxYearFormatError}
import v1.models.request.amendOtherDeductions.{AmendOtherDeductionsBody, AmendOtherDeductionsRawData, AmendOtherDeductionsRequest, Seafarers}

class AmendOtherDeductionsRequestParserSpec extends UnitSpec {

  private val nino = "AA123456A"
  private val taxYear = "2019-20"
  private val requestBodyJson = Json.parse(
    """
      |{
      |    "seafarers":[
      |      {
      |      "customerReference": "SEAFARERS1234",
      |      "amountDeducted": 2342.22,
      |      "nameOfShip": "Blue Wave",
      |      "fromDate": "2018-08-17",
      |      "toDate":"2018-10-02"
      |      }
      |    ]
      |}
        """.stripMargin)



  val inputData =
    AmendOtherDeductionsRawData(nino, taxYear, requestBodyJson)

  val inputNone =
    AmendOtherDeductionsRawData(nino, taxYear, Json.obj())

  trait Test extends MockAmendOtherDeductionsValidator {
    lazy val parser = new AmendOtherDeductionsRequestParser(mockValidator)
  }

  "parse" should {

    "return a request object" when {
      "valid request data is supplied" in new Test {
        MockAmendOtherDeductionsValidator.validate(inputData).returns(Nil)

        parser.parseRequest(inputData) shouldBe
          Right(AmendOtherDeductionsRequest(Nino(nino), taxYear, AmendOtherDeductionsBody(
            Some(Seq(Seafarers(
              Some("SEAFARERS1234"),
              2342.22,
              "Blue Wave",
              "2018-08-17",
              "2018-10-02"
            )))
          )))
      }
    }
    "return an ErrorWrapper" when {

      "a single validation error occurs" in new Test {
        MockAmendOtherDeductionsValidator.validate(inputData)
          .returns(List(NinoFormatError))

        parser.parseRequest(inputData) shouldBe
          Left(ErrorWrapper(None, NinoFormatError, None))
      }

      "multiple validation errors occur" in new Test {
        MockAmendOtherDeductionsValidator.validate(inputData)
          .returns(List(NinoFormatError, TaxYearFormatError))

        parser.parseRequest(inputData) shouldBe
          Left(ErrorWrapper(None, BadRequestError, Some(Seq(NinoFormatError, TaxYearFormatError))))
      }
    }
  }
}
