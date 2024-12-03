package com.queue_hub.isis3510_s3_g31.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.reviews.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val dataLayerFacade: DataLayerFacade
): ViewModel() {

    private val _reviewState = MutableStateFlow<ReviewViewState<List<Review>>>(ReviewViewState.Loading())
    val reviewState: StateFlow<ReviewViewState<List<Review>>> = _reviewState

    private val _placeState = MutableStateFlow<ReviewViewState<Place>>(ReviewViewState.Loading())
    val placeState: StateFlow<ReviewViewState<Place>> = _placeState

    private val _idPlace = MutableStateFlow("")

    fun onPlaceIdChange(idPlace: String) {

        if(idPlace.isEmpty()){
            return
        }

        println("idPlace OBTENIDO: $idPlace")
        _idPlace.value = idPlace
        getReviews(idPlace)
        getPlace()

    }


    private fun getReviews(idPlace: String){
        viewModelScope.launch (Dispatchers.IO){


            if (idPlace.isNotEmpty()) {

                _reviewState.value = ReviewViewState.Loading()
                dataLayerFacade.getReviews(idPlace).collect { reviews ->
                    _reviewState.value = ReviewViewState.Success(reviews)
                }

            } else {
                _reviewState.value = ReviewViewState.Loading()
            }

        }
    }

    private fun getPlace() {
        viewModelScope.launch(Dispatchers.IO) {

            _placeState.value = ReviewViewState.Loading()
            val fetchedPlace = dataLayerFacade.getPlaceToDetail() ?: Place()

            if (!fetchedPlace.id.isEmpty()) {
                _placeState.value = ReviewViewState.Success(fetchedPlace)
            }
        }
    }
}