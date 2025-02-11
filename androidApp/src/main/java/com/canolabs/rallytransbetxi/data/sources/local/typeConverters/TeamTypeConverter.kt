package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.canolabs.rallytransbetxi.data.models.responses.Team
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TeamTypeConverter {

    @TypeConverter
    fun fromTeam(team: Team): String {
        return Json.encodeToString(team)
    }

    @TypeConverter
    fun toTeam(teamString: String): Team {
        return Json.decodeFromString(teamString)
    }
}