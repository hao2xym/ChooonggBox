package chooongg.box.simple.modules.main.entity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chooongg.box.simple.api.HTTP
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun getRes() {
        viewModelScope.launch {
            HTTP.apiWanAndroid().allPackage()
        }
    }
}