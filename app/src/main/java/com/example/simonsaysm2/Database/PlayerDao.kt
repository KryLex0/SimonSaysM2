package com.example.simonsaysm2.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simonsaysm2.Database.Player

@Dao
interface PlayerDao {

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Query("SELECT * FROM Players WHERE name LIKE :nomP AND " +
            "score LIKE :scoreP LIMIT 1")
    fun findPlayerByData(nomP: String, scoreP: Long): Player

    /////////////////////////////////////////////////////////////////////////////////

    @Query("SELECT * FROM Players")
    fun getAll(): List<Player>

    @Query("SELECT * FROM Players WHERE id=(SELECT max(id) FROM Players)")
    fun getLastScore(): Player

    @Query("SELECT * FROM Players WHERE score=(SELECT max(score) FROM Players)")
    fun getHighScore(): Player

    @Query("SELECT * FROM Players ORDER BY score DESC, name ASC LIMIT 10")
    fun getTenLastAll(): List<Player>


    @Query("SELECT * FROM Players WHERE difficulty LIKE 'Facile' ORDER BY score DESC, name ASC LIMIT 10")
    fun getTenLastFacile(): List<Player>

    @Query("SELECT * FROM Players WHERE difficulty LIKE 'Normal' ORDER BY score DESC, name ASC LIMIT 10")
    fun getTenLastNormal(): List<Player>

    @Query("SELECT * FROM Players WHERE difficulty LIKE 'Difficile' ORDER BY score DESC, name ASC LIMIT 10")
    fun getTenLastDifficile(): List<Player>

/////////////////////////////////////////////////////////////////////////////////

    @Query("DELETE FROM Players WHERE id LIKE :playerId")
    fun deletePlayerFromId(playerId: Long)

    @Query("DELETE FROM Players")
    fun deleteAll()


}