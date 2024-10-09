package com.canolabs.rallytransbetxi.data.models.responses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val number: String = "",
    var team: Team = Team(),
    val time: String = "",
    val disqualified: Boolean = false,
    val isGlobal: Boolean = false, // True for global results, false for stage results
    val stageId: String? = null // Nullable, only used for stage results
)