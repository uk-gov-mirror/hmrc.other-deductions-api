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

package v1.models.requestData.amendOtherDeductions

import play.api.libs.json.Json
import support.UnitSpec
import v1.models.utils.JsonErrorValidators

class AmendOtherDeductionsBodySpec extends UnitSpec with JsonErrorValidators {
  val amendOtherDeductionsBody = AmendOtherDeductionsBody(Seq(
    Seafarers(
      Some("myRef"),
      2000.99,
      "Blue Bell",
      "2018-04-06",
      "2019-04-06"
    ))
  )
  val multipleSeafarersAmendOtherDeductionsBody = AmendOtherDeductionsBody(Seq(
    Seafarers(
      Some("myRef"),
      2000.99,
      "Blue Bell",
      "2018-04-06",
      "2019-04-06"
    ),
    Seafarers(
      Some("myRef"),
      2000.99,
      "Blue Bell",
      "2018-04-06",
      "2019-04-06"
    ))
  )

  val noRefAmendOtherDeductionsBody = AmendOtherDeductionsBody(Seq(
    Seafarers(
      None,
      2000.99,
      "Blue Bell",
      "2018-04-06",
      "2019-04-06"
    ))
  )

  val json = Json.parse(
    """{
      | "seafarers": [{
      |   "customerReference": "myRef",
      |   "amountDeducted": 2000.99,
      |   "nameOfShip": "Blue Bell",
      |   "fromDate": "2018-04-06",
      |   "toDate": "2019-04-06"
      |   }]
      |}""".stripMargin)

  val jsonMultipleSeafarers = Json.parse(
    """{
      | "seafarers": [{
      |   "customerReference": "myRef",
      |   "amountDeducted": 2000.99,
      |   "nameOfShip": "Blue Bell",
      |   "fromDate": "2018-04-06",
      |   "toDate": "2019-04-06"
      |   },
      |   {
      |   "customerReference": "myRef",
      |   "amountDeducted": 2000.99,
      |   "nameOfShip": "Blue Bell",
      |   "fromDate": "2018-04-06",
      |   "toDate": "2019-04-06"
      |   }
      |   ]
      |}""".stripMargin)

  val jsonNoRef = Json.parse(
    """{
      | "seafarers": [{
      |   "amountDeducted": 2000.99,
      |   "nameOfShip": "Blue Bell",
      |   "fromDate": "2018-04-06",
      |   "toDate": "2019-04-06"
      |   }]
      |}""".stripMargin)

  "reads" when {
    "passed a valid JSON" should {
      "return a valid model" in {
        amendOtherDeductionsBody shouldBe json.as[AmendOtherDeductionsBody]
      }
    }
  }
  "reads from JSON with multiple seafarers" when {
    "passed a JSON with multiple seafarers" should {
      "return a valid model with multiple seafarers" in {
        multipleSeafarersAmendOtherDeductionsBody shouldBe jsonMultipleSeafarers.as[AmendOtherDeductionsBody]
      }
    }
  }
  "reads from JSON with no customer reference" should {
    "return a model with no customer reference" in {
      noRefAmendOtherDeductionsBody shouldBe jsonNoRef.as[AmendOtherDeductionsBody]
    }
  }
  "writes" when {
    "passed valid model" should {
      "return valid JSON" in {
        Json.toJson(amendOtherDeductionsBody) shouldBe json
      }
    }
  }
  "writes from body with multiple seafarers" when {
    "passed a model with multiple seafarers" should {
      "return a JSON with multiple seafarers" in {
        Json.toJson(multipleSeafarersAmendOtherDeductionsBody) shouldBe jsonMultipleSeafarers
      }
    }
  }
  "write from a body with no customer reference" should {
    "return a JSON with no customer reference" in {
      Json.toJson(noRefAmendOtherDeductionsBody) shouldBe jsonNoRef
    }
  }
}
