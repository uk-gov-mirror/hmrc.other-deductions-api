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

package v1.controllers.requestParsers.validators.validations

import config.AppConfig
import mocks.MockAppConfig
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import support.UnitSpec
import v1.mocks.MockCurrentTaxYear
import v1.models.errors.RuleTaxYearNotSupportedError
import v1.models.utils.JsonErrorValidators

class MtdTaxYearValidationSpec extends UnitSpec with JsonErrorValidators {

  class Test extends MockCurrentTaxYear with MockAppConfig {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

    implicit val appConfig: AppConfig = mockAppConfig

    MockedAppConfig.minimumPermittedTaxYear
      .returns(2022)

  }

  "validate" should {
    "return no errors" when {
      "a valid tax year is supplied" in new Test {

        val validTaxYear = "2022-23"
        val validationResult = MtdTaxYearValidation.validate(validTaxYear)
        validationResult.isEmpty shouldBe true
      }

      "the minimum allowed tax year is supplied" in new Test {
        val validTaxYear = "2021-22"
        val validationResult = MtdTaxYearValidation.validate(validTaxYear)
        validationResult.isEmpty shouldBe true
      }
    }

    "return the given error" when {
      "a tax year below the minimum is supplied" in new Test {

        val invalidTaxYear = "2020-21"
        val validationResult = MtdTaxYearValidation.validate(invalidTaxYear)
        validationResult.isEmpty shouldBe false
        validationResult.length shouldBe 1
        validationResult.head shouldBe RuleTaxYearNotSupportedError
      }
    }
  }
}
