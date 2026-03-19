package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.micelios.data.local.entity.HyphaEntity
import com.example.micelios.data.local.entity.HyphaMemberEntity
import com.example.micelios.data.local.relation.HyphaWithMembers
import kotlinx.coroutines.flow.Flow

@Dao
interface HyphaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHypha(hypha: HyphaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: HyphaMemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<HyphaMemberEntity>)

    @Query("""
        SELECT h.* FROM hypha_table h
        INNER JOIN hypha_member_table hm ON hm.hyphaId = h.id
        WHERE hm.userId = :userId
        ORDER BY h.createdAt DESC
    """)
    fun getHyphasForUser(userId: Long): Flow<List<HyphaEntity>>

    @Query("SELECT * FROM hypha_table WHERE id = :hyphaId LIMIT 1")
    fun getHyphaById(hyphaId: Long): Flow<HyphaEntity?>

    @Query("""
        SELECT * FROM hypha_member_table
        WHERE hyphaId = :hyphaId
        ORDER BY joinedAt ASC
    """)
    fun getMembersByHypha(hyphaId: Long): Flow<List<HyphaMemberEntity>>

    @Transaction
    @Query("SELECT * FROM hypha_table WHERE id = :hyphaId LIMIT 1")
    fun getHyphaWithMembers(hyphaId: Long): Flow<HyphaWithMembers?>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM hypha_member_table
            WHERE hyphaId = :hyphaId AND userId = :userId
        )
    """)
    suspend fun isMemberOfHypha(hyphaId: Long, userId: Long): Boolean
}