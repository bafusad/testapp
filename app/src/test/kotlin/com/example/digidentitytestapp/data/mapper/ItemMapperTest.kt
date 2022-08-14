package com.example.digidentitytestapp.data.mapper

import com.example.digidentitytestapp.data.entity.CatalogItem
import com.example.digidentitytestapp.domain.entity.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class ItemMapperTest {

    @TestFactory
    fun `mapToDomain should properly map to domain entity`() =
        listOf(
            CatalogItem("bchidhs", null, "cjidbsj", "ncjdsids")
                    to Item("ncjdsids", "bchidhs", 0.0, "cjidbsj"),
            CatalogItem("123", 1.5, "uhew", "1")
                    to Item("1", "123", 1.5, "uhew"),
            CatalogItem("cbyreu jerw", 1764.58437, "7483t4u3g", "1764237632")
                    to
                    Item("1764237632", "cbyreu jerw", 1764.58437, "7483t4u3g")
        ).map { (dataItem, expectedResult) ->
            dynamicTest("when data item is $dataItem") {
                with(createEnvironment()) {
                    val actualResult = mapper.mapToDomain(dataItem)
                    assertThat(actualResult).isEqualTo(expectedResult)
                }
            }
        }

    @TestFactory
    fun `mapToData should properly map to data entity entity`(): List<DynamicTest> {
        return listOf(
            Item("1", "text1", 1.0, "img1") to
                    CatalogItem("text1", 1.0, "img1", "1"),
            Item("2", "text2", 2.0, "img2") to
                    CatalogItem("text2", 2.0, "img2", "2"),
            Item("ciudwhc8ew", "cweiuheciw", 0.0, "hcdiuhcudi") to
                    CatalogItem("cweiuheciw", 0.0, "hcdiuhcudi", "ciudwhc8ew"),
        ).map { (domainItem, expectedResult) ->
            dynamicTest("when domain item is $domainItem") {
                with(createEnvironment()) {
                    val actualResult = mapper.mapToData(domainItem)
                    assertThat(actualResult).isEqualTo(expectedResult)
                }
            }
        }
    }

    private fun createEnvironment(): Environment {
        return Environment(ItemMapper())
    }

    private data class Environment(val mapper: ItemMapper)
}