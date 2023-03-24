import java.lang.Exception

object Cache {
    private val _cache: MutableMap<String, Any> = mutableMapOf()
    private var enable: Boolean = true

    init {
        _cache[Mode.DECODE_DOL.value] = CacheData.DOLCache(
            dol = "9F02069F03069F1A0295055F2A029A039C019F37049F35019F45029F4C089F34039F21039F7C149F1D08",
            data = "00000000000100000000000001440000008000014423032400669B25B9220000000000000000000044030217424700000000000000000000000000000000000000000080000000000000"
        )
    }

    fun <T> saveToCache(key: String, value: T) {
        if (enable)
            _cache[key] = value as Any
    }

    fun <T> getDataFromCache(key: String): T? {
        if (!enable)
            return null

        return try {
            _cache[key] as T
        } catch (e: Exception) {
            return null
        }
    }

    fun turnOnCache() {
        enable = true
    }

    fun turnOffCache() {
        enable = false
    }

    fun switch() {
        enable = !enable
    }

    fun inEnabled() = enable
}

sealed class CacheData {
    data class DOLCache(
        val dol: String,
        val data: String
    ) : CacheData()

    data class TLVCache(
        val tlv: String
    ) : CacheData()

    data class TVRCache(
        val tvr: String
    ) : CacheData()

    data class TSICache(
        val tsi: String
    ) : CacheData()
}
