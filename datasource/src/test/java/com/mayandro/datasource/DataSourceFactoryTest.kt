package com.mayandro.datasource

import com.mayandro.datasource.factory.DataSourceFactoryImpl
import com.mayandro.remote.RemoteDataSource
import io.mockk.*
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DataSourceFactoryTest {
    private val remoteDataSource = mockk<RemoteDataSource>()

    @Test
    fun getRemoteDataSource() {
        val dataSourceFactory = mockk<DataSourceFactoryImpl>()
        //STUB calls
        every { dataSourceFactory.retrieveRemoteDataStore() } returns remoteDataSource
        //Execute the code
        val result = dataSourceFactory.retrieveRemoteDataStore()
        //Verify
        verify { dataSourceFactory.retrieveRemoteDataStore() }

        assertEquals(remoteDataSource, result)
    }
}