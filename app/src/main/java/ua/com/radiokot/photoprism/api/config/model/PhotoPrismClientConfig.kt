package ua.com.radiokot.photoprism.api.config.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PhotoPrismClientConfig
@JsonCreator
constructor(
    @JsonProperty("downloadToken")
    val downloadToken: String,
    @JsonProperty("previewToken")
    val previewToken: String,
    @JsonProperty("public")
    val public: Boolean,
)