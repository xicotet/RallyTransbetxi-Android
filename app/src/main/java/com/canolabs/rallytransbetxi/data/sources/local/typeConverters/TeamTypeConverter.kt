package com.canolabs.rallytransbetxi.data.sources.local.typeConverters

import androidx.room.TypeConverter
import com.canolabs.rallytransbetxi.data.models.responses.Team
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TeamTypeConverter {

    @TypeConverter
    fun fromTeam(team: Team): String {
        return Gson().toJson(team)
    }

    @TypeConverter
    fun toTeam(teamString: String): Team {
        val type = object : TypeToken<Team>() {}.type
        return Gson().fromJson(teamString, type)
    }
}