package com.animsh.runningtracker.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animsh.runningtracker.db.Run
import com.animsh.runningtracker.other.SortsType
import com.animsh.runningtracker.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    private val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortsType.DATE

    init {
        runs.addSource(runsSortedByDate){result ->
            if(sortType == SortsType.DATE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed){result ->
            if(sortType == SortsType.AVG_SPEED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned){result ->
            if(sortType == SortsType.CALORIES_BURNED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance){result ->
            if(sortType == SortsType.DISTANCE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis){result ->
            if(sortType == SortsType.RUNNING_TIME){
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortsType) = when(sortType){
        SortsType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortsType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortsType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortsType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortsType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}