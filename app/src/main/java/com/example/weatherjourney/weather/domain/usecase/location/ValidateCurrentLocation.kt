package com.example.weatherjourney.weather.domain.usecase.location

import com.example.weatherjourney.util.Result
import com.example.weatherjourney.weather.data.mapper.coordinate
import com.example.weatherjourney.weather.domain.repository.LocationRepository

class ValidateCurrentLocation(private val repository: LocationRepository) {

    suspend operator fun invoke(shouldUpdateLastLocation: Boolean = false): Result<Boolean> {
        val currentCoordinate = when (val result = repository.getCurrentCoordinate()) {
            is Result.Success -> result.data
            is Result.Error -> return Result.Success(false) // Location permission is denied
        }

        val currentLocation = repository.getCurrentLocation()
            // If current location in db is null, fetch it with currentCoordinate and return
            ?: return when (
                val result =
                    repository.fetchCurrentLocationIfNeeded(currentCoordinate)
            ) {
                is Result.Success -> {
                    if (shouldUpdateLastLocation) {
                        repository.updateLastLocationFromCurrentOne()
                    }

                    Result.Success(true)
                }

                is Result.Error -> result
            }

        if (currentLocation.coordinate != currentCoordinate) {
            repository.deleteLocation(currentLocation)
            return when (val result = repository.fetchCurrentLocationIfNeeded(currentCoordinate)) {
                is Result.Success -> Result.Success(true)
                is Result.Error -> result
            }
        }

        return Result.Success(false)
    }
}
