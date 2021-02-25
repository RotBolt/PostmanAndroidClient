package io.rotlabs.postmanandroidclient.data.models

import io.rotlabs.postmanandroidclient.utils.network.MimeType
import java.io.File

/**
 *  Model class to wrap the Body data and type of Body which is sent along with Request
 */

sealed class BodyInfo(bodyType: BodyType) {

    class NoBody : BodyInfo(BodyType.NO_BODY)

    data class RawBody(val content: String) : BodyInfo(BodyType.RAW)

    data class FormDataBody(val content: Map<String, FormDataContent>) :
        BodyInfo(BodyType.FORM_DATA)
}


sealed class FormDataContent {
    data class FormDataText(val content: String) : FormDataContent()
    data class FormDataFile(val file: File, val mimeType: MimeType) : FormDataContent()
}


enum class BodyType {
    NO_BODY,
    FORM_DATA,
    RAW
}