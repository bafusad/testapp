package com.example.digidentitytestapp.domain.usecase

import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.domain.repository.CacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

internal class CachingUseCaseImplTest {

    @Test
    fun `getCache result should be the same as in repository`() {
        val items = listOf(
            Item("1", "text1", 1.0, "img1"),
            Item("2", "text2", 2.0, "img2"),
            Item("ciudwhc8ew", "cweiuheciw", 0.0, "hcdiuhcudi"),
            Item("1764237632", "cbyreu jerw", 1764.58437, "7483t4u3g"),
        )
        with(createEnvironment(items)) {

            val actualResult = useCase.getCache()

            assertThat(actualResult).isEqualTo(items)
        }
    }

    @Test
    fun `saveItems should only save items if key is generated`() {
        val items = listOf(
            Item("1", "text1", 1.0, "img1"),
            Item("2", "text2", 2.0, "img2"),
        )
        with(createEnvironment(isSecretKeyGeneratedResult = true)) {
            useCase.saveItems(items)

            verify(repository, times(0)).generateAndSaveKey()
            verify(repository, times(1)).saveItems(items)
        }
    }

    @Test
    fun `saveItems should generate key before save items if key is not generated`() {
        val items = listOf(
            Item("1", "text1", 1.0, "img1"),
            Item("2", "text2", 2.0, "img2"),
        )
        with(createEnvironment(isSecretKeyGeneratedResult = false)) {
            useCase.saveItems(items)

            verify(repository, times(1)).generateAndSaveKey()
            verify(repository, times(1)).saveItems(items)
        }
    }

    private fun createEnvironment(
        getCacheItemsResult: List<Item> = emptyList(),
        isSecretKeyGeneratedResult: Boolean = false
    ): Environment {
        val repository = mock<CacheRepository>() {
            on { getCacheItems() } doReturn getCacheItemsResult

            on { isSecretKeyGenerated() } doReturn isSecretKeyGeneratedResult
        }
        return Environment(repository = repository, useCase = CachingUseCaseImpl(repository))
    }

    private class Environment(
        val repository: CacheRepository,
        val useCase: CachingUseCaseImpl
    )

}