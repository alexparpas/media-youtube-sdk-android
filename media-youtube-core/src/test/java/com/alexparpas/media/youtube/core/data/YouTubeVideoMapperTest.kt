package com.alexparpas.media.youtube.core.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class YouTubeVideoMapperTest {
    private lateinit var youTubeVideoMapper: YouTubeVideoMapper

    @Before
    fun setUp() {
        youTubeVideoMapper = YouTubeVideoMapper()
    }

    @Test
    fun `if number is smaller than 1k, show actual number`() {
        //Given
        val viewCount = 999.toDouble()

        //Perform
        val countFormatted = youTubeVideoMapper.map(viewCount)

        //Assert
        assertEquals("999 views", countFormatted)
    }

    @Test
    fun `if number is in the thousands, show actual number`() {
        //Given
        val viewCount = 1001.toDouble()

        //Perform
        val countFormatted = youTubeVideoMapper.map(viewCount)

        //Assert
        assertEquals("1k views", countFormatted)
    }

    @Test
    fun `if number is in the millions, show actual number`() {
        //Given
        val viewCount = 1000000.toDouble()

        //Perform
        val countFormatted = youTubeVideoMapper.map(viewCount)

        //Assert
        assertEquals("1M views", countFormatted)
    }

    @Test
    fun `if number is null, then return empty string`() {
        //Given
        val viewCount = null

        //Perform
        val countFormatted = youTubeVideoMapper.map(viewCount)

        //Assert
        assertEquals("", countFormatted)
    }

    @Test
    fun `if duration is minutes, then show the appropriate formatted duration`() {
        //Given
        val durationRaw = "PT10M23S"

        //Perform
        val durationFormatted = youTubeVideoMapper.map(durationRaw)

        //Assert
        assertEquals("10:23", durationFormatted)
    }

    @Test
    fun `if duration is hours, then show the appropriate formatted duration`() {
        //Given
        val durationRaw = "PT10H17M37S"

        //Perform
        val durationFormatted = youTubeVideoMapper.map(durationRaw)

        //Assert
        assertEquals("10:17:37", durationFormatted)
    }
}