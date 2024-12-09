package com.bangkit.pedulibumil.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IdentityViewModel : ViewModel() {

    private val _isDataSubmitted = MutableLiveData(false)
    val isDataSubmitted: LiveData<Boolean> = _isDataSubmitted

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _dateOfBirth = MutableLiveData<String>()
    val dateOfBirth: LiveData<String> = _dateOfBirth

    private val _pregnancyAge = MutableLiveData<String>()
    val pregnancyAge: LiveData<String> = _pregnancyAge

    fun setName(name: String) {
        _name.value = name
    }

    fun setDateOfBirth(dateOfBirth: String) {
        _dateOfBirth.value = dateOfBirth
    }

    fun setPregnancyAge(pregnancyAge: String) {
        _pregnancyAge.value = pregnancyAge
    }

    fun markDataAsSubmitted() {
        _isDataSubmitted.value = true
    }
}
