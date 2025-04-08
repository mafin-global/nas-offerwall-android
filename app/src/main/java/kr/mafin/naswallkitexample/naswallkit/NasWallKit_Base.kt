package kr.mafin.naswallkitexample.naswallkit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.mafin.naswallkit.define.NasWallError

/***************************************************************************************************
 * NasWallKit 연동 상태
 **************************************************************************************************/
enum class NasWallKitStatus {
    /** Idle */
    IDLE,
    /** 로딩 */
    LOADING,
    /** 성공 */
    SUCCESS,
    /** 실패 */
    FAIL;

    /** Idle 또는 로딩 상태 여부 */
    fun isIdleOrLoading(): Boolean {
        return this in listOf(IDLE, LOADING)
    }

    /** 로딩 상태 여부 */
    fun isLoading(): Boolean {
        return this == LOADING
    }

    /** Idle 또는 실패 상태 여부 */
    fun isIdleOrFail(): Boolean {
        return this in listOf(IDLE, FAIL)
    }

    /** 성공 상태 여부 */
    fun isSuccess(): Boolean {
        return this == SUCCESS
    }

    /** 실패 상태 여부 */
    fun isFail(): Boolean {
        return this == FAIL
    }

    /** 성공 또는 실패 상태 여부 */
    fun isSuccessOrFail(): Boolean {
        return this in listOf(SUCCESS, FAIL)
    }
}

/***************************************************************************************************
 * NasWallKit 연동 Base Class
 **************************************************************************************************/
open class NasWallKitBase<T> {
    /** 연동 상태 (내부 수정용) */
    private var _status = MutableStateFlow(NasWallKitStatus.IDLE)
    /** 연동 상태 */
    var status: StateFlow<NasWallKitStatus> = _status

    /** 오류 (내부 수정용) */
    private var _error = MutableStateFlow<NasWallError?>(null)
    /** 오류 */
    var error: StateFlow<NasWallError?> = _error

    /** 데이터 (내부 수정용) */
    private var _data = MutableStateFlow<T?>(null)
    /** 데이터 */
    var data: StateFlow<T?> = _data

    /***********************************************************************************************
     * 로딩 처리
     **********************************************************************************************/
    protected fun loading() {
        mainThreadLaunch {
            _status.value = NasWallKitStatus.LOADING
            _error.value = null
        }
    }

    /***********************************************************************************************
     * 성공 처리
     **********************************************************************************************/
    protected fun success(data: T?, handler: ((error: NasWallError?) -> Unit)?) {
        mainThreadLaunch {
            _status.value = NasWallKitStatus.SUCCESS
            _error.value = null
            _data.value = data
            handler?.invoke(null)
        }
    }

    /***********************************************************************************************
     * 실패 처리
     **********************************************************************************************/
    protected fun fail(error: NasWallError, handler: ((error: NasWallError?) -> Unit)?) {
        mainThreadLaunch {
            if (!error.isDuplicateCall()) {
                _status.value = NasWallKitStatus.FAIL
                _error.value = error
                _data.value = null
            }
            handler?.invoke(error)
        }
    }

    /***********************************************************************************************
     * 데이터 정리하고 IDLE 상태로 변경
     **********************************************************************************************/
    protected fun cleanup() {
        _status.value = NasWallKitStatus.IDLE
        _error.value = null
        _data.value = null
    }

    companion object {
        private fun mainThreadLaunch(block: suspend CoroutineScope.() -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                block()
            }
        }
    }
}