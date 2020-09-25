package com.animsh.runningtracker.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.animsh.runningtracker.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    val totalTimeRun = mainRepository.getTotalTimesInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()

    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
}