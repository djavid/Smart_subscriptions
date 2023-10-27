package com.djavid.smartsubs

import com.djavid.data.storage.RealTimeRepository
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

	@Test
	fun test() {
		println(listOf("rub", "usd", "rub").map { it }.groupBy { it }.mapValues { it })
	}

	@Test
	fun addition_isCorrect() {
		assertEquals(4, 2 + 2)
	}
}
