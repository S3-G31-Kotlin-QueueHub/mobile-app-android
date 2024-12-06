package com.queue_hub.isis3510_s3_g31
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import kotlinx.coroutines.Dispatchers
import com.queue_hub.isis3510_s3_g31.data.clusters.Cluster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MapViewModel(private val dataLayerFacade: DataLayerFacade): ViewModel() {
    private val _clusterList = MutableStateFlow<List<Cluster>>(emptyList())
    val clusterList: StateFlow<List<Cluster>> = _clusterList


    init{
        findClusteres()
    }

    fun findClusteres() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerFacade.getClusters().collect{ clusters ->

                println("ESTOY BUSCANDO " + clusters.toString())
            _clusterList.value = clusters
                println("YA BUSQUE ")
        }

        }
    }


}