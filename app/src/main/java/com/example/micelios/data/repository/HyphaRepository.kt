package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.HyphaDao
import com.example.micelios.data.local.entity.HyphaEntity
import com.example.micelios.domain.model.Hypha
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HyphaRepository(private val hyphaDao: HyphaDao) {

    fun getAllHyphas(): Flow<List<Hypha>> {
        return hyphaDao.getAllHyphas().map { list ->
            list.map {
                Hypha(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    createdAt = it.createdAt
                )
            }
        }
    }

    suspend fun insertHypha(name: String, description: String) {
        hyphaDao.insert(
            HyphaEntity(
                name = name,
                description = description
            )
        )
    }
}