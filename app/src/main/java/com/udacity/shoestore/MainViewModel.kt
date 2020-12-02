package com.udacity.shoestore

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.User
import com.udacity.shoestore.util.State
import timber.log.Timber

class MainViewModel: ViewModel() {

    private val _userList = mutableListOf<User>()

    private val _email = MutableLiveData("")
    val email get() = _email

    private val _password = MutableLiveData("")
    val password get() = _password

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _eventLogin = MutableLiveData<Boolean>()
    val eventLogin : LiveData<Boolean> get() = _eventLogin

    private val _eventCreate = MutableLiveData<Boolean>()
    val eventCreate : LiveData<Boolean> get() = _eventCreate

    private val _shoeList = MutableLiveData<MutableList<Shoe>>(mutableListOf())
    val shoeList: LiveData<MutableList<Shoe>> get() = _shoeList

    private var _currentShoe: Shoe? = null
    val shoe = MutableLiveData<Shoe>()

    private val _eventSave = MutableLiveData<Boolean>()
    val eventSave: LiveData<Boolean> get() = _eventSave

    /*
    * Use only for [LoginFragment]
    * */
    private fun validateInput(): Boolean {
        var isValid = true
        _email.value.let { email ->
            when {
                email.isNullOrEmpty() -> {
                    _state.value = State.EMAIL_FIELD_EMPTY
                    isValid = false
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    _state.value = State.EMAIL_INVALID
                    isValid = false
                }
                else -> _state.value = State.EMAIL_PASS
            }
        }
        _password.value.let {
            if (it.isNullOrEmpty()) {
                _state.value = State.PASSWORD_FIELD_EMPTY
                isValid = false
            }
            else _state.value = State.PASSWORD_PASS
        }
        return isValid
    }

    /*
    * Use only for [LoginFragment]
    * */
    fun onCreateUser() {
        if (validateInput()) {
            val user = User(_email.value!!, _password.value!!)
            val existUser =_userList.find { it.email == user.email }

            when {
                existUser != null -> _eventCreate.value = false
                else -> {
                    _userList.add(user)
                    _eventCreate.value = true
                }
            }
        }
    }

    /*
    * Use only for [LoginFragment]
    * */
    fun onLogin() {
        if (_email.value == null || _email.value == null)
            _eventLogin.value = false
        else {
            val user = User(_email.value!!, _password.value!!)
            _eventLogin.value = _userList.contains(user)
        }
    }

    fun isLoginDone() {
        cleanLoginStatus()
    }

    fun isCreateDone() {
        cleanLoginStatus()
    }

    private fun cleanLoginStatus() {
        _eventCreate.value = null
        _eventLogin.value = null
        _state.value = null
        _password.value = null
        _email.value = null
    }

    /*
    * Use only for [ShoeDetailFragment]
    * */
    fun getShoe(name: String) {
        if (name == "") { // Adding New Shoe
            _currentShoe = Shoe("")
            shoe.value = _currentShoe?.copy()
        }
        else // Editing shoe
            _shoeList.value?.let { list ->
                _currentShoe = list.find { it.name == name }
                shoe.value = _currentShoe?.copy()
            }
    }

    /*
    * Use only for [ShoeDetailFragment]
    * */
    private fun validateShoe(): Boolean {
        shoe.value?.let { shoe ->
            when {
                shoe.name.isEmpty() -> {
                    _state.value = State.SHOE_NAME_EMPTY
                    return false
                }
                _currentShoe!!.name.isNotEmpty() -> { // Editing shoe
                    val exist = _shoeList.value?.find { it.name == shoe.name && it != _currentShoe }
                    if (exist != null) {
                        _state.value = State.SHOE_NAME_EXIST
                        return false
                    }
                }
                _currentShoe!!.name.isEmpty() -> { // Adding new shoe
                    val exist = _shoeList.value?.find { it.company == shoe.name }
                    if (exist != null) {
                        _state.value = State.SHOE_NAME_EXIST
                        return false
                    }
                }
                else -> return true
            }
        }
        return true
    }

    /*
    * Use only for [ShoeDetailFragment]
    * */
    fun onSaveShoe() {
        if (validateShoe()) {
            Timber.i("onSaveShoe()")
            shoe.value?.let { edit ->
                _currentShoe?.let { current ->
                    Timber.i("onSaveShoe() - ${current.name}")
                    if (current.name.isEmpty()) {
                        _shoeList.value!!.add(edit)
                        Timber.i("onSaveShoe() size: ${_shoeList.value!!.size}")
                        _eventSave.value = true
                    } else {
                        current.name = edit.name
                        current.size = edit.size
                        current.company = edit.company
                        current.description = edit.description
                        _eventSave.value = true
                    }
                }
            }
        }
    }

    fun cleanState() { _state.value = null }

    fun onSaveShoeDone() {
        _state.value = null
        _eventSave.value = null
    }

    override fun onCleared() {
        super.onCleared()
        _state.value = null
        _password.value = null
        _state.value = null
        _shoeList.value = null
    }
}