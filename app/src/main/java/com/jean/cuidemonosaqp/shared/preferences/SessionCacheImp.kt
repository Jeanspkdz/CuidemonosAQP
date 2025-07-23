package com.jean.cuidemonosaqp.shared.preferences

import androidx.datastore.core.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SessionRepositoryImp @Inject constructor(
    private val dataStore: DataStore<Session>
): SessionRepository {

    override suspend fun updateSession(session: Session) {
        dataStore.updateData {
            session
        }
    }

    override fun observeUserId(): Flow<String> {
        return dataStore.data.map {
            it.id
        }
    }

    override fun observeToken(): Flow<String> {
        return dataStore.data.map {
            it.token
        }
    }

    override suspend fun getToken(): String {
        return dataStore.data.first().token
    }

    override fun getTokenSync(): String {
        return runBlocking(Dispatchers.IO) {
            dataStore.data.first().token
        }
    }

    override suspend fun getUserId(): String {
        return dataStore.data.first().id
    }

    override suspend fun updateToken(token: String?) {
        dataStore.updateData {
            it.copy(
                token = token!!
            )
        }
    }

    override suspend fun updateUserId(id: String?) {
       dataStore.updateData {
           it.copy(
               id = id!!
           )
       }
    }

    override suspend fun clearSession() {
        dataStore.updateData {
            Session()
        }
    }

}