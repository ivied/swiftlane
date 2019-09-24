package dev.udev.swiftlane.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dev.udev.swiftlane.livedata.EmptyLiveData
import dev.udev.swiftlane.models.Payload
import dev.udev.swiftlane.models.Photo
import dev.udev.swiftlane.repo.PhotoRepository
import java.util.*
import javax.inject.Inject

class PicViewModel @Inject constructor(photoRepository: PhotoRepository) : ViewModel() {
    private val data = MutableLiveData<String>()
    val query = data
    val results: LiveData<Payload<List<Photo>>> = Transformations.switchMap(data) { term ->
        if (term.isNullOrBlank()) {
            EmptyLiveData.create()
        } else {
            photoRepository.retrieve(term, 1)
        }
    }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == data.value) {
            return
        }
        data.value = input
    }

    fun refresh() {
        data.value?.let { data.value = it }
    }
}